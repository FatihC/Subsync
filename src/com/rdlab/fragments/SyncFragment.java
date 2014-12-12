package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.model.PushRequest;
import com.rdlab.subssync.R;
import com.rdlab.utility.Helper;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SyncFragment extends Fragment implements ServiceTaskEvent {

	ImageButton sync;
	ImageButton push;
	TextView txtPending;
	ServiceOrganizer organizer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_sync, container, false);
		
		organizer = new ServiceOrganizer(this, rootView.getContext());
		
		push=(ImageButton)rootView.findViewById(R.id.push);
		txtPending=(TextView)rootView.findViewById(R.id.txtPendingRequestCount);
		
		txtPending.append(String.format(": %d", getPendingRequests().size()));
		
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
		if (items.toString().equals("error")) {
			Helper.giveNotification(getView().getContext(), "Ýþlem sýrasýnda hata oluþtu. Kýsa süre sonra tekrar deneyiniz.");
			return;
		}
		
		Helper.giveNotification(getView().getContext(), "Ýþleminiz baþarýyla tamamlanmýþtýr.");
		txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		updatePendingRequest();
	}
	
	private void updatePendingRequest(){
		List<PushRequest> pushRequestList=getPendingRequests();
		for (PushRequest pushRequest : pushRequestList) {
			if (!pushRequest.isPushed()) {
				pushRequest.setPushed(true);
				pushRequest.save();
			}
		}
	}
	
	private List<PushRequest> getPendingRequests(){
		List<PushRequest> pushRequestList=PushRequest.listAll(PushRequest.class);
		List<PushRequest> result=new ArrayList<PushRequest>();
		for (PushRequest pushRequest : pushRequestList) {
			if (pushRequest.isPushed()) {
				continue;
			}
			result.add(pushRequest);
		}
		return result;
	}
	
	private void sendPendingItems(){
		List<PushRequest> pushRequestList=getPendingRequests();
		if (pushRequestList==null) {
			Helper.giveNotification(getView().getContext(), "Gönderilecek herhangi bir eþleþtirme bulunmamaktadýr.");
			return;
		}
		
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		for (PushRequest pr : pushRequestList) {
			
			Gson g=new Gson();
			String converted=g.toJson(pr, PushRequest.class);
			paramsx.add(new BasicNameValuePair("",converted));
			/*paramsx.add(new BasicNameValuePair("userSerno",pr.getUserSerno().toString()));
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
			}*/
		}
		
		
		
		ServiceRequest req = new ServiceRequest("push", paramsx);
		new ServiceOrganizer(this, getView().getContext()).execute(req);
	}
}
