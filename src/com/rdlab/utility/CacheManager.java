package com.rdlab.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rdlab.model.AddressListItem;

public class CacheManager {

	private static CacheManager instance;
	private static Object lockThis = new Object();
	private static Map<String, ArrayList<AddressListItem>> cacheList;

	private CacheManager() {
		// TODO Auto-generated constructor stub
		cacheList = new HashMap<String, ArrayList<AddressListItem>>();
	}

	public static CacheManager getInstance() {
		if (instance == null) {
			synchronized (lockThis) {
				instance = new CacheManager();
			}
		}

		return instance;
	}

	public ArrayList<AddressListItem> getItem(String key) {
		ArrayList<AddressListItem> cacheItem = new ArrayList<AddressListItem>();
		if (cacheList.containsKey(key)) {
			cacheItem = cacheList.get(key);
		}
		return cacheItem;
	}
	
	public void setItem(String key,ArrayList<AddressListItem> items)
	{
		if (!cacheList.containsKey(key)) {
			cacheList.put(key, items);
			return;
		}
		cacheList.remove(key);
		setItem(key, items);
	}
	
	public void ClearCache()
	{
		cacheList.clear();
	}
	
	public boolean checkItemExist(String key){
		return cacheList.containsKey(key);
	}

}
