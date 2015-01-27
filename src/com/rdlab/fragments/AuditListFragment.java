package com.rdlab.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;

public class AuditListFragment extends BaseFragment {

	ListView lstAudit;
	TextView txtSelectedUnitForAudit;
	
	String uavtAddresNo;
	String doorNumber;
	String csbmCode;
	String streetCode;
	String villageCode;
	String districtCode;
	String indoorNumber;
	String siteName;
	String blockName;
	boolean isSync;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bund = new Bundle();
		if (savedInstanceState != null) {
			bund = savedInstanceState;
		} else {
			bund = getArguments();
		}

		uavtAddresNo = bund.getString(Constants.UAVT_TAG);
		doorNumber = bund.getString(Constants.DOOR_NUMBER_TAG);
		indoorNumber = bund.getString(Constants.INDOOR_TAG);
		csbmCode = bund.getString(Constants.CSBM_CODE_TAG);
		streetCode = bund.getString(Constants.STREET_CODE_TAG);
		villageCode = bund.getString(Constants.VILLAGE_CODE_TAG);
		districtCode = bund.getString(Constants.DISTRICT_CODE_TAG);
		siteName = bund.getString(Constants.SITE_NAME_TAG);
		blockName = bund.getString(Constants.BLOCK_NAME_TAG);
		isSync = bund.getBoolean(Constants.CHECKED_UAVT);
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s > %s >", doorNumber.trim(),
				indoorNumber.trim()));
		
		View rootView=inflater.inflate(R.layout.fragment_audit_list, container,false);
		
		txtSelectedUnitForAudit=(TextView)rootView.findViewById(R.id.txtSelectedUnitForAudit);
		lstAudit=(ListView)rootView.findViewById(R.id.lstAudit);
		
		txtSelectedUnitForAudit.setText(sb.toString());
		
		//TODO: implement listview with adapter to show .
		
		return rootView;
	}
}
