package com.rdlab.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdlab.model.AuditLog;
import com.rdlab.model.AuditStatus;
import com.rdlab.model.UnitItem;
import com.rdlab.subssync.R;

public class UnitItemAdapter extends ArrayAdapter<UnitItem> {

	private ArrayList<UnitItem> _addresses;
	private ArrayList<UnitItem> _dtAddress;
	private Context _context;
	private Filter filter;
	private boolean forControl;

	/* private ItemType _itemType; */

	public UnitItemAdapter(Context context, int resourceId,
			ArrayList<UnitItem> addressList, boolean forControl) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
		this._dtAddress = new ArrayList<UnitItem>(addressList);
		this._context = context;
		this.forControl = forControl;

	}

	public ArrayList<UnitItem> getData() {
		return this._addresses;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this._addresses.size();
	}

	@Override
	public UnitItem getItem(int arg0) {
		// TODO Auto-generated method stub
		return this._addresses.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this._context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.unit_list_item, null);
			AbsListView.LayoutParams lp=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,60);
			arg1.setLayoutParams(lp);
		}

		UnitItem item = this._addresses.get(arg0);

		TextView txtID = (TextView) arg1.findViewById(R.id.item_indoor_number);
		TextView txtWir = (TextView) arg1.findViewById(R.id.item_wiring);
		TextView txtMB = (TextView) arg1.findViewById(R.id.item_meter_brand);
		TextView txtMS = (TextView) arg1.findViewById(R.id.item_meter_ser);
		TextView txtSta = (TextView) arg1.findViewById(R.id.item_stat);
		TextView txtSbs = (TextView) arg1.findViewById(R.id.item_sbs);
		ImageView imgStatus = (ImageView) arg1.findViewById(R.id.item_synced);

		txtID.setText(item.getIndoorNumber());
		txtWir.setText(item.getWiringNo());
		txtMB.setText(item.getMeterpointBrand());
		txtMS.setText(item.getMeterpointSerno());
		txtSta.setText(item.getStatusName());
		txtSbs.setText(item.getSubscriberName());

		if (!forControl) {
			boolean checkStatus = item.isSynced();
			if (!checkStatus) {
				imgStatus.setImageResource(R.drawable.no);
			}
			return arg1;
		}
		
		imgStatus.setVisibility(View.GONE);
		if (!checkAuditLogExist(item.getUAVTNo())) {
			arg1.setBackgroundColor(Color.RED);
			return arg1;
		}
		if (!checkLastAuditSuccess(item.getUAVTNo())) {
			arg1.setBackgroundColor(Color.YELLOW);
			return arg1;
		}
		
		arg1.setBackgroundColor(Color.GREEN);
		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter(_dtAddress);
		return filter;
	}

	private boolean checkAuditLogExist(String uavtNo) {
		String sql = String.format(
				"SELECT * FROM AUDIT_LOG WHERE UAVT_CODE='%s' AND AUDIT_STATUS=%s ", uavtNo,AuditStatus.Active.getStringVal());
		ArrayList<AuditLog> logs = (ArrayList<AuditLog>) AuditLog
				.findWithQuery(AuditLog.class, sql, null);
		return logs.size() > 0;
	}
	
	private boolean checkLastAuditSuccess(String uavtNo){
		String sql = String.format(
				"SELECT * FROM AUDIT_LOG WHERE UAVT_CODE='%s' AND AUDIT_STATUS=%s ", uavtNo,AuditStatus.Active.getStringVal());
		ArrayList<AuditLog> logs = (ArrayList<AuditLog>) AuditLog
				.findWithQuery(AuditLog.class, sql, null);
		if (logs.size()<1) {
			return false;	
		}
		
		AuditLog lastItem=logs.get(logs.size()-1);
		if (lastItem.AuditProgressStatus.equals("1")) {
			return false;
		}
		return true;
		
	}

	@SuppressLint("DefaultLocale")
	private class AppFilter extends Filter {

		private ArrayList<UnitItem> items;

		public AppFilter(ArrayList<UnitItem> items) {
			// TODO Auto-generated constructor stub
			this.items = items;
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {

			FilterResults result = new FilterResults();
			if (chars != null && !chars.toString().isEmpty()
					&& chars.length() > 0) {
				ArrayList<UnitItem> filter = new ArrayList<UnitItem>();

				for (UnitItem object : items) {
					// the filtering itself:
					if (object.toString().contains(chars))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				ArrayList<UnitItem> filter = new ArrayList<UnitItem>();
				for (UnitItem object : items) {
					filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			ArrayList<UnitItem> filtered = (ArrayList<UnitItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((UnitItem) filtered.get(i));
			notifyDataSetInvalidated();
		}

	}

}
