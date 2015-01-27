package com.rdlab.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.model.AuditLog;
import com.rdlab.model.AuditOptions;
import com.rdlab.model.ControlStatus;
import com.rdlab.model.PushRequest;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;
import com.rdlab.utility.Helper;

public class AuditFormFragment extends BaseFragment {

	RadioGroup rdgAuditConditions;
	RadioButton rd_mbs_uptodate;
	RadioButton rd_mbs_not_uptodate;
	RadioButton rd_subscribed;
	RadioButton rd_not_subscribed;
	RadioButton rd_info_correct;
	RadioButton rd_not_need_subscribe;
	EditText txtFormSerNo;
	EditText txtFormDescription;
	Button btnSaveAudit;

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
	AuditOptions selectedOptions;

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

		View rootView = inflater.inflate(R.layout.fragment_make_audit,
				container, false);

		rdgAuditConditions = (RadioGroup) rootView
				.findViewById(R.id.rdgAuditConditions);
		rd_mbs_uptodate = (RadioButton) rootView
				.findViewById(R.id.rd_mbs_uptodate);
		rd_mbs_not_uptodate = (RadioButton) rootView
				.findViewById(R.id.rd_mbs_not_uptodate);
		rd_subscribed = (RadioButton) rootView.findViewById(R.id.rd_subscribed);
		rd_not_subscribed = (RadioButton) rootView
				.findViewById(R.id.rd_not_subscribed);
		rd_info_correct = (RadioButton) rootView
				.findViewById(R.id.rd_info_correct);
		rd_not_need_subscribe = (RadioButton) rootView
				.findViewById(R.id.rd_not_need_subscribe);
		txtFormSerNo = (EditText) rootView.findViewById(R.id.txtFormSerNo);
		txtFormDescription = (EditText) rootView
				.findViewById(R.id.txtFormDescription);
		btnSaveAudit = (Button) rootView.findViewById(R.id.btnSaveAudit);

		// clearing selection in radio group
		rdgAuditConditions.clearCheck();

		// showing necessary radio buttons for situation
		switch (getControlStatus()) {
		case NoWiringMeterExist:
			rd_info_correct.setVisibility(View.GONE);
			rd_not_need_subscribe.setVisibility(View.GONE);
			break;
		case ClosedOrMeterUnreachable:
		case UnreachableUnitOrMeter:
			rd_mbs_uptodate.setVisibility(View.GONE);
			rd_mbs_not_uptodate.setVisibility(View.GONE);
			rd_subscribed.setVisibility(View.GONE);
			rd_not_subscribed.setVisibility(View.GONE);
			rd_not_need_subscribe.setVisibility(View.GONE);
			break;
		case NoMeterNoSubscriber:
			rd_mbs_uptodate.setVisibility(View.GONE);
			rd_mbs_not_uptodate.setVisibility(View.GONE);
			rd_info_correct.setVisibility(View.GONE);
			break;
		}

		rdgAuditConditions
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == R.id.rd_mbs_uptodate) {
							selectedOptions = AuditOptions.AlreadySubscribedMeterUpToDate;
						} else if (checkedId == R.id.rd_mbs_not_uptodate) {
							selectedOptions = AuditOptions.AlreadySubscribedMeterNotUpToDate;
						} else if (checkedId == R.id.rd_subscribed) {
							selectedOptions = AuditOptions.NotSubscribeSubscribed;
						} else if (checkedId == R.id.rd_not_subscribed) {
							selectedOptions = AuditOptions.NotSubscriberCouldntSubscribed;
						} else if (checkedId == R.id.rd_info_correct) {
							selectedOptions = AuditOptions.CheckInfoCorrect;
						} else if (checkedId == R.id.rd_not_need_subscribe) {
							selectedOptions = AuditOptions.NoNeedForSubscription;
						}

					}
				});

		btnSaveAudit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createAuditLog();
				Helper.giveNotification(getView().getContext(), "Tespit baþarýyla sisteme eklenmiþtir.");

			}
		});
		return rootView;
	}

	private void createAuditLog(){
		AuditLog log= new AuditLog();
		log.UserSerno=Constants.LoggedUserSerno;
		log.AuditFormDescription=txtFormDescription.getText().toString();
		log.AuditFormSerno=txtFormSerNo.getText().toString();
		log.AuditOptionSelection=selectedOptions.getValString();
		//TODO: implement progress status according to selection
		//log.AuditProgressStatus
		log.BlockName=blockName;
		log.CreateDate=DateUtils.nowLong();
		log.CsbmCode=csbmCode;
		log.DistrictCode=districtCode;
		log.DoorNumber=doorNumber;
		log.IndoorNumber=indoorNumber;
		log.SiteName=siteName;
		log.StreetCode=streetCode;
		log.UavtCode=uavtAddresNo;
		log.VillageCode=villageCode;
		AuditLog.save(log);
	}
	
	private ControlStatus getControlStatus() {
		String sql = String
				.format("SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='%s'",
						uavtAddresNo);
		PushRequest pr = PushRequest
				.findWithQuery(PushRequest.class, sql, null).get(0);
		if ((pr.wiringNo == null || pr.wiringNo.equals(""))
				&& (pr.meterNo != null && !pr.meterNo.equals(""))) {
			return ControlStatus.NoWiringMeterExist;
		} else if (pr.checkStatus.equals("1") || pr.checkStatus.equals("4")) {
			return ControlStatus.UnreachableUnitOrMeter;
		} else if (pr.checkStatus.equals("2") || pr.checkStatus.equals("3")) {
			return ControlStatus.ClosedOrMeterUnreachable;
		} else if (pr.checkStatus.equals("5") || pr.checkStatus.equals("6")
				|| pr.checkStatus.equals("7")) {
			return ControlStatus.NoMeterNoSubscriber;
		} else {
			return ControlStatus.ClosedOrMeterUnreachable;
		}
	}
}
