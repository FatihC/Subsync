package com.rdlab.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdlab.model.UnitItem;
import com.rdlab.subssync.R;

public class UnitItemAdapter extends ArrayAdapter<UnitItem> {

	private ArrayList<UnitItem> _addresses;
	private ArrayList<UnitItem> _dtAddress;
	private Context _context;
	private Filter filter;

	/* private ItemType _itemType; */

	public UnitItemAdapter(Context context, int resourceId,
			ArrayList<UnitItem> addressList) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
		this._dtAddress=new ArrayList<UnitItem>(addressList);
		this._context = context;

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
		}

		UnitItem item = this._addresses.get(arg0);

		TextView txtID = (TextView) arg1.findViewById(R.id.item_indoor_number);
		TextView txtWir = (TextView) arg1.findViewById(R.id.item_wiring);
		TextView txtMB = (TextView) arg1.findViewById(R.id.item_meter_brand);
		TextView txtMS = (TextView) arg1.findViewById(R.id.item_meter_ser);
		TextView txtSta = (TextView) arg1.findViewById(R.id.item_stat);
		TextView txtSbs = (TextView) arg1.findViewById(R.id.item_sbs);

		ImageView imgStatus = (ImageView) arg1.findViewById(R.id.item_synced);
		boolean checkStatus = item.isSynced();
		if (!checkStatus) {
			imgStatus.setImageResource(R.drawable.no);
		}

		txtID.setText(item.getIndoorNumber());
		txtWir.setText(item.getWiringNo());
		txtMB.setText(item.getMeterpointBrand());
		txtMS.setText(item.getMeterpointSerno());
		txtSta.setText(item.getStatusName());
		txtSbs.setText(item.getSubscriberName());
		

		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter(_dtAddress);
		return filter;
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
			if (chars != null&&!chars.toString().isEmpty() && chars.length() > 0) {
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
