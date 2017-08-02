package org.shaolin.bmdp.analyzer.dao;

import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.shaolin.bmdp.analyzer.internal.AnalyzerServiceImpl;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ddc.client.ZooKeeperFactory;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;

/**
 * Created by lizhiwe on 4/18/2016.
 */
public class ZKDistributedJobEnginerTest {

	@Test
	public void testEmpty(){}
	
	public void testDispatcherJob() throws Exception {
		AppContext.register(IServerServiceManager.INSTANCE);

		IServerServiceManager.INSTANCE.setMasterNodeName("test");

		Configuration cfg = new Configuration();
		cfg.addInputStream(getClass().getClassLoader().getResourceAsStream(
				"hibernate.cfg.xml"));

		IServerServiceManager.INSTANCE.setHibernateConfiguration(cfg);

		ZooKeeperFactory.getInstance().getZooKeeper("localhost:2182", 30000,
				null);

		AnalyzerServiceImpl analyzerService = new AnalyzerServiceImpl();

		analyzerService.startService();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
