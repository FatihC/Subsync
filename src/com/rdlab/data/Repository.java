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
		String[] cols = new String[] { "DOOR_NUMBER", "SITE_NAME", "BLOCK_NAME" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE", "CSBM_CODE" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymous(
				"selectWhereGrouped", new Class[] { Class.class,
						String[].class, String[].class, String[].class }, 3,
				cols, whereColumns, params);
		ArrayList<BlockItem> result = new ArrayList<BlockItem>();

		try {

			for (Object object : items) {

				String doorNumber, siteName, blockName;

				int count;

				Field fieldDoor = object.getClass().getDeclaredField(
						"DoorNumber");
				fieldDoor.setAccessible(true);
				Field fieldSite = object.getClass()
						.getDeclaredField("SiteName");
				fieldSite.setAccessible(true);
				Field fieldBlock = object.getClass().getDeclaredField(
						"BlockName");
				fieldBlock.setAccessible(true);

				/*
				 * long id; Field
				 * fieldId=object.getClass().getSuperclass().getDeclaredField
				 * ("id"); fieldId.setAccessible(true);
				 * id=Long.parseLong(fieldId.get(object).toString());
				 */

				Field fieldUnit = object.getClass().getDeclaredField(
						"UnitCount");
				fieldUnit.setAccessible(true);

				count = fieldUnit.getInt(object);
				doorNumber = fieldDoor.get(object).toString();
				siteName = fieldSite.get(object).toString();
				blockName = fieldBlock.get(object).toString();

				result.add(new BlockItem(doorNumber, siteName, blockName, true,
						count));
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

		return result;
	}

	@Override
	public ArrayList<UnitItem> getUnitItems(String... params) {
		String[] cols = new String[] { "INDOOR_NUMBER", "UAVT_ADDRESS_NO" };
		String[] whereColumns = new String[] { "DISTRICT_CODE", "VILLAGE_CODE",
				"STREET_CODE", "CSBM_CODE", "DOOR_NUMBER" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymous(
				"selectWhere", new Class[] { Class.class, String[].class,
						String[].class, String[].class }, 3, cols,
				whereColumns, params);
		ArrayList<UnitItem> result = new ArrayList<UnitItem>();

		try {

			for (Object object : items) {

				String indoorNumber = "", uavtNo = "";

				Field fieldIndoor = object.getClass().getDeclaredField(
						"IndoorNumber");
				fieldIndoor.setAccessible(true);
				Field fieldUavt = object.getClass().getDeclaredField(
						"UAVTAddressNo");
				fieldUavt.setAccessible(true);

				if (fieldIndoor.get(object) != null) {
					indoorNumber = fieldIndoor.get(object).toString();
				}
				if (fieldUavt.get(object) != null) {
					uavtNo = fieldUavt.get(object).toString();
				}

				result.add(new UnitItem(indoorNumber, uavtNo, true));
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

		return result;
	}

	@Override
	public SubscriberItem getSubscriberDetail(String... params) {
		// TODO Auto-generated method stub
		String[] cols = new String[] { "TESISAT_NO", "UNVAN",
				"ILK_SOZLESME_TARIHI" };
		String[] whereColumns = new String[] { "TESISAT_NO" };

		ArrayList<?> items = (ArrayList<?>) Helper.invokeMethodAnonymousForMBS(
				"selectWhereWithLong", new Class[] { Class.class, String[].class,
						String[].class, String[].class }, 3, cols,
				whereColumns, params);
		SubscriberItem item =null;
		try {

			for (Object object : items) {

				Long tesisat = null,sozl = null;
				String unvan = null;

				Field fieldTes = object.getClass().getDeclaredField(
						"TesisatNo");
				fieldTes.setAccessible(true);
				
				Field fieldSoz = object.getClass().getDeclaredField(
						"IlkSozlesmeTarihi");
				fieldSoz.setAccessible(true);
				
				Field fieldUnvan = object.getClass().getDeclaredField(
						"Unvan");
				fieldUnvan.setAccessible(true);
				
				Number n=(Number) fieldTes.get(object);
				Number n1=(Number) fieldSoz.get(object);
				
				
				if (n != null) {
					tesisat = n.longValue();
				}
				if (n1 != null) {
					sozl = n.longValue();
				}
				if (fieldUnvan.get(object)!=null) {
					unvan = fieldUnvan.get(object).toString();
				}

				item=new SubscriberItem(tesisat, sozl, unvan);
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

}
