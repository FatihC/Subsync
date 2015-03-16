package com.rdlab.fragments;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.model.*;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;

public class AddDoorFragment extends BaseFragment {

	EditText outDoorNum;
	EditText siteName;
	EditText blockName;
	Button saveBtn;
	Button cancelBtn;

	String csbmCode;
	String csbmName;
	String streetCode;
	String streetName;
	String villageCode;
	String villageName;
	String districtCode;
	String districtName;

	boolean isUpdate = false;
	String editDoor, editSite, editBlock;

	String message;
	boolean backPressed = false;

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

		if (bund.containsKey(Constants.EDIT_BLOCK)
				|| bund.containsKey(Constants.EDIT_SITE)
				|| bund.containsKey(Constants.EDIT_OUTDOOR)) {
			// edit durumudur.
			isUpdate = true;
			editBlock = bund.getString(Constants.EDIT_BLOCK);
			editSite = bund.getString(Constants.EDIT_SITE);
			editDoor = bund.getString(Constants.EDIT_OUTDOOR);
		}

		csbmCode = bund.getString(Constants.CSBM_CODE_TAG);
		streetCode = bund.getString(Constants.STREET_CODE_TAG);
		villageCode = bund.getString(Constants.VILLAGE_CODE_TAG);
		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);

		streetName = bund.getString(Constants.STREET_NAME_TAG);
		villageName = bund.getString(Constants.VILLAGE_NAME_TAG);
		districtName = bund.getString(Constants.DISTRICT_NAME_TAG);
		csbmName = bund.getString(Constants.CSBM_NAME_TAG);

		View rootView = inflater.inflate(R.layout.fragment_add_door, container,
				false);

		outDoorNum = (EditText) rootView.findViewById(R.id.txtOutdoorNo);
		siteName = (EditText) rootView.findViewById(R.id.txtSiteName);
		blockName = (EditText) rootView.findViewById(R.id.txtBlockName);

		saveBtn = (Button) rootView.findViewById(R.id.btnAddDoor);
		cancelBtn = (Button) rootView.findViewById(R.id.btnCancelDoor);

		if (isUpdate) {
			outDoorNum.setText(editDoor);
			siteName.setText(editSite);
			blockName.setText(editBlock);
		}

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!validateInputs()) {
					warnUser();
					return;
				}
				if (isUpdate) {
					try {
						Helper.updateData(districtCode, villageCode,
								streetCode, csbmCode, editDoor, "", outDoorNum
										.getText().toString(), siteName
										.getText().toString(), blockName
										.getText().toString(), "", false);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						saveItem();
					} catch (ClassNotFoundException
							| java.lang.InstantiationException
							| IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Helper.giveNotification(getView().getContext(),
						"Yeni dýþ kapý baþarýyla eklenmiþtir");
				getActivity().getFragmentManager().popBackStack();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().getFragmentManager().popBackStack();
			}
		});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					backPressed = true;
					message = "Kaydetme iþlemini tamamlamadan geri dönmek istiyor musunuz?";
					warnUser();
					return true;
				} else {
					return false;
				}
			}
		});

		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText("DIÞ KAPI EKLEME");
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		return rootView;
	}

	private boolean validateInputs() {
		if (outDoorNum.getText().toString().isEmpty()) {
			// Herhangi bir alan eksik
			message = "Tüm veri alanlarý dolu olmadan kayýt yapamazsýnýz.";
			return false;
		}
		return true;
	}

	private void warnUser() {
		AlertDialog dlg = new AlertDialog.Builder(getView().getContext())
				.create();
		dlg.setCancelable(false);
		dlg.setTitle("Uyarý");
		dlg.setMessage(message);
		dlg.setButton(DialogInterface.BUTTON_POSITIVE, "Evet",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (backPressed) {
							getActivity().getFragmentManager().popBackStack();
						}
					}
				});

		dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Hayýr",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		dlg.show();
	}

	private void saveItem() throws ClassNotFoundException,
			java.lang.InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		Class<?> t = Class.forName(Constants.SelectedClassName);
		Object entity = t.getDeclaredConstructor(
				new Class[] { String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, int.class }).newInstance(
				Constants.SelectedCountyCode, Constants.SelectedCountyName,
				districtCode, districtName, villageCode, villageName,
				streetCode, streetName, csbmCode, csbmName, "",
				outDoorNum.getText().toString(), siteName.getText().toString(),
				blockName.getText().toString(), UUID.randomUUID().toString(),
				"-", Enums.NewlyAdded.getVal());
		entity.getClass().getMethod("save", new Class[] { Object.class })
				.invoke(null, entity);

	}
}