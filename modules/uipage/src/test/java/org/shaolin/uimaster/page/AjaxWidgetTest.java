package org.shaolin.uimaster.page;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.SerializeUtil;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.TextField;
import org.shaolin.uimaster.page.ajax.Widget;

import junit.framework.Assert;

public class AjaxWidgetTest {

	@BeforeClass
	public static void setup() {
		LocaleContext.createLocaleContext("default");
		// initialize registry
		Registry.getInstance().initRegistry();
		String[] filters = new String[] {};
		// initialize entity manager.
		IEntityManager entityManager = IServerServiceManager.INSTANCE.getEntityManager();
		((EntityManager)entityManager).init(new ArrayList(), filters);
		WebConfig.setServletContextPath("E:/test/web/");
		
		AppContext.register(new AppServiceManagerImpl("test", ODTest.class.getClassLoader()));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testToJson() throws Exception {
		MockHttpRequest request = new MockHttpRequest();
		MockHttpResponse response = new MockHttpResponse();
		
        UserRequestContext htmlContext = new UserRequestContext(request, response);
        htmlContext.setCurrentFormInfo("test", "", "");
        htmlContext.setIsDataToUI(true);
		
		TextField text = new TextField("aa", Layout.NULL);
		text.setUIEntityName("TestEntity");
		text.addAttribute("alt", "fdsfdsfs");
		text.addStyle("color", "red");
		text.addConstraint("allowBlank", "false", "this is not allowed to be emtpy!");
		String json = text.toJSON().toString();
		System.out.println("JSON: " + json);
		
		TextField text1 = (TextField)Widget.covertFromJSON(text.toJSON());
		String json1 = text1.toJSON().toString();
		System.out.println("JSON: " + json1);
		System.out.println("TextField weight: " + SerializeUtil.estimateObjectSizeString(text1));
		System.out.println("JSON weight: " + SerializeUtil.estimateObjectSizeString(json1));
		
		Assert.assertEquals(json, json1);
		
	}
	
}
