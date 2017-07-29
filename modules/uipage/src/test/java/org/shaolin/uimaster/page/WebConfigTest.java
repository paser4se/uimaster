package org.shaolin.uimaster.page;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shaolin.uimaster.page.flow.MasterInstanceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MasterInstanceListener.class)
@ContextConfiguration(classes=WebConfigSpringInstance.class)
@EnableAutoConfiguration
public class WebConfigTest {
	
	@Autowired
	private WebConfigSpringInstance instance;
	
	@Autowired
    private Environment env;
	
	@Autowired
	private TestServiceAnnotation testService;
	
	@Test
	public void testConfig() {
		System.out.println(env.getClass());
		System.out.println(testService.getClass());
		
		Assert.assertEquals("http://127.0.0.1:8080/uimaster", instance.getResourceServer());
		Assert.assertEquals("/uimaster/webflow.do", instance.getActionPath());
		Assert.assertEquals("/uimaster/ajaxservice", instance.getAjaxServiceURL());
		Assert.assertEquals("/uimaster", instance.getContextRoot());
		Assert.assertEquals("Dev", instance.getRunningMode());
		Assert.assertEquals("http://127.0.0.1:8080/uimaster/uploadFile", instance.getUploadServer());
		
		Assert.assertEquals(5, instance.getCommoncss().length);
		Assert.assertEquals(7, instance.getCommonjs().length);
		Assert.assertEquals(5, instance.getCommonMobcss().length);
		Assert.assertEquals(7, instance.getCommonMobjs().length);
		
		WebConfig.setSpringInstance(instance);
		Assert.assertEquals("http://127.0.0.1:8080/uimaster", WebConfig.getResourceContextRoot());
		Assert.assertEquals("/uimaster/webflow.do", WebConfig.getActionPath());
		Assert.assertEquals("/uimaster/ajaxservice", WebConfig.getAjaxServiceURI());
		Assert.assertEquals("/temp", WebConfig.getTempResourcePath());
		Assert.assertEquals("/uimaster", WebConfig.getWebContextRoot());
		
		MockHttpRequest request = new MockHttpRequest();
		request.setAttribute(WebConfig.APP_RESOURCE_PATH, "/storage/sdcard0");
		Assert.assertEquals("file:///storage/sdcard0/uimaster", WebConfig.getAppContextRoot(request));
		Assert.assertEquals("file:///storage/sdcard0/uimaster", WebConfig.getAppResourceContextRoot(request));
		Assert.assertEquals("http://127.0.0.1:8080/uimaster", WebConfig.getAppImageContextRoot(request));
		
		Assert.assertEquals("http://127.0.0.1:8080/uimaster/js/org/shaolin/vogerp/commonmodel/page/GlobalStatisticTest.js", 
				WebConfig.getImportJS("org.shaolin.vogerp.commonmodel.page.GlobalStatisticTest"));
		Assert.assertEquals("http://127.0.0.1:8080/uimaster/css/org/shaolin/vogerp/commonmodel/page/GlobalStatisticTest.css", 
				WebConfig.getImportCSS("org.shaolin.vogerp.commonmodel.page.GlobalStatisticTest"));
		
	}
}
