package com.rdlab.fragments;

import java.util.ArrayList;

import com.rdlab.adapters.AddressItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.AddressListItem;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.ReadOperation;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CSBMFragment extends BaseFragment implements DataEvent {

	AddressItemAdapter _adapter;
	ArrayList<AddressListItem> addressList;
	ArrayList<AddressListItem> filteredList;
	EditText searchText;
	ListView searchResult;
	ReadOperation operation;
	String streetCode;
	String streetName;
	String villageCode;
	String villageName;
	String districtCode;
	String districtName;
	boolean forControl=false;

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

		streetCode = bund.getString(Constants.STREET_CODE_TAG);
		streetName = bund.getString(Constants.STREET_NAME_TAG);
		villageCode = bund.getString(Constants.VILLAGE_CODE_TAG);
		villageName = bund.getString(Constants.VILLAGE_NAME_TAG);
		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);
		districtName = bund.getString(Constants.DISTRICT_NAME_TAG);
		forControl = bund.getBoolean(Constants.FOR_CONTROL);

		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("%s > %s > %s > %S", Constants.SelectedCountyName,
//				districtName.trim(), villageName.trim(),streetName.trim()));
		sb.append(String.format("%s >",streetName.trim()));

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		((TextView)rootView.findViewById(R.id.selectedValues)).setVisibility(View.VISIBLE);
		((TextView)rootView.findViewById(R.id.selectedValues)).setText(sb.toString());
		searchText = (EditText) rootView.findViewById(R.id.searchText);
		searchResult = (ListView) rootView.findViewById(R.id.addressItemList);
		searchText.setHint("C/S/B/M Ara");
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
				AddressListItem item = (AddressListItem) arg0.getItemAtPosition(arg2);

				BlockFragment df = new BlockFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, item.GetCode());
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, item.GetName());
				b.putBoolean(Constants.FOR_CONTROL, forControl);
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
		
		ActionBar ab=getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info=(TextView)ab.getCustomView().findViewById(R.id.txtTitle);
		info.setText(Constants.CSBM_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME|ActionBar.NAVIGATION_MODE_LIST|ActionBar.DISPLAY_HOME_AS_UP);

		return rootView;
	}

	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<AddressListItem> castedItems = (ArrayList<AddressListItem>) items;
		setListView(castedItems);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(Constants.STREET_CODE_TAG, streetCode);

		super.onSaveInstanceState(outState);
	}

	private void setListView(ArrayList<AddressListItem> items) {
		_adapter = new AddressItemAdapter(
				getActivity().getApplicationContext(), 0, items);
		addressList = items;
		searchResult.setAdapter(_adapter);
	}

	private void getData(View rootView) {
		ItemConditions cond = new ItemConditions();
		cond.setStreetCode(streetCode);
		cond.setDistrictCode(districtCode);
		cond.setVillageCode(villageCode);
		operation = new ReadOperation(rootView.getContext(), this, cond,forControl);
		operation.execute(ItemType.CSBM);
	}

}
