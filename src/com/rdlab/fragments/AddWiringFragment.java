package com.rdlab.fragments;

import com.rdlab.events.DataEvent;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.model.PushRequest;
import com.rdlab.model.SubscriberItem;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;
import com.rdlab.utility.Helper;
import com.rdlab.utility.ReadOperation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddWiringFragment extends Fragment implements DataEvent {

	EditText wiringNo, meterSerno;
	TextView title;
	Spinner meterBrands, statuses;
	Button save, search, cancel;
	ArrayAdapter<String> brandAdapter, statusAdapter;
	String uavtAddresNo;
	String doorNumber;
	String csbmCode;
	String streetCode;
	String villageCode;
	String districtCode;
	String indoorNumber;
	boolean isSync;

	ReadOperation operator;
	boolean foundStatusSbs = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Bundle bund = new Bundle();
		if (savedInstanceState != null) {
			bund = savedInstanceState;
		} else {
			bund = getArguments();
		}

		uavtAddresNo = bund.getString(Constants.UAVT_TAG);
		doorNumber = bund.getString(Constants.DOOR_NUMBER_TAG);
		indoorNumber = bund.getString(Constants.INDOOR_TAG);
		csbmCode = bund.getString(Constants.CSBM_CODE_TAG);
		streetCode = bund.getString(Constants.STREET_CODE_TAG);
		villageCode = bund.getString(Constants.VILLAGE_CODE_TAG);
		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);
		isSync = bund.getBoolean(Constants.CHECKED_UAVT);

		final View rootView = inflater.inflate(R.layout.fragment_add_unit,
				container, false);
		wiringNo = (EditText) rootView.findViewById(R.id.txtWiring);
		meterSerno = (EditText) rootView.findViewById(R.id.txtMeterSerno);
		meterBrands = (Spinner) rootView.findViewById(R.id.spBrands);
		statuses = (Spinner) rootView.findViewById(R.id.spStatuses);
		save = (Button) rootView.findViewById(R.id.btnSave);
		search = (Button) rootView.findViewById(R.id.btnSearch);
		title = (TextView) rootView.findViewById(R.id.txtTitle);
		cancel = (Button) rootView.findViewById(R.id.btnCancel);

		brandAdapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), R.layout.spinner_item,
				Constants.METER_BRANDS);

		statusAdapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), R.layout.spinner_item,
				Constants.STATUSES);

		meterBrands.setAdapter(brandAdapter);
		statuses.setAdapter(statusAdapter);
		
		rootView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1==KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// save to db
				Integer stat = !isSync ? 1 : 0;

				PushRequest pr = new PushRequest();
				// pr.setUserSerno(Constants.LoggedUserSerno);
				pr.setUserSerno(Constants.LoggedUserSerno);
				pr.setDistrictCode(Constants.SelectedUniversalCountyCode);
				pr.setCsbmCode(csbmCode);
				pr.setIndoorNumber(indoorNumber);
				pr.setStreetCode(streetCode);
				pr.setVillageCode(villageCode);
				pr.setCreateDate(DateUtils.nowLong());
				pr.setExistOnUavt(stat);
				pr.setUavtCode(uavtAddresNo);
				pr.setDoorNumber(doorNumber);
				pr.setPushed(false);

				String wiringNo = "", meterNo = "", selectedBrand = "", selectedStatus = "";

				wiringNo = AddWiringFragment.this.wiringNo.getText().toString();
				meterNo = AddWiringFragment.this.meterSerno.getText()
						.toString();
				selectedBrand = AddWiringFragment.this.meterBrands
						.getSelectedItem().toString();
				selectedStatus = AddWiringFragment.this.statuses
						.getSelectedItem().toString();

				if (foundStatusSbs) {
					pr.setWiringNo(wiringNo);
					pr.setCustomerName(title.getText().toString());
				} else {
					if (meterNo.length() > 0 && selectedBrand.length() > 0) {
						pr.setMeterNo(meterNo);
						pr.setMeterBrand(selectedBrand);
					} else if (selectedStatus.length() > 0) {
						pr.setCheckStatus(""+AddWiringFragment.this.statuses.getSelectedItemId());
					} else {
						Helper.giveNotification(getView().getContext(), Constants.NO_ITEM_SELECTED);
						return;
					}
				}

				PushRequest.save(pr);

				Toast.makeText(getActivity().getApplicationContext(),
						"Bilgi Eklendi", Toast.LENGTH_LONG).show();
				getActivity().getFragmentManager().popBackStack();
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchWiring(rootView);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getFragmentManager().popBackStack();
			}
		});

		return rootView;
	}

	
	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		if (items != null) {
			foundStatusSbs = true;
			SubscriberItem sb = (SubscriberItem) items;
			title.setText(sb.getUnvan());
			Helper.giveNotification(getView().getContext(), "Abone bulundu.");

			return;
		}

		Helper.giveNotification(getView().getContext(), "Abone bulunamadý.");
	}

	private void searchWiring(View rootView) {
		ItemConditions cond = new ItemConditions();
		cond.setTesisatNo(Long.parseLong(wiringNo.getText().toString()));
		operator = new ReadOperation(rootView.getContext(), this, cond);
		operator.execute(ItemType.Subscriber);
	}

}
