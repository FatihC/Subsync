package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode.Callback;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rdlab.adapters.UnitItemAdapter;
import com.rdlab.adapters.UnitMatchItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.Enums;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.model.PushRequest;
import com.rdlab.model.UnitItem;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.utility.PendingItems;
import com.rdlab.utility.ReadOperation;

public class UnitFragment extends BaseFragment implements DataEvent {

	UnitItemAdapter _adapter;
	UnitMatchItemAdapter _altAdapter;
	ArrayList<UnitItem> addressList;
	ArrayList<PushRequest> requestList;
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
	String doorNumber;
	String siteName;
	String blockName;
	int itemCurrentPosition;

	String message;
	boolean backPressed = false;
	boolean dialogShowed = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// clearing push request

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
		siteName = bund.getString(Constants.SITE_NAME_TAG);
		blockName = bund.getString(Constants.BLOCK_NAME_TAG);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s > %s > %s >", streetName.trim(),
				csbmName.trim(), doorNumber.trim()));

		View rootView = inflater.inflate(R.layout.fragment_block, container,
				false);

		((TextView) rootView.findViewById(R.id.blockSelectedValues)).setText(sb
				.toString());
		searchText = (EditText) rootView.findViewById(R.id.searchBlockText);
		searchResult = (ListView) rootView.findViewById(R.id.blockList);
		searchText.setHint("Ýç Kapý No ile Ara");

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
				UnitItem item = addressList.get(--arg2);

				AddWiringFragment df = new AddWiringFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DOOR_NUMBER_TAG, doorNumber);
				b.putString(Constants.UAVT_TAG, item.getUAVTNo());
				b.putString(Constants.INDOOR_TAG, item.getIndoorNumber());
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.SITE_NAME_TAG, siteName);
				b.putString(Constants.BLOCK_NAME_TAG, blockName);
				b.putBoolean(Constants.CHECKED_UAVT, item.isSynced());

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

		// Action bar overriden
		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		addButton = (Button) ab.getCustomView().findViewById(R.id.btnAddNew);
		addButton.setText("Birim Ekle");
		addButton.setVisibility(View.VISIBLE);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddNewUnitFragment df = new AddNewUnitFragment();
				Bundle b = new Bundle();
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, csbmName);
				b.putString(Constants.DOOR_NUMBER_TAG, doorNumber);
				b.putString(Constants.SITE_NAME_TAG, siteName);
				b.putString(Constants.BLOCK_NAME_TAG, blockName);

				df.setArguments(b);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, df);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();
			}
		});


		info.setText(Constants.UNIT_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		View header = inflater.inflate(R.layout.header_unit_list_item, null);
		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				return;
			}
		});
		searchResult.addHeaderView(header);

		//back button implementation
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					backPressed = true;
					message = "Tüm eþleþmeler yapýlmadan çýkarsanýz verileriniz silinecektir. Çýkmak istediðinize emin misiniz?";
					if (checkItemsForMatch()) {
						try {
							doBulkOperations();
							Helper.updateStatus(districtCode, villageCode,
									streetCode, csbmCode, doorNumber, "",
									Enums.ReadyToSync,false);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getActivity().getFragmentManager().popBackStack();
					} else if (!dialogShowed) {
						warnUser();

					}
					return true;
				} else {
					return false;
				}
			}
		});

		getData(rootView);
		return rootView;
	}

	private void doBulkOperations() {
		for (PushRequest pr : PendingItems.PushRequests) {
			String sql="SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='"+pr.uavtCode+"'";
			List<PushRequest> prList=PushRequest.findWithQuery(PushRequest.class, sql, null);
			if (prList.size()>0) {
				PushRequest itemToUpdate=prList.get(0);
				itemToUpdate.setPushed(false);
				itemToUpdate.wiringNo=pr.wiringNo;
				itemToUpdate.customerName=pr.customerName;
				itemToUpdate.meterBrand=pr.meterBrand;
				itemToUpdate.meterBrandCode=pr.meterBrandCode;
				itemToUpdate.meterNo=pr.meterNo;
				itemToUpdate.checkStatus=pr.checkStatus;
				itemToUpdate.blockName=pr.blockName;
				itemToUpdate.siteName=pr.siteName;
				itemToUpdate.doorNumber=pr.doorNumber;
				itemToUpdate.indoorNumber=pr.indoorNumber;
				PushRequest.save(itemToUpdate);
			}
			else {
				PushRequest.save(pr);	
			}
		}

		for (String str : PendingItems.IndoorNumbers) {
			try {
				Helper.updateStatus(districtCode, villageCode, streetCode,
						csbmCode, doorNumber, str, Enums.ReadyToSync,false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e("Update wiring error", "hata oluþtu update ederken");
				e.printStackTrace();
			}
		}

		PendingItems.PushRequests.clear();
		PendingItems.IndoorNumbers.clear();
	}

	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<UnitItem> castedItems = (ArrayList<UnitItem>) items;
		setListView(castedItems);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void setListView(ArrayList<UnitItem> items) {
		getMatches(items);
		_adapter = new UnitItemAdapter(getActivity().getApplicationContext(),
				0, items,false);
		addressList = items;
		searchResult.setAdapter(_adapter);
		// searchResult.setAdapter(_altAdapter);
	}

	private boolean checkItemsForMatch() {
		for (UnitItem unt : addressList) {
			if (!unt.isSynced()) {
				return false;
			}
		}

		return true;
	}

	private void getMatches(ArrayList<UnitItem> items) {
		for (UnitItem item : items) {
			PushRequest selected = null;
			for (PushRequest pr : PendingItems.PushRequests) {
				String pUavt = pr.uavtCode;
				String cmp = item.getUAVTNo();
				if (pUavt.equals(cmp)) {
					selected = pr;
				}
			}
			if (selected != null) {
				item.setMeterpointBrand(selected.meterBrand);
				item.setMeterpointSerno(selected.meterNo);
				if (selected.checkStatus != null) {
					if (Helper.IsIntParseable(selected.checkStatus)) {
						item.setStatusName(Constants.STATUSES.get(Integer
								.parseInt(selected.checkStatus)));	
					}
					else {
						item.setStatusName("");	
					}
				}

				item.setSubscriberName(selected.customerName);
				item.setWiringNo(selected.wiringNo);
				item.setCheckStatus(Enums.Completed.getVal());
			} else {
				String sql = "SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='"
						+ item.getUAVTNo() + "'";
				List<PushRequest> pr = PushRequest.findWithQuery(
						PushRequest.class, sql, null);
				if (pr.size() > 0) {
					PushRequest prItem = pr.get(0);
					item.setMeterpointBrand(prItem.meterBrand);
					item.setMeterpointSerno(prItem.meterNo);
					if (prItem.checkStatus != null) {
						if (Helper.IsIntParseable(prItem.checkStatus)) {
							item.setStatusName(Constants.STATUSES.get(Integer
									.parseInt(prItem.checkStatus)));	
						}
						else {
							item.setStatusName("");	
						}
					}

					item.setSubscriberName(prItem.customerName);
					item.setWiringNo(prItem.wiringNo);
					item.setCheckStatus(Enums.Completed.getVal());
				}
			}
		}
	}

	private void getData(View rootView) {

		ItemConditions cond = new ItemConditions();
		cond.setCSBMCode(csbmCode);
		cond.setStreetCode(streetCode);
		cond.setDistrictCode(districtCode);
		cond.setVillageCode(villageCode);
		cond.setDoorNumber(doorNumber);
		operation = new ReadOperation(rootView.getContext(), this, cond,false);
		operation.execute(ItemType.Indoor);
	}

	private void warnUser() {
		dialogShowed = true;
		AlertDialog dlg = new AlertDialog.Builder(getView().getContext())
				.create();
		dlg.setCancelable(false);
		dlg.setTitle("Uyarý");
		dlg.setMessage(message);
		dlg.setButton(DialogInterface.BUTTON_POSITIVE, "Evet",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						PendingItems.PushRequests.clear();
						PendingItems.IndoorNumbers.clear();
						getActivity().getFragmentManager().popBackStack();
						dialogShowed = false;
					}
				});

		dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Hayýr",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialogShowed = false;
						return;
					}
				});
		dlg.show();
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
					
					
					UnitItem unitItem = _adapter.getItem(--itemCurrentPosition);

					if (_adapter.getCount()<2) {
						Helper.giveNotification(getView().getContext(),
								"Dýþ kapýya baðlý tek birimi dýþ kapý silerek gerçekleþtiriniz.");
						mode.finish();
						break;
					}
					
					if (unitItem.getCheckStatus() != Enums.NewlyAdded.getVal()
							&& !Helper.IsUUID(unitItem.getUAVTNo())) {
						Helper.giveNotification(getView().getContext(),
								"Manuel eklenen veriler silinebilir.");
						mode.finish();
						break;
					}

					for (PushRequest prItem : PendingItems.PushRequests) {
						String prUavt = prItem.uavtCode;
						String checUavt = unitItem.getUAVTNo();
						if (prUavt.equals(checUavt)) {
							PendingItems.PushRequests.remove(prItem);
							break;
						}
					}
					
					Helper.deleteItem(districtCode, villageCode, streetCode, csbmCode, doorNumber, unitItem.getIndoorNumber(),unitItem.getUAVTNo());
					
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
				UnitItem unitItem = _adapter.getItem(--itemCurrentPosition);
				if (unitItem.getCheckStatus() != Enums.NewlyAdded.getVal()
						&& !Helper.IsUUID(unitItem.getUAVTNo())) {
					Helper.giveNotification(getView().getContext(),
							"Manuel eklenen veriler düzenlenebilir.");
					mode.finish();
					break;
				}

				AddNewUnitFragment df = new AddNewUnitFragment();
				Bundle b = new Bundle();
				//ilk item için indoor boþ olacak
				if (unitItem.getIndoorNumber().isEmpty()||unitItem.getIndoorNumber()==null) {
					b.putString(Constants.EDIT_INDOOR, "");
				}
				else {
					b.putString(Constants.EDIT_INDOOR, unitItem.getIndoorNumber());
				}
				b.putString(Constants.DISTRICT_CODE_TAG, districtCode);
				b.putString(Constants.VILLAGE_CODE_TAG, villageCode);
				b.putString(Constants.STREET_CODE_TAG, streetCode);
				b.putString(Constants.CSBM_CODE_TAG, csbmCode);
				b.putString(Constants.DISTRICT_NAME_TAG, districtName);
				b.putString(Constants.VILLAGE_NAME_TAG, villageName);
				b.putString(Constants.STREET_NAME_TAG, streetName);
				b.putString(Constants.CSBM_NAME_TAG, csbmName);
				b.putString(Constants.DOOR_NUMBER_TAG, doorNumber);
				b.putString(Constants.SITE_NAME_TAG, siteName);
				b.putString(Constants.BLOCK_NAME_TAG, blockName);

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
