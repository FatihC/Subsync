package com.rdlab.adapters;

import java.util.ArrayList;

import com.rdlab.events.DataEvent;
import com.rdlab.model.ItemConditions;
import com.rdlab.model.ItemType;
import com.rdlab.model.PushRequest;
import com.rdlab.model.SubscriberItem;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.utility.ReadOperation;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("unused")
public class UnitMatchItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<PushRequest> pushRequestList;

	public UnitMatchItemAdapter(Context context,
			ArrayList<PushRequest> pushRequestList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.pushRequestList = pushRequestList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pushRequestList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pushRequestList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = null;
		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final ViewHolder holder = new ViewHolder();

			view = inflater.inflate(R.layout.unit_match_list_item, null);
			holder.wiringNo = (EditText) view.findViewById(R.id.edtUntWiring);
			holder.meterNo = (EditText) view.findViewById(R.id.edtUntMeterNo);

			holder.spBrand = (Spinner) view.findViewById(R.id.spUntBrand);
			holder.spStatus = (Spinner) view.findViewById(R.id.spUntStat);

			holder.txtIndoor = (TextView) view.findViewById(R.id.txtUntDoor);
			holder.txtSbs = (TextView) view.findViewById(R.id.txtUntSbs);

			holder.wiringNo
					.setOnFocusChangeListener(new OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							// TODO Auto-generated method stub
							if (!hasFocus) {
								final int position = (int) v.getTag();
								final EditText Caption = (EditText) v;
								pushRequestList.get(position).wiringNo = Caption.getText().toString();
							}
						}
					});

			view.setTag(holder);

			holder.spBrand.setTag(1);
			holder.spStatus.setTag(1);
			holder.wiringNo.setTag(arg0);
			holder.meterNo.setTag(arg0);

			// holder.spBrand.setId(0);
			// holder.spStatus.setId(0);
			//
			// holder.wiringNo.setId(0);
			// holder.meterNo.setId(0);

		} else {
			view = arg1;
			((ViewHolder) view.getTag()).spBrand.setTag(1);
			((ViewHolder) view.getTag()).spStatus.setTag(1);
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		PushRequest pr = (PushRequest) this.getItem(arg0);

		ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
				R.layout.spinner_item, Constants.METER_BRANDS);

		ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(context,
				R.layout.spinner_item, Constants.STATUSES);

		holder.spBrand.setAdapter(brandAdapter);
		holder.spStatus.setAdapter(statusAdapter);
		holder.txtIndoor.setText(pr.getIndoorNumber());
		holder.wiringNo.setText(pr.getWiringNo());
		holder.meterNo.setText(pr.getMeterNo());
		

		holder.meterNo.setId(Integer.parseInt(holder.meterNo.getTag()
				.toString()));
		holder.wiringNo.setId(Integer.parseInt(holder.wiringNo.getTag()
				.toString()));

		holder.spBrand.setSelection(Integer.parseInt(holder.spBrand.getTag()
				.toString()));
		holder.spStatus.setSelection(Integer.parseInt(holder.spStatus.getTag()
				.toString()));

		return view;
	}

	static class ViewHolder {
		TextView txtIndoor;
		TextView txtSbs;
		EditText wiringNo;
		EditText meterNo;
		Spinner spBrand;
		Spinner spStatus;

	}
}
