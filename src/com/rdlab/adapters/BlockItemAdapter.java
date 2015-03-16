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
import com.rdlab.model.ItemConditions;
import com.rdlab.subssync.R;

public class BlockItemAdapter extends ArrayAdapter<BlockItem> {

	private ArrayList<BlockItem> _addresses;
	private ArrayList<BlockItem> _dtAddress;
	private Context _context;
	private Filter filter;
	private ItemConditions _conditions;
	private boolean _forControl;

	/* private ItemType _itemType; */

	public BlockItemAdapter(Context context, int resourceId,
			ArrayList<BlockItem> addressList,ItemConditions cond,boolean forControl) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, addressList);
		this._addresses = addressList;
		this._dtAddress=new ArrayList<BlockItem>(addressList);
		this._context = context;
		this._conditions=cond;
		this._forControl=forControl;

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

		BlockItem item = this._addresses.get(arg0);

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

//	private boolean getColorOfRow(String doorNumber)
//	{
//		
//	}
	
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter(_dtAddress);
		return filter;
	}

	@SuppressLint("DefaultLocale")
	private class AppFilter extends Filter {

		private ArrayList<BlockItem> items;

		public AppFilter(ArrayList<BlockItem> items) {
			// TODO Auto-generated constructor stub
			this.items = items;
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			
			FilterResults result = new FilterResults();
			if (chars != null&&!chars.toString().isEmpty() && chars.length() > 0) {
				ArrayList<BlockItem> filter = new ArrayList<BlockItem>();

				for (BlockItem object : items) {
					// the filtering itself:
					if (object.toString().contains(chars))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				ArrayList<BlockItem> filter = new ArrayList<BlockItem>();
				for (BlockItem object : items) {
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
			ArrayList<BlockItem> filtered = (ArrayList<BlockItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((BlockItem) filtered.get(i));
			notifyDataSetInvalidated();
		}

	}

}