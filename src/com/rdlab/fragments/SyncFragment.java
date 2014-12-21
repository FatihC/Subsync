package com.rdlab.fragments;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

	// TODO:Ýleride fetch geldiðinde last sync date i alýp ve date.now yollayýp
	// aralýk data alacaksýn
	// TODO:Þuanlýk buna gerek yok.
	ImageButton sync;
	ImageButton push;
	TextView txtPending;
	TextView txtLastSyncDate;
	ServiceOrganizer organizer;
	boolean pushedCalled = false;
	boolean fetchCalled = false;
	boolean fetchUserCalled = false;
	// String startDate;
	boolean updateSyncDate = true;
	boolean forFirst = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sync, container,
				false);

		organizer = new ServiceOrganizer(this, rootView.getContext(), "Ýþlem",
				"Ýþlemler yapýlýyor.");

		push = (ImageButton) rootView.findViewById(R.id.push);
		txtPending = (TextView) rootView
				.findViewById(R.id.txtPendingRequestCount);
		txtLastSyncDate = (TextView) rootView
				.findViewById(R.id.txtLastSyncDate);

		String dt = getLastSyncDate();
		if (!dt.isEmpty()) {
			txtLastSyncDate.setText("Son güncelleme tarihi : " + DateUtils.FormatLongStringDateToString(dt));
			forFirst = false;
		}

		int pendingCount = getPendingRequests().size();
		if (pendingCount == 0) {
			txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
		} else {
			txtPending.append(String.format(": %d", pendingCount));
		}

		push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				push.setClickable(false);
				// startDate = getLastSyncDate();
				sendPendingItems();
			}
		});

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
			push.setClickable(true);
			break;
		case FetchRequest:
			Helper.giveNotification(getView().getContext(),
					"Eþleþim verisi alma iþlemi tamamlandý.");
			doPostFetchOperation(result.data);
			
			// ws iþlemleri bitti takýl kafana göre
			
			updateLastSyncDate();
			doPostPushOperation();
			push.setClickable(false);
			Helper.giveNotification(getView().getContext(),
					"Ýþleminiz baþarýyla tamamlanmýþtýr.");
			txtPending.setText("Bekleyen iþ emriniz bulunmamaktadýr");
			break;
		case FetchUserRequest:
			doPostFetchUserOperation(result.data);
			fetchMbs();
			break;
		case Login:
			break;
		case FetchMBS:
			doPostFetchMbsOperation(result.data);
			fetchData();
			break;
		case Push:
			Helper.giveNotification(getView().getContext(),
					"Veri gönderim iþlemi tamamlandý.");
			fetchUserData();
			break;
		default:
			break;
		}
	}

	private void doPostPushOperation() {
		List<PushRequest> pushRequestList = PushRequest
				.listAll(PushRequest.class);
		for (PushRequest pushRequest : pushRequestList) {
			pushRequest.setPushed(true);
			PushRequest.save(pushRequest);
		}
	}

	private void doPostFetchOperation(Object data) {
		ProgressDialog prg = new ProgressDialog(getView().getContext());
		prg.setCancelable(false);
		prg.setTitle("Adres Güncelleme");
		prg.setMessage("Adres verileri güncelleniyor.");
		prg.show();

		Gson g = new Gson();
		ArrayList<PushRequest> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<PushRequest>>() {
				}.getType());
		if (vals == null) {
			prg.dismiss();
			return;
		}
		for (PushRequest pushRequest : vals) {
			String sql = "SELECT * FROM PUSH_REQUEST WHERE UAVT_CODE='"
					+ pushRequest.uavtCode + "'";
			List<PushRequest> existedPr = PushRequest.findWithQuery(
					PushRequest.class, sql, null);
			if (existedPr.size() > 0) {
				// exist on db check for date
				boolean containsNewerItem = true;
				for (PushRequest existedPrItem : existedPr) {
					if (existedPrItem.createDate >= pushRequest.createDate) {
						containsNewerItem = false;
					}
				}

				if (containsNewerItem) {
					// do nothing because we have newer item
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
				}
			} else {
				// new data so we add to table
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
		prg.show();

		Gson g = new Gson();
		ArrayList<SubscriberInfo> vals = g.fromJson(data.toString(),
				new TypeToken<ArrayList<SubscriberInfo>>() {
				}.getType());
		if (vals == null) {
			prg.dismiss();
			return;
		}

		for (SubscriberInfo subscriberInfo : vals) {
			Class<?> t;
			try {
				t = Class.forName(Constants.SelectedClassName);

				String sql = "SELECT * FROM " + NamingHelper.toSQLName(t)
						+ "_MBS WHERE TESISAT_NO=" + subscriberInfo.TESISAT_NO
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
								Long.valueOf(subscriberInfo.SOZLESME_TARIHI));
						fUnv.set(object, String.valueOf(subscriberInfo.UNVAN));

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
									Long.valueOf(subscriberInfo.TESISAT_NO),
									Long.valueOf(subscriberInfo.SOZLESME_TARIHI),
									String.valueOf(subscriberInfo.UNVAN));
					newObj.getClass()
							.getMethod("save", new Class[] { Object.class })
							.invoke(null, newObj);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.lang.InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

//		prg.dismiss();

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
				Class<?> t = Class.forName(itemClassName);
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("SELECT * FROM %s",
						NamingHelper.toSQLName(t)));
				sb.append(" WHERE ");
				sb.append(String
						.format("VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s' AND UAVT_ADDRESS_NO='%s'",
								pushRequest.villageCode,
								pushRequest.streetCode, pushRequest.csbmCode,
								pushRequest.uavtCode));
				List<?> listOfT = SugarRecord.findWithQuery(t, sb.toString(),
						null);
				if (listOfT.isEmpty()) {
					// böyle bir kayýt yok ekle
					// get district name,villagename,streetname,csbmname
					sb = new StringBuilder();
					sb.append(String
							.format("SELECT DISTRICT_NAME,DISTRICT_CODE,VILLAGE_NAME,STREET_NAME,CSBM_NAME FROM %s",
									NamingHelper.toSQLName(t)));
					sb.append(" WHERE ");
					sb.append(String
							.format(" VILLAGE_CODE='%s' AND STREET_CODE='%s' AND CSBM_CODE='%s'",
									pushRequest.villageCode,
									pushRequest.streetCode,
									pushRequest.csbmCode));

					List<?> forNameList = SugarRecord.findWithQuery(t,
							sb.toString(), null);
					for (Object object : forNameList) {
						Field dName;

						dName = object.getClass().getDeclaredField(
								"DistrictName");
						dName.setAccessible(true);
						Field vName = object.getClass().getDeclaredField(
								"VillageName");
						vName.setAccessible(true);
						Field sName = object.getClass().getDeclaredField(
								"StreetName");
						sName.setAccessible(true);
						Field cName = object.getClass().getDeclaredField(
								"CSBMName");
						cName.setAccessible(true);

						Field dCode = object.getClass().getDeclaredField(
								"DistrictCode");
						dCode.setAccessible(true);

						saveItem(dCode.get(object).toString(), dName
								.get(object).toString(), vName.get(object)
								.toString(), sName.get(object).toString(),
								cName.get(object).toString(),
								pushRequest.siteName, pushRequest.blockName,
								pushRequest, itemClassName);
					}

				} else {
					// böyle bir kayýt var

				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.lang.InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				// district countycode
				Helper.updateStatus("", pushRequest.villageCode,
						pushRequest.streetCode, pushRequest.csbmCode,
						pushRequest.doorNumber, "", Enums.ReadyToSync, true);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void saveItem(String districtCode, String districtName,
			String villageName, String streetName, String csbmName,
			String siteName, String blockName, PushRequest pushRequest,
			String className) throws ClassNotFoundException,
			java.lang.InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		if (className.isEmpty()) {
			return;
		}
		Class<?> t = Class.forName(className);
		Object entity = t.getDeclaredConstructor(
				new Class[] { String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, int.class }).newInstance(
				Constants.SelectedCountyCode, Constants.SelectedCountyName,
				districtCode, districtName, pushRequest.villageCode,
				villageName, pushRequest.streetCode, streetName,
				pushRequest.csbmCode, csbmName, "", pushRequest.doorNumber,
				siteName, blockName, pushRequest.uavtCode,
				pushRequest.indoorNumber, Enums.ReadyToSync.getVal());
		entity.getClass().getMethod("save", new Class[] { Object.class })
				.invoke(null, entity);

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

	private List<PushRequest> getPendingRequests() {
		List<PushRequest> pushRequestList = PushRequest
				.listAll(PushRequest.class);
		List<PushRequest> result = new ArrayList<PushRequest>();
		for (PushRequest pushRequest : pushRequestList) {
			if (pushRequest.isPushed()) {
				continue;
			}
			result.add(pushRequest);
		}
		return result;
	}

	private long getMaxSbsDate() throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException,
			IllegalArgumentException {
		Class<?> t = Class.forName(Constants.SelectedClassName+"Mbs");
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(
				"select max(ILK_SOZLESME_TARIHI)as ILK_SOZLESME_TARIHI from %s",
				NamingHelper.toSQLName(t)));
		List<?> listOft = SugarRecord.findWithQuery(t, sb.toString(), null);
		if (listOft.size() > 0) {
			Object obj = listOft.get(0);
			Field f = obj.getClass().getDeclaredField("IlkSozlesmeTarihi");
			f.setAccessible(true);
			Number n1 = (Number) f.get(obj);
			if (n1 != null) {
				return n1.longValue();
			}
			return 0;
		} else
			return 0;

	}

	private void updateLastSyncDate() {
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		boolean contains = false;
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_SYNC_TAG)) {
					configuration.setValue(DateUtils.nowLong().toString());
					Configuration.save(configuration);
					contains = true;
					break;
				}
			}
		}

		if (cf.size() == 0 && !contains) {
			Configuration cfNew = new Configuration();
			cfNew.setKey(Constants.LAST_SYNC_TAG);
			if (forFirst) {
				cfNew.setValue(DateUtils.nowLong().toString());
			}
			Configuration.save(cfNew);
		}

	}

	private String getLastSyncDate() {
		String date = "";
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.LAST_SYNC_TAG)) {
					try {
						return configuration.getValue();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}
		}

		return date;
	}

	private void sendPendingItems() {
		List<PushRequest> pushRequestList = getPendingRequests();
		if (pushRequestList == null || pushRequestList.isEmpty()) {
			Helper.giveNotification(getView().getContext(),
					"Gönderilecek herhangi bir eþleþtirme bulunmamaktadýr.");
			return;
		}

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		for (PushRequest pr : pushRequestList) {

			Gson g = new Gson();
			String converted = g.toJson(pr, PushRequest.class);
			paramsx.add(new BasicNameValuePair("", converted));
		}

		ServiceRequest req = new ServiceRequest("push", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Eþleþme gönderim",
				"Eþleþme verileri gönderiliyor.").execute(req);
		pushedCalled = true;
	}

	private void fetchData() {
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedUniversalCountyCode));
		paramsx.add(new BasicNameValuePair("LastProcessDate", getLastSyncDate()));

		ServiceRequest req = new ServiceRequest("fetch", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Veri güncelleme",
				"Eþleþme verileri güncelleniyor").execute(req);
		fetchCalled = true;
	}

	private void fetchMbs() {
		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode",
				Constants.SelectedUniversalCountyCode));

		long date = 0;
		try {
			date = getMaxSbsDate();
		} catch (ClassNotFoundException | NoSuchFieldException
				| IllegalAccessException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paramsx.add(new BasicNameValuePair("LastProcessDate", String
				.valueOf(date)));

		ServiceRequest req = new ServiceRequest("fetchMbs", paramsx);
		new ServiceOrganizer(this, getView().getContext(), "Abone güncelleme",
				"Abone verileri güncelleniyor").execute(req);
		fetchCalled = true;
	}

	private void fetchUserData() {

		List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
		paramsx.add(new BasicNameValuePair("DistrictCode", "35"));

		ServiceRequest req = new ServiceRequest("fetchUsers", paramsx);
		new ServiceOrganizer(this, getView().getContext(),
				"Kullanýcý verisi güncelleme",
				"Kullanýcý verisi güncelleme iþlemi yapýlýyor.").execute(req);
		fetchUserCalled = true;
	}
}
