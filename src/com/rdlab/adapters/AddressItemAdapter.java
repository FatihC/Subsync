package com.rdlab.adapters;

import java.util.ArrayList;

import com.rdlab.model.AddressListItem;
import com.rdlab.subssync.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class AddressItemAdapter extends ArrayAdapter<AddressListItem> {

	private ArrayList<AddressListItem> _addresses;
	private ArrayList<AddressListItem> _dtAddress;
	private Context _context;
	private Filter filter;

	/* private ItemType _itemType; */

	public AddressItemAdapter(Context context, int resourceId,
			ArrayList<AddressListItem> addressList) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
		this._dtAddress=new ArrayList<AddressListItem>(addressList);
		this._context = context;

	}
	
	public ArrayList<AddressListItem> getData() {
		return this._addresses;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this._addresses.size();
	}

	@Override
	public AddressListItem getItem(int arg0) {
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
			arg1 = inflater.inflate(R.layout.filter_list_item, null);
		} 
		AddressListItem address = _addresses.get(arg0);

		TextView txtTitle = (TextView) arg1.findViewById(R.id.item_name);
		txtTitle.setText(address.GetName());
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

		private ArrayList<AddressListItem> items;

		public AppFilter(ArrayList<AddressListItem> items) {
			// TODO Auto-generated constructor stub
			this.items = items;
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			
			FilterResults result = new FilterResults();
			if (chars != null&&!chars.toString().isEmpty() && chars.length() > 0) {
				ArrayList<AddressListItem> filter = new ArrayList<AddressListItem>();

				for (AddressListItem object : items) {
					// the filtering itself:
					if (object.toString().contains(chars))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				ArrayList<AddressListItem> filter = new ArrayList<AddressListItem>();
				for (AddressListItem object : items) {
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
			ArrayList<AddressListItem> filtered = (ArrayList<AddressListItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((AddressListItem) filtered.get(i));
			notifyDataSetInvalidated();
		}

	}

}
