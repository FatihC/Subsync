package com.rdlab.dependencyInjection;

/*import com.rdlab.data.IRepository;
import com.rdlab.data.Repository;*/
import com.rdlab.fragments.HomeFragment;

import dagger.Module;
/*import dagger.Provides;*/

@Module(library = true, injects = HomeFragment.class)
public class SubSyncModule {
	public SubSyncModule() {
		// TODO Auto-generated constructor stub
	}

	/*@Provides
	public IRepository provideRepository() {
		return new Repository();
	}*/

}
