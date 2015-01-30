package com.rdlab.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rdlab.adapters.UnitItemAdapter;
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

public class UnitControlFragment extends BaseFragment implements DataEvent {

	private final static Logger log = Logger.getLogger(UnitControlFragment.class);
	
	UnitItemAdapter _adapter;
	
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
				
				final Dialog dlg=new Dialog(getView().getContext());
				dlg.setContentView(R.layout.dialog_unit_control);
				dlg.setTitle("Uyarý");
				
				WindowManager.LayoutParams lp=dlg.getWindow().getAttributes();
				lp.width=400;
				lp.height=250;
				
				final UnitItem item = addressList.get(--arg2);
				
				Button btnUpdate=(Button)dlg.findViewById(R.id.btnUpdateData);
				Button btnMakeAudit=(Button)dlg.findViewById(R.id.btnMakeAudit);
				Button btnAuditList=(Button)dlg.findViewById(R.id.btnAuditList);

				
				btnUpdate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();

						AddWiringFragment df = new AddWiringFragment();
						df.setArguments(getBundle(item));

						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.replace(R.id.frame_container, df);
						ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
						ft.addToBackStack(null);
						ft.commit();	
					}
				});
				
				btnMakeAudit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						
						AuditFormFragment frg=new AuditFormFragment();
						frg.setArguments(getBundle(item));
						
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.replace(R.id.frame_container, frg);
						ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
						ft.addToBackStack(null);
						ft.commit();	
					}
				});
				
				btnAuditList.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						
						AuditListFragment frg=new AuditListFragment();
						frg.setArguments(getBundle(item));
						
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.replace(R.id.frame_container, frg);
						ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
						ft.addToBackStack(null);
						ft.commit();	
					}
				});
				
				dlg.show();
			}
		});


		// Action bar overriden
		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText(Constants.UNIT_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		//list view header item added
		View header = inflater.inflate(R.layout.header_unit_list_item, null);
		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				return;
			}
		});
		searchResult.addHeaderView(header);
		
		getData(rootView);
		return rootView;
	}
	
	@Override
	public void OnDataChanged(Object items) {
		// TODO Auto-generated method stub		
		log.info("Data gathered from unit control");
		
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
				0, items,true);
		addressList = items;
		searchResult.setAdapter(_adapter);
		// searchResult.setAdapter(_altAdapter);
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
		operation = new ReadOperation(rootView.getContext(), this, cond,true);
		operation.execute(ItemType.Indoor);
	}

	private Bundle getBundle(UnitItem item){
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
		b.putBoolean(Constants.FOR_CONTROL, true);
		b.putBoolean(Constants.CHECKED_UAVT, item.isSynced());
		return b;
	}
}
