package org.shaolin.uimaster.page;

import org.junit.Assert;
import org.junit.Test;
import org.shaolin.bmdp.runtime.SpringBootTestRoot;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;

public class WebConfigTest extends SpringBootTestRoot {
	@Test
	public void testConfig() {
		WebConfigSpringInstance instance = IServerServiceManager.INSTANCE.getService(WebConfigSpringInstance.class);
		//instance.getSingleCommonJs() instance.getSingleCommonAppJs()
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
		Assert.assertEquals(3, instance.getSingleCommonJs().get("org_shaolin_GlobalStatisticTest").length);
		Assert.assertEquals(3, instance.getSingleCommonAppJs().get("org_shaolin_GlobalStatisticTest_mob").length);
		
		WebConfig.setSpringInstance(instance);
		Assert.assertEquals("http://127.0.0.1:8080/uimaster", WebConfig.getResourceContextRoot());
		Assert.assertEquals("/uimaster/webflow.do", WebConfig.getActionPath());
		Assert.assertEquals("/uimaster/ajaxservice", WebConfig.getAjaxServiceURI());
		Assert.assertEquals("/temp", WebConfig.getTempResourcePath());
		Assert.assertEquals("/uimaster", WebConfig.getWebContextRoot());
		
		Assert.assertEquals(5, WebConfig.getCommonCss().length);
		Assert.assertEquals(7, WebConfig.getCommonJs().length);
		Assert.assertEquals(5, WebConfig.getCommonMobCss().length);
		Assert.assertEquals(7, WebConfig.getCommonMobJs().length);
		Assert.assertEquals(1, WebConfig.getSingleCommonJS("org.shaolin.CustomerManagementTest").length);
		Assert.assertEquals(1, WebConfig.getSingleCommonAppJS("org.shaolin.CustomerManagementTest_mob").length);
		Assert.assertEquals(3, WebConfig.getSingleCommonJS("org.shaolin.GlobalStatisticTest").length);
		Assert.assertEquals(3, WebConfig.getSingleCommonAppJS("org.shaolin.GlobalStatisticTest_mob").length);
		Assert.assertEquals(1, WebConfig.getSingleCommonCSS("org.shaolin.CustomerManagementTest").length);
		Assert.assertEquals(1, WebConfig.getSingleCommonAppCSS("org.shaolin.CustomerManagementTest_mob").length);
		
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
