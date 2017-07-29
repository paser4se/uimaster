package org.shaolin.bmdp.runtime.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootApplication
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ServerServiceManagerImpl.class)
@ContextConfiguration(classes=SPConfiguration.class)
public class ServerServiceManagerImplTest {

	@Autowired
	IServerServiceManager serviceManager;
	
	@Test
	public void test() {
		System.out.println(serviceManager.getMasterNodeName());
		
	}
	
}
