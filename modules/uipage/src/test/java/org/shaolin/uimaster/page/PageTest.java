package org.shaolin.uimaster.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.shaolin.bmdp.datamodel.flowdiagram.RectangleNodeType;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.perf.SingleKPI;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.ajax.AList;
import org.shaolin.uimaster.page.ajax.CheckBox;
import org.shaolin.uimaster.page.ajax.ComboBox;
import org.shaolin.uimaster.page.ajax.TextArea;
import org.shaolin.uimaster.page.ajax.TextField;
import org.shaolin.uimaster.page.ajax.TreeItem;
import org.shaolin.uimaster.page.ajax.TreeItem.LinkAttribute;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.ajax.json.RequestData;
import org.shaolin.uimaster.page.cache.ODFormObject;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;
import org.shaolin.uimaster.page.exception.ODProcessException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.od.ODPageContext;
import org.shaolin.uimaster.page.od.ODProcessor;
import org.shaolin.uimaster.page.od.PageODProcessor;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.shaolin.uimaster.test.be.CustomerImpl;
import org.shaolin.uimaster.test.be.ICustomer;
import org.shaolin.uimaster.test.ce.Gender;

import junit.framework.Assert;

public class PageTest {

	@BeforeClass
	public static void setup() {
		LocaleContext.createLocaleContext("default");
		// initialize registry
		Registry.getInstance().initRegistry();
		String[] filters = new String[] {"/uipage/"};
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
	
	@AfterClass
	public static void teardown() {
		
	}
	
	@Test
	public void testConstraint() throws EvaluationException {
		MockHttpRequest request = new MockHttpRequest();
		MockHttpResponse response = new MockHttpResponse();
		
        UserRequestContext htmlContext = new UserRequestContext(request, response);
        htmlContext.setCurrentFormInfo("test", "", "");
        htmlContext.setIsDataToUI(true);
		
        Map ajaxWidgetMap = new HashMap();
        Map pageComponentMap = new HashMap();
        request.getSession(true).setAttribute(AjaxContext.AJAX_COMP_MAP, ajaxWidgetMap);
        ajaxWidgetMap.put(AjaxContext.GLOBAL_PAGE, htmlContext.getPageAjaxWidgets());
        
		RequestData requestData = new RequestData();
        AjaxActionHelper.createAjaxContext(new AjaxContext(new HashMap(), requestData));
        AjaxActionHelper.getAjaxContext().initData();
        AjaxActionHelper.getAjaxContext().setRequest(request, null);
		
		TextField textField = new TextField("textField");
		try {
			textField.addConstraint("allowBlank", false, "The field is not allowed blank!");
			textField.getValue();
			Assert.fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			textField.setValue("hello");
			textField.getValue();
		}
		TextArea textArea = new TextArea("textArea");
		try {
			textArea.addConstraint("maxLength", 5, "The max length must less than 5!");
			textArea.setValue("1000000");
			textArea.getValue();
			Assert.fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			textArea.setValue("1000");
			textArea.getValue();
		}
		CheckBox checkbo = new CheckBox("checkBox");
		try {
			checkbo.addConstraint("mustCheck", true, "the checkbox must be selected!");
			checkbo.isSelected();
			Assert.fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			checkbo.setSelected(true);
			checkbo.isSelected();
		}
		AList list = new AList("list");
		try {
			List<String> optionValues = new ArrayList<String>();
			optionValues.add("a");
			optionValues.add("b");
			list.setOptions(optionValues, optionValues);
			list.addConstraint("selectedValuesConstraint", new String[]{"a"}, "the a value must be selected in list!");
			list.getValues();
			Assert.fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			List<String> values = new ArrayList<String>();
			values.add("a");
			list.setValues(values);
			list.getValues();
		}
		
		ComboBox combobox = new ComboBox("comboBox");
		try {
			List<String> optionValues = new ArrayList<String>();
			optionValues.add("a");
			optionValues.add("b");
			combobox.setOptions(optionValues, optionValues);
			combobox.addConstraint("selectedValueConstraint", "b", "the a value must be selected in combobox!");
			combobox.getValue();
			Assert.fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			combobox.setValue("b");
			combobox.getValue();
		}
	}
	
	@Test
	public void testD2UAndU2D() throws IllegalStateException, IllegalArgumentException, IOException {
		String page = "org.shaolin.uimaster.page.SearchCustomer";
		
		MockHttpRequest request = new MockHttpRequest();
		MockHttpResponse response = new MockHttpResponse();
		
        UserRequestContext htmlContext = new UserRequestContext(request, response);
        htmlContext.setCurrentFormInfo(page, "", "");
        htmlContext.setIsDataToUI(true);
		
        Map ajaxWidgetMap = new HashMap();
        request.getSession(true).setAttribute(AjaxContext.AJAX_COMP_MAP, ajaxWidgetMap);
        ajaxWidgetMap.put(AjaxContext.GLOBAL_PAGE, htmlContext.getPageAjaxWidgets());
        
        ICustomer customer = new CustomerImpl();
        customer.setId(1101);
        customer.setName("Shaolin Wu");
        
        Map inputParams = new HashMap();
        inputParams.put("customer", customer);
        htmlContext.setODMapperData(inputParams);
		
		try {
			UIPageObject pageObject = HTMLUtil.parseUIPage(page);
			
			List<String> components = pageObject.getUIForm().getAllComponentID("functionsTab.machiningInfoPanel1");
			Assert.assertEquals(components.toString(), "[machiningTable1, machiningInfoPanel1]");
			Assert.assertEquals(pageObject.getUIForm().getComponentProperty("machiningTable1").get("beElememt"), "org.shaolin.uimaster.test.be.Customer");
			Assert.assertTrue(pageObject.getUIForm().getComponentProperty("machiningTable1").containsKey("queryExpr"));
			
			components = pageObject.getUIForm().getAllComponentID("functionsTab.personalAccountForm");
			Assert.assertEquals(components.toString(), "[personalAccountForm]");
			Assert.assertEquals(pageObject.getUIForm().getComponentProperty("personalAccountForm").get("referenceEntity"), "org.shaolin.uimaster.form.Customer");
			Assert.assertEquals(pageObject.getUIForm().getComponents().size(), 25);
			
			
			PageDispatcher dispatcher = new PageDispatcher(pageObject);
			dispatcher.forwardPage(htmlContext);
			
			System.out.println("HTML Code: \n" + response.getHtmlCode());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
        inputParams.put("customer", new CustomerImpl());
		
        htmlContext.resetRepository();
        htmlContext.setCurrentFormInfo(page, "", "");
        htmlContext.setODMapperData(inputParams);
        htmlContext.setIsDataToUI(false);
        request.setAttribute(ODPageContext.OUT_NAME, "out1");
        try
        {
        	PageODProcessor pageODProcessor = new PageODProcessor(htmlContext, page);
            pageODProcessor.process();
            
            System.out.println("UI To Data outcomes: \n" + htmlContext.getODMapperData());
            ICustomer result = (ICustomer)htmlContext.getODMapperData().get("customer");
            Assert.assertEquals(customer.getId(), result.getId());
            Assert.assertEquals(customer.getName(), result.getName());
        } catch (ODProcessException e) {
        	e.printStackTrace();
		}
	}
	
	@Test
	public void testSingleForm() {
		try {
			MockHttpRequest request = new MockHttpRequest();
			MockHttpResponse response = new MockHttpResponse();
			
            UserRequestContext htmlContext = new UserRequestContext(request, response);
            htmlContext.setCurrentFormInfo("org.shaolin.uimaster.form.Customer", "customer.", "");
            htmlContext.setIsDataToUI(true);
            htmlContext.setFormObject(PageCacheManager.getUIFormObject(htmlContext.getFormName()));
            
            UserRequestContext.UserContext.set(htmlContext);
            AjaxActionHelper.createAjaxContext(new AjaxContext(new HashMap(), new RequestData()));
            
			ODFormObject odEntityObject = PageCacheManager.getODFormObject(htmlContext.getFormName());
            HTMLReferenceEntityType newReferObject = new HTMLReferenceEntityType("customer", htmlContext.getFormName());
            
            CustomerImpl customerPojo = new CustomerImpl();
            customerPojo.setId(10 + (int)(Math.random()* 100));
            customerPojo.setName("John Prine");
            Map inputParams = new HashMap();
            inputParams.put("customer", customerPojo);
            inputParams.put(odEntityObject.getUiParamName(), newReferObject);
            
            htmlContext.setODMapperData(inputParams);
        	ODProcessor processor = new ODProcessor(htmlContext, htmlContext.getFormName(), -1);
        	ODContext odContext = processor.process();
        	VariableEvaluator ee = new VariableEvaluator(odContext);
        	Widget refForm = newReferObject.createAjaxWidget(ee);
        	htmlContext.addAjaxWidget(refForm.getId(), refForm);
        	
            Map referenceEntityMap = new HashMap();
            htmlContext.setRefEntityMap(referenceEntityMap);
            Map result = htmlContext.getODMapperData();
            
            htmlContext.printHTMLAttributeValues();
            
        	UIFormObject formObject = HTMLUtil.parseUIForm("org.shaolin.uimaster.form.Customer");
			PageDispatcher dispatcher = new PageDispatcher(formObject, odContext);
			dispatcher.forwardForm(htmlContext, 0, Boolean.FALSE, newReferObject);
			
			String htmlCode = response.getHtmlCode();
			System.out.println("HTML Code: \n" + htmlCode);
			Assert.assertTrue(htmlCode.indexOf(customerPojo.getName()) != -1);
            
			
            HashMap ajaxWidgetMap = new HashMap();
            ajaxWidgetMap.put(AjaxContext.GLOBAL_PAGE, htmlContext.getPageAjaxWidgets());
            request.getSession().setAttribute(AjaxContext.AJAX_COMP_MAP, ajaxWidgetMap);
            CustomerImpl customerPojo1 = new CustomerImpl();
            inputParams.clear();
            inputParams.put("customer", customerPojo1);
            inputParams.put(odEntityObject.getUiParamName(), newReferObject);
            
            htmlContext.resetRepository();
            htmlContext.setCurrentFormInfo("org.shaolin.uimaster.form.Customer", "customer.", "");
            htmlContext.setODMapperData(inputParams);
            htmlContext.setIsDataToUI(false);
            request.setAttribute(ODPageContext.OUT_NAME, "out1");
            
        	ODProcessor pageODProcessor = new ODProcessor(htmlContext, "org.shaolin.uimaster.form.Customer", 0);
            pageODProcessor.process();
            
            System.out.println("UI To Data outcomes: \n" + htmlContext.getODMapperData());
            ICustomer result0 = (ICustomer)htmlContext.getODMapperData().get("customer");
            Assert.assertEquals(customerPojo1.getId(), result0.getId());
            Assert.assertEquals(customerPojo1.getName(), result0.getName());
            
            Map<String, SingleKPI> items = RestUIPerfMonitor.getKPICollector().getAllKIPs();
            for (Map.Entry<String, SingleKPI> item : items.entrySet()) {
	            JSONObject jsonKPIs = new JSONObject(item.getValue());
	            System.out.println(jsonKPIs.toString());
            }
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSinglePage() throws IllegalStateException, IllegalArgumentException, IOException {
		for (int i=0; i<20; i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						testSinglePage0();
					} catch (Exception e) {
						e.printStackTrace();
						Assert.fail();
					}
				}
			}).start();
		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}
	
	private void testSinglePage0() throws EntityNotFoundException, UIPageException {
		String page = "org.shaolin.uimaster.page.AddCustomer";
		
		LocaleContext.createLocaleContext("zh_CN");
		
		MockHttpRequest request = new MockHttpRequest();
		request.setAttribute(WebflowConstants.FRAME_NAME, "frame1");
		request.setAttribute("_framePrefix", "frame1");
		request.setAttribute("_frameTarget", "frame1");
		
		MockHttpResponse response = new MockHttpResponse();
		
        UserRequestContext htmlContext = new UserRequestContext(request, response);
        htmlContext.setCurrentFormInfo(page, "", "");
        htmlContext.setIsDataToUI(true);
		
        long id = (long)(Math.random() * 100000);
        ICustomer customer = new CustomerImpl();
        customer.setId(id);
        customer.setName("Shaolin Wu " + customer.getId());
        customer.setGender(Gender.MALE);
        
        Map inputParams = new HashMap();
        inputParams.put("customer", customer);
        htmlContext.setODMapperData(inputParams);
		
		UIPageObject pageObject = HTMLUtil.parseUIPage(page);
		PageDispatcher dispatcher = new PageDispatcher(pageObject);
		dispatcher.forwardPage(htmlContext);
		
		String htmlCode = response.getHtmlCode();
		System.out.println("HTML Code: \n" + htmlCode);
		Assert.assertTrue(htmlCode.indexOf(id + "") != -1);
	}
	
	@Test
	public void testJSON() throws JSONException {
		JSONArray array = new JSONArray("[{\"id\": \"General Setup\",\"top\": \"200px\",\"left\": \"980px\"},{\"id\": \"Master Data Management\",\"top\": \"220px\",\"left\": \"680px\"}]");
		Assert.assertEquals(array.length(), 2);
		Assert.assertEquals(array.getJSONObject(0).getString("id"), "General Setup");
		
		RectangleNodeType node = new RectangleNodeType();
		node.setId("node1");
		node.setName("Hello");
		node.setX(0);
		node.setY(0);
		
		System.out.println((new JSONObject(node)).toString());
		
		Map data = new HashMap();
        data.put("cmd","addNode");
        data.put("data", (new JSONObject(node)).toString());
        
        System.out.println((new JSONObject(data)).toString());
        
        ArrayList result = new ArrayList();
        for (int i=0;i<1;i++) {
            TreeItem item = new TreeItem();
            item.setId("id" + i);
            item.setText("Node" + i);
            item.setA_attr(new LinkAttribute("#"));
            item.setIcon(null);
            
            TreeItem child = new TreeItem();
            child.setId("id" + i);
            child.setText("Node" + i);
            child.setA_attr(new LinkAttribute("#"));
            child.setIcon(null);
            
            TreeItem child1 = new TreeItem();
            child1.setId("id" + i);
            child1.setText("Node" + i);
            child1.setA_attr(new LinkAttribute("#"));
            child1.setIcon(null);
            
            item.getChildren().add(child);
            item.getChildren().add(child1);
            
            result.add(item);
        } 
        JSONArray jsonArray = new JSONArray(result);
        System.out.println(jsonArray.toString());
	}
	
	@Test
	public void testSplit() {
		String s = "Wu Shaolin";
		String[] items = s.split(" ");
		System.out.println(items[0]);
		System.out.println(items[1]);
	}
}
