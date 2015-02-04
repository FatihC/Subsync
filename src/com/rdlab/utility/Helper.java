package com.rdlab.utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;
import com.rdlab.data.Repository;
import com.rdlab.model.AddressListItem;
import com.rdlab.model.AuditLog;
import com.rdlab.model.BlockItem;
import com.rdlab.model.Configuration;
import com.rdlab.model.Enums;
import com.rdlab.model.UnitItem;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * @author N04155-1013
 * 
 */
public class Helper {

	private final static Logger log = Logger.getLogger(Helper.class);

	public static <T> AddressListItem mapAddressItemToListItem(T addressItem,
			String[] fields) {
		String code = "", name = "";

		for (String field : fields) {
			if (field.contains("Code")) {
				code = getFieldValue(field, addressItem).toString();
			} else if (field.contains("Name")) {
				name = getFieldValue(field, addressItem).toString();
			} else {
				name = getFieldValue(field, addressItem).toString();
				code = getFieldValue(field, addressItem).toString();
			}
		}

		return new AddressListItem(code, name);

	}

	public static ArrayList<AddressListItem> mapAddressItemListToListItemList(
			ArrayList<?> addressItems, String[] fields) {
		log.info("mapAddressItemListToListItemList method called.");
		ArrayList<AddressListItem> result = new ArrayList<AddressListItem>();
		for (Object object : addressItems) {
			result.add(mapAddressItemToListItem(object, fields));
		}
		return result;
	}

	private static <T> Object getFieldValue(String fieldName, T item) {
		try {
			Field f = item.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(item);
		} catch (ReflectiveOperationException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			log.error(String
					.format("Error occured while getting field value for %s class and %s field",
							item.getClass().getName(), fieldName));
			return "";
		}
	}

	public static ArrayList<String> checkIfOnlyCenterExist() {
		String[] cols = new String[] { "DISTRICT_CODE", "DISTRICT_NAME" };
		String[] fields = new String[] { "DistrictCode", "DistrictName" };

		ArrayList<AddressListItem> districts = selectDistinct(cols, fields);
		String name = districts.get(0).GetName();
		if (districts.size() == 1 && name.trim().equalsIgnoreCase("merkez")) {
			String[] cols2 = new String[] { "VILLAGE_CODE", "VILLAGE_NAME" };
			String[] fields2 = new String[] { "VillageCode", "VillageName" };
			String[] whereColumns = new String[] { "DISTRICT_CODE" };

			ArrayList<AddressListItem> villages = selectDistinctWhere(cols2,
					fields2, whereColumns, districts.get(0).GetCode());
			String vName = villages.get(0).GetName();
			if (villages.size() == 1 && vName.trim().equalsIgnoreCase("merkez")) {
				// district and village contains only "MERKEZ" and 1 element
				// 1code 2 name,3 v code,4 v.name
				return new ArrayList<String>(Arrays.asList(districts.get(0)
						.GetCode(), districts.get(0).GetName(), villages.get(0)
						.GetCode(), villages.get(0).GetName()));
			}
		}

		return new ArrayList<String>();
	}

	public static ArrayList<AddressListItem> selectDistinct(String[] cols,
			String[] fields) {

		ArrayList<?> items = (ArrayList<?>) invokeMethodAnonymous(
				"selectDistinct", new Class[] { Class.class, String[].class },
				1, getClassName(), cols);
		return Helper.mapAddressItemListToListItemList(items, fields);
	}

	public static Object invokeMethodAnonymous(String methodName,
			Class<?>[] methodArgs, int paramCount, Object... params) {
		Class<?> selectedClass;
		try {
			selectedClass = getClassName();
			Method m = selectedClass.getMethod(methodName, methodArgs);

			return m.invoke(null, params);
			//
			// if (paramCount > 1) {
			// return m.invoke(null, selectedClass, params[0], params[1],
			// params[2]);
			// }
			// return m.invoke(null, selectedClass, params[0]);
		} catch (NoSuchMethodException e) {
			log.error(String
					.format("NoSuchMethodException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (IllegalAccessException e) {
			log.error(String
					.format("IllegalAccessException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (IllegalArgumentException e) {
			log.error(String
					.format("IllegalArgumentException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (InvocationTargetException e) {
			log.error(String
					.format("InvocationTargetException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		}

		return null;
	}

	public static Object invokeMethodAnonymousForMBS(String methodName,
			Class<?>[] methodArgs, int paramCount, String[]... params) {
		Class<?> selectedClass;
		try {
			selectedClass = Class.forName(Constants.SelectedClassName + "Mbs");
			Method m = selectedClass.getMethod(methodName, methodArgs);

			if (paramCount > 1) {
				return m.invoke(null, selectedClass, params[0], params[1],
						params[2]);
			}
			return m.invoke(null, selectedClass, params[0]);
		} catch (ClassNotFoundException e) {
			log.error(String
					.format("ClassNotFoundException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (NoSuchMethodException e) {
			log.error(String
					.format("NoSuchMethodException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (IllegalAccessException e) {
			log.error(String
					.format("IllegalAccessException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (IllegalArgumentException e) {
			log.error(String
					.format("IllegalArgumentException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		} catch (InvocationTargetException e) {
			log.error(String
					.format("InvocationTargetException Error occured invokeMethodAnonymous for %s method and %s class",
							methodName, getClassName().getName()));
		}

		return null;
	}

	public static ArrayList<AddressListItem> selectDistinctWhere(String[] cols,
			String[] fields, String[] whereColumns, String... params) {

		ArrayList<?> items = (ArrayList<?>) invokeMethodAnonymous(
				"selectDistinctWithWhere", new Class[] { Class.class,
						String[].class, String[].class, String[].class }, 3,
				getClassName(), cols, whereColumns, params);
		return Helper.mapAddressItemListToListItemList(items, fields);
	}

	public static boolean checkConnectionExist(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}

		log.warn("There is no internet connection!");
		return false;
	}

	public static void giveNotification(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void updateStatus(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String indoorNumber, Enums value, boolean updateAll)
			throws ClassNotFoundException {
		try {
			String sql = "", innerQuery = "", tableName = getTableName();

			// Preparing sql statement for update purpose
			innerQuery = String
					.format("SELECT ID FROM %s WHERE  VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
							tableName, villageCode, streetCode, csbmCode,
							doorNumber);
			if (!updateAll) {
				innerQuery += String.format(" AND DISTRICT_CODE='%s'",
						districtCode);
			}
			if (!indoorNumber.isEmpty()) {
				innerQuery += String.format(" AND INDOOR_NUMBER='%s'",
						indoorNumber);
			}
			sql = String.format(
					"UPDATE %s SET CHECK_STATUS='%d' WHERE ID IN (%s)",
					tableName, value.getVal(), innerQuery);

			SugarRecord.executeQuery(sql, null);
		} catch (Exception e) {
			log.error(String
					.format("Error occured while updating status with given StreetCode {%s} and CsbmCode {%s} and exc is {%s}",
							streetCode, csbmCode, e.getMessage()));
		}
	}

	public static void deleteItem(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String indoorNumber, String uavtNo) throws ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(String
				.format("DISTRICT_CODE='%s' AND "
						+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
						districtCode, villageCode, streetCode, csbmCode,
						doorNumber));
		if (!indoorNumber.isEmpty()) {
			sb.append(String.format(
					" AND INDOOR_NUMBER='%s' AND UAVT_ADDRESS_NO='%s'",
					indoorNumber, uavtNo));
		}

		invokeMethodAnonymous("deleteAll", new Class[] { Class.class,
				String.class, String[].class }, 3, getClassName(),
				sb.toString(), null);
	}

	public static void updateData(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String indoorNumber, String newDoorNum, String newSiteName,
			String newBlockName, String newIndoorNum, boolean fromUnit)
			throws ClassNotFoundException {
		String sql="",innerQuery="",tableName=getTableName();
		
		try {
			innerQuery=String
					.format("SELECT * FROM %s WHERE DISTRICT_CODE='%s' AND "
							+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
							tableName, districtCode, villageCode, streetCode,
							csbmCode, doorNumber);
			
			if (fromUnit) {
				innerQuery+=String.format(" AND INDOOR_NUMBER='%s'", indoorNumber);
				sql=String.format("UPDATE %s SET INDOOR_NUMBER='%s' WHERE ID IN (%s)",tableName,newIndoorNum,innerQuery);
				
				SugarRecord.executeQuery(sql, null);
				return;
			}
			
			sql=String.format("UPDATE %s SET DOOR_NUMBER='%s',BLOCK_NAME='%s',SITE_NAME='%s' WHERE ID IN (%s)", tableName,newDoorNum,newBlockName,newSiteName,innerQuery);
			SugarRecord.executeQuery(sql, null);
		} catch (Exception e) {
			log.error(String.format("Error occured in updating data function with exc is {%s}",e.getMessage()));
		}
	}

	public static boolean checkBlockUnitsNewlyAdded(String districtCode,
			String villageCode, String streetCode, String csbmCode,
			String doorNumber) {

		try {
			Class<?> t = getClassName();
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("SELECT UAVT_ADDRESS_NO FROM %s",
					getTableName()));
			sb.append(" WHERE ");
			sb.append(String
					.format("DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
							districtCode, villageCode, streetCode, csbmCode,
							doorNumber));

			List<?> listOfT = SugarRecord.findWithQuery(t, sb.toString(), null);
			for (Object object : listOfT) {
				Field f = object.getClass().getDeclaredField("UAVTAddressNo");
				f.setAccessible(true);
				String value = f.get(object).toString();
				if (IsUUID(value) || value.isEmpty()) {
					return true;
				}
			}
		} catch (NoSuchFieldException | IllegalAccessException |IllegalArgumentException e) {
			log.error(String.format("Error occured in checkBlockUnitsNewlyAdded with an exception {%s}",e.getMessage()));
		}
		return false;
	}

	public static boolean checkBlockUnitExist(String districtCode,
			String villageCode, String streetCode, String csbmCode,
			String doorNumber) {

		try {
			Class<?> t = getClassName();
			String sql="",innerQuery="",tableName=getTableName();
			
			innerQuery=String.format("SELECT UAVT_ADDRESS_NO FROM %s WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
					tableName,districtCode, villageCode, streetCode, csbmCode,
					doorNumber);
			
			sql=String.format("SELECT ID FROM PUSH_REQUEST WHERE UAVT_CODE IN (%s)",innerQuery);

			List<?> listOfT = SugarRecord.findWithQuery(t, sql,null);
			if (listOfT.isEmpty()) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			log.error(String.format("Error occured in checkBlockUnitExist an exception is {%s}",e.getMessage()));
			return false;
		}
	}

	public static boolean IsUUID(String value) {
		boolean isGuid = false;
		try {
			UUID.fromString(value);
			isGuid = true;
		} catch (Exception e) {
			log.error(String.format("Error occured in IsUUID an exception is {%s}",e.getMessage()));
		}

		return isGuid;
	}

	public static boolean IsIntParseable(String val) {
		try {
			@SuppressWarnings("unused")
			int x = Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("Integer parse warning",
					"Unable to parse string value to int");
			return false;
		}
	}

	public static Class<?> getClassName() {
		try {
			return Class.forName(Constants.SelectedClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static String getTableName() {
		return NamingHelper.toSQLName(getClassName());
	}

	/**
	 * Getting result from sugarrecord with given sql query
	 * 
	 * @param fields
	 *            - for only addressýtem operation for other operation types you
	 *            can enter null value
	 * @param sql
	 *            - sql query for getting data from database
	 * @param operationType
	 *            - operation type enum declared in repository class.ç
	 * @return for addressýtem - List<AddressItem> for block item -
	 *         List<BlockItem> for unit item - List<UnitItem>
	 */
	public static ArrayList<?> getResultWithSql(String[] fields, String sql,
			Repository.OperationObjectType operationType) {

		List<?> listOfT = SugarRecord.findWithQuery(Helper.getClassName(), sql,
				null);
		switch (operationType) {
		case AddressItem:
			ArrayList<AddressListItem> result = new ArrayList<AddressListItem>();
			for (Object object : listOfT) {
				result.add(Helper.mapAddressItemToListItem(object, fields));
			}
			return result;
		case BlockItem:
			return mapItemsToBlockItemList(listOfT);
		case UnitItem:
			return mapItemsToUnitItemList(listOfT);
		default:
			return null;
		}

	}

	public static ArrayList<BlockItem> mapItemsToBlockItemList(List<?> items) {
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

				Field fieldCheck = object.getClass().getDeclaredField(
						"CheckStatus");
				fieldCheck.setAccessible(true);

				Field fieldUnit = object.getClass().getDeclaredField(
						"UnitCount");
				fieldUnit.setAccessible(true);

				count = fieldUnit.getInt(object);
				doorNumber = fieldDoor.get(object).toString();
				siteName = fieldSite.get(object).toString();
				blockName = fieldBlock.get(object).toString();

				int check = fieldCheck.getInt(object);

				result.add(new BlockItem(doorNumber, siteName, blockName,
						check, count));
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<UnitItem> mapItemsToUnitItemList(List<?> items) {
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

				Field fieldCheck = object.getClass().getDeclaredField(
						"CheckStatus");
				fieldCheck.setAccessible(true);

				if (fieldIndoor.get(object) != null) {
					indoorNumber = fieldIndoor.get(object).toString();
				}
				if (fieldUavt.get(object) != null) {
					uavtNo = fieldUavt.get(object).toString();
				}

				int check = fieldCheck.getInt(object);

				result.add(new UnitItem(indoorNumber, uavtNo, check));
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressLint("SdCardPath")
	public static void configureLog4J() {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName("/sdcard/Download/uavt.log");
		logConfigurator.setUseFileAppender(true);
		logConfigurator.setRootLevel(Level.ALL);
		logConfigurator.setLevel("org.apache", Level.ALL);
		logConfigurator.configure();
	}

	public static void updateLastSyncDate(boolean forFirst) {
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		boolean contains = false;
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_SYNC_TAG)) {
					configuration.setValue(DateUtils.nowLong().toString());
					Configuration.save(configuration);
					contains = true;
					break;
				}
			}
		}

		if (cf.size() == 0 && !contains) {
			Configuration cfNew = new Configuration();
			cfNew.setKey(Constants.LAST_SYNC_TAG);
			if (forFirst) {
				cfNew.setValue(DateUtils.nowLong().toString());
			}
			Configuration.save(cfNew);
		}

	}
	
	public static void updateLastPushDate(boolean forFirst) {
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		boolean contains = false;
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_PUSH_TAG)) {
					configuration.setValue(DateUtils.nowLong().toString());
					Configuration.save(configuration);
					contains = true;
					break;
				}
			}
		}

		if (cf.size() == 0 && !contains) {
			Configuration cfNew = new Configuration();
			cfNew.setKey(Constants.LAST_PUSH_TAG);
			if (forFirst) {
				cfNew.setValue(DateUtils.nowLong().toString());
			}
			Configuration.save(cfNew);
		}

	}

	public static String getLastSyncDate() {
		String date = "";
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_SYNC_TAG)) {
					try {
						return configuration.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;
				}
			}
		}

		return date;
	}
	
	public static String getLastPushDate() {
		String date = "";
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_PUSH_TAG)) {
					try {
						return configuration.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;
				}
			}
		}

		return date;
	}

	public static long getMaxSbsDate() {

		try {
			Class<?> t = Class.forName(Constants.SelectedClassName + "Mbs");
			StringBuilder sb = new StringBuilder();
			sb.append(String
					.format("select max(ILK_SOZLESME_TARIHI)as ILK_SOZLESME_TARIHI from %s",
							NamingHelper.toSQLName(t)));
			List<?> listOft = SugarRecord.findWithQuery(t, sb.toString(), null);
			if (listOft.size() > 0) {
				Object obj = listOft.get(0);
				Field f = obj.getClass().getDeclaredField("IlkSozlesmeTarihi");
				f.setAccessible(true);
				Number n1 = (Number) f.get(obj);
				if (n1 != null) {
					return n1.longValue();
				}
				return 0;
			} else
				return 0;
		} catch (ClassNotFoundException | NoSuchFieldException
				| IllegalAccessException | IllegalArgumentException e) {
			log.error("Error occured in getting Max Subscriber Contract date");
			return 0;
		}

	}

	public static void updateLogStatus(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String uavtCode, String value) {
		String sql = String
				.format("UPDATE AUDIT_LOG SET AUDIT_STATUS='%s' WHERE DISTRICT_CODE='%s' AND VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
						value, districtCode, villageCode, streetCode, csbmCode,
						doorNumber);
		if (uavtCode.length() > 1) {
			sql += String.format(" AND UAVT_CODE='%s'", uavtCode);
		}

		AuditLog.executeQuery(sql, null);
	}
}
