package com.rdlab.fragments;

import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_user, container,false);
		TextView txtUsername=(TextView)rootView.findViewById(R.id.lblUsername);
		TextView txtCounty=(TextView)rootView.findViewById(R.id.lblCounty);
		txtUsername.setText(Constants.LoggedUserFullname);
		txtCounty.setText(Constants.SelectedCountyName);
		return rootView;
	}
}
