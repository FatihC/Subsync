package com.rdlab.utility;

import org.apache.log4j.Logger;

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

	private final static Logger log = Logger.getLogger(ReadOperation.class);
	
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
		dialog.setTitle("Güncelleniyor");
		dialog.setMessage("Lütfen bekleyiniz...");
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
		try {
			if (forControl) {
				return getItemsForControl(arg0[0]);
			}
			
			return getItems(arg0[0]);
		} catch (Exception e) {
			log.error(String.format("Error occured in executing doInBackground function with exception message %s",e.getMessage() ));
			return null;
		}
	}

	private Object getItems(ItemType itemType){
		try {
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
		} catch (Exception e) {
			log.error(String.format("Error occured in executing getItems function with exception message %s",e.getMessage() ));
			return null;
		}
	}
	
	private Object getItemsForControl(ItemType itemType)
	{
		try {
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
			case Subscriber:
				return repository.getSubscriberDetail(conditions.getTesisatNo().toString());
			default:
				return null;
			}
			
		} catch (Exception e) {
			log.error(String.format("Error occured in executing getItemsForControl function with exception message %s",e.getMessage() ));
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
