package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.PendingItems;
import com.rdlab.webservice.PushRequest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWiringFragment extends Fragment {

	EditText wiringNo, meterSerno;
	Spinner meterBrands, statuses;
	Button save;
	ArrayList<String> brands = new ArrayList<String>(Arrays.asList("KOHLER",
			"VIKO", "ELEKTROMED", "NAR"));
	ArrayList<String> statusList = new ArrayList<String>(Arrays.asList(
			"PAR�ALANMI�", "B�NA YOK"));
	ArrayAdapter<String> brandAdapter,statusAdapter;
	String uavtAddresNo;
	String doorNumber;

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
		
		uavtAddresNo=bund.getString(Constants.UAVT_TAG);
		doorNumber=bund.getString(Constants.DOOR_NUMBER_TAG);
		
		View rootView = inflater.inflate(R.layout.fragment_add_unit, container,
				false);
		wiringNo = (EditText) rootView.findViewById(R.id.txtWiring);
		meterSerno = (EditText) rootView.findViewById(R.id.txtMeterSerno);
		meterBrands = (Spinner) rootView.findViewById(R.id.spBrands);
		statuses = (Spinner) rootView.findViewById(R.id.spStatuses);
		save = (Button) rootView.findViewById(R.id.btnSave);

		brandAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),R.layout.spinner_item, brands);

		statusAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),R.layout.spinner_item, statusList);

		meterBrands.setAdapter(brandAdapter);
		statuses.setAdapter(statusAdapter);

		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//save to db
				PushRequest pr=new PushRequest();
				pr.setUserSerno(Constants.LoggedUserSerno);
				pr.setDistrictCode(Long.parseLong(Constants.SelectedCountyCode));
				pr.setCreateDate(new java.util.Date().getTime());
				pr.setUavtCode(Long.parseLong(uavtAddresNo));
				pr.setDoorNumber(doorNumber);
				
				String wiringNo,meterNo,selectedBrand,selectedStatus;
				
				wiringNo=AddWiringFragment.this.wiringNo.getText().toString();
				meterNo=AddWiringFragment.this.meterSerno.getText().toString();
				selectedBrand=AddWiringFragment.this.meterBrands.getSelectedItem().toString();
				selectedStatus=AddWiringFragment.this.statuses.getSelectedItem().toString();
				
				if (wiringNo.length()>0) {
					pr.setWiringNo(wiringNo);
				}
				else if(meterNo.length()>0||selectedBrand.length()>0){
					pr.setMeterNo(meterNo);
					pr.setMeterBrand(selectedBrand);
				}
				else if(selectedStatus.length()>0){
					pr.setCheckStatus(1);
				}
				
				PendingItems.PushRequests.add(pr);
				Toast.makeText(getActivity().getApplicationContext(), "Bilgi Eklendi", Toast.LENGTH_LONG).show();
				getActivity().getFragmentManager().popBackStack();
			}
		});
		
		return rootView;
	}

}
