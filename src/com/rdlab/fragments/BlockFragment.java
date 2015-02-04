package com.rdlab.fragments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;
import com.rdlab.adapters.BlockItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.events.DataEvent;
import com.rdlab.model.AuditStatus;
import com.rdlab.model.BlockItem;
import com.rdlab.model.Enums;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.model.PushRequest;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.utility.PendingItems;
import com.rdlab.utility.ReadOperation;

import android.app.ActionBar;
import android.app.Fragment;
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
	private final static Logger log = Logger.getLogger(BlockFragment.class);

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
	boolean forControl = false;

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
		forControl = bund.getBoolean(Constants.FOR_CONTROL);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" %s > %s >", streetName.trim(),
				csbmName.trim()));

		View rootView = inflater.inflate(R.layout.fragment_block, container,
				false);

		((TextView) rootView.findViewById(R.id.blockSelectedValues)).setText(sb
				.toString());
		searchText = (EditText) rootView.findViewById(R.id.searchBlockText);
		searchResult = (ListView) rootView.findViewById(R.id.blockList);
		searchResult.setTextFilterEnabled(true);
		searchText.setHint("Dýþ Kapý No ile Ara");
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				_adapter.getFilter().filter(arg0.toString());
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
				BlockItem item = (BlockItem) arg0.getItemAtPosition(arg2);
				Fragment frg;
				if (!forControl) {
					frg = new UnitFragment();
					frg.setArguments(getBundle(item));
				} else {
					frg = new UnitControlFragment();
					frg.setArguments(getBundle(item));
				}

				log.info(String
						.format("Item clicked for detail with controlValue = [{%b}] and doorNubmer = [{%s}]",
								forControl, item.getDoorNumber()));

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, frg);
				ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				ft.addToBackStack(null);
				ft.commit();

			}

		});

		searchResult.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (forControl) {
					return false;
				}
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
		if (!forControl) {
			addButton = (Button) ab.getCustomView()
					.findViewById(R.id.btnAddNew);
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

					log.info("New item creation linked clicked for block item");

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.frame_container, df);
					ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
					ft.addToBackStack(null);
					ft.commit();
				}
			});

		}

		info.setText(Constants.BLOCK_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		// setting list view header
		View header = inflater.inflate(R.layout.header_block_list_item, null);
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
		_adapter = new BlockItemAdapter(getActivity().getApplicationContext(),
				0, items);
		addressList = items;
		searchResult.setAdapter(_adapter);
	}

	private Bundle getBundle(BlockItem item) {
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
		b.putBoolean(Constants.FOR_CONTROL, forControl);
		b.putString(Constants.BLOCK_NAME_TAG, item.getBlockName());
		return b;
	}

	@SuppressWarnings("unused")
	private void setProperStatus(ArrayList<BlockItem> items) {
		for (BlockItem blockItem : items) {
			boolean exist = Helper.checkBlockUnitExist(districtCode,
					villageCode, streetCode, csbmCode,
					blockItem.getDoorNumber());
			if (exist) {
				blockItem.setCheckStatus(Enums.ReadyToSync.getVal());
			}
		}
	}

	private void getData(View rootView) {

		ItemConditions cond = new ItemConditions();
		cond.setCSBMCode(csbmCode);
		cond.setStreetCode(streetCode);
		cond.setDistrictCode(districtCode);
		cond.setVillageCode(villageCode);
		operation = new ReadOperation(rootView.getContext(), this, cond,
				forControl);
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
					boolean containsNewlyAdded = Helper
							.checkBlockUnitsNewlyAdded(districtCode,
									villageCode, streetCode, csbmCode,
									blockItem.getDoorNumber());
					log.info(String
							.format("Item delete action clicked for item item doornubmer = [{%s}] and item contains newly added item [{%b}]",
									blockItem.getDoorNumber(),
									containsNewlyAdded));

					if (blockItem.getCheckStatus() != Enums.NewlyAdded.getVal()
							&& !containsNewlyAdded) {
						Helper.giveNotification(getView().getContext(),
								"Manuel eklenen veriler silinebilir.");
						log.warn("Item is newly added item unless synchronization you cant delete item");
						mode.finish();
						break;
					}

					Class<?> t = Class.forName(Constants.SelectedClassName);
					StringBuilder sb = new StringBuilder();
					String tableName = NamingHelper.toSQLName(t);
					sb.append(String
							.format("SELECT UAVT_ADDRESS_NO FROM %s WHERE DISTRICT_CODE='%s' AND "
									+ " VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND DOOR_NUMBER='%s'",
									tableName, districtCode, villageCode,
									streetCode, csbmCode,
									blockItem.getDoorNumber()));

					List<?> listT = SugarRecord.findWithQuery(t, sb.toString(),
							null);

					for (PushRequest prItem : PendingItems.PushRequests) {
						for (Object object : listT) {
							Field f = object.getClass().getDeclaredField(
									"UavtAddressNo");
							f.setAccessible(true);
							String value = f.get(object).toString();
							String uavt = prItem.uavtCode;
							if (uavt.equals(value)) {
								PendingItems.PushRequests.remove(prItem);
							}
						}

						// String prDoor = prItem.doorNumber;
						// String checDoor = blockItem.getDoorNumber();
						// String prVil= prItem.villageCode;
						// String prStree = prItem.streetCode;
						// String prCsbm= prItem.csbmCode;
						// if
						// (prDoor.equals(checDoor)&&prVil.equals(villageCode)&&prStree.equals(streetCode)&&prCsbm.equals(csbmCode))
						// {
						// PendingItems.PushRequests.remove(prItem);
						// }

					}

					Helper.deleteItem(districtCode, villageCode, streetCode,
							csbmCode, blockItem.getDoorNumber(), "", "");
					Helper.updateLogStatus(districtCode, villageCode, streetCode, csbmCode, blockItem.getDoorNumber(),"",AuditStatus.Passive.getStringVal());
					_adapter.notifyDataSetChanged();
					getData(getView());
				} catch (ClassNotFoundException | NoSuchFieldException
						| IllegalAccessException | IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mode.finish();
				break;
			}
			case R.id.edit: {
				BlockItem blockItem = _adapter.getItem(--itemCurrentPosition);
				boolean containsNewlyAdded = Helper.checkBlockUnitsNewlyAdded(
						districtCode, villageCode, streetCode, csbmCode,
						blockItem.getDoorNumber());

				if (blockItem.getCheckStatus() != Enums.NewlyAdded.getVal()
						&& !containsNewlyAdded) {
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
