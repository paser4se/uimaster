package org.shaolin.bmdp.workflow.internal;

import org.junit.Assert;
import org.junit.Test;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.SpringBootTestRoot;
import org.shaolin.bmdp.runtime.spi.FlowEvent;
import org.shaolin.bmdp.workflow.be.ITask;
import org.shaolin.bmdp.workflow.coordinator.IResourceManager;
import org.springframework.stereotype.Service;

public class ProcessFlowTest extends SpringBootTestRoot {

	private WorkflowLifecycleServiceImpl wfservice = new WorkflowLifecycleServiceImpl();
	
	private CoordinatorServiceImpl coordinator = new CoordinatorServiceImpl();
	
	private MockEventProducer producer = new MockEventProducer();
	
	public ProcessFlowTest() {
	}
	
	@Test
	public void testEmpty(){}
	
	public void testExecuteFlow() throws Exception {
		wfservice.startService();
		coordinator.markAsTestCaseFlag();
		coordinator.startService();
		producer.setEventProcessor(AppContext.get().getService(WorkFlowEventProcessor.class));
		
		normalTest("NodeTest", "producer", 1000);
		
		missionTest("mission-flow", "producer1", 1000);
		
		Thread.sleep(1000);
		wfservice.stopService();
	}
	
	private void normalTest(String nodeName, String eventConsumer, int waitSeconds) throws InterruptedException {
        FlowEvent evt;
        evt = new FlowEvent(eventConsumer);
        evt.setAttribute("Request", nodeName);
        evt.setAttribute("NodeName", nodeName);
        producer.sendEvent(evt);
        Thread.sleep(waitSeconds);
        Assert.assertEquals(evt.getAttribute("Response"), nodeName);
    }
	
	private void missionTest(String nodeName, String eventConsumer, int waitSeconds) throws InterruptedException {
        FlowEvent evt;
        evt = new FlowEvent(eventConsumer);
        evt.setAttribute("Request", nodeName);
        evt.setAttribute("NodeName", nodeName);
        evt.setAttribute("orderObject", "orderObject");
        producer.sendEvent(evt);//place and order
        Thread.sleep(waitSeconds);
        
        //auto approved order
    }
	
	@Service
	public class ResourceManagerImpl implements IResourceManager {

		@Override
		public Class getServiceInterface() {
			return IResourceManager.class;
		}

		@Override
		public void assignOnwer(ITask task) {
			task.setPartyId(1);
		}

		@Override
		public Object getResource(long orgId, long partyId) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
}
