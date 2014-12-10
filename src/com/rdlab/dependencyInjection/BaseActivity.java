package com.rdlab.dependencyInjection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.orm.SugarContext;
import com.rdlab.data.DataInitializer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// entry point for dependency injection dagger
		/* ((BaseApplication) getApplication()).getGraph().inject(this); */

		SugarContext.init(this);
		FragmentManager.enableDebugLogging(true);

		DataInitializer.InitData();

		/*try {
			reverseCopyDb();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	@SuppressLint("SdCardPath")
	private void copyDb() throws Exception {

		File currentDB = getDatabasePath("sub_sync.dat");
		File dstDB = new File("/sdcard/Download", "test.dat");
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

		File dstDB = getDatabasePath("sub_sync.dat");
		File currentDB = new File("/sdcard/Download", "test.dat");
		@SuppressWarnings("resource")
		FileChannel fi = new FileInputStream(currentDB).getChannel();
		@SuppressWarnings("resource")
		FileChannel fo = new FileOutputStream(dstDB).getChannel();
		fo.transferFrom(fi, 0, fi.size()); // to copy from source to destination
		fi.close();
		fo.close();
	}
}
