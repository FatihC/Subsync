package com.rdlab.webservice;

import java.util.List;


import com.rdlab.model.PushRequest;

/*import retrofit.Callback;*/
import retrofit.http.Body;
import retrofit.http.POST;

public interface IOperationService {

	@POST("/login")
	int login(@Body LoginModel user);
	
	@POST("/push")
	String push(@Body PushRequest user);
	
	@POST("/fetch/uavt")
	List<Long> fetchUavt(@Body UavtFetchRequest user);
	
}
