package org.shaolin.bmdp.runtime.internal;

import org.junit.Assert;
import org.junit.Test;
import org.shaolin.bmdp.runtime.SpringBootTestRoot;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.springframework.stereotype.Service;

public class ServerServiceManagerImplTest extends SpringBootTestRoot {

	@Test
	public void testLifeCycle() {
		IServerServiceManager serviceManager = IServerServiceManager.INSTANCE;
		serviceManager.registerLifeCycleProvider(new LifeCycleProviderA());
		Assert.assertEquals(serviceManager.getLifeCycleServiceList().size(), 3);
		serviceManager.configureLifeCycleProviders();
		serviceManager.startLifeCycleProviders();
		serviceManager.stopLifeCycleProviders();
		Assert.assertEquals(serviceManager.getLifeCycleServiceList().size(), 3);
	}
	
	@Test
	public void testRegisterService() {
		IServerServiceManager.INSTANCE.register(new SimpleServcie());
		Assert.assertNotNull(IServerServiceManager.INSTANCE.getService(SimpleServcie.class));
		Assert.assertEquals(IServerServiceManager.INSTANCE.getServiceSize(), 2);
	}
	
	public static class SimpleServcie implements IServiceProvider {

		@Override
		public Class getServiceInterface() {
			return SimpleServcie.class;
		}
		
	}
	
	public static class LifeCycleProviderA implements ILifeCycleProvider {

		@Override
		public void configService() {
			
		}
		
		@Override
		public void startService() {
			System.out.println("hello service!");
		}

		@Override
		public boolean readyToStop() {
			return false;
		}

		@Override
		public void stopService() {
			
		}

		@Override
		public void reload() {
			
		}

		@Override
		public int getRunLevel() {
			return 1;
		}

	}
	
}
