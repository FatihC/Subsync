package com.rdlab.data;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.rdlab.model.AddressListItem;
import com.rdlab.model.BlockItem;
import com.rdlab.model.SubscriberItem;
import com.rdlab.model.UnitItem;
import com.rdlab.utility.Helper;

public class Repository implements IRepository {

	private final static Logger log = Logger.getLogger(Repository.class);

	public static enum OperationObjectType {
		AddressItem, BlockItem, UnitItem,
	}

	@Inject
	public Repository() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<AddressListItem> getDistrictItems(String... params) {
		// TODO Auto-generated method stub
		String[] cols = new String[] { "DISTRICT_CODE", "DISTRICT_NAME" };
		String[] fields = new String[] { "DistrictCode", "DistrictName" };

		log.info("getDistrictItems called from database");

		return Helper.selectDistinct(cols, fields);
	}

	@Override
	public ArrayList<AddressListItem> getVillageItems(String... params) {
		String[] cols = new String[] { "VILLAGE_CODE", "VILLAGE_NAME" };
		String[] fields = new String[] { "VillageCode", "VillageName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE" };

		log.info("getVillageItems called from database");

		return Helper.selectDistinctWhere(cols, fields, whereColumns, params);
	}

	@Override
	public ArrayList<AddressListItem> getStreetItems(String... params) {
		String[] cols = new String[] { "STREET_CODE", "STREET_NAME" };
		String[] fields = new String[] { "StreetCode", "StreetName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE" };

		log.info(String.format("getStreetItems called from database"));

		return Helper.selectDistinctWhere(cols, fields, whereColumns, params);
	}

	@Override
	public ArrayList<AddressListItem> getCSBMItems(String... params) {
		String[] cols = new String[] { "CSBM_CODE", "CSBM_NAME" };
		String[] fields = new String[] { "CSBMCode", "CSBMName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE" };

		log.info(String.format(
				"getCSBMItems called from database with street code =[{%s}]",
				params[2]));

		return Helper.selectDistinctWhere(cols, fields, whereColumns, params);
	}

	@Override
	public ArrayList<BlockItem> getBlockItems(String... params) {
		// TODO Auto-generated method stub
		String[] cols = new String[] { "DOOR_NUMBER", "SITE_NAME",
				"BLOCK_NAME", "CHECK_STATUS" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE", "CSBM_CODE" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymous(
				"selectWhereGrouped", new Class[] { Class.class,
						String[].class, String[].class, String[].class }, 3,
				Helper.getClassName(),cols, whereColumns, params);

		log.info(String
				.format("getBlockItems called from database with street code =[{%s}] and csbm code = [{%s}]",
						params[2], params[3]));

		return Helper.mapItemsToBlockItemList(items);
	}

	@Override
	public ArrayList<UnitItem> getUnitItems(String... params) {
		String[] cols = new String[] { "INDOOR_NUMBER", "UAVT_ADDRESS_NO",
				"CHECK_STATUS" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE", "CSBM_CODE", "DOOR_NUMBER" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymous(
				"selectWhere", new Class[] { Class.class, String[].class,
						String[].class, String[].class }, 3,Helper.getClassName(), cols,
				whereColumns, params);

		log.info(String
				.format("getUnitItems called from database with street code =[{%s}] and csbm code = [{%s}] and door nubmer = [{%s}]",
						params[2], params[3], params[4]));

		return Helper.mapItemsToUnitItemList(items);
	}

	@Override
	public SubscriberItem getSubscriberDetail(String... params) {
		// TODO Auto-generated method stub
		String[] cols = new String[] { "TESISAT_NO", "UNVAN",
				"ILK_SOZLESME_TARIHI" };
		String[] whereColumns = new String[] { "TESISAT_NO" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymousForMBS(
				"selectWhereWithLong", new Class[] { Class.class,
						String[].class, String[].class, String[].class }, 3,
				cols, whereColumns, params);
		SubscriberItem item = null;
		try {

			for (Object object : items) {

				Long tesisat = null, sozl = null;
				String unvan = null;

				Field fieldTes = object.getClass()
						.getDeclaredField("TesisatNo");
				fieldTes.setAccessible(true);

				Field fieldSoz = object.getClass().getDeclaredField(
						"IlkSozlesmeTarihi");
				fieldSoz.setAccessible(true);

				Field fieldUnvan = object.getClass().getDeclaredField("Unvan");
				fieldUnvan.setAccessible(true);

				Number n = (Number) fieldTes.get(object);
				Number n1 = (Number) fieldSoz.get(object);

				if (n != null) {
					tesisat = n.longValue();
				}
				if (n1 != null) {
					sozl = n.longValue();
				}
				if (fieldUnvan.get(object) != null) {
					unvan = fieldUnvan.get(object).toString();
				}

				item = new SubscriberItem(tesisat, sozl, unvan);
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AddressListItem> getDistrictItemsForControl(
			String... params) {
		String[] fields = new String[] { "DistrictCode", "DistrictName" };
		String sql = String
				.format("SELECT DISTINCT DISTRICT_CODE, DISTRICT_NAME FROM %s WHERE DISTRICT_CODE='%s'",
						Helper.getTableName(), params[0]);

		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AddressListItem> getVillageItemsForControl(
			String... params) {
		String[] fields = new String[] { "VillageCode", "VillageName" };
		String sql = String
				.format("SELECT DISTINCT VILLAGE_CODE, VILLAGE_NAME FROM %s WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s'",
						Helper.getTableName(), params[0], params[1]);

		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AddressListItem> getStreetItemsForControl(String... params) {
		String[] fields = new String[] { "StreetCode", "StreetName" };
		String sql = String
				.format("SELECT DISTINCT STREET_CODE, STREET_NAME FROM %s WHERE STREET_CODE IN (%s)",
						Helper.getTableName(), getControlDataSqlForGivenColumn("STREET_CODE",null, null));

		log.info(String.format("SQL query is %s",sql));
		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AddressListItem> getCSBMItemsForControl(String... params) {
		String[] fields = new String[] { "CSBMCode", "CSBMName" };
		String[] cols = new String[] { "STREET_CODE" };
		String sql = String
				.format("SELECT DISTINCT CSBM_CODE, CSBM_NAME FROM %s WHERE STREET_CODE IN (%s) ",
						Helper.getTableName(), getControlDataSqlForGivenColumn("STREET_CODE",cols, params));

		log.info(String.format("SQL query is %s",sql));
		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BlockItem> getBlockItemsForControl(String... params) {
		String[] cols = new String[] { "STREET_CODE", "CSBM_CODE" };
		String sql = String
				.format("SELECT DOOR_NUMBER,SITE_NAME,BLOCK_NAME,CHECK_STATUS,COUNT(1) AS 'UNIT_COUNT' FROM %s"
						+ " WHERE STREET_CODE IN (%s) AND DOOR_NUMBER IN (%s) %s"
						+ " GROUP BY DOOR_NUMBER,SITE_NAME,BLOCK_NAME,CHECK_STATUS",
						Helper.getTableName(), getControlDataSqlForGivenColumn("STREET_CODE",cols, params),getControlDataSqlForGivenColumn("DOOR_NUMBER",cols, params)
						,getConditionSql(cols, params));

		log.info(String.format("SQL query is %s",sql));
		return (ArrayList<BlockItem>) Helper.getResultWithSql(null, sql,
				OperationObjectType.BlockItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UnitItem> getUnitItemsForControl(String... params) {
		String[] cols = new String[] { "STREET_CODE", "CSBM_CODE",
				"DOOR_NUMBER" };
		String sql = String.format(
				"SELECT INDOOR_NUMBER,UAVT_ADDRESS_NO,CHECK_STATUS FROM %s"
						+ " WHERE STREET_CODE IN (%s)   %s", Helper.getTableName(),
						getControlDataSqlForGivenColumn("STREET_CODE",cols, params),getConditionSql(cols, params));
		
		log.info(String.format("SQL query is %s",sql));
		return (ArrayList<UnitItem>) Helper.getResultWithSql(null, sql,
				OperationObjectType.UnitItem);
	}

	private String getControlDataSqlForGivenColumn(String col,String[] conditions, String[] params){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("SELECT DISTINCT %s FROM PUSH_REQUEST WHERE IFNULL(WIRING_NO,'')='' AND ( METER_NO IS NOT NULL OR CHECK_STATUS IS NOT NULL) ", col));
		if (params != null && params.length > 0) {
			sb.append(getConditionSql(conditions, params));
		}

		return sb.toString();
	}
	
	private String getConditionSql(String[] conditions, String[] params) {
		String sql = "";
		for (int i = 0; i < params.length; i++) {
			sql += String.format("AND %s ='%s'", conditions[i], params[i]);
		}

		return sql;
	}
}
