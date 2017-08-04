/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.workflow.internal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.hibernate.criterion.Order;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.be.IPersistentEntity;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.shaolin.bmdp.workflow.be.INotification;
import org.shaolin.bmdp.workflow.be.IServerNodeInfo;
import org.shaolin.bmdp.workflow.be.ISession;
import org.shaolin.bmdp.workflow.be.ITask;
import org.shaolin.bmdp.workflow.be.ITaskHistory;
import org.shaolin.bmdp.workflow.be.NotificationImpl;
import org.shaolin.bmdp.workflow.be.ServerNodeInfoImpl;
import org.shaolin.bmdp.workflow.be.TaskHistoryImpl;
import org.shaolin.bmdp.workflow.be.TaskImpl;
import org.shaolin.bmdp.workflow.ce.PeriodicType;
import org.shaolin.bmdp.workflow.ce.TaskStatusType;
import org.shaolin.bmdp.workflow.coordinator.ICoordinatorService;
import org.shaolin.bmdp.workflow.coordinator.INotificationListener;
import org.shaolin.bmdp.workflow.coordinator.ITaskListener;
import org.shaolin.bmdp.workflow.dao.CoordinatorModel;
import org.shaolin.bmdp.workflow.dao.CustCoordinatorModel;
import org.shaolin.bmdp.workflow.ws.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorServiceImpl implements ILifeCycleProvider, ICoordinatorService, IServiceProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(CoordinatorServiceImpl.class);
	
	// make this for whole system, not only for one application instance.
	private static ScheduledExecutorService scheduler;
	
	private final Map<ITask, ScheduledFuture<?>> futures = new HashMap<ITask, ScheduledFuture<?>>();
	
	private final List<INotificationListener> listeners = new ArrayList<INotificationListener>();
	
	private List<IServerNodeInfo> serverNodes;
	
	private boolean testCaseFlag = false;
	
	private IAppServiceManager appService;
	
	public CoordinatorServiceImpl() {}
	
	public void setAppService(IAppServiceManager appService) {
		this.appService = appService;
	}

	void markAsTestCaseFlag() {
		testCaseFlag = true;
	}
	
	@Override
	public void addNotificationListener(INotificationListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void reload() {
		ServerNodeInfoImpl sc = new ServerNodeInfoImpl();
		this.serverNodes = CoordinatorModel.INSTANCE.searchServerNodes(sc, null, 0, -1);
	}
	
	@Override
	public List<ISession> getActiveSessions(ISession session, int offset, int count) {
		TaskImpl task = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			task.setOrgId(UserContext.getUserContext().getOrgId());
		}
		task.setSessionId(session.getSessionId());
		task.setEnabled(true);
		return CustCoordinatorModel.INSTANCE.searchSessions(task, offset, count);
	}
	
	@Override
	public List<ISession> getPassiveSessions(ISession session, int offset, int count) {
		TaskImpl task = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			task.setOrgId(UserContext.getUserContext().getOrgId());
		}
		task.setSessionId(session.getSessionId());
		task.setEnabled(true);
		return CustCoordinatorModel.INSTANCE.searchSessionHistory(task, offset, count);
	}
	
	@Override
	public long getActiveSessionSize(ISession session) {
		TaskImpl task = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			task.setOrgId(UserContext.getUserContext().getOrgId());
		}
		task.setSessionId(session.getSessionId());
		task.setEnabled(true);
		return CustCoordinatorModel.INSTANCE.searchSessionCount(task);
	}
	
	@Override
	public long getPassiveSessionSize(ISession session) {
		TaskImpl task = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			task.setOrgId(UserContext.getUserContext().getOrgId());
		}
		task.setSessionId(session.getSessionId());
		task.setEnabled(true);
		return CustCoordinatorModel.INSTANCE.searchSessionHistoryCount(task);
	}
	
	@Override
	public long getTaskSize() {
		TaskImpl condition = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			condition.setOrgId(UserContext.getUserContext().getOrgId());
		}
		condition.setEnabled(true);
		return CoordinatorModel.INSTANCE.searchTasksCount(condition);
	}
	
	@Override
	public ITaskHistory getHistoryTask(long taskId) {
		if (taskId == 0) {
			return null;
		}
		TaskHistoryImpl condition = new TaskHistoryImpl();
		condition.setTaskId(taskId);
		List<ITaskHistory> result = CoordinatorModel.INSTANCE.searchTasksHistory(condition, null, 0, 1);
		if (result == null || result.size() == 0) {
			throw new IllegalArgumentException("Failed to search the history task by this id: " + taskId);
		}
		return result.get(0);
	}
	
	public List<ITask> getTasks(TaskStatusType status) {
		TaskImpl condition = new TaskImpl();
		if (UserContext.getUserContext() != null && !UserContext.getUserContext().isAdmin()) {
			condition.setOrgId(UserContext.getUserContext().getOrgId());
		}
		condition.setStatus(status);
		return CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
	}
	
	@Override
	public List<ITask> getTasksBySessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().length() == 0) {
			return Collections.emptyList();
		}
		
		TaskImpl condition = new TaskImpl();
		condition.setSessionId(sessionId);
		condition.setEnabled(true);
		List<ITask> sessionTasks = CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
		if (sessionTasks.size() > 0) {
			return new ArrayList<ITask>(sessionTasks);
		} 
		
		List<ITask> allTasks = new ArrayList<ITask>();
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setSessionId(sessionId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, -1);
		if (list != null) {
			for (ITaskHistory h: list) {
				allTasks.add(moveToTask(h));
			}
		}
		return allTasks;
	}
	
	public List<ITaskHistory> getHistoryTasksBySessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().length() == 0) {
			return Collections.emptyList();
		}
		
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setSessionId(sessionId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, -1);
		return list;
	}
	
	public ITaskHistory getLastHisTaskBySessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().length() == 0) {
			return null;
		}
		
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setSessionId(sessionId);
		historyCriteria.setEnabled(true);
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("createDate"));
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, orders, 0, -1);
		if (list.size() > 0) {
			return list.get(0);
		} 
		return null;
	}
	
	public ITask getLastTaskBySessionId(String sessionId) {
		TaskImpl condition = new TaskImpl();
		condition.setSessionId(sessionId);
		condition.setEnabled(true);
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("createDate"));
		List<ITask> sessionTasks = CoordinatorModel.INSTANCE.searchTasks(condition, orders, 0, -1);
		if (sessionTasks.size() > 0) {
			return sessionTasks.get(0);
		} 
		return null;
	}
	
	@Override
	public ITask getTask(long taskId) {
		if (taskId == 0) {
			return null;
		}
		return CoordinatorModel.INSTANCE.get(taskId, TaskImpl.class);
	}

	@Override
	public boolean isTaskExecutedOnNode(String sessionId, long taskId, String flowNode) {
		if (taskId > 0 && this.isPendingTask(taskId)) {
			return true;
		} else {
			List<ITaskHistory> list = this.getHistoryTasksBySessionId(sessionId);
			for (ITaskHistory task : list) {
				if (task.getTaskId() == taskId || flowNode.equals(task.getExecutedNode()) 
						|| ICoordinatorService.END_SESSION_NODE_NAME.equals(task.getExecutedNode())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getSessionId(long taskId) {
		if (taskId == 0) {
			return "";
		}
		
		TaskImpl task = CoordinatorModel.INSTANCE.get(taskId, TaskImpl.class);
		if (task != null) {
			return task.getSessionId();
		}
		
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setTaskId(taskId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, 1);
		if (list != null && list.size() > 0) {
			return list.get(0).getSessionId();
		}
		throw new IllegalArgumentException("Session Id can't be found by this task id: " + taskId);
	}
	
	@Override
	public boolean isSessionEnded(String sessionId) {
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setSessionId(sessionId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, -1);
		for (ITaskHistory item : list) {
			if (ICoordinatorService.END_SESSION_NODE_NAME.equals(item.getExecutedNode()) 
					&& item.getTaskId() == -1 && item.getStatus() == TaskStatusType.COMPLETED) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ITask> getAllExpiredTasks() {
		return getTasks(TaskStatusType.EXPIRED);
	}
	
	@Override
	public List<ITask> getPartyTasks(long partyId) {
		TaskImpl condition = new TaskImpl();
		condition.setPartyId(partyId);
		condition.setEnabled(true);
		List<ITask> partyTasks = CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
		return partyTasks;
	}
	
	@Override
	public boolean isPendingTask(long taskId) {
		if (taskId == 0) {
			return false;
		}
		ITask task = getTask(taskId);
		if (task == null) {
			return false;
		}
		return task.getStatus() == TaskStatusType.NOTSTARTED 
				|| task.getStatus() == TaskStatusType.INPROGRESS;
	}

	@Override
	public void addTask(final ITask task) {
		if (task.getStatus() == TaskStatusType.COMPLETED
				|| task.getStatus() == TaskStatusType.CANCELLED) {
			throw new IllegalArgumentException("Task is finished: " + task.getStatus());
		}
		
		task.setStatus(TaskStatusType.NOTSTARTED);
		
		if (!testCaseFlag) {
			if (task.getId() == 0) {
				CoordinatorModel.INSTANCE.create(task);
			}
		}
		schedule(task);
		if (!testCaseFlag) {
			CoordinatorModel.INSTANCE.update(task);
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	private void schedule(final ITask task) {
		if (task.getStatus() == TaskStatusType.COMPLETED
				|| task.getStatus() == TaskStatusType.CANCELLED) {
			throw new IllegalArgumentException("Task must be not in started state: " + task.getStatus());
		}
		//No need at here.
		//AppContext.get().getService(IResourceManager.class).assignOnwer(task);
		
		if (task.getPeriodicJob() != null) {
			long initialDelay;
			long period;
			long nowGap = 24 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if (task.getPeriodicValue() >= nowGap) {
				long v = task.getPeriodicValue() - nowGap;
				initialDelay = v == 0 ? 1 : v;
				period = v == 0 ? 1 : v;
			} else {
				initialDelay = 24 + task.getPeriodicValue() - nowGap;
				period = 24 + task.getPeriodicValue() - nowGap;
			}
			
			if (task.getPeriodicType() == PeriodicType.WEEKLY) {
				initialDelay = initialDelay + (7 * 12);
			} else if (task.getPeriodicType() == PeriodicType.MONTHLY) {
				//TODO: Bug on the accuracy
				initialDelay = initialDelay + (30 * 12);
			}
			ScheduledFuture<?> f = scheduler.scheduleAtFixedRate(task.getPeriodicJob(), initialDelay, period, TimeUnit.HOURS);
			futures.put(task, f);
			logger.debug("Scheduled a periodic job: " + task);
			return;
		}
		
		if (task.getExpiredTime() != null) {
			long delay = task.getExpiredTime().getTime() - System.currentTimeMillis();
			if (delay <= 0) {
				expireTask(task);
				return;
			}
			ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
				@Override
				public void run() {
					expireTask(task);
				}
			}, delay, TimeUnit.MILLISECONDS);
			futures.put(task, future);
		}
		task.setStatus(TaskStatusType.INPROGRESS);
		
		taskToNotification(task);
	}
	
	public void postponeTask(ITask task, Date date) {
		if (task.getExpiredTime() != null) {
			if (task.getExpiredTime().getTime() >= date.getTime()) {
				throw new IllegalArgumentException("Current task expired time is greater than the given date.");
			}
		}
		task.setExpiredTime(date);
		task.setStatus(TaskStatusType.NOTSTARTED);
				
		updateTask(task);
	}
	
	@Override
	public void updateTask(final ITask task) {
		if (task.getId() <= 0) {
			throw new IllegalArgumentException("The created task can't be updated!");
		}
		
		ScheduledFuture<?> future = futures.remove(task);
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		
		if (task.getStatus() == TaskStatusType.NOTSTARTED) {
			schedule(task);
		}
		if (!testCaseFlag) {
			CoordinatorModel.INSTANCE.update(task);
	        HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
	private void expireTask(ITask task) {
		if (logger.isDebugEnabled()) {
			logger.debug("Task is expired!  {}", task.toString());
		}
		if (task.getStatus() == TaskStatusType.EXPIRED) {
			return;
		}
		AppContext.register(appService);
		
		ScheduledFuture<?> future = futures.remove(task); 
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		
		task.setStatus(TaskStatusType.EXPIRED);
		task.setCompleteRate(0);
		
		if (!testCaseFlag) {
			// only update the task that give the customer change to make the decision.
			CoordinatorModel.INSTANCE.update(task);
			// commit DB session once task completed.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
        
		if (task.getListener() != null) {
			ITaskListener listener = (ITaskListener)task.getListener();
			listener.notifyExpired();
		}
		
		taskToNotification(task);
	}
	
	@Override
	public void completeTask(final ITask task) {
		if (task == null) {
			return;
		}
		AppContext.register(appService);
		if (logger.isTraceEnabled()) {
			logger.trace("Task is completed.  {}", task.toString());
		}
		
		ScheduledFuture<?> future = futures.remove(task); 
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		
		task.setStatus(TaskStatusType.COMPLETED);
		task.setCompleteRate(100);
		
		if (task.getListener() != null) {
			ITaskListener listener = (ITaskListener)task.getListener();
			listener.notifyCompleted();
		}
		taskToNotification(task);
	}
	
	@Override
	public void cancelTask(final ITask task) {
		AppContext.register(appService);
		if (logger.isTraceEnabled()) {
			logger.trace("Task is cancelled.  {}", task.toString());
		}
		
		ScheduledFuture<?> future = futures.remove(task);
		if (future != null && !future.isDone()) {
			future.cancel(true);
			task.setStatus(TaskStatusType.CANCELLED);
			
			if (task.getListener() != null) {
				ITaskListener listener = (ITaskListener)task.getListener();
				listener.notifyCancelled();
			}
			taskToNotification(task);
		}
	}
	
	boolean moveToHistory(final String sessionId) {
		if (sessionId == null || sessionId.trim().length() == 0) {
			return false;
		}
		try {
			TaskImpl condition = new TaskImpl();
			condition.setSessionId(sessionId);
			condition.setEnabled(true);
			List<ITask> sessionTasks = CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
			List<ITask> allTasks = new ArrayList<ITask>(sessionTasks);
			while (allTasks.size() > 0) {
				ITask task = (ITask)allTasks.remove(0);
				TaskHistoryImpl history = new TaskHistoryImpl();
				history.setTaskId(task.getId());
				history.setOrgId(task.getOrgId());
				history.setSessionId(task.getSessionId());
				if (task.getFlowState() != null) {
					FlowRuntimeContext runtime;
					try {
						runtime = FlowRuntimeContext.unmarshall(task.getFlowState());
						history.setExecutedNode(runtime.currentNodeToString());
					} catch (Exception e) {
					}
				}
				history.setCompleteRate(task.getCompleteRate());
				history.setDescription(task.getDescription());
				history.setEnabled(task.isEnabled());
				history.setExpiredTime(task.getExpiredTime());
				history.setPartyId(task.getPartyId());
				history.setPartyType(task.getPartyType());
				history.setPriority(task.getPriority());
				history.setSendEmail(task.getSendEmail());
				history.setSendSMS(task.getSendSMS());
				history.setStatus(task.getStatus());
				history.setSubject(task.getSubject());
				history.setComments(task.getComments());
				history.setCreateDate(task.getCreateDate());
				
				CoordinatorModel.INSTANCE.create(history);
				CoordinatorModel.INSTANCE.delete(task);
			}
			ITaskHistory htask = new TaskHistoryImpl();
	        if (UserContext.getCurrentUserContext() != null) {
				htask.setOrgId((Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID));
			}
	        htask.setSessionId(sessionId);
	        htask.setTaskId(-1);
	        htask.setExecutedNode(ICoordinatorService.END_SESSION_NODE_NAME);
	        htask.setSubject("Task: " + ICoordinatorService.END_SESSION_NODE_NAME);
	        htask.setDescription("Flow is finished!");
	        htask.setEnabled(true);
	        htask.setCreateDate(new Date());
	        htask.setStatus(TaskStatusType.COMPLETED);
	        htask.setCompleteRate(100);
	        CoordinatorModel.INSTANCE.create(htask);
	        
	        HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
	        return true;
		} catch (Throwable e) {
			logger.warn("" + e.getMessage(), e);
			HibernateUtil.releaseSession(HibernateUtil.getSession(), false);
			return false;
		} 
	}
	
	private ITask moveToTask(ITaskHistory hTask) {
		TaskImpl task = new TaskImpl();
		task.setId(hTask.getTaskId());
		task.setOrgId(hTask.getOrgId());
		task.setCompleteRate(hTask.getCompleteRate());
		task.setDescription(hTask.getDescription());
		task.setEnabled(hTask.isEnabled());
		task.setExpiredTime(hTask.getExpiredTime());
		task.setPartyId(hTask.getPartyId());
		task.setPartyType(hTask.getPartyType());
		task.setPriority(hTask.getPriority());
		task.setSendEmail(hTask.getSendEmail());
		task.setSendSMS(hTask.getSendSMS());
		task.setStatus(hTask.getStatus());
		task.setSubject(hTask.getSubject());
		task.setComments(hTask.getComments());
		task.setSessionId(hTask.getSessionId());
		task.setCreateDate(hTask.getCreateDate());
		return task;
	}
	
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	@Override
	public void configService() {
		
	}
	
	@Override
	public void startService() {
		this.futures.clear();
		
		this.setAppService(IServerServiceManager.INSTANCE);
		
		// make this shared
		scheduler = IServerServiceManager.INSTANCE.getSchedulerService()
				.createScheduler("system", "wf-coordinator", Runtime.getRuntime().availableProcessors());
		
		if (testCaseFlag) {
			return;
		}
		// TODO: must be optimized by nodejs.
		// load all pending tasks when system up.
		/**
		TaskImpl condition = new TaskImpl();
		List<ITask> tasks = CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
		for (ITask t : tasks) {
			workingTasks.put(t.getId(), t);
			t.setListener(new MissionListener(t));
			
			if (t.getStatus() == TaskStatusType.NOTSTARTED || t.getStatus() == TaskStatusType.INPROGRESS) {
				if (t.getExpiredTime() == null || System.currentTimeMillis() < t.getExpiredTime().getTime()) {
					schedule(t);
				} else {
					//trigger it directly.
					expireTask(t);
				}
			}
		}
		*/
		ServerNodeInfoImpl sc = new ServerNodeInfoImpl();
		this.serverNodes = CoordinatorModel.INSTANCE.searchServerNodes(sc, null, 0, -1);
	}

	@Override
	public boolean readyToStop() {
		return true;
	}

	@Override
	public void stopService() {
		scheduler.shutdown();
		listeners.clear();
		
		Set<ITask> tasks = futures.keySet();
		for (ITask task: tasks) {
			ScheduledFuture<?> future = futures.get(task);
			if (future != null && !future.isDone()) {
				future.cancel(true);
			}
		}
		futures.clear();
	}

	@Override
	public int getRunLevel() {
		return 20;
	}
	
	private void taskToNotification(ITask t) {
//		NotificationImpl notifier = new NotificationImpl();
//		notifier.setSubject("[" + t.getStatus().getDisplayName() + "] " + t.getSubject());
//		notifier.setDescription(t.getDescription());
//		notifier.setPartyId(t.getPartyId());
//		notifier.setRead(false);
		// no need since all tasks have already been tracking.
		// addNotification(notifier, false);
	}
	
	private static class NotificationTask implements Runnable {
		
		private final INotification message;
		
		private final List<INotificationListener> listeners;
		
		public NotificationTask(INotification message, List<INotificationListener> listeners) {
			this.message = message;
			this.listeners = listeners;
		}
		@Override
		public void run() {
			if (NotificationService.push(message, message.getPartyId())) {
				message.setRead(true);
			}
			CoordinatorModel.INSTANCE.create(message, true);
			for (INotificationListener listener : listeners) {
				listener.received(message);
			}
		}
	}
	
	@Override
	public void addNotification(final INotification message, final boolean needRemoted) {
		scheduler.submit(new NotificationTask(message, listeners));
	}
	
	@Override
	public void addNotificationToAdmin(final INotification message, final boolean needRemoted) {
		message.setPartyId(1);
		message.setOrgId(1);
		scheduler.submit(new NotificationTask(message, listeners));
	}
	
	public List<IServerNodeInfo> getServerNodes() {
		return new ArrayList<IServerNodeInfo>(this.serverNodes);
	}
	
	@Override
	public void addServerNode(String ipAddress, int port) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeServerNode(String ipAddress, int port) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<INotification> pullNotifications(long partyId, Date queryDate) {
		NotificationImpl scFlow = new NotificationImpl();
		scFlow.setPartyId(partyId);
		scFlow.setCreateDate(queryDate);
		scFlow.setRead(false);
		return CoordinatorModel.INSTANCE.searchNotification(scFlow, null, 0, -1);
	}
	
	@Override
	public void cleanAllNotifications(Long partyId) {
		NotificationImpl scFlow = new NotificationImpl();
		scFlow.setPartyId(partyId);
		List<INotification> items = CoordinatorModel.INSTANCE.searchNotification(scFlow, null, 0, -1);
		List<IPersistentEntity> entities = new ArrayList<IPersistentEntity>();
		for (INotification i : items) {
			entities.add(i);
		}
		CoordinatorModel.INSTANCE.batchDelete(entities, true);
	}
	
	public List<INotification> pullCommonNotifications() {
		return Collections.emptyList();
//		NotificationImpl scFlow = new NotificationImpl();
//		scFlow.setPartyId(0);
//		return CoordinatorModel.INSTANCE.searchNotification(scFlow, null, 0, -1);
	}

	public static String toURL(IServerNodeInfo serverNode) {
		//TODO: whether is the http or http configured
		return serverNode.getProtocol() + serverNode.getIpAddress() + ":" + serverNode.getPort() + serverNode.getDomain();
	} 
	
	@Override
	public Class<?> getServiceInterface() {
		return ICoordinatorService.class;
	}
	
}
