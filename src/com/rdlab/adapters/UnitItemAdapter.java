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
	private Context _context;
	private Filter filter;

	/* private ItemType _itemType; */

	public UnitItemAdapter(Context context, int resourceId,
			ArrayList<UnitItem> addressList) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
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
		TextView txtWMS = (TextView) arg1.findViewById(R.id.item_wiring_or_meter_or_stat);
		TextView txtSM = (TextView) arg1.findViewById(R.id.item_sbs_or_meterser);
		TextView txtSbs = (TextView) arg1.findViewById(R.id.item_sbs);

		ImageView imgStatus = (ImageView) arg1.findViewById(R.id.item_synced);
		boolean checkStatus = item.isSynced();
		if (checkStatus) {
			imgStatus.setImageResource(R.drawable.no);
		}
		
		txtID.setText(item.getIndoorNumber());

		String wiring = "YOK", sbs = "YOK",mpName="YOK",mpSer="YOK",sta="YOK";
		if (item.getWiringNo()!=null&&!item.getWiringNo().isEmpty()&&!item.getWiringNo().equals("")) {
			wiring=item.getWiringNo();
			sbs=item.getSubscriberName();
			txtWMS.setText(wiring);
			txtSM.setText(sbs);
			return arg1;
		}
		if (item.getMeterpointBrand()!=null&&!item.getMeterpointBrand().isEmpty()&&!item.getMeterpointBrand().equals("")) {
			mpName=item.getMeterpointBrand();
			mpSer=item.getMeterpointSerno();
			sbs=item.getSubscriberName();
			txtWMS.setText(mpName);
			txtSM.setText(mpSer);
			txtSbs.setText(sbs);
			return arg1;
		}
		if (item.getStatusName()!=null&&!item.getStatusName().isEmpty()&&!item.getStatusName().equals("")) {
			sta=item.getStatusName();
			txtWMS.setText(sta);
			return arg1;
		}

		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter(_addresses);
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
			String filterSeq = chars.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (filterSeq != null && filterSeq.length() > 0) {
				ArrayList<UnitItem> filter = new ArrayList<UnitItem>();

				for (UnitItem object : items) {
					// the filtering itself:
					if (object.getIndoorNumber().toLowerCase()
							.contains(filterSeq))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				// add all objects
				synchronized (this) {
					result.values = items;
					result.count = items.size();
				}
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
