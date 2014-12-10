package com.rdlab.utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import com.rdlab.model.AddressListItem;

public class Helper {

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
			e.printStackTrace();
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
			Class<?>[] methodArgs, int paramCount, String[]... params) {
		Class<?> selectedClass;
		try {
			selectedClass = Class.forName(Constants.SelectedClassName);
			Method m = selectedClass.getMethod(methodName, methodArgs);

			if (paramCount > 1) {
				return m.invoke(null, selectedClass, params[0], params[1],
						params[2]);
			}
			return m.invoke(null, selectedClass, params[0]);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static Object invokeMethodAnonymousForMBS(String methodName,
			Class<?>[] methodArgs, int paramCount, String[]... params) {
		Class<?> selectedClass;
		try {
			selectedClass = Class.forName(Constants.SelectedClassName+"Mbs");
			Method m = selectedClass.getMethod(methodName, methodArgs);

			if (paramCount > 1) {
				return m.invoke(null, selectedClass, params[0], params[1],
						params[2]);
			}
			return m.invoke(null, selectedClass, params[0]);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
