package uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility;

import java.io.*;

import com.sleepycat.db.*;

public class XMLDatabaseEnvironment {

	public static Environment createEnvironment(File home)
			throws DatabaseException, FileNotFoundException {

		EnvironmentConfig config = new EnvironmentConfig();
		config.setCacheSize(50 * 1024 * 1024);
		config.setAllowCreate(true);
		config.setInitializeCache(true);
		config.setTransactional(false);
		config.setInitializeLocking(true);
		config.setInitializeLogging(false);

		config.setLogAutoRemove(true);

		config.setLogBufferSize(10000);

		config.setMaxLockers(10000);
		config.setMaxLockObjects(10000);
		config.setMaxLocks(10000);
		return new Environment(home, config);
	}

}
