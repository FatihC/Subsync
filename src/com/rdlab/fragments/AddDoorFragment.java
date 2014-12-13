package com.rdlab.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

		View rootView = inflater.inflate(R.layout.fragment_add_door, container,
				false);

		outDoorNum = (EditText) rootView.findViewById(R.id.txtOutdoorNo);
		siteName = (EditText) rootView.findViewById(R.id.txtSiteName);
		blockName = (EditText) rootView.findViewById(R.id.txtBlockName);

		saveBtn = (Button) rootView.findViewById(R.id.btnAddDoor);
		cancelBtn = (Button) rootView.findViewById(R.id.btnCancelDoor);

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveItem();
				Helper.giveNotification(getView().getContext(), "Yeni dýþ kapý baþarýyla eklenmiþtir");
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

		return rootView;
	}

	private void saveItem() {
		if (Constants.SelectedUniversalCountyCode.equals("32")) {
			Eyyubiye inst = new Eyyubiye(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Eyyubiye.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("33")) {
			Haliliye inst = new Haliliye(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Haliliye.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("34")) {
			Karakopru inst = new Karakopru(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Karakopru.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("35")) {
			Akcakale inst = new Akcakale(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Akcakale.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("36")) {
			Birecik inst = new Birecik(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Birecik.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("37")) {
			Bozova inst = new Bozova(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Bozova.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("38")) {
			Ceylanpinar inst = new Ceylanpinar(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Ceylanpinar.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("39")) {
			Halfeti inst = new Halfeti(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Halfeti.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("40")) {
			Harran inst = new Harran(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Harran.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("41")) {
			Hilvan inst = new Hilvan(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Hilvan.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("42")) {
			Siverek inst = new Siverek(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Siverek.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("43")) {
			Suruc inst = new Suruc(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Suruc.save(inst);
		} else if (Constants.SelectedUniversalCountyCode.equals("44")) {
			Viransehir inst = new Viransehir(Constants.SelectedCountyCode,
					Constants.SelectedCountyName, districtCode, districtName,
					villageCode, villageName, streetCode, streetName, csbmCode,
					csbmName, "", outDoorNum.getText().toString(), siteName
							.getText().toString(), blockName.getText()
							.toString(), "", "",true);
			Viransehir.save(inst);
		}

	}
}