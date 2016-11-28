package org.shaolin.bmdp.workflow.dao;

import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.utils.HttpSender;
import org.shaolin.bmdp.workflow.be.NotificationImpl;
import org.shaolin.uimaster.page.ajax.json.JSONObject;


public class CoordinatorModelTest {

    @Test
    public void testsearchTasks() throws Exception {
    	long a = System.currentTimeMillis();
    	System.out.println(a);
    	Thread.sleep(2000);
    	long b = System.currentTimeMillis();
    	System.out.println(b);
    	System.out.println(b-a);
    	
    }

	@Test
	public void testsearchPendingTasks() throws Exception {
		Registry.getInstance().initRegistry();
		AppContext.register(new AppServiceManagerImpl("test", CoordinatorModelTest.class.getClassLoader()));
		
		NotificationImpl message = new NotificationImpl();
		message.setPartyId(1);
		message.setSubject("interface test");
		message.setDescription("data test");
		message.setCreateDate(new java.util.Date());

		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"uimaster_noti_item\"><div style=\"color:blue;\">");
		sb.append("[").append(message.getCreateDate()).append("] ").append(message.getSubject());
		sb.append("</div><div style=\"width:100%;\">");
		sb.append(message.getDescription()).append("</div></div>");
		JSONObject json = new JSONObject();
		if (message.getPartyId() == 0 && message.getOrgId() == 0) {
			json.put("toAll", true);
		} else {
			json.put("partyId", message.getPartyId());
		}
		json.put("message", sb.toString());
		
		HttpSender sender = new HttpSender();
		//sender.doPostSSLWithJson("https://mock/uimaster/notify", json.toString(), "utf-8");
	}

}

