package com.rdlab.utility;

import com.rdlab.data.IRepository;
import com.rdlab.data.Repository;
import com.rdlab.events.DataEvent;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ReadOperation extends
		AsyncTask<ItemType, Void, Object> {

	private ProgressDialog dialog;
	IRepository repository;
	DataEvent delegate;
	ItemConditions conditions;
	boolean forControl;

	public ReadOperation(Context context, DataEvent delegate,
			ItemConditions conditions,boolean forControl) {
		// TODO Auto-generated constructor stub
		repository = new Repository();
		this.delegate = delegate;
		this.conditions = conditions;
		this.forControl=forControl;

		dialog = new ProgressDialog(context);
		dialog.setTitle("G�ncelleniyor");
		dialog.setMessage("L�tfen bekleyiniz...");
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.dialog.show();
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(ItemType... arg0) {
		// TODO Auto-generated method stub
		if (forControl) {
			return getItemsForControl(arg0[0]);
		}
		
		return getItems(arg0[0]);
	}

	private Object getItems(ItemType itemType){
		switch (itemType) {
		case District:
			return repository.getDistrictItems();
		case Village:
			return repository.getVillageItems(conditions.getDistrictCode());
		case Street:
			return repository.getStreetItems(conditions.getDistrictCode(),conditions.getVillageCode());
		case CSBM:
			return repository.getCSBMItems(conditions.getDistrictCode(),conditions.getVillageCode(),conditions.getStreetCode());
		case Block:
			return repository.getBlockItems(conditions.getDistrictCode(),conditions.getVillageCode(),conditions.getStreetCode(),conditions.getCSBMCode());
		case Indoor:
			return repository.getUnitItems(conditions.getDistrictCode(),conditions.getVillageCode(),conditions.getStreetCode(),conditions.getCSBMCode(),conditions.getDoorNumber());
		case Subscriber:
			return repository.getSubscriberDetail(conditions.getTesisatNo().toString());
		default:
			return null;
		}
	}
	
	private Object getItemsForControl(ItemType itemType)
	{
		switch (itemType) {
		case District:
			return repository.getDistrictItemsForControl();
		case Village:
			return repository.getVillageItemsForControl();
		case Street:
			return repository.getStreetItemsForControl();
		case CSBM:
			return repository.getCSBMItemsForControl(conditions.getStreetCode());
		case Block:
			return repository.getBlockItemsForControl(conditions.getStreetCode(),conditions.getCSBMCode());
		case Indoor:
			return repository.getUnitItemsForControl(conditions.getStreetCode(),conditions.getCSBMCode(),conditions.getDoorNumber());
		default:
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		delegate.OnDataChanged(result);
		super.onPostExecute(result);
	}

}
