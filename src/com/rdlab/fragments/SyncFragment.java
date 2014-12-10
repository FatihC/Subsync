package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.subssync.R;
import com.rdlab.utility.PendingItems;
import com.rdlab.webservice.PushRequest;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SyncFragment extends Fragment implements ServiceTaskEvent {

	ImageButton sync;
	ImageButton push;
	ServiceOrganizer organizer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_sync, container, false);
		
		organizer = new ServiceOrganizer(this, rootView.getContext());
		
		push=(ImageButton)rootView.findViewById(R.id.push);
		
		push.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendPendingItems();
			}
		});
		
		return rootView;
	}


	@Override
	public void serviceReturned(Object items) {
		// TODO Auto-generated method stub
		//String x=items.toString();
	}
	
	private void sendPendingItems(){
		PushRequest pr=PendingItems.PushRequests.get(0);
		
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("userSerno",pr.getUserSerno().toString()));
		paramsx.add(new BasicNameValuePair("districtCode",pr.getDistrictCode().toString()));
		paramsx.add(new BasicNameValuePair("createDate",pr.getCreateDate().toString()));
		paramsx.add(new BasicNameValuePair("uavtCode",pr.getUavtCode().toString()));
		paramsx.add(new BasicNameValuePair("doorNumber",pr.getDoorNumber().toString()));
		paramsx.add(new BasicNameValuePair("existOnUavt","0"));
		
		if (pr.getWiringNo()!=null) {
			paramsx.add(new BasicNameValuePair("wiringNo",pr.getWiringNo().toString()));
			paramsx.add(new BasicNameValuePair("customerName",pr.getCustomerName().toString()));
		}
		if (pr.getMeterNo()!=null) {
			paramsx.add(new BasicNameValuePair("meterNo",pr.getMeterNo().toString()));
			paramsx.add(new BasicNameValuePair("meterBrand",pr.getMeterBrand().toString()));
		}
		if (pr.getCheckStatus()!=null) {
			paramsx.add(new BasicNameValuePair("checkStatus",pr.getCheckStatus().toString()));
		}
		
		ServiceRequest req = new ServiceRequest("push", paramsx);
		organizer.execute(req);
	}
}
