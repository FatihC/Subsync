package com.rdlab.dependencyInjection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.orm.SugarContext;
import com.rdlab.data.DataInitializer;
import com.rdlab.model.Configuration;
import com.rdlab.model.PushRequest;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;

public class BaseActivity extends Activity {

	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		SugarContext.init(this);
		FragmentManager.enableDebugLogging(true);

		DataInitializer.InitData();

//		File dstDB = new File(
//				"/data/data/com.rdlab.subssync/databases/sub_sync.dat");
//		if (!dstDB.exists()) {
//			try {
//				reverseCopy();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		File dstDB = new File("/data/data/com.rdlab.subssync/databases/sub_sync.dat");
		File currentDB = new File("/sdcard/Download", "db.dat");
		
		if (dstDB.exists()&&getPendingRequests().size()>0&&currentDB.exists()) {
			Helper.giveNotification(this, "Bekleyen iþ emriniz var veritabaný deðiþimi yapýlmamýþtýr.");
			return;
		}
		
		
		if (currentDB.exists()) {
			try {
				reverseCopy();
				currentDB.delete();
				assignConfigVals();
				Helper.giveNotification(this, "Veritabaný deðiþim iþlemi tamamlanmýþtýr.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			assignConfigVals();
		}
		
	}
	
	private void assignConfigVals(){
		List<Configuration> cf = Configuration.listAll(Configuration.class);
		if (cf.size() > 0) {
			// check et varmý
			for (Configuration configuration : cf) {
				String cfKey = configuration.getKey();
				if (cfKey.equals(Constants.SELECTED_COUNTY_CODE)) {
					Constants.SelectedUniversalCountyCode=configuration.getValue();
				}
				if (cfKey.equals(Constants.SELECTED_COUNTY_NAME)) {
					Constants.SelectedCountyName=configuration.getValue();
				}
				if (cfKey.equals(Constants.SELECTED_COUNTY_DB_CODE)) {
					Constants.SelectedCountyCode=configuration.getValue();
				}
				if (cfKey.equals(Constants.SELECTED_CLASS_NAME)) {
					Constants.SelectedClassName=configuration.getValue();
				}
			}
		}
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

	@SuppressLint("SdCardPath")
	private void copyDb() throws Exception {

		File currentDB = getDatabasePath("sub_sync.dat");
		File dstDB = new File("/sdcard/Download", "db.dat");
		@SuppressWarnings("resource")
		FileChannel fi = new FileInputStream(currentDB).getChannel();
		@SuppressWarnings("resource")
		FileChannel fo = new FileOutputStream(dstDB).getChannel();
		fo.transferFrom(fi, 0, fi.size()); // to copy from source to destination
		fi.close();
		fo.close();
	}

	@SuppressLint("SdCardPath")
	private void reverseCopyDb() throws Exception {

		// File dstDB = getDatabasePath("sub_sync.dat");
		File dstDB = new File("/data/data/com.rdlab.subssync/databases");
		File currentDB = new File("/sdcard/Download", "db.dat");
		@SuppressWarnings("resource")
		FileChannel fi = new FileInputStream(currentDB).getChannel();
		@SuppressWarnings("resource")
		FileChannel fo = new FileOutputStream(dstDB).getChannel();
		FileUtils.copyFile(currentDB, dstDB);

		fo.transferFrom(fi, 0, fi.size()); // to copy from source to destination
		fi.close();
		fo.close();
	}

	@SuppressLint("SdCardPath")
	private void reverseCopy() throws Exception {

		File dstDB = new File(
				"/data/data/com.rdlab.subssync/databases/sub_sync.dat");
		File currentDB = new File("/sdcard/Download", "db.dat");
		FileUtils.copyFile(currentDB, dstDB);
	}

	@SuppressLint("SdCardPath")
	private void copy() throws Exception {

		File currentDB = new File(
				"/data/data/com.rdlab.subssync/databases/sub_sync.dat");
		File dstDB = new File("/sdcard/Download", "db-imp.dat");
		FileUtils.copyFile(currentDB, dstDB);
	}
}
