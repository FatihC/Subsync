package com.rdlab.fragments;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.rdlab.adapters.AuditItemAdapter;
import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.model.AuditLog;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;

public class AuditListFragment extends BaseFragment {

	private final static Logger log = Logger.getLogger(AuditListFragment.class);

	ListView lstAudit;
	TextView txtSelectedUnitForAudit;
	AuditItemAdapter adapter;

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
		sb.append(String.format("Dýþ Kapý No: %s > Ýç Kapý No : %s >", doorNumber.trim(),
				indoorNumber.trim()));

		View rootView = inflater.inflate(R.layout.fragment_audit_list,
				container, false);

		txtSelectedUnitForAudit = (TextView) rootView
				.findViewById(R.id.txtSelectedUnitForAudit);
		lstAudit = (ListView) rootView.findViewById(R.id.lstAudit);

		txtSelectedUnitForAudit.setText(sb.toString());

		// customize bar
		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText(Constants.AUDIT_LOG_LIST);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		// list view header item added
		View header = inflater.inflate(R.layout.header_audit_log_item, null);
		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				return;
			}
		});
		lstAudit.addHeaderView(header);

		getData();
		
		return rootView;
	}

	private void getData() {
		String sql = String.format(
				"SELECT * FROM AUDIT_LOG WHERE UAVT_CODE='%s'", uavtAddresNo);
		ArrayList<AuditLog> auditLogs = (ArrayList<AuditLog>) AuditLog
				.findWithQuery(AuditLog.class, sql, null);
		log.info(String.format("Sql is  %s", sql));

		if (auditLogs == null || auditLogs.size() < 1) {
			log.warn(String.format("Empty audit log result for %s",
					uavtAddresNo));
			return;
		}
		adapter = new AuditItemAdapter(getActivity().getApplicationContext(),
				0, auditLogs);
		lstAudit.setAdapter(adapter);
	}
}
