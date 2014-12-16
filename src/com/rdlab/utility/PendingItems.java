package com.rdlab.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rdlab.model.PushRequest;

public class PendingItems {
	public static List<PushRequest> PushRequests=Collections.synchronizedList(new ArrayList<PushRequest>());
	public static List<String> IndoorNumbers=Collections.synchronizedList(new ArrayList<String>());
	
}
