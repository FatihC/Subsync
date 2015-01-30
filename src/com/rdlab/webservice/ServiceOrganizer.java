package com.rdlab.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.utility.Helper;

public class ServiceOrganizer extends AsyncTask<ServiceRequest, Void, Object> {
	 private static final String API_URL ="http://uavt.dedas.com.tr/UAVTWebapi/api/v1/uavt/";
//	 private static final String API_URL = "http://192.168.56.1/UAVTWebapi/api/v1/uavt/";
//	private static final String API_URL = "http://10.34.61.33/UAVTWebapi/api/v1/uavt/";
//	private static final String API_URL = "http://192.168.2.238/UAVTWebapi/api/v1/uavt/";
	
 	private final static Logger log = Logger.getLogger(ServiceOrganizer.class);
	 
	private ServiceTaskEvent delegate;
	private ProgressDialog dialog;
	private static Context context;
	private String requestMethodName;
	private String dialogMessage;
	private String dialogTitle;

	public ServiceOrganizer(ServiceTaskEvent delegate, Context context,String dialogTitle,String dialogMessage) {
		// TODO Auto-generated constructor stub
		this.delegate = delegate;
		ServiceOrganizer.context = context;
		this.dialogTitle=dialogTitle;
		this.dialogMessage=dialogMessage;
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setTitle(this.dialogTitle);
		dialog.setMessage(this.dialogMessage);
	}

	private String getAbsoluteUrl(String methodName) {
		return API_URL + methodName;
	}

	public String getJSONFromUrl(String methodName, List<NameValuePair> params) {

		InputStream is = null;
		String outPut = "error";
		try {

			this.requestMethodName = methodName;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(getAbsoluteUrl(methodName));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			outPut = convertStreamToString(is);
			log.debug("Response retrieved from webservice");

		} catch (Exception e) {
			log.error(String.format("Error occured during web service with message %s",e.getMessage()));
			Helper.giveNotification(context, "Beklenmeyen hata olu�tu");
		} 

		return outPut;

	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			log.error(String.format("Error occured during convertStreamToString with message %s",e.getMessage()));

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(String.format("Error occured during convertStreamToString with message %s",e.getMessage()));
			}
		}
		return sb.toString();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.dialog.show();
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(ServiceRequest... params) {
		// TODO Auto-generated method stub
		try {
			ServiceRequest param = params[0];
			return getJSONFromUrl(param.methodName, param.params);	
		} catch (Exception e) {
			// TODO: handle exception
			log.error(String.format("Error occured during doInBackground with message %s",e.getMessage()));
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			return null;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		ServiceResult res=new ServiceResult();
		if (result==null||result.toString().equals("error")) {
			log.warn("Response null or server side error occured");
			res.setOperation(ServiceOperation.Error);
			res.setData("");
		}
		else {
			if (this.requestMethodName.equals("login")) {
				res.setOperation(ServiceOperation.Login);
			}
			else if (this.requestMethodName.equals("push")) {
				res.setOperation(ServiceOperation.Push);
			}
			else if (this.requestMethodName.equals("fetch")) {
				res.setOperation(ServiceOperation.FetchRequest);
			}
			else if (this.requestMethodName.equals("fetchUsers")) {
				res.setOperation(ServiceOperation.FetchUserRequest);
			}
			else if(this.requestMethodName.equals("fetchMbs")){
				res.setOperation(ServiceOperation.FetchMBS);
			}
			else if(this.requestMethodName.equals("fetchNew")){
				res.setOperation(ServiceOperation.FetchNewUavt);
			}
			else if (this.requestMethodName.equals("pushLogs")) {
				res.setOperation(ServiceOperation.PushLogs);
			}
			else if (this.requestMethodName.equals("fetchLogs")) {
				res.setOperation(ServiceOperation.FetchLogs);
			}
			else {
				res.setOperation(ServiceOperation.Error);
			}
			res.setData(result);
		}
		
		delegate.serviceReturned(res);

		super.onPostExecute(result);
	}
}
