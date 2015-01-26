package com.rdlab.data;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.inject.Inject;

import com.rdlab.model.AddressListItem;
import com.rdlab.model.BlockItem;
import com.rdlab.model.SubscriberItem;
import com.rdlab.model.UnitItem;
import com.rdlab.utility.Helper;

public class Repository implements IRepository {

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

		return Helper.selectDistinct(cols, fields);
	}

	@Override
	public ArrayList<AddressListItem> getVillageItems(String... params) {
		String[] cols = new String[] { "VILLAGE_CODE", "VILLAGE_NAME" };
		String[] fields = new String[] { "VillageCode", "VillageName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE" };

		return Helper.selectDistinctWhere(cols, fields, whereColumns, params);
	}

	@Override
	public ArrayList<AddressListItem> getStreetItems(String... params) {
		String[] cols = new String[] { "STREET_CODE", "STREET_NAME" };
		String[] fields = new String[] { "StreetCode", "StreetName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE" };

		return Helper.selectDistinctWhere(cols, fields, whereColumns, params);
	}

	@Override
	public ArrayList<AddressListItem> getCSBMItems(String... params) {
		String[] cols = new String[] { "CSBM_CODE", "CSBM_NAME" };
		String[] fields = new String[] { "CSBMCode", "CSBMName" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE" };

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
				cols, whereColumns, params);

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
						String[].class, String[].class }, 3, cols,
				whereColumns, params);

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
				.format("SELECT DISTINCT STREET_CODE, STREET_NAME FROM %s WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s'",
						Helper.getTableName(), params[0], params[1], params[2]);

		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AddressListItem> getCSBMItemsForControl(String... params) {
		String[] fields = new String[] { "CSBMCode", "CSBMName" };
		String sql = String
				.format("SELECT DISTINCT CSBM_CODE, CSBM_NAME FROM %s WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s'",
						Helper.getTableName(), params[0], params[1], params[2],
						params[3]);

		return (ArrayList<AddressListItem>) Helper.getResultWithSql(fields,
				sql, OperationObjectType.AddressItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BlockItem> getBlockItemsForControl(String... params) {
		String sql = String
				.format("SELECT DOOR_NUMBER,SITE_NAME,BLOCK_NAME,CHECK_STATUS,COUNT(1) AS 'UNIT_COUNT' FROM %s"
						+ " WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'"
						+ " GROUP BY DOOR_NUMBER,SITE_NAME,BLOCK_NAME,CHECK_STATUS",
						Helper.getTableName(),params[0], params[1], params[2], params[3], params[4]);

		return (ArrayList<BlockItem>) Helper.getResultWithSql(null, sql,
				OperationObjectType.BlockItem);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UnitItem> getUnitItemsForControl(String... params) {
		String sql = String
				.format("SELECT INDOOR_NUMBER,UAVT_ADDRESS_NO,CHECK_STATUS FROM %s"
						+ " WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND "
						+ " CSBM_CODE='%s' AND DOOR_NUMBER='%s' AND INDOOR_NUMBER='%s' AND UAVT_ADDRESS_NO='%s'",
						Helper.getTableName(),params[0], params[1], params[2], params[3], params[4],params[5],params[6]);
		return (ArrayList<UnitItem>) Helper.getResultWithSql(null, sql,
				OperationObjectType.UnitItem);
	}
}
