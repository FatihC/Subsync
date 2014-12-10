package com.rdlab.data;

import java.util.ArrayList;

import com.rdlab.model.AddressListItem;
import com.rdlab.model.BlockItem;
import com.rdlab.model.SubscriberItem;
import com.rdlab.model.UnitItem;

public interface IRepository {
	ArrayList<AddressListItem> getDistrictItems(String... params);
	ArrayList<AddressListItem> getVillageItems(String... params);
	ArrayList<AddressListItem> getStreetItems(String... params);
	ArrayList<AddressListItem> getCSBMItems(String... params);
	ArrayList<BlockItem> getBlockItems(String... params);
	ArrayList<UnitItem> getUnitItems(String... params);
	SubscriberItem getSubscriberDetail(String...params);
}
