package com.rdlab.fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rdlab.adapters.AddressItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.AddressListItem;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.ReadOperation;

public class VillageFragment extends BaseFragment implements DataEvent {

	
	AddressItemAdapter _adapter;
	ArrayList<AddressListItem> addressList;
	ArrayList<AddressListItem> filteredList;
	EditText searchText;
	ListView searchResult;
	ReadOperation operation;
	String districtCode;
	String districtName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bund = new Bundle();
		if (savedInstanceState != null) {
			bund = savedInstanceState;
		} else {
			bund = getArguments();
		}

		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);
		districtName= bund.getString(Constants.DISTRICT_NAME_TAG);
		
		StringBuilder sb=new StringBuilder();
		sb.append(String.format("%s > %s", Constants.SelectedCountyName,districtName.trim()));

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		((TextView)rootView.findViewById(R.id.selectedValues)).setText(sb.toString());
		searchText = (EditText) rootView.findViewById(R.id.searchText);
		searchResult = (ListView) rootView.findViewById(R.id.addressItemList);
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				/*
				 * filteredList=Helper.filterItem(arg0.toString(), addressList);
				 * 
				 * _adapter.getData().clear();
				 * _adapter.getData().addAll(filteredList);
				 * _adapter.notifyDataSetChanged();
				 * searchResult.invalidateViews();
				 */

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

				AddressListItem item = addressList.get(arg2);

				StreetFragment df = new StreetFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_CODE_TAG, item.GetCode());
				b.putString(Constants.VILLAGE_NAME_TAG, item.GetName());
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
		info.setText(Constants.VILLAGE_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME|ActionBar.NAVIGATION_MODE_LIST|ActionBar.DISPLAY_HOME_AS_UP);
		
		return rootView;
	}

	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<AddressListItem> castedItems=(ArrayList<AddressListItem>) items;
		setListView(castedItems);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(Constants.DISTRICT_CODE_TAG, districtCode);
		super.onSaveInstanceState(outState);
	}
	
	private void setListView(ArrayList<AddressListItem> items){
		_adapter = new AddressItemAdapter( getActivity()
				.getApplicationContext(),0,items);
		addressList = items;
		searchResult.setAdapter(_adapter);
	}

	private void getData(View rootView) {

		ItemConditions cond = new ItemConditions();
		cond.setDistrictCode(districtCode);
		operation = new ReadOperation(rootView.getContext(), this,cond);
		operation.execute(ItemType.Village);
	}

}
