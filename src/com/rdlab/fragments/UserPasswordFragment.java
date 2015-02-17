package com.rdlab.fragments;

import java.util.List;

import com.rdlab.model.Users;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.Helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserPasswordFragment extends Fragment {

	EditText txtNewPass;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_user_password, container,false);
		
		TextView txtSelUserName=(TextView)rootView.findViewById(R.id.txtSelectedUserName);
		txtNewPass=(EditText)rootView.findViewById(R.id.edtNewPass);
		Button btnSave=(Button)rootView.findViewById(R.id.btnSavePassword);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newPass=txtNewPass.getText().toString();
				if (txtNewPass.getText().length()<1) {
					Helper.giveNotification(getView().getContext(), "Lütfen þifre alanýný boþ doldurunuz.");
					return;
				}
				
				String sql="SELECT * FROM USERS WHERE USER_SERNO= "+Constants.LoggedUserSerno;
				
				List<Users> users=Users.findWithQuery(Users.class, sql, null);
				if (users.size()>0) {
					Users selUser=users.get(0);
					selUser.setPassword(newPass);
					Users.save(selUser);
					Helper.giveNotification(getView().getContext(), "Þifreniz baþarýyla deðiþtirilmiþtir.");
				}
				
			}
		});
		
		txtSelUserName.setText(Constants.LoggedUserFullname);
		
		return rootView;
	}
}
