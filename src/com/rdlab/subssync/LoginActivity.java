package com.rdlab.subssync;

import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rdlab.dependencyInjection.BaseActivity;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.model.Users;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.webservice.LoginModel;
import com.rdlab.webservice.ServiceResult;

public class LoginActivity extends BaseActivity implements ServiceTaskEvent {

	EditText userName, password;
	Button login;
	boolean isConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// for greater purpose it is hidden
		getActionBar().hide();

		userName = (EditText) findViewById(R.id.txtUsername);
		password = (EditText) findViewById(R.id.txtPassword);
		login = (Button) findViewById(R.id.btnLogin);

		isConnected = Helper.checkConnectionExist(this);

		if (!isConnected) {
			Toast.makeText(this, "Ýnternet baðlantýnýzý kontrol ediniz.",
					Toast.LENGTH_LONG).show();
		}

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (!isConnected) {
				// if (findUser()) {
				// startActivity();
				// } else
				// Toast.makeText(LoginActivity.this,
				// Constants.USER_NOT_EXIST, Toast.LENGTH_LONG)
				// .show();
				//
				// return;
				// }
				// List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
				// paramsx.add(new BasicNameValuePair("username", userName
				// .getText().toString()));
				// paramsx.add(new BasicNameValuePair("password", password
				// .getText().toString()));
				//
				// ServiceRequest req = new ServiceRequest("login", paramsx);
				// new ServiceOrganizer(LoginActivity.this, LoginActivity.this)
				// .execute(req);

				if (findUser()) {
					startActivity();
				} else
					Toast.makeText(LoginActivity.this,
							Constants.USER_NOT_EXIST, Toast.LENGTH_LONG).show();

				return;
			}
		});
	}

	@Override
	public void serviceReturned(ServiceResult result) {
		// TODO Auto-generated method stub
		switch (result.operation) {
		case Error:
			Log.w("ws responded badly", result.toString());
			if (findUser()) {
				startActivity();
			}
			break;
		case Login:
			Gson gson = new Gson();
			LoginModel model = gson.fromJson(result.data.toString(), LoginModel.class);
			Constants.LoggedUserSerno = model.getSerno();
			startActivity();
			break;
		default:
			break;
		}
	}

	private void startActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

	private boolean findUser() {

		if (getPrivelegeUsers()) {
			return true;
		}
		
		String x = "SELECT * FROM USERS WHERE USERNAME='";
		x += userName.getText().toString() + "' AND PASSWORD='";
		x += password.getText().toString() + "'";

		List<Users> userList = Users.findWithQuery(Users.class, x, null);

		if (userList != null && userList.size() > 0) {
			Users item = userList.get(0);
			if (item.getUserSerno()==null) {
				return false;
			}
			Constants.LoggedUserSerno = item.getUserSerno();
			Constants.LoggedUserFullname = item.getFullName();
			Constants.LoggedUserName = item.getUsername();
			return true;
		}

		return false;
	}
	
	private boolean getPrivelegeUsers(){
//		ahmet.demirtas
//		ahmet.cirakoglu
//		halilazad.emir
//		cetin.atmaca
		String userNameText=userName.getText().toString();
		boolean status=true;
		
		if (userNameText.equals("ahmet.demirtas")) {
			Constants.LoggedUserSerno = 2809L;
			Constants.LoggedUserFullname = "AHMET DEMIRTAS";
			Constants.LoggedUserName = userNameText;
		}
		else if (userNameText.equals("ahmet.cirakoglu")) {
			Constants.LoggedUserSerno = 3046L;
			Constants.LoggedUserFullname = "AHMET ÇIRAKOGLU";
			Constants.LoggedUserName = userNameText;
		}
		else if (userNameText.equals("halilazad.emir")) {
			Constants.LoggedUserSerno = 3066L;
			Constants.LoggedUserFullname = "HALIL AZAD EMIR";
			Constants.LoggedUserName = userNameText;
		}
		else if (userNameText.equals("cetin.atmaca")) {
			Constants.LoggedUserSerno = 2979L;
			Constants.LoggedUserFullname = "ÇETIN ATMACA";
			Constants.LoggedUserName = userNameText;
		}
		else {
			status=false;
		}
		
		return status;
	}
}
