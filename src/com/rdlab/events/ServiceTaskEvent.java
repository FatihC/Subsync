package com.rdlab.events;

import com.rdlab.webservice.ServiceResult;

public interface ServiceTaskEvent {
	void  serviceReturned(ServiceResult result);
}
