package org.shaolin.uimaster.page;

import org.junit.Test;
import org.shaolin.bmdp.runtime.SpringBootTestRoot;
import org.shaolin.bmdp.utils.SerializeUtil;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.TextField;
import org.shaolin.uimaster.page.ajax.Widget;

import junit.framework.Assert;

public class AjaxWidgetTest extends SpringBootTestRoot {

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
		//text.addConstraint("allowBlank", "false", "this is not allowed to be emtpy!");
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
