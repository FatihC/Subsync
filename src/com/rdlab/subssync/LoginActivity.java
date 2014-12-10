package com.rdlab.subssync;

import com.rdlab.webservice.LoginModel;
import com.rdlab.webservice.OperationService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText userName,password;
	Button login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		userName=(EditText)findViewById(R.id.txtUsername);
		password=(EditText)findViewById(R.id.txtPassword);
		login=(Button)findViewById(R.id.btnLogin);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OperationService ser=new OperationService();
				ser.getOperationService().login(new LoginModel(userName.getText().toString(),password.getText().toString()));
				long userSerno=1;
				Toast.makeText(getApplicationContext(), String.format("Value is %f", userSerno), Toast.LENGTH_LONG).show();
			}
		});
		
	}
}
