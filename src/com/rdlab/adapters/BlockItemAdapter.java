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

import com.rdlab.model.BlockItem;
import com.rdlab.model.Enums;

import com.rdlab.subssync.R;

public class BlockItemAdapter extends ArrayAdapter<BlockItem> {

	private ArrayList<BlockItem> _addresses;
	private ArrayList<BlockItem> _fAddress;
	private Context _context;
	private Filter filter;

	/* private ItemType _itemType; */

	public BlockItemAdapter(Context context, int resourceId,
			ArrayList<BlockItem> addressList) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
		this._fAddress = addressList;
		this._context = context;

	}

	public ArrayList<BlockItem> getData() {
		return this._addresses;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this._addresses.size();
	}

	@Override
	public BlockItem getItem(int arg0) {
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
		// if (arg1 == null) {
		LayoutInflater inflater = (LayoutInflater) this._context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		arg1 = inflater.inflate(R.layout.block_list_item, null);
		// }

		BlockItem item = this._fAddress.get(arg0);

		TextView txtDoor = (TextView) arg1.findViewById(R.id.item_door_number);
		TextView txtSite = (TextView) arg1.findViewById(R.id.item_site_name);
		TextView txtBlock = (TextView) arg1.findViewById(R.id.item_block_name);
		TextView txtUnitCount = (TextView) arg1
				.findViewById(R.id.item_unit_count);
		ImageView imgStatus = (ImageView) arg1.findViewById(R.id.imgStatus);

		txtDoor.setText(item.getDoorNumber());
		txtUnitCount.setText(String.valueOf(item.getUnitCount()));

		String block = "YOK", site = "YOK";

		if (!item.getSiteName().isEmpty()
				&& !item.getSiteName().equalsIgnoreCase("")
				&& item.getSiteName() != null) {
			site = item.getSiteName();
		}

		if (!item.getBlockName().isEmpty()
				&& !item.getBlockName().equalsIgnoreCase("")
				&& item.getBlockName() != null) {
			block = item.getBlockName();
		}

		txtSite.setText(site);
		txtBlock.setText(block);
		int status = item.getCheckStatus();

		if (status == Enums.NewlyAdded.getVal()) {
			imgStatus.setImageResource(R.drawable.ic_quest);
		} else if (status == Enums.NotStarted.getVal()) {
			imgStatus.setImageResource(R.drawable.no);
		}

		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter();
		return filter;
	}

	@SuppressLint("DefaultLocale")
	private class AppFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<BlockItem> filteredItems = new ArrayList<BlockItem>();

				for (int i = 0, l = _addresses.size(); i < l; i++) {
					BlockItem country = _addresses.get(i);
					if (country.getDoorNumber().toLowerCase().contains(constraint))
						filteredItems.add(country);
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				synchronized (this) {
					result.values = _addresses;
					result.count = _addresses.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			_fAddress = (ArrayList<BlockItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = _fAddress.size(); i < l; i++)
				add(_fAddress.get(i));
			notifyDataSetInvalidated();
		}

	}

}