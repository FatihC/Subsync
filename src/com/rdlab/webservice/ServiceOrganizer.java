package com.rdlab.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rdlab.events.ServiceTaskEvent;

public class ServiceOrganizer extends AsyncTask<ServiceRequest, Void, Object> {
	private static final String API_URL = "http://192.168.1.18/UAVTWebapi/api/v1/uavt/";
	private ServiceTaskEvent delegate;
	private ProgressDialog dialog;
	
	public ServiceOrganizer(ServiceTaskEvent delegate,Context context) {
		// TODO Auto-generated constructor stub
		this.delegate=delegate;
		dialog = new ProgressDialog(context);
		dialog.setTitle("Güncelleniyor");
		dialog.setMessage("Lütfen bekleyiniz...");
	}
	
	private static String getAbsoluteUrl(String methodName) {
		return API_URL + methodName;
	}

	public static String getJSONFromUrl(String methodName,List<NameValuePair> params) {
		
		
		InputStream is = null;
		//JSONObject json = null;
		String outPut = "";
		// Making the HTTP request
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(getAbsoluteUrl(methodName));
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
			outPut = convertStreamToString(is);
			Log.e("JSON", outPut.toString());
			
			
			//json = new JSONObject(outPut);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			Log.e("x" + "ERROR", e.toString());

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("x" + "ERRO", e.toString());
			}
		}
		return sb.toString();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//this.dialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected Object doInBackground(ServiceRequest... params) {
		// TODO Auto-generated method stub
		ServiceRequest param=params[0];
		return getJSONFromUrl(param.methodName, param.params);
	}
	
	@Override
	protected void onPostExecute(Object result) {
		/*if (dialog.isShowing()) {
			dialog.dismiss();
		}*/

		delegate.serviceReturned(result);
		super.onPostExecute(result);
	}
}

