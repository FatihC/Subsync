package com.rdlab.adapters;

import java.util.ArrayList;

import com.rdlab.model.NavigationDrawerItem;
import com.rdlab.subssync.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerAdapter extends BaseAdapter {

	private Context _context;
	private ArrayList<NavigationDrawerItem> _items;

	public NavigationDrawerAdapter(Context context,
			ArrayList<NavigationDrawerItem> items) {
		this._context = context;
		this._items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return _items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this._context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) arg1.findViewById(R.id.icon);
		TextView txtTitle = (TextView) arg1.findViewById(R.id.title);
		TextView txtCount = (TextView) arg1.findViewById(R.id.counter);

		imgIcon.setImageResource(this._items.get(arg0).getIcon());
		txtTitle.setText(this._items.get(arg0).getTitle());

		// displaying count
		// check whether it set visible or not
		if (this._items.get(arg0).getCounterVisibility()) {
			txtCount.setText(this._items.get(arg0).getCount());
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return arg1;
	}

}
