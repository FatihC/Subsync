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
	private ArrayList<AddressListItem> _faddress;
	private Context _context;
	private Filter filter;

	/* private ItemType _itemType; */

	public AddressItemAdapter(Context context, int resourceId,
			ArrayList<AddressListItem> addressList) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = new ArrayList<AddressListItem>(addressList);
		this._faddress = new ArrayList<AddressListItem>(addressList);
		this._context = context;

	}

	public class ViewHolder {
		TextView txtTitle;
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
		ViewHolder viewHolder;
		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this._context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.filter_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) arg1.findViewById(R.id.item_name);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		AddressListItem address = _faddress.get(arg0);

		TextView txtTitle = viewHolder.txtTitle;
		txtTitle.setText(address.GetName());
		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter();
		return filter;
	}

	private class AppFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			FilterResults result = new FilterResults();

			if (chars != null && !chars.toString().isEmpty()) {
				ArrayList<AddressListItem> filter = new ArrayList<AddressListItem>(
						_addresses);
				ArrayList<AddressListItem> nfilter = new ArrayList<AddressListItem>();
				for (AddressListItem addressListItem : filter) {
					if (addressListItem.toString().contains(chars)) {
						nfilter.add(addressListItem);
					}
				}
				result.count = nfilter.size();
				result.values = nfilter;
			} else {
				synchronized (this) {
					ArrayList<AddressListItem> list = new ArrayList<AddressListItem>(
							_addresses);
					result.values = list;
					result.count = list.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			_faddress = (ArrayList<AddressListItem>) results.values;
			clear();

			for (AddressListItem itm : _faddress) {
				add(itm);
			}
			notifyDataSetChanged();

		}

	}

}
