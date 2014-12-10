package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;

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

public class AddWiringFragment extends Fragment {

	EditText wiringNo, meterSerno;
	Spinner meterBrands, statuses;
	Button save;
	ArrayList<String> brands = new ArrayList<String>(Arrays.asList("KOHLER",
			"VIKO", "ELEKTROMED", "NAR"));
	ArrayList<String> statusList = new ArrayList<String>(Arrays.asList(
			"PARÇALANMIÞ", "BÝNA YOK"));
	ArrayAdapter<String> brandAdapter,statusAdapter;
	String uavtAddresNo;

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
		
		View rootView = inflater.inflate(R.layout.fragment_add_unit, container,
				false);
		wiringNo = (EditText) rootView.findViewById(R.id.txtWiring);
		meterSerno = (EditText) rootView.findViewById(R.id.txtMeterSerno);
		meterBrands = (Spinner) rootView.findViewById(R.id.spBrands);
		statuses = (Spinner) rootView.findViewById(R.id.spStatuses);
		save = (Button) rootView.findViewById(R.id.btnSave);

		brandAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item, brands);

		statusAdapter = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item, statusList);

		meterBrands.setAdapter(brandAdapter);
		statuses.setAdapter(statusAdapter);

		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//save to db
			}
		});
		
		return rootView;
	}

}
