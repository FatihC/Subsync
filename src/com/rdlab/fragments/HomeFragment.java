package com.rdlab.fragments;

import java.util.ArrayList;

import com.rdlab.adapters.AddressItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.AddressListItem;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.subssync.R;
import com.rdlab.utility.CacheManager;
import com.rdlab.utility.Constants;
import com.rdlab.utility.ReadOperation;

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

public class HomeFragment extends BaseFragment implements DataEvent {

	AddressItemAdapter _adapter;
	ArrayList<AddressListItem> addressList;
	ArrayList<AddressListItem> filteredList;
	EditText searchText;
	ListView searchResult;
	ReadOperation operation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		getActivity().getActionBar().setTitle("ÝLÇE SEÇÝMÝ");

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		searchText = (EditText) rootView.findViewById(R.id.searchText);
		searchResult = (ListView) rootView.findViewById(R.id.addressItemList);

		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

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

				DistrictFragment df = new DistrictFragment();
				Bundle b = new Bundle();
				b.putInt(Constants.COUNTY_CODE_TAG, item.GetId());
				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		if (savedInstanceState!=null) {
			searchResult.onRestoreInstanceState(savedInstanceState.getParcelable(Constants.HOME_LIST_STATE));
		}
		getData(rootView);

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
		outState.putParcelable(Constants.HOME_LIST_STATE, searchResult.onSaveInstanceState());
		super.onSaveInstanceState(outState);
	}

	private void setListView(ArrayList<AddressListItem> items) {
		_adapter = new AddressItemAdapter(
				getActivity().getApplicationContext(), 0, items);
		addressList = items;
		searchResult.setAdapter(_adapter);
	}

	private void getData(View rootView) {

		if (CacheManager.getInstance()
				.checkItemExist(Constants.COUNTY_CODE_TAG)) {
			setListView(CacheManager.getInstance().getItem(
					Constants.COUNTY_CODE_TAG));
			return;
		}

		ItemConditions cond = new ItemConditions();
		cond.setCityCode(Constants.CITY_CODE);
		operation = new ReadOperation(rootView.getContext(), this, cond);
		operation.execute(ItemType.County);
	}

}
