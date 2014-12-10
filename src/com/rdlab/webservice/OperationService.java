package com.rdlab.webservice;

import com.google.gson.Gson;

import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public class OperationService {
	private Converter DATA_CONVERTER = new GsonConverter(new Gson());
	private static final String API_URL = "http://192.168.1.18/UAVTWebapi/api/v1/uavt";
	IOperationService service;

	public OperationService() {
		// TODO Auto-generated constructor stub
		RestAdapter adapter = new RestAdapter.Builder()
				.setEndpoint(API_URL)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(DATA_CONVERTER)
				.build();
		service = adapter.create(IOperationService.class);
	}

	public IOperationService getOperationService() {
		return service;
	}

}
