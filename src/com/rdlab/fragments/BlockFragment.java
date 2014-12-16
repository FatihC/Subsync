package com.rdlab.fragments;

import java.util.ArrayList;

import com.rdlab.adapters.BlockItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.BlockItem;
import com.rdlab.model.Enums;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.utility.ReadOperation;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionMode.Callback;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class BlockFragment extends BaseFragment implements DataEvent {

	BlockItemAdapter _adapter;
	ArrayList<BlockItem> addressList;
	EditText searchText;
	ListView searchResult;
	Button addButton;
	Button saveMatchButton;
	ReadOperation operation;
	String csbmCode;
	String csbmName;
	String streetCode;
	String streetName;
	String villageCode;
	String villageName;
	String districtCode;
	String districtName;
	int itemCurrentPosition;

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

		StringBuilder sb = new StringBuilder();
		// sb.append(String.format("%s > %s > %s > %s > %s",
		// Constants.SelectedCountyName,
		// districtName.trim(),
		// villageName.trim(),streetName.trim(),csbmName.trim()));
		sb.append(String.format(" %s > %s >", streetName.trim(),
				csbmName.trim()));

		View rootView = inflater.inflate(R.layout.fragment_block, container,
				false);

		((TextView) rootView.findViewById(R.id.blockSelectedValues)).setText(sb
				.toString());
		searchText = (EditText) rootView.findViewById(R.id.searchBlockText);
		searchResult = (ListView) rootView.findViewById(R.id.blockList);
		searchText.setHint("Dýþ Kapý No ile Ara");
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
				BlockItem item = addressList.get(--arg2);

				UnitFragment df = new UnitFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, csbmName);
				b.putString(Constants.DOOR_NUMBER_TAG, item.getDoorNumber());
				b.putString(Constants.SITE_NAME_TAG, item.getSiteName());
				b.putString(Constants.BLOCK_NAME_TAG, item.getBlockName());
				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();

			}

		});

		searchResult.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				arg0.startActionMode(modeCallback);
				arg0.setSelected(true);
				itemCurrentPosition = arg2;
				return true;
			}
		});

		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		addButton = (Button) ab.getCustomView().findViewById(R.id.btnAddNew);
		addButton.setText("Dýþ Kapý Ekle");
		addButton.setVisibility(View.VISIBLE);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddDoorFragment df = new AddDoorFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, csbmName);
				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		saveMatchButton = (Button) ab.getCustomView()
				.findViewById(R.id.btnSave);
		saveMatchButton.setVisibility(View.GONE);

		info.setText(Constants.BLOCK_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		View header = inflater.inflate(R.layout.header_block_list_item, null);
		searchResult.addHeaderView(header);

		getData(rootView);

		return rootView;
	}

	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<BlockItem> castedItems = (ArrayList<BlockItem>) items;
		setListView(castedItems);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(Constants.CSBM_CODE_TAG, csbmCode);
		super.onSaveInstanceState(outState);
	}

	private void setListView(ArrayList<BlockItem> items) {
		// Parcelable state=searchResult.onSaveInstanceState();
		_adapter = new BlockItemAdapter(getActivity().getApplicationContext(),
				0, items);
		addressList = items;
		searchResult.setAdapter(_adapter);
		// searchResult.onRestoreInstanceState(state);
	}

	private void getData(View rootView) {

		ItemConditions cond = new ItemConditions();
		cond.setCSBMCode(csbmCode);
		cond.setStreetCode(streetCode);
		cond.setDistrictCode(districtCode);
		cond.setVillageCode(villageCode);
		operation = new ReadOperation(rootView.getContext(), this, cond);
		operation.execute(ItemType.Block);
	}

	private ActionMode.Callback modeCallback = new Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("Detay");
			mode.getMenuInflater().inflate(R.menu.contextmenu_listview, menu);
			return true;
		}


		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.delete: {
				try {
					BlockItem blockItem = _adapter
							.getItem(--itemCurrentPosition);
					boolean containsNewlyAdded=Helper.checkBlockUnitsNewlyAdded(districtCode, villageCode, streetCode, csbmCode, blockItem.getDoorNumber());
					
					if (blockItem.getCheckStatus() != Enums.NewlyAdded.getVal()&&!containsNewlyAdded) {
						Helper.giveNotification(getView().getContext(),
								"Manuel eklenen veriler silinebilir.");
						mode.finish();
						break;
					}
					Helper.updateStatus(districtCode, villageCode, streetCode,
							csbmCode, blockItem.getDoorNumber(), "",
							Enums.Deleted);
					_adapter.notifyDataSetChanged();
					getData(getView());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mode.finish();
				break;
			}
			case R.id.edit: {
				BlockItem blockItem = _adapter.getItem(--itemCurrentPosition);
				boolean containsNewlyAdded=Helper.checkBlockUnitsNewlyAdded(districtCode, villageCode, streetCode, csbmCode, blockItem.getDoorNumber());
				
				if (blockItem.getCheckStatus() != Enums.NewlyAdded.getVal()&&!containsNewlyAdded) {
					Helper.giveNotification(getView().getContext(),
							"Manuel eklenen veriler düzenlenebilir.");
					mode.finish();
					break;
				}
				AddDoorFragment df = new AddDoorFragment();
				Bundle b = new Bundle();
				b.putString(Constants.EDIT_BLOCK, blockItem.getBlockName());
				b.putString(Constants.EDIT_OUTDOOR, blockItem.getDoorNumber());
				b.putString(Constants.EDIT_SITE, blockItem.getSiteName());

				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, csbmName);
				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();
				mode.finish();
				break;
			}

			default:
				return false;
			}
			return true;
		}
	};
}
