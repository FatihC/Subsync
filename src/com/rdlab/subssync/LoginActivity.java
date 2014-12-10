package com.rdlab.subssync;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.rdlab.events.ServiceTaskEvent;
import com.rdlab.utility.Constants;
import com.rdlab.webservice.LoginModel;
import com.rdlab.webservice.ServiceOrganizer;
import com.rdlab.webservice.ServiceRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements ServiceTaskEvent {

	EditText userName, password;
	Button login;
	ServiceOrganizer organizer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		userName = (EditText) findViewById(R.id.txtUsername);
		password = (EditText) findViewById(R.id.txtPassword);
		login = (Button) findViewById(R.id.btnLogin);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<NameValuePair> paramsx = new ArrayList<NameValuePair>();
				paramsx.add(new BasicNameValuePair("username", userName
						.getText().toString()));
				paramsx.add(new BasicNameValuePair("password", password
						.getText().toString()));

				ServiceRequest req = new ServiceRequest("login", paramsx);
				organizer.execute(req);
			}
		});

		organizer = new ServiceOrganizer(this, this);
	}

	@Override
	public void serviceReturned(Object items) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		LoginModel model = gson.fromJson(items.toString(), LoginModel.class);
		Constants.LoggedUserSerno = model.getSerno();

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
