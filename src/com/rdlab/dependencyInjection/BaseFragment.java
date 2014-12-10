package com.rdlab.dependencyInjection;

import android.app.Fragment;
import android.os.Bundle;

public class BaseFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// entry point for dependency injection dagger
		/*((BaseApplication) getActivity().getApplication()).inject(this);*/
	}
}
