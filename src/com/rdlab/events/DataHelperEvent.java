package com.rdlab.events;

import java.util.ArrayList;


import com.rdlab.model.AddressListItem;

public interface DataHelperEvent {
	void  OnDataRetrieved(ArrayList<AddressListItem> items);
}
