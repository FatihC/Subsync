package com.rdlab.fragments;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.util.NamingHelper;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.model.*;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;
import com.rdlab.utility.Helper;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;
import com.rdlab.webservice.ServiceResult;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SyncFragment extends Fragment implements ServiceTaskEvent {

	private final static Logger log = Logger.getLogger(SyncFragment.class);

	ImageButton updateConfig;
	ImageButton fetch;
	ImageButton push;
	TextView txtPending;
	TextView txtPendingAuditCount;
	TextView txtLastSyncDate;
	TextView txtLastPushDate;
	ServiceOrganizer organizer;

	boolean pushCallResponsed=false;
	boolean pushLogsCallResponsed=false;
	
	boolean fetchNewUavtCallResponsed=false;
	boolean fetchMbsCallResponsed=false;
	boolean fetchLogsCallResponsed=false;
	boolean fetchDataCallResponsed=false;
	
	boolean fetchUserCallResponsed=false;

	boolean updateSyncDate = true;
	boolean forFirstSync = true;
	boolean forFirstPush = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sync, container,
				false);

		organizer = new ServiceOrganizer(this, rootView.getContext(), "Ýþlem",
				"Ýþlemler yapýlýyor.");

		push = (ImageButton) rootView.findViewById(R.id.push);
		fetch = (ImageButton) rootView.findViewById(R.id.fetch);
		updateConfig = (ImageButton) rootView.findViewById(R.id.updateConfig);
		txtPending = (TextView) rootView
				.findViewById(R.id.txtPendingRequestCount);
		txtPendingAuditCount=(TextView)rootView.findViewById(R.id.txtPendingAuditCount);
		txtLastSyncDate = (TextView) rootView
				.findViewById(R.id.txtLastSyncDate);
		txtLastPushDate = (TextView) rootView
				.findViewById(R.id.txtLastPushDate);

		// setting sync date to text view
		setLastProcessDates();

		// setting pending request size to text view
		setPendingRequestSize();

		//set click methods
		push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!checkPushAvailable()) {
					Helper.giveNotification(getView().getContext(), "1 günden fazla senkronizasyon yapmadan gönderim yapamazsýnýz.");
					return;
				}
				updatePushButtonView(false);
				push();
				pushLogs();
				
			}
		});
		
		fetch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				updateFetchButtonView(false);
				fetchNewUavt();
				fetchMbs();
				fetchLogs();
				fetchData();
			}
		});
		
		updateConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateConfigButtonView(false);
				fetchUserData();
			}
		});

		//overriding action bar for current fragment
		ActionBar ab = getActivity().getActionBar();
		ab.setCustomView(R.layout.custom_action_bar);
		TextView info = (TextView) ab.getCustomView().findViewById(
				R.id.txtTitle);
		info.setText(Constants.SYNC_HEADER_TEXT);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);

		return rootView;
	}

	@Override
	public void serviceReturned(ServiceResult result) {
		switch (result.operation) {
		case Error:
			Helper.giveNotification(getView().getContext(),
					"Ýþlem sýrasýnda hata oluþtu. Kýsa süre sonra tekrar deneyiniz.");
			updateSyncDate = false;
			//refreshing user interface buttons in order to send it again
			updateConfigButtonView(true);
			updateFetchButtonView(true);
			updatePushButtonView(true);
			break;
		case FetchNewUavt:
			doPostFetchNewUavtOperation(result.data);
			fetchNewUavtCallResponsed=true;
			finishFetchProcess();
			break;
		case FetchMBS:
			doPostFetchMbsOperation(result.data);
			fetchMbsCallResponsed=true;
			finishFetchProcess();
			break;
		case FetchLogs:
			doPostFetchLogsOperation(result.data);
			fetchLogsCallResponsed=true;
			finishFetchProcess();
			break;
		case FetchRequest:
			doPostFetchOperation(result.data);
			fetchDataCallResponsed=true;
			finishFetchProcess();
			break;
		case FetchUserRequest:
			doPostFetchUserOperation(result.data);
			fetchUserCallResponsed=true;
			break;
		case Push:
			Helper.giveNotification(getView().getContext(),
					"Veri gönderim iþlemi tamamlandý.");
			pushCallResponsed=true;
			finishPushProcess();
			break;
		case PushLogs:
			Helper.giveNotification(getView().getContext(),"Tespit verisi gönderim iþlemi tamamlandý.");
			pushLogsCallResponsed=true;
			finishPushProcess();
			break;
		default:
			break;
		}
	}
	
	private void finishPushProcess(){
		if (!pushCallResponsed) {
			return;
		}
		if (!pushLogsCallResponsed) {
			return;
		}
		
		doPostPushOperation();
		doPostPushLogOperation();
		Helper.updateLastPushDate(forFirstPush);
		updatePushButtonView(true);
		
		Helper.giveNotification(getView().getContext(),
				"Ýþleminiz baþarýyla tamamlanmýþtýr.");
		txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		
		pushCallResponsed=false;
		pushLogsCallResponsed=false;
	}
	
	private void finishFetchProcess(){
		if (!fetchDataCallResponsed) {
			return;
		}
		if (!fetchLogsCallResponsed) {
			return;
		}
		if (!fetchMbsCallResponsed) {
			return;
		}
		if (!fetchNewUavtCallResponsed) {
			return;
		}
		
		Helper.updateLastSyncDate(forFirstSync);
		updateFetchButtonView(true);
		
		fetchDataCallResponsed=false;
		fetchLogsCallResponsed=false;
		fetchMbsCallResponsed=false;
		fetchNewUavtCallResponsed=false;
	}
	
	private void updatePushButtonView(boolean state){
		push.setClickable(state);
	}

	private void updateFetchButtonView(boolean state){
		fetch.setClickable(state);
	}
	
	private void updateConfigButtonView(boolean state){
		updateConfig.setClickable(state);
	}
	
	private void setLastProcessDates() {
		String dt = Helper.getLastSyncDate();
		if (!dt.isEmpty()) {
			txtLastSyncDate.setText("Son güncelleme tarihi : "
					+ DateUtils.FormatLongStringDateToString(dt));
			forFirstSync = false;
		}
		
		dt = Helper.getLastPushDate();
		if (!dt.isEmpty()) {
			txtLastPushDate.setText("Son gönderim tarihi : "
					+ DateUtils.FormatLongStringDateToString(dt));
			forFirstPush = false;
		}
	}

	/**
	 * Check retrieved uavt from ws exist in current db
	 * @param params
	 * With order 0-VillageCode,1-StreetCode,2-csbmCode,3-uavtCode
	 * @return
	 */
	private boolean checkRetrievedUavtExist(String...params){
		String sql = "";

		sql = String.format("SELECT ID FROM %s WHERE VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND UAVT_ADDRESS_NO='%s'",
						Helper.getTableName(), params);

		List<?> listOfT = SugarRecord.findWithQuery(
				Helper.getClassName(), sql, null);
		if (!listOfT.isEmpty()) {
			return false;
		}
		return true;
	}
	
	private void setPendingRequestSize() {
		int pendingCount = getPendingRequestSize();
		if (pendingCount == 0) {
			txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		} else {
			txtPending.append(String.format(": %d", pendingCount));
		}
		
		pendingCount=getPendingLogRequestSize();
		if (pendingCount == 0) {
			txtPendingAuditCount.setText("Bekleyen tespit bulunmamaktadýr");
		} else {
			txtPendingAuditCount.append(String.format(": %d", pendingCount));
		}
	}
	
	private void doPostFetchLogsOperation(Object data){
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Tespit Güncelleme");
		prg.setMessage("Yeni tespit verileri güncelleniyor.");
		
		Gson g = new Gson();
		ArrayList<AuditLog> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<AuditLog>>() {
				}.getType());
		if (vals == null) {
			return;
		}
		
		prg.show();

		for (AuditLog auditLog : vals) {
			String sql=String.format("SELECT * FROM AUDIT_LOGS WHERE UAVT_CODE='%s' AND CREATE_DATE>=%s",auditLog.UavtCode,
					String.valueOf(auditLog.CreateDate));
			List<AuditLog> existedAuditLogs=AuditLog.findWithQuery(AuditLog.class, sql, null);
			if (existedAuditLogs!=null&&existedAuditLogs.size()>0) {
				// contains newer item
				for (AuditLog existedAuditItem : existedAuditLogs) {
					existedAuditItem.AuditedCheckStatus = auditLog.AuditedCheckStatus;
					existedAuditItem.AuditFormDescription = auditLog.AuditFormDescription;
					existedAuditItem.AuditFormSerno = auditLog.AuditFormSerno;
					existedAuditItem.AuditOptionSelection = auditLog.AuditOptionSelection;
					existedAuditItem.AuditProgressStatus = auditLog.AuditProgressStatus;
					existedAuditItem.AuditStatus = auditLog.AuditStatus;
					existedAuditItem.Pushed = auditLog.Pushed;
					existedAuditItem.RecordStatus = auditLog.RecordStatus;
					AuditLog.save(existedAuditItem);
				}
			}
			else {
				AuditLog.save(auditLog);
			}
		}
		
		
		String sql = "UPDATE AUDIT_LOG SET PUSHED=1 WHERE ID IN (SELECT ID FROM AUDIT_LOG WHERE PUSHED=0)";
		PushRequest.executeQuery(sql);

		prg.dismiss();

	}

	private void doPostPushOperation() {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Eþleþme Güncelleme");
		prg.setMessage("Yeni eþleþme verileri güncelleniyor.");
		prg.show();

		String sql = "UPDATE PUSH_REQUEST SET PUSHED=1 WHERE ID IN (SELECT ID FROM PUSH_REQUEST WHERE PUSHED=0)";

		PushRequest.executeQuery(sql);

		prg.dismiss();
	}

	private void doPostPushLogOperation() {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Tespit Güncelleme");
		prg.setMessage("Yeni tespit verileri güncelleniyor.");
		prg.show();

		String sql = "UPDATE AUDIT_LOG SET PUSHED=1 WHERE ID IN (SELECT ID FROM AUDIT_LOG WHERE PUSHED=0)";

		AuditLog.executeQuery(sql);

		prg.dismiss();
	}

	private void doPostFetchOperation(Object data) {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Veri Güncelleme");
		prg.setMessage("Diðer eþleþme verileri güncelleniyor.");

		Gson g = new Gson();
		ArrayList<PushRequest> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<PushRequest>>() {
				}.getType());
		if (vals == null) {
			return;
		}

		prg.show();

		for (PushRequest pushRequest : vals) {

			// String sql = "SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='"+
			// pushRequest.uavtCode + "'";
			String sql = String
					.format("SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='%s' AND CREATE_DATE >= %s",
							pushRequest.uavtCode,
							String.valueOf(pushRequest.createDate));
			List<PushRequest> existedPr = PushRequest.findWithQuery(
					PushRequest.class, sql, null);

			if (existedPr != null && existedPr.size() > 0) {
				// contains newer item
				for (PushRequest existedPrItem : existedPr) {
					existedPrItem.customerName = pushRequest.customerName;
					existedPrItem.wiringNo = pushRequest.wiringNo;
					existedPrItem.meterBrand = pushRequest.meterBrand;
					existedPrItem.meterBrandCode = pushRequest.meterBrandCode;
					existedPrItem.meterNo = pushRequest.meterNo;
					existedPrItem.checkStatus = pushRequest.checkStatus;
					existedPrItem.setPushed(true);
					PushRequest.save(existedPrItem);
					updateOrSaveOnUavt(pushRequest);
				}
			} else {
				PushRequest.save(pushRequest);
				updateOrSaveOnUavt(pushRequest);
			}
		}

		prg.dismiss();
	}

	private void doPostFetchMbsOperation(Object data) {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setTitle("Abone Güncelleme");
		prg.setCancelable(false);
		prg.setMessage("Abone verileri güncelleniyor.");

		Gson g = new Gson();
		ArrayList<SubscriberInfo> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<SubscriberInfo>>() {
				}.getType());
		if (vals == null) {
			return;
		}
		prg.show();
		for (SubscriberInfo subscriberInfo : vals) {
			Class<?> t;
			try {
				t = Class.forName(Constants.SelectedClassName+"Mbs");

				String sql = "SELECT * FROM " + NamingHelper.toSQLName(t)
						+ " WHERE TESISAT_NO=" + subscriberInfo.TesisatNo
						+ "";
				List<?> listOfT = SugarRecord.findWithQuery(t, sql, null);
				if (listOfT.size() > 0) {
					// item var update et
					for (Object object : listOfT) {
						Field fUnv = object.getClass()
								.getDeclaredField("Unvan");
						Field fSoz = object.getClass().getDeclaredField(
								"IlkSozlesmeTarihi");

						fUnv.setAccessible(true);
						fSoz.setAccessible(true);
						fSoz.set(object,
								Long.valueOf(subscriberInfo.SozlesmeTarihi));
						fUnv.set(object, String.valueOf(subscriberInfo.Unvan));

						object.getClass()
								.getMethod("save", new Class[] { Object.class })
								.invoke(null, object);
					}
				} else {
					// yoksa ekle
					// eklendi
					Object newObj = t
							.getDeclaredConstructor(
									new Class[] { Long.class, Long.class,
											String.class })
							.newInstance(
									Long.valueOf(subscriberInfo.TesisatNo),
									Long.valueOf(subscriberInfo.SozlesmeTarihi),
									String.valueOf(subscriberInfo.Unvan));
					newObj.getClass()
							.getMethod("save", new Class[] { Object.class })
							.invoke(null, newObj);
				}

			} catch (ClassNotFoundException | java.lang.InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				prg.dismiss();
			}

		}

		prg.dismiss();

	}

	private void doPostFetchNewUavtOperation(Object data) {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setTitle("Yeni UAVT Güncelleme");
		prg.setCancelable(false);
		prg.setMessage("UAVT verileri güncelleniyor.");

		Gson g = new Gson();
		ArrayList<UavtModel> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<UavtModel>>() {
				}.getType());
		if (vals == null) {
			return;
		}
		prg.show();
		for (UavtModel uavtModel : vals) {
			try {
				Class<?> t = Class.forName(Constants.SelectedClassName);

				String sql = "SELECT * FROM " + NamingHelper.toSQLName(t)
						+ " WHERE CSBM_CODE='" + uavtModel.CSBMCode + "'";
				List<?> listOfT = SugarRecord.findWithQuery(t, sql, null);
				if (listOfT.size() > 0) {
					// böyle eleman var bunu yazma
					continue;
				}

				Object entity = t.getDeclaredConstructor(
						new Class[] { String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, int.class }).newInstance(
						uavtModel.CountyCode, uavtModel.CountyName,
						uavtModel.DistrictCode, uavtModel.DistrictName,
						uavtModel.VillageCode, uavtModel.VillageName,
						uavtModel.StreetCode, uavtModel.StreetName,
						uavtModel.CSBMCode, uavtModel.CSBMName,
						uavtModel.BuildingCode, uavtModel.DoorNumber,
						uavtModel.SiteName, uavtModel.BlockName,
						uavtModel.UAVTAddressNo, uavtModel.IndoorNumber,
						uavtModel.CheckStatus);
				entity.getClass()
						.getMethod("save", new Class[] { Object.class })
						.invoke(null, entity);
			} catch (java.lang.InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				prg.dismiss();
				e.printStackTrace();
			}
		}

		prg.dismiss();
	}

	private void updateOrSaveOnUavt(PushRequest pushRequest) {
		if (Helper.IsUUID(pushRequest.uavtCode)) {
			// yeni eklenmiþ bir birim ya da kapý
			// sistemde var mý kontrol et yoksa ekle varsa güncelle
			try {
				String itemClassName = Constants.COUNTY_CODES
						.get(pushRequest.districtCode);
				if (itemClassName == null || itemClassName.isEmpty()) {
					return;
				}

				if (checkRetrievedUavtExist(pushRequest.villageCode,
								pushRequest.streetCode, pushRequest.csbmCode,pushRequest.uavtCode)) {
					return;
				}

				// böyle bir kayýt yok ekle
				// get district name,villagename,streetname,csbmname
				String sql = String
						.format("SELECT DISTRICT_NAME,DISTRICT_CODE,VILLAGE_NAME,STREET_NAME,CSBM_NAME FROM %s WHERE  VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' LIMIT 2",
								Helper.getTableName(), pushRequest.villageCode,
								pushRequest.streetCode, pushRequest.csbmCode);

				List<?> forNameList = SugarRecord.findWithQuery(
						Helper.getClassName(), sql, null);
				if (forNameList == null || forNameList.isEmpty()) {
					return;
				}

				Object firstObj = forNameList.get(0);
				Field dCode, dName, vName, sName, cName;
				String dCodeVal, dNameVal, vNameVal, sNameVal, cNameVal;

				dName = firstObj.getClass().getDeclaredField("DistrictName");
				dName.setAccessible(true);
				dNameVal = dName.get(firstObj).toString();

				vName = firstObj.getClass().getDeclaredField("VillageName");
				vName.setAccessible(true);
				vNameVal = vName.get(firstObj).toString();

				sName = firstObj.getClass().getDeclaredField("StreetName");
				sName.setAccessible(true);
				sNameVal = sName.get(firstObj).toString();

				cName = firstObj.getClass().getDeclaredField("CSBMName");
				cName.setAccessible(true);
				cNameVal = cName.get(firstObj).toString();

				dCode = firstObj.getClass().getDeclaredField("DistrictCode");
				dCode.setAccessible(true);
				dCodeVal = dCode.get(firstObj).toString();

				saveItem(dCodeVal, dNameVal, vNameVal, sNameVal, cNameVal,
						pushRequest.siteName, pushRequest.blockName,
						pushRequest, itemClassName);

			} catch (NoSuchFieldException | IllegalArgumentException
					| IllegalAccessException e) {
				log.error(String
						.format("Error occured in update or save uavt with an exception %s",
								e.getMessage()));
			}
		} else {
			try {
				// district countycode
				Helper.updateStatus("", pushRequest.villageCode,
						pushRequest.streetCode, pushRequest.csbmCode,
						pushRequest.doorNumber, "", Enums.ReadyToSync, true);
			} catch (ClassNotFoundException e) {
				log.error(String
						.format("Error occured in update or save uavt with an exception %s",
								e.getMessage()));
			}
		}
	}

	private void saveItem(String districtCode, String districtName,
			String villageName, String streetName, String csbmName,
			String siteName, String blockName, PushRequest pushRequest,
			String className) {

		try {
			if (className.isEmpty()) {
				return;
			}
			Class<?> t = Class.forName(className);
			Object entity = t.getDeclaredConstructor(
					new Class[] { String.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, int.class }).newInstance(
					Constants.SelectedCountyCode, Constants.SelectedCountyName,
					districtCode, districtName, pushRequest.villageCode,
					villageName, pushRequest.streetCode, streetName,
					pushRequest.csbmCode, csbmName, "", pushRequest.doorNumber,
					siteName, blockName, pushRequest.uavtCode,
					pushRequest.indoorNumber, Enums.ReadyToSync.getVal());
			entity.getClass().getMethod("save", new Class[] { Object.class })
					.invoke(null, entity);
		} catch (java.lang.InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | ClassNotFoundException e) {
			log.error(String
					.format("Error occured in update or save uavt with an exception %s",
							e.getMessage()));
		}

	}

	private void doPostFetchUserOperation(Object data) {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Kullanýcý Güncelleme");
		prg.setMessage("Kullanýcý verileri güncelleniyor.");
		prg.show();
		Gson g = new Gson();
		ArrayList<Users> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<Users>>() {
				}.getType());
		Users.deleteAll(Users.class);
		for (Users users : vals) {
			Users.save(users);
		}

		prg.dismiss();
	}

	private int getPendingRequestSize() {
		Integer val = PushRequest
				.getListCount("SELECT COUNT(*) AS COUNT FROM PUSH_REQUEST WHERE PUSHED=0");
		if (val == null) {
			// no data
			return 0;
		}
		return val;

	}
	
	private int getPendingLogRequestSize() {
		Integer val = AuditLog.getListCount("SELECT COUNT(*) AS COUNT FROM AUDIT_LOG WHERE PUSHED=0");
		if (val == null) {
			// no data
			return 0;
		}
		return val;

	}

	private List<PushRequest> getPendingRequests() {
		List<PushRequest> pushRequestList = PushRequest.findWithQuery(
				PushRequest.class, "SELECT * FROM PUSH_REQUEST WHERE PUSHED=0",
				null);
		return pushRequestList;
	}

	private List<AuditLog> getPendingLogs() {
		List<AuditLog> logList = AuditLog.findWithQuery(AuditLog.class,
				"SELECT * FROM AUDIT_LOG WHERE PUSHED=0", null);
		return logList;
	}

	private boolean checkPushAvailable(){
		long pushDate=DateUtils.nowLong();
		long syncDate=Long.parseLong(Helper.getLastSyncDate());
		long diff=pushDate-syncDate;
		if (diff>=1000000) {
			return false;
		}
		return true;
	}
	
	private void push() {
		List<PushRequest> pushRequestList = getPendingRequests();
		if (pushRequestList == null || pushRequestList.isEmpty()) {
			log.warn("Gönderilecek herhangi bir eþleþtirme bulunmamaktadýr.");
			pushCallResponsed=true;
			return;
		}

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		for (PushRequest pr : pushRequestList) {

			Gson g = new Gson();
			String converted = g.toJson(pr, PushRequest.class);
			paramsx.add(new BasicNameValuePair("", converted));
		}

		log.debug(String.format("push ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("push", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Eþleþme gönderim",
				"Eþleþme verileri gönderiliyor.").execute(req);
	}

	private void pushLogs() {
		List<AuditLog> logList = getPendingLogs();
		if (logList == null || logList.isEmpty()) {
			log.warn("Gönderilecek herhangi bir eþleþtirme bulunmamaktadýr.");
			pushLogsCallResponsed=true;
			return;
		}

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		for (AuditLog pr : logList) {

			Gson g = new Gson();
			String converted = g.toJson(pr, AuditLog.class);
			paramsx.add(new BasicNameValuePair("", converted));
		}

		log.debug(String.format("pushLogs ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("pushLogs", paramsx);
		new ServiceOrganizer(this, getView().getContext(),
				"Tespit veri gönderim", "Tespit verileri gönderiliyor.")
				.execute(req);
	}

	private void fetchNewUavt() {
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedCountyCode));
		paramsx.add(new BasicNameValuePair("LastProcessDate", Helper
				.getLastSyncDate()));

		log.debug(String.format("FetchNewUavt ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("fetchNew", paramsx);
		new ServiceOrganizer(this, getView().getContext(),
				"Yeni UAVT güncelleme", "Eþleþme verileri güncelleniyor")
				.execute(req);
	}

	private void fetchData() {
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedUniversalCountyCode));
		paramsx.add(new BasicNameValuePair("LastProcessDate", Helper
				.getLastSyncDate()));

		log.debug(String.format("fetchData ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("fetch", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Veri güncelleme",
				"Eþleþme verileri güncelleniyor").execute(req);
	}

	private void fetchMbs() {
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedUniversalCountyCode));

		long date = Helper.getMaxSbsDate();
		paramsx.add(new BasicNameValuePair("LastProcessDate", String
				.valueOf(date)));

		log.debug(String.format("fetchMbs ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("fetchMbs", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Abone güncelleme",
				"Abone verileri güncelleniyor").execute(req);
	}

	private void fetchUserData() {

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode", "35"));

		log.debug(String.format("fetchUserData ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("fetchUsers", paramsx);
		new ServiceOrganizer(this, getView().getContext(),
				"Kullanýcý verisi güncelleme",
				"Kullanýcý verisi güncelleme iþlemi yapýlýyor.").execute(req);
	}

	private void fetchLogs() {

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedUniversalCountyCode));
		paramsx.add(new BasicNameValuePair("LastProcessDate", Helper
				.getLastSyncDate()));

		log.debug(String.format("fetchLogs ws called with %s",
				new Gson().toJson(paramsx, paramsx.getClass())));
		ServiceRequest req = new ServiceRequest("fetchLogs", paramsx);
		new ServiceOrganizer(this, getView().getContext(),
				"Tespit verisi güncelleme",
				"Tespit verisi güncelleme iþlemi yapýlýyor.").execute(req);
	}
}
