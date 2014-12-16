package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.model.Configuration;
import com.rdlab.model.PushRequest;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;
import com.rdlab.utility.Helper;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SyncFragment extends Fragment implements ServiceTaskEvent {

	
	//TODO:Ýleride fetch geldiðinde last sync date i alýp ve date.now yollayýp aralýk data alacaksýn
	//TODO:Þuanlýk buna gerek yok.
	ImageButton sync;
	ImageButton push;
	TextView txtPending;
	TextView txtLastSyncDate;
	ServiceOrganizer organizer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sync, container,
				false);

		organizer = new ServiceOrganizer(this, rootView.getContext());

		push = (ImageButton) rootView.findViewById(R.id.push);
		txtPending = (TextView) rootView
				.findViewById(R.id.txtPendingRequestCount);
		txtLastSyncDate=(TextView)rootView.findViewById(R.id.txtLastSyncDate);
		
		String dt=getLastSyncDate();
		if (!dt.isEmpty()) {
			txtLastSyncDate.setText("Son güncelleme tarihi : "+dt);
		}

		int pendingCount = getPendingRequests().size();
		if (pendingCount == 0) {
			txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		} else {
			txtPending.append(String.format(": %d", pendingCount));
		}

		push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				push.setClickable(false);
				sendPendingItems();
			}
		});

		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText(Constants.SYNC_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		return rootView;
	}

	@Override
	public void serviceReturned(Object items) {
		// TODO Auto-generated method stub
		if (items.toString().equals("error") || !items.toString().isEmpty()) {
			Helper.giveNotification(getView().getContext(),
					"Ýþlem sýrasýnda hata oluþtu. Kýsa süre sonra tekrar deneyiniz.");
			push.setClickable(true);
			return;
		}
		Helper.giveNotification(getView().getContext(),
				"Ýþleminiz baþarýyla tamamlanmýþtýr.");
		txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		// PushRequest.deleteAll(PushRequest.class);
		List<PushRequest> pushRequestList = PushRequest
				.listAll(PushRequest.class);
		for (PushRequest pushRequest : pushRequestList) {
			pushRequest.setPushed(true);
			PushRequest.save(pushRequest);
		}
		updateLastSyncDate();
		push.setClickable(false);
	}

	private List<PushRequest> getPendingRequests() {
		List<PushRequest> pushRequestList = PushRequest
				.listAll(PushRequest.class);
		List<PushRequest> result = new ArrayList<PushRequest>();
		for (PushRequest pushRequest : pushRequestList) {
			if (pushRequest.isPushed()) {
				continue;
			}
			result.add(pushRequest);
		}
		return result;
	}

	private void sendPendingItems() {
		List<PushRequest> pushRequestList = getPendingRequests();
		if (pushRequestList == null || pushRequestList.isEmpty()) {
			Helper.giveNotification(getView().getContext(),
					"Gönderilecek herhangi bir eþleþtirme bulunmamaktadýr.");
			return;
		}

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		for (PushRequest pr : pushRequestList) {

			Gson g = new Gson();
			String converted = g.toJson(pr, PushRequest.class);
			paramsx.add(new BasicNameValuePair("", converted));
		}

		ServiceRequest req = new ServiceRequest("push", paramsx);
		new ServiceOrganizer(this, getView().getContext()).execute(req);
	}

	private void updateLastSyncDate() {
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
			cfNew.setValue(DateUtils.nowLong().toString());
			Configuration.save(cfNew);
		}

	}
	
	private String getLastSyncDate(){
		String date="";
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_SYNC_TAG)) {
					try {
						Date dt = DateUtils.parse(DateUtils.yyyyMMddHHmmssSSS, Long.parseLong(configuration.getValue()));
						date=DateUtils.ConvertDateToString(dt);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
			}
		}
		
		return date;
	}
}
