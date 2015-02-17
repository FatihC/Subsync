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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.model.Enums;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;

public class AddNewUnitFragment extends BaseFragment {

	EditText unitNum;
	Button saveBtn;
	Button cancelBtn;
	Spinner unitType;

	ArrayAdapter<String> statusAdapter;
	
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
	String editIndoorNumber="";
	
	boolean isUpdate=false,backPressed;
	
	String message;


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

		if (bund.containsKey(Constants.EDIT_INDOOR)) {
			isUpdate=true;
			editIndoorNumber=bund.getString(Constants.EDIT_INDOOR);
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
		blockName= bund.getString(Constants.BLOCK_NAME_TAG);

		View rootView = inflater.inflate(R.layout.fragment_add_new_unit, container,
				false);

		unitNum = (EditText) rootView.findViewById(R.id.txtUnitNo);
		unitType=(Spinner)rootView.findViewById(R.id.spUnitType);

		saveBtn = (Button) rootView.findViewById(R.id.btnAddUnit);
		cancelBtn = (Button) rootView.findViewById(R.id.btnCancelUnit);
		
		statusAdapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), R.layout.spinner_item,
				Constants.UNIT_STATUS);
		
		unitType.setAdapter(statusAdapter);
		if (isUpdate) {
			unitNum.setText(editIndoorNumber);
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
						String indoorNum=unitNum.getText().toString()+" "+unitType.getSelectedItem().toString();
						Helper.updateData(districtCode, villageCode,
								streetCode, csbmCode, doorNumber, editIndoorNumber,"", "","", indoorNum,true);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						saveItem();
					} catch (java.lang.InstantiationException
							| IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Helper.giveNotification(getView().getContext(), "Yeni birim baþarýyla eklenmiþtir");
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

		ActionBar ab=getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info=(TextView)ab.getCustomView().findViewById(R.id.txtTitle);
		info.setText("BÝRÝM EKLEME");
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME|ActionBar.NAVIGATION_MODE_LIST|ActionBar.DISPLAY_HOME_AS_UP);
		
		return rootView;
	}

	private boolean validateInputs() {
		if (unitType.getSelectedItem().toString().isEmpty()) {
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
	
	private void saveItem() throws java.lang.InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		
		Class<?> t=Class.forName(Constants.SelectedClassName);
		Object entity= t.getDeclaredConstructor(new Class[]{String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class
				,String.class,String.class,String.class,String.class,String.class,String.class,int.class}).newInstance(Constants.SelectedCountyCode,
						Constants.SelectedCountyName, districtCode, districtName,
						villageCode, villageName, streetCode, streetName, csbmCode,
						csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
		entity.getClass().getMethod("save", new Class[]{Object.class}).invoke(null, entity);
		
//		if (Constants.SelectedUniversalCountyCode.equals("32")) {
//			Eyyubiye inst = new Eyyubiye(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Eyyubiye.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("33")) {
//			Haliliye inst = new Haliliye(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Haliliye.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("34")) {
//			Karakopru inst = new Karakopru(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Karakopru.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("35")) {
//			Akcakale inst = new Akcakale(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Akcakale.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("36")) {
//			Birecik inst = new Birecik(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Birecik.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("37")) {
//			Bozova inst = new Bozova(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Bozova.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("38")) {
//			Ceylanpinar inst = new Ceylanpinar(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Ceylanpinar.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("39")) {
//			Halfeti inst = new Halfeti(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Halfeti.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("40")) {
//			Harran inst = new Harran(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Harran.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("41")) {
//			Hilvan inst = new Hilvan(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Hilvan.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("42")) {
//			Siverek inst = new Siverek(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Siverek.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("43")) {
//			Suruc inst = new Suruc(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Suruc.save(inst);
//		} else if (Constants.SelectedUniversalCountyCode.equals("44")) {
//			Viransehir inst = new Viransehir(Constants.SelectedCountyCode,
//					Constants.SelectedCountyName, districtCode, districtName,
//					villageCode, villageName, streetCode, streetName, csbmCode,
//					csbmName, "", doorNumber, siteName, blockName, UUID.randomUUID().toString(),unitNum.getText().toString()+" "+unitType.getSelectedItem() ,Enums.NewlyAdded.getVal());
//			Viransehir.save(inst);
//		}

	}
}