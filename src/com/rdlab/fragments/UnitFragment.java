package com.rdlab.fragments;

import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rdlab.adapters.UnitItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.model.UnitItem;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.ReadOperation;

public class UnitFragment extends BaseFragment implements DataEvent {
	
	UnitItemAdapter _adapter;
	ArrayList<UnitItem> addressList;
	EditText searchText;
	ListView searchResult;
	ImageButton addButton;
	ReadOperation operation;
	String csbmCode;
	String csbmName;
	String streetCode;
	String streetName;
	String villageCode;
	String villageName;
	String districtCode;
	String districtName;
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

		csbmCode = bund.getString(Constants.CSBM_CODE_TAG);
		streetCode = bund.getString(Constants.STREET_CODE_TAG);
		villageCode = bund.getString(Constants.VILLAGE_CODE_TAG);
		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);
		
		streetName = bund.getString(Constants.STREET_NAME_TAG);
		villageName = bund.getString(Constants.VILLAGE_NAME_TAG);
		districtName = bund.getString(Constants.DISTRICT_NAME_TAG);
		csbmName = bund.getString(Constants.CSBM_NAME_TAG);
		doorNumber = bund.getString(Constants.DOOR_NUMBER_TAG);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s > %s > %s > %s > %s > %s", Constants.SelectedCountyName,
				districtName.trim(), villageName.trim(),streetName.trim(),csbmName.trim(),doorNumber.trim()));

		View rootView = inflater.inflate(R.layout.fragment_block, container,
				false);

		((TextView)rootView.findViewById(R.id.headerBlockText)).setText(Constants.BLOCK_HEADER_TEXT);
		((TextView)rootView.findViewById(R.id.blockSelectedValues)).setText(sb.toString());
		addButton=(ImageButton)rootView.findViewById(R.id.addNewBlockAdd);
		searchText = (EditText) rootView.findViewById(R.id.searchBlockText);
		searchResult = (ListView) rootView.findViewById(R.id.blockList);
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				_adapter.getFilter().filter(arg0);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UnitItem item = addressList.get(arg2);

				AddWiringFragment df = new AddWiringFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DOOR_NUMBER_TAG, doorNumber);
				b.putString(Constants.UAVT_TAG, item.getUAVTNo());
				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		getData(rootView);
		
		return rootView;
	}

	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<UnitItem> castedItems=(ArrayList<UnitItem>) items;
		setListView(castedItems);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private void setListView(ArrayList<UnitItem> items){
		_adapter = new UnitItemAdapter( getActivity()
				.getApplicationContext(),0,items);
		addressList = items;
		searchResult.setAdapter(_adapter);
	}

	private void getData(View rootView) {

		ItemConditions cond = new ItemConditions();
		cond.setCSBMCode(csbmCode);
		cond.setStreetCode(streetCode);
		cond.setDistrictCode(districtCode);
		cond.setVillageCode(villageCode);
		cond.setDoorNumber(doorNumber);
		operation = new ReadOperation(rootView.getContext(), this,cond);
		operation.execute(ItemType.Indoor);
	}

}
