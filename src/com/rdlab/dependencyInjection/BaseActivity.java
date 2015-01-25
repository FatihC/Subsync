package com.rdlab.dependencyInjection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
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
		
		if (dstDB.exists()&&getPendingRequests()>0&&currentDB.exists()) {
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
					setDistrictVillageCode(configuration.getValue());
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
	
	private void setDistrictVillageCode(String countyCode){
		if(countyCode.equals("32")) {Constants.SelectedDistrictCode="1771";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="49153";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("33")) {Constants.SelectedDistrictCode="1773";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="49155";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("34")) {Constants.SelectedDistrictCode="1775";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="49157";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("35")) {Constants.SelectedDistrictCode="1392";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32013";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("36")) {Constants.SelectedDistrictCode="1394";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32103";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("37")) {Constants.SelectedDistrictCode="1396";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32176";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("38")) {Constants.SelectedDistrictCode="1399";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32256";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("39")) {Constants.SelectedDistrictCode="1400";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32292";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("40")) {Constants.SelectedDistrictCode="1401";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32326";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("41")) {Constants.SelectedDistrictCode="1402";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32423";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("42")) {Constants.SelectedDistrictCode="1405";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32487";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("43")) {Constants.SelectedDistrictCode="1412";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32620";Constants.SelectedVillageName="MERKEZ";}
		else if(countyCode.equals("44")) {Constants.SelectedDistrictCode="1414";Constants.SelectedDistrictName="MERKEZ";Constants.SelectedVillageCode="32705";Constants.SelectedVillageName="MERKEZ";}


	}
	
	private int getPendingRequests() {
		Integer val=PushRequest.getListCount("SELECT COUNT(*) AS COUNT FROM PUSH_REQUEST WHERE PUSHED=0");
		if (val==null) {
			//no data
			return 0;
		}
		return val;
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
