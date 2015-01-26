package com.rdlab.utility;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;
import com.rdlab.data.Repository;
import com.rdlab.model.AddressListItem;
import com.rdlab.model.Akcakale;
import com.rdlab.model.Birecik;
import com.rdlab.model.BlockItem;
import com.rdlab.model.Bozova;
import com.rdlab.model.Ceylanpinar;
import com.rdlab.model.Enums;
import com.rdlab.model.Eyyubiye;
import com.rdlab.model.Halfeti;
import com.rdlab.model.Haliliye;
import com.rdlab.model.Harran;
import com.rdlab.model.Hilvan;
import com.rdlab.model.Karakopru;
import com.rdlab.model.PushRequest;
import com.rdlab.model.Siverek;
import com.rdlab.model.Suruc;
import com.rdlab.model.UnitItem;
import com.rdlab.model.Viransehir;

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
			log.error(String.format("Error occured while getting field value for %s class and %s field", item.getClass().getName(),fieldName));
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
				1, cols);
		return Helper.mapAddressItemListToListItemList(items, fields);
	}

	public static Object invokeMethodAnonymous(String methodName,
			Class<?>[] methodArgs, int paramCount, Object[]... params) {
		Class<?> selectedClass;
		try {
			selectedClass = getClassName();
			Method m = selectedClass.getMethod(methodName, methodArgs);

			if (paramCount > 1) {
				return m.invoke(null, selectedClass, params[0], params[1],
						params[2]);
			}
			return m.invoke(null, selectedClass, params[0]);
		} catch (NoSuchMethodException e) {
			log.error(String.format("NoSuchMethodException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (IllegalAccessException e) {
			log.error(String.format("IllegalAccessException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (IllegalArgumentException e) {
			log.error(String.format("IllegalArgumentException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (InvocationTargetException e) {
			log.error(String.format("InvocationTargetException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
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
			log.error(String.format("ClassNotFoundException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (NoSuchMethodException e) {
			log.error(String.format("NoSuchMethodException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (IllegalAccessException e) {
			log.error(String.format("IllegalAccessException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (IllegalArgumentException e) {
			log.error(String.format("IllegalArgumentException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		} catch (InvocationTargetException e) {
			log.error(String.format("InvocationTargetException Error occured invokeMethodAnonymous for %s method and %s class", methodName,getClassName().getName()));
		}

		return null;
	}

	public static ArrayList<AddressListItem> selectDistinctWhere(String[] cols,
			String[] fields, String[] whereColumns, String... params) {

		ArrayList<?> items = (ArrayList<?>) invokeMethodAnonymous(
				"selectDistinctWithWhere", new Class[] { Class.class,
						String[].class, String[].class, String[].class }, 3,
				cols, whereColumns, params);
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
		StringBuilder sb = new StringBuilder();
		String tableName = getTableName();
		if (updateAll) {
			sb.append(String
					.format("SELECT * FROM %s WHERE  VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
							tableName, villageCode, streetCode,
							csbmCode, doorNumber));
		}
		else {
			sb.append(String
					.format("SELECT * FROM %s WHERE DISTRICT_CODE='%s' AND "
							+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
							tableName, districtCode, villageCode, streetCode,
							csbmCode, doorNumber));
		}
		if (!indoorNumber.isEmpty()) {
			sb.append(String.format(" AND INDOOR_NUMBER='%s'", indoorNumber));
		}

		if (Constants.SelectedUniversalCountyCode.equals("32")) {
			List<Eyyubiye> result = Eyyubiye.findWithQuery(Eyyubiye.class,
					sb.toString(), null);

			if (result.size() > 1) {
				for (Eyyubiye getx : result) {
					Eyyubiye item = getx;
					item.setCheckStatus(value.getVal());
					Eyyubiye.save(item);
				}
			} else if(result.size() == 1){
				Eyyubiye item = result.get(0);
				item.setCheckStatus(value.getVal());
				Eyyubiye.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("33")) {
			List<Haliliye> result = Haliliye.findWithQuery(Haliliye.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Haliliye getx : result) {
					Haliliye item = getx;
					item.setCheckStatus(value.getVal());
					Haliliye.save(item);
				}
			} else if(result.size() == 1){
				Haliliye item = result.get(0);
				item.setCheckStatus(value.getVal());
				Haliliye.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("34")) {
			List<Karakopru> result = Karakopru.findWithQuery(Karakopru.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Karakopru getx : result) {
					Karakopru item = getx;
					item.setCheckStatus(value.getVal());
					Karakopru.save(item);
				}
			} else if(result.size() == 1){
				Karakopru item = result.get(0);
				item.setCheckStatus(value.getVal());
				Karakopru.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("35")) {
			List<Akcakale> result = Akcakale.findWithQuery(Akcakale.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Akcakale getx : result) {
					Akcakale item = getx;
					item.setCheckStatus(value.getVal());
					Akcakale.save(item);
				}
			} else if(result.size() == 1){
				Akcakale item = result.get(0);
				item.setCheckStatus(value.getVal());
				Akcakale.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("36")) {
			List<Birecik> result = Birecik.findWithQuery(Birecik.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Birecik getx : result) {
					Birecik item = getx;
					item.setCheckStatus(value.getVal());
					Birecik.save(item);
				}
			} else if(result.size() == 1){
				Birecik item = result.get(0);
				item.setCheckStatus(value.getVal());
				Birecik.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("37")) {
			List<Bozova> result = Bozova.findWithQuery(Bozova.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Bozova getx : result) {
					Bozova item = getx;
					item.setCheckStatus(value.getVal());
					Bozova.save(item);
				}
			} else if(result.size() == 1){
				Bozova item = result.get(0);
				item.setCheckStatus(value.getVal());
				Bozova.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("38")) {
			List<Ceylanpinar> result = Ceylanpinar.findWithQuery(
					Ceylanpinar.class, sb.toString(), null);
			if (result.size() > 1) {
				for (Ceylanpinar getx : result) {
					Ceylanpinar item = getx;
					item.setCheckStatus(value.getVal());
					Ceylanpinar.save(item);
				}
			} else if(result.size() == 1){
				Ceylanpinar item = result.get(0);
				item.setCheckStatus(value.getVal());
				Ceylanpinar.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("39")) {
			List<Halfeti> result = Halfeti.findWithQuery(Halfeti.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Halfeti getx : result) {
					Halfeti item = getx;
					item.setCheckStatus(value.getVal());
					Halfeti.save(item);
				}
			} else if(result.size() == 1){
				Halfeti item = result.get(0);
				item.setCheckStatus(value.getVal());
				Halfeti.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("40")) {
			List<Harran> result = Harran.findWithQuery(Harran.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Harran getx : result) {
					Harran item = getx;
					item.setCheckStatus(value.getVal());
					Harran.save(item);
				}
			} else if(result.size() == 1){
				Harran item = result.get(0);
				item.setCheckStatus(value.getVal());
				Harran.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("41")) {
			List<Hilvan> result = Hilvan.findWithQuery(Hilvan.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Hilvan getx : result) {
					Hilvan item = getx;
					item.setCheckStatus(value.getVal());
					Hilvan.save(item);
				}
			} else if(result.size() == 1){
				Hilvan item = result.get(0);
				item.setCheckStatus(value.getVal());
				Hilvan.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("42")) {
			List<Siverek> result = Siverek.findWithQuery(Siverek.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Siverek getx : result) {
					Siverek item = getx;
					item.setCheckStatus(value.getVal());
					Siverek.save(item);
				}
			} else if(result.size() == 1){
				Siverek item = result.get(0);
				item.setCheckStatus(value.getVal());
				Siverek.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("43")) {
			List<Suruc> result = Suruc.findWithQuery(Suruc.class,
					sb.toString(), null);
			if (result.size() > 1) {
				for (Suruc getx : result) {
					Suruc item = getx;
					item.setCheckStatus(value.getVal());
					Suruc.save(item);
				}
			} else if(result.size() == 1){
				Suruc item = result.get(0);
				item.setCheckStatus(value.getVal());
				Suruc.save(item);
			}

		} else if (Constants.SelectedUniversalCountyCode.equals("44")) {
			List<Viransehir> result = Viransehir.findWithQuery(
					Viransehir.class, sb.toString(), null);
			if (result.size() > 1) {
				for (Viransehir getx : result) {
					Viransehir item = getx;
					item.setCheckStatus(value.getVal());
					Viransehir.save(item);
				}
			} else if(result.size() == 1){
				Viransehir item = result.get(0);
				item.setCheckStatus(value.getVal());
				Viransehir.save(item);
			}
		}
	}
	
	public static void deleteItem(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String indoorNumber,String uavtNo)
			throws ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(String
				.format("DISTRICT_CODE='%s' AND "
						+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
						 districtCode, villageCode, streetCode,
						csbmCode, doorNumber));
		if (!indoorNumber.isEmpty()) {
			sb.append(String.format(" AND INDOOR_NUMBER='%s' AND UAVT_ADDRESS_NO='%s'", indoorNumber,uavtNo));
		}

		if (Constants.SelectedUniversalCountyCode.equals("32")) {
			Eyyubiye.deleteAll(Eyyubiye.class,
					sb.toString(), null);
		} else if (Constants.SelectedUniversalCountyCode.equals("33")) {
			Haliliye.deleteAll(Haliliye.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("34")) {
			Karakopru.deleteAll(Karakopru.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("35")) {
			Akcakale.deleteAll(Akcakale.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("36")) {
			Birecik.deleteAll(Birecik.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("37")) {
			Bozova.deleteAll(Bozova.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("38")) {
			Ceylanpinar.deleteAll(Ceylanpinar.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("39")) {
			Halfeti.deleteAll(Halfeti.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("40")) {
			Harran.deleteAll(Harran.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("41")) {
			Hilvan.deleteAll(Hilvan.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("42")) {
			Siverek.deleteAll(Siverek.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("43")) {
			Suruc.deleteAll(Suruc.class,
					sb.toString(), null);

		} else if (Constants.SelectedUniversalCountyCode.equals("44")) {
			Viransehir.deleteAll(Viransehir.class,
					sb.toString(), null);
		}
	}

	public static void updateData(String districtCode, String villageCode,
			String streetCode, String csbmCode, String doorNumber,
			String indoorNumber, String newDoorNum, String newSiteName,
			String newBlockName, String newIndoorNum,boolean fromUnit)
			throws ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		String tableName =getTableName();
		sb.append(String
				.format("SELECT * FROM %s WHERE DISTRICT_CODE='%s' AND "
						+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
						tableName, districtCode, villageCode, streetCode,
						csbmCode, doorNumber));
		if (fromUnit) {
			sb.append(String.format(" AND INDOOR_NUMBER='%s'", indoorNumber));
		}

		if (Constants.SelectedUniversalCountyCode.equals("32")) {
			List<Eyyubiye> result = Eyyubiye.findWithQuery(Eyyubiye.class,
					sb.toString(), null);
			Eyyubiye item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Eyyubiye.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("33")) {
			List<Haliliye> result = Haliliye.findWithQuery(Haliliye.class,
					sb.toString(), null);
			Haliliye item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Haliliye.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("34")) {
			List<Karakopru> result = Karakopru.findWithQuery(Karakopru.class,
					sb.toString(), null);
			Karakopru item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Karakopru.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("35")) {
			List<Akcakale> result = Akcakale.findWithQuery(Akcakale.class,
					sb.toString(), null);
			Akcakale item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Akcakale.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("36")) {
			List<Birecik> result = Birecik.findWithQuery(Birecik.class,
					sb.toString(), null);
			Birecik item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Birecik.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("37")) {
			List<Bozova> result = Bozova.findWithQuery(Bozova.class,
					sb.toString(), null);
			Bozova item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Bozova.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("38")) {
			List<Ceylanpinar> result = Ceylanpinar.findWithQuery(
					Ceylanpinar.class, sb.toString(), null);
			Ceylanpinar item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Ceylanpinar.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("39")) {
			List<Halfeti> result = Halfeti.findWithQuery(Halfeti.class,
					sb.toString(), null);
			Halfeti item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Halfeti.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("40")) {
			List<Harran> result = Harran.findWithQuery(Harran.class,
					sb.toString(), null);
			Harran item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Harran.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("41")) {
			List<Hilvan> result = Hilvan.findWithQuery(Hilvan.class,
					sb.toString(), null);
			Hilvan item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Hilvan.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("42")) {
			List<Siverek> result = Siverek.findWithQuery(Siverek.class,
					sb.toString(), null);
			Siverek item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Siverek.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("43")) {
			List<Suruc> result = Suruc.findWithQuery(Suruc.class,
					sb.toString(), null);
			Suruc item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Suruc.save(item);
		} else if (Constants.SelectedUniversalCountyCode.equals("44")) {
			List<Viransehir> result = Viransehir.findWithQuery(
					Viransehir.class, sb.toString(), null);
			Viransehir item = result.get(0);
			if (!fromUnit) {
				item.DoorNumber = newDoorNum;
				item.BlockName = newBlockName;
				item.SiteName = newSiteName;
			} else {
				item.IndoorNumber = newIndoorNum;
			}
			Viransehir.save(item);
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
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean checkBlockUnitExist(String districtCode,
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
				List<PushRequest> pr = PushRequest.findWithQuery(
						PushRequest.class,
						"SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='" + value
								+ "'", null);
				if (pr.isEmpty()) {
					return false;
				}
			}
			return true;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean IsUUID(String value) {
		boolean isGuid = false;
		try {
			UUID.fromString(value);
			isGuid = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isGuid;
	}

	public static boolean IsIntParseable(String val)
	{
		try {
			@SuppressWarnings("unused")
			int x=Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("Integer parse warning", "Unable to parse string value to int");
			return false;
		}
	}

	public static Class<?> getClassName(){
		try {
			return Class.forName(Constants.SelectedClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public static String getTableName(){
		return NamingHelper.toSQLName(getClassName());
	}

	/**
	 * Getting result from sugarrecord with given sql query
	 * @param fields - for only addressýtem operation for other operation types you can enter null value
	 * @param sql - sql query for getting data from database
	 * @param operationType - operation type enum declared in repository class.ç
	 * @return for addressýtem - List<AddressItem> for block item - List<BlockItem> for  unit item - List<UnitItem>
	 */
	public static ArrayList<?> getResultWithSql(String[] fields,String sql,Repository.OperationObjectType operationType) {
		
		List<?> listOfT=SugarRecord.findWithQuery(Helper.getClassName(),sql,null);
		switch (operationType) {
		case AddressItem:
			ArrayList<AddressListItem> result=new ArrayList<AddressListItem>();
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
	
	public static ArrayList<BlockItem> mapItemsToBlockItemList(List<?> items){
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
	
	public static ArrayList<UnitItem> mapItemsToUnitItemList(List<?> items){
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
	
	public static void configureLog4J(){
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "uavt.log");
		logConfigurator.setRootLevel(Level.ALL);
		logConfigurator.setLevel("org.apache", Level.ALL);
		logConfigurator.configure();
	}
}
