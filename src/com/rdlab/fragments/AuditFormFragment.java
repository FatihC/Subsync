package com.rdlab.fragments;

import org.apache.log4j.Logger;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.rdlab.dependencyInjection.BaseFragment;
import com.rdlab.model.AuditLog;
import com.rdlab.model.AuditOptions;
import com.rdlab.model.AuditProgressStatus;
import com.rdlab.model.AuditStatus;
import com.rdlab.model.ControlStatus;
import com.rdlab.model.PushRequest;
import com.rdlab.model.RecordStatus;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;
import com.rdlab.utility.Helper;

public class AuditFormFragment extends BaseFragment {

	private final static Logger loger = Logger
			.getLogger(AuditFormFragment.class);

	RadioGroup rdgAuditConditions;
	RadioButton rd_mbs_uptodate;
	RadioButton rd_mbs_not_uptodate;
	RadioButton rd_subscribed;
	RadioButton rd_not_subscribed;
	RadioButton rd_info_correct;
	RadioButton rd_not_need_subscribe;
	TextView lblSerno;
	TextView lblSernoText;
	EditText txtFormSerNo;
	EditText txtFormSerNoText;
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
	boolean isClosedMeter = false;
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
		txtFormSerNoText = (EditText) rootView
				.findViewById(R.id.txtFormSerNoText);
		btnSaveAudit = (Button) rootView.findViewById(R.id.btnSaveAudit);
		
		lblSerno=(TextView)rootView.findViewById(R.id.lblSerno);
		lblSernoText=(TextView)rootView.findViewById(R.id.lblSernoText);

		// clearing selection in radio group
		rdgAuditConditions.clearCheck();

		// showing necessary radio buttons for situation
		switch (getControlStatus()) {
		case NoWiringMeterExist:
			rd_info_correct.setVisibility(View.GONE);
			rd_not_need_subscribe.setVisibility(View.GONE);
			break;
		case ClosedOrMeterUnreachable:
			rd_mbs_uptodate.setVisibility(View.GONE);
			rd_mbs_not_uptodate.setVisibility(View.GONE);
			rd_subscribed.setVisibility(View.GONE);
			rd_not_subscribed.setVisibility(View.GONE);
			rd_not_need_subscribe.setVisibility(View.GONE);
			break;
		case UnreachableUnitOrMeter:
			isClosedMeter = true;
			rd_mbs_uptodate.setVisibility(View.GONE);
			rd_mbs_not_uptodate.setVisibility(View.GONE);
			rd_subscribed.setVisibility(View.GONE);
			rd_not_subscribed.setVisibility(View.GONE);
			rd_not_need_subscribe.setVisibility(View.GONE);
			txtFormSerNo.setVisibility(View.GONE);
			txtFormSerNoText.setVisibility(View.GONE);
			lblSerno.setVisibility(View.GONE);
			lblSernoText.setVisibility(View.GONE);
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
			}
		});

		// Action bar overriden
		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText(Constants.AUDIT_LOG_FORM);
		Button saveMatchButton = (Button) ab.getCustomView().findViewById(
				R.id.btnSave);
		saveMatchButton.setVisibility(View.VISIBLE);
		saveMatchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createAuditLog();

			}
		});
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);
		return rootView;
	}

	private void createAuditLog() {
		loger.info("Audit log oluþturuluyor.");
		try {
			String serno = txtFormSerNo.getText().toString();
			String sernoText = txtFormSerNoText.getText().toString();
			if (!isClosedMeter) {
				if (serno.isEmpty()) {
					Helper.giveNotification(getView().getContext(),
							"Lütfen form serno alanýný doldurunuz!");
					return;
				}
				if (sernoText.isEmpty()) {
					Helper.giveNotification(getView().getContext(),
							"Lütfen form serno deðer alanýný doldurunuz!");
					return;
				}
			}
			if (selectedOptions == null) {
				Helper.giveNotification(getView().getContext(),
						"Lütfen durum seçimi yapýnýz!");
				return;
			}

			AuditLog log = new AuditLog();
			log.UserSerno = Constants.LoggedUserSerno;
			log.AuditFormDescription = txtFormDescription.getText().toString();
			if (isClosedMeter) {
				log.AuditFormSerno = "";
				log.AuditFormSernoText = "";	
			}else{
				log.AuditFormSerno = txtFormSerNo.getText().toString();
				log.AuditFormSernoText = txtFormSerNoText.getText().toString();
			}
			
			log.AuditOptionSelection = selectedOptions.getValString();
			log.AuditProgressStatus = defineProgressStatus(selectedOptions);
			log.AuditedCheckStatus = getCheckStatusOfElement();
			log.AuditStatus = AuditStatus.Active.getStringVal();
			log.RecordStatus = RecordStatus.Active.getVal();
			log.BlockName = blockName;
			log.CreateDate = DateUtils.nowLong();
			log.CsbmCode = csbmCode;
			log.DistrictCode = districtCode;
			log.DoorNumber = doorNumber;
			log.IndoorNumber = indoorNumber;
			log.SiteName = siteName;
			log.StreetCode = streetCode;
			log.UavtCode = uavtAddresNo;
			log.VillageCode = villageCode;
			log.Pushed = false;
			AuditLog.save(log);

			loger.info("Audit log kaydedildi");

			Helper.giveNotification(getView().getContext(),
					"Tespit baþarýyla sisteme eklenmiþtir.");
			getActivity().getFragmentManager().popBackStack();
		} catch (Exception e) {
			// TODO: handle exception
			loger.error(String.format(
					"Audit log kayýt sýrasýnda hata oluþtu exc ise %s",
					e.getMessage()));
		}
	}

	private String defineProgressStatus(AuditOptions selectedOption) {
		switch (selectedOption) {
		case AlreadySubscribedMeterUpToDate:
		case CheckInfoCorrect:
		case NoNeedForSubscription:
		case NotSubscribeSubscribed:
			return AuditProgressStatus.Finished.getStringVal();
		default:
			return AuditProgressStatus.Progress.getStringVal();
		}
	}

	private ControlStatus getControlStatus() {
		PushRequest pr = getRelatedPushRequest();
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

	private String getCheckStatusOfElement() {
		PushRequest pr = getRelatedPushRequest();
		return pr.checkStatus;
	}

	private PushRequest getRelatedPushRequest() {
		String sql = String
				.format("SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='%s'",
						uavtAddresNo);
		PushRequest pr = PushRequest
				.findWithQuery(PushRequest.class, sql, null).get(0);
		return pr;
	}

}
