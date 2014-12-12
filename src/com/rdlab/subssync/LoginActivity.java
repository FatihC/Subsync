package com.rdlab.subssync;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rdlab.dependencyInjection.BaseActivity;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.model.Users;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;
import com.rdlab.webservice.LoginModel;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements ServiceTaskEvent {

	EditText userName, password;
	Button login;
	boolean isConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

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
				if (!isConnected) {
					if (findUser()) {
						startActivity();
					} else
						Toast.makeText(LoginActivity.this,
								Constants.USER_NOT_EXIST, Toast.LENGTH_LONG)
								.show();

					return;
				}
				List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
				paramsx.add(new BasicNameValuePair("username", userName
						.getText().toString()));
				paramsx.add(new BasicNameValuePair("password", password
						.getText().toString()));

				ServiceRequest req = new ServiceRequest("login", paramsx);
				new ServiceOrganizer(LoginActivity.this, LoginActivity.this)
						.execute(req);
			}
		});
	}

	@Override
	public void serviceReturned(Object items) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		LoginModel model = gson.fromJson(items.toString(), LoginModel.class);
		Constants.LoggedUserSerno = model.getSerno();
		startActivity();

	}

	private void startActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

	private boolean findUser() {
		List<Users> userList = Select
				.from(Users.class)
				.where(Condition.prop("Username").eq(
						userName.getText().toString()))
				.and(Condition.prop("Password").eq(
						password.getText().toString())).list();

		if (userList != null && userList.size() > 0) {
			Users item = userList.get(0);
			Constants.LoggedUserSerno = item.getUserSerno();
			Constants.LoggedUserFullname = item.getFullName();
			Constants.LoggedUserName = item.getUsername();
			return true;
		}

		return false;
	}
}
