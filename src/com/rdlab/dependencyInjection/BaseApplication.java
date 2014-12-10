package com.rdlab.dependencyInjection;

import java.util.Arrays;
import java.util.List;

import android.app.Application;
import dagger.*;

public class BaseApplication extends Application {

	private ObjectGraph graph;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		graph = ObjectGraph.create(new AndroidModule(this));
		graph.inject(this);
	}

	protected List<Object> getModules() {
		// TODO Auto-generated method stub
		return Arrays.asList(new AndroidModule(this),new SubSyncModule());
	}

	public void inject(Object object) {
		graph.inject(object);
	}
	
	public ObjectGraph getGraph(){
		return this.graph;
	}

}
