package com.rdlab.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.subssync.R;

public class AuditListFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_audit_list, container,false);
		return rootView;
	}
}
