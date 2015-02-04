package com.rdlab.subssync;

import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rdlab.dependencyInjection.BaseActivity;
import com.rdlab.model.Users;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;

public class LoginActivity extends BaseActivity {

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
				if (findUser()) {
					startActivity();
				} else
					Toast.makeText(LoginActivity.this,
							Constants.USER_NOT_EXIST, Toast.LENGTH_LONG).show();

				return;
			}
		});
	}

	private void startActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

	private boolean findUser() {

		
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
}
