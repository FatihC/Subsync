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
		View rootView = inflater.inflate(R.layout.fragment_sync, container,
				false);

		organizer = new ServiceOrganizer(this, rootView.getContext());

		push = (ImageButton) rootView.findViewById(R.id.push);
		txtPending = (TextView) rootView
				.findViewById(R.id.txtPendingRequestCount);

		int pendingCount = getPendingRequests().size();
		if (pendingCount == 0) {
			txtPending.append("Bekleyen iþ emriniz bulunmamaktadýr");
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
		PushRequest.deleteAll(PushRequest.class);
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
		if (pushRequestList == null) {
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
}
