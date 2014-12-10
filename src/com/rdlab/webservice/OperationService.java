package com.rdlab.webservice;


import com.google.gson.Gson;
import com.rdlab.model.LoginModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.POST;

public class OperationService {
	Converter DATA_CONVERTER = new GsonConverter(new Gson());
	private static final String API_URL="http://cherry:31035/uavt/rest/api";
	
	interface IOperation{
		@POST("/login")
		void login(@Body LoginModel user,Callback<Integer> callback);
	}
	
	public void CallFunction(){
		RestAdapter.Builder adaptBuilder=new RestAdapter.Builder();
		adaptBuilder.setConverter(DATA_CONVERTER);
		adaptBuilder.setEndpoint(API_URL);
		
		RestAdapter adapter=adaptBuilder.build();
		IOperation service=adapter.create(IOperation.class);
		
	}
}
