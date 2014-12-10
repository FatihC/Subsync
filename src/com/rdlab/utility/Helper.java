package com.rdlab.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

		return new AddressListItem( code, name);

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

}
