package com.rdlab.dependencyInjection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;


@Module(library = true, injects = BaseApplication.class)
public class AndroidModule {
	private final BaseApplication application;

	public AndroidModule(BaseApplication application) {
		// TODO Auto-generated constructor stub
		this.application = application;
	}

	@Provides
	@Singleton
	public Context provideApplicationContext() {
		return application;
	}
}
