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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.shaolin.bmdp.utils.HttpSender;
import org.shaolin.bmdp.utils.SerializeUtil;
import org.shaolin.bmdp.workflow.be.INotification;
import org.shaolin.bmdp.workflow.be.IServerNodeInfo;
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
import org.shaolin.bmdp.workflow.coordinator.IResourceManager;
import org.shaolin.bmdp.workflow.coordinator.ITaskListener;
import org.shaolin.bmdp.workflow.dao.CoordinatorModel;
import org.shaolin.bmdp.workflow.internal.FlowContainer.MissionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordinatorServiceImpl implements ILifeCycleProvider, ICoordinatorService, IServiceProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(CoordinatorServiceImpl.class);
	
	// make this for whole system, not only for one application instance.
	private ScheduledExecutorService pool;
	
	private final Map<ITask, ScheduledFuture<?>> futures = new HashMap<ITask, ScheduledFuture<?>>();
	
	private final ConcurrentHashMap<Long, ITask> workingTasks = new ConcurrentHashMap<Long, ITask>();
	
	private final ConcurrentHashMap<Long, List<INotification>> allNotifications 
		= new ConcurrentHashMap<Long, List<INotification>>();
	
	private final List<INotificationListener> listeners = new ArrayList<INotificationListener>();
	
	private List<IServerNodeInfo> serverNodes;
	
	private boolean testCaseFlag = false;
	
	private IAppServiceManager appService;
	
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
		if (AppContext.isMasterNode()) {
			ServerNodeInfoImpl sc = new ServerNodeInfoImpl();
			this.serverNodes = CoordinatorModel.INSTANCE.searchServerNodes(sc, null, 0, -1);
		}
	}
	
	@Override
	public List<ITask> getAllTasks() {
		long orgId = 0;
		if (UserContext.getCurrentUserContext() != null) {
			orgId = (Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID);
		}
		List<ITask> allTasks = new ArrayList<ITask>();
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getOrgId() == orgId) {
				allTasks.add(t);
			}
		}
		return allTasks;
	}
	
	@Override
	public int getTaskSize() {
		return workingTasks.size();
	}
	
	public List<Long> getAllTaskOnwers() {
		long orgId = 0;
		if (UserContext.getCurrentUserContext() != null) {
			orgId = (Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID);
		}
		List<Long> onwers = new ArrayList<Long>();
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getOrgId() == orgId) {
				onwers.add(t.getPartyId());
			}
		}
		return onwers;
	}
	
	@Override
	public List<ITaskHistory> getHistoryTasks(TaskStatusType status) {
		TaskHistoryImpl condition = new TaskHistoryImpl();
		if (UserContext.getCurrentUserContext() != null) {
			condition.setOrgId((Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID));
		}
		condition.setStatus(TaskStatusType.EXPIRED);
		return CoordinatorModel.INSTANCE.searchTasksHistory(condition, null, 0, -1);
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
	
	@Override
	public List<ITask> getTasks(TaskStatusType status) {
		if (status == TaskStatusType.EXPIRED) {
			TaskImpl condition = new TaskImpl();
			if (UserContext.getCurrentUserContext() != null) {
				condition.setOrgId((Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID));
    		}
			condition.setStatus(TaskStatusType.EXPIRED);
			return CoordinatorModel.INSTANCE.searchTasks(condition, null, 0, -1);
		}
		long orgId = 0;
		if (UserContext.getCurrentUserContext() != null) {
			orgId = (Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID);
		}
		List<ITask> partyTasks = new ArrayList<ITask>();
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getStatus() == status && t.getOrgId() == orgId) {
				partyTasks.add(t);
			}
		}
		return partyTasks;
	}
	
	@Override
	public List<ITask> getTasksBySessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().length() == 0) {
			return Collections.emptyList();
		}
		
		List<ITask> sessionTasks = new ArrayList<ITask>();
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getSessionId().equals(sessionId)) {
				sessionTasks.add(t);
			}
		}
		
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setSessionId(sessionId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, -1);
		if (list != null) {
			for (ITaskHistory h: list) {
				sessionTasks.add(moveToTask(h));
			}
		}
		return sessionTasks;
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
	
	public ITask getLastTaskBySessionId(String sessionId) {
		return null;
	}
	
	public String getSessionId(long taskId) {
		if (taskId == 0) {
			return "";
		}
		
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getId() == taskId) {
				return t.getSessionId();
			}
		}
		
		TaskHistoryImpl historyCriteria = new TaskHistoryImpl();
		historyCriteria.setTaskId(taskId);
		List<ITaskHistory> list = CoordinatorModel.INSTANCE.searchTasksHistory(historyCriteria, null, 0, -1);
		if (list != null && list.size() > 0) {
			return list.get(0).getSessionId();
		}
		throw new IllegalArgumentException("Session Id can't be found by this task id: " + taskId);
	}
	
	@Override
	public List<ITask> getAllExpiredTasks() {
		return getTasks(TaskStatusType.EXPIRED);
	}
	
	@Override
	public List<ITask> getPartyTasks(long partyId) {
		long orgId = 0;
		if (UserContext.getCurrentUserContext() != null) {
			orgId = (Long)UserContext.getUserData(UserContext.CURRENT_USER_ORGID);
		}
		List<ITask> partyTasks = new ArrayList<ITask>();
		Collection<ITask> tasks = workingTasks.values();
		for (ITask t : tasks) {
			if (t.getOrgId() == orgId && t.getPartyId() == partyId) {
				partyTasks.add(t);
			}
		}
		return partyTasks;
	}
	
	@Override
	public ITask getTask(long taskId) {
		if (taskId == 0) {
			return null;
		}
		Collection<ITask> tasks= workingTasks.values();
		for (ITask t : tasks) {
			if (t.getId() == taskId) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public boolean isTaskExecutedOnNode(long taskId, String flowNode) {
		if (taskId == 0) {
			return false;
		}
		
		if (this.isPendingTask(taskId)) {
			return true;
		} else {
			ITaskHistory history = this.getHistoryTask(taskId);
			List<ITaskHistory> list = this.getHistoryTasksBySessionId(history.getSessionId());
			for (ITaskHistory task : list) {
				if (task.getExecutedNode() == null) {
					continue;
				}
				if (flowNode.equals(task.getExecutedNode())) {
					// matched node.
					return true;
				} else if (ICoordinatorService.END_SESSION_NODE_NAME.equals(task.getExecutedNode())) {
					// session is terminated.
					return true;
				}
			}
		}
		return false;
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
		if (workingTasks.putIfAbsent(task.getId(), task) == null) {
			schedule(task);
		}
		if (!testCaseFlag) {
			CoordinatorModel.INSTANCE.update(task);
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	private void schedule(final ITask task) {
		if (task.getStatus() == TaskStatusType.COMPLETED
				|| task.getStatus() == TaskStatusType.CANCELLED) {
			workingTasks.remove(task.getId());
			throw new IllegalArgumentException("Task must be not in started state: " + task.getStatus());
		}
		AppContext.get().getService(IResourceManager.class).assignOnwer(task);
		
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
			ScheduledFuture<?> f = pool.scheduleAtFixedRate(task.getPeriodicJob(), initialDelay, period, TimeUnit.HOURS);
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
			ScheduledFuture<?> future = pool.schedule(new Runnable() {
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
	public void updateTask(ITask task) {
		if (task.getId() <= 0) {
			throw new IllegalArgumentException("The created task can't be updated!");
		}
		
		ScheduledFuture<?> future = futures.remove(task);
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		if (!workingTasks.containsKey(task.getId())) {
			workingTasks.put(task.getId(), task);
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
		workingTasks.remove(task.getId());
		
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
	public void completeTask(ITask task) {
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
		workingTasks.remove(task.getId());
		
		task.setStatus(TaskStatusType.COMPLETED);
		task.setCompleteRate(100);
		
		if (!testCaseFlag) {
			moveToHistory(task, HibernateUtil.getSession());
			// commit DB session once task completed.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
		
		if (task.getListener() != null) {
			ITaskListener listener = (ITaskListener)task.getListener();
			listener.notifyCompleted();
		}
		taskToNotification(task);
	}
	
	@Override
	public void cancelTask(ITask task) {
		AppContext.register(appService);
		if (logger.isTraceEnabled()) {
			logger.trace("Task is cancelled.  {}", task.toString());
		}
		
		ScheduledFuture<?> future = futures.remove(task);
		if (future != null && !future.isDone()) {
			future.cancel(true);
			
			workingTasks.remove(task.getId());
			task.setStatus(TaskStatusType.CANCELLED);
			if (!testCaseFlag) {
				moveToHistory(task, HibernateUtil.getSession());
				// commit DB session once task completed.
				HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
			}
			
			if (task.getListener() != null) {
				ITaskListener listener = (ITaskListener)task.getListener();
				listener.notifyCancelled();
			}
			
			taskToNotification(task);
		}
	}
	
	private void moveToHistory(ITask task, Session session) {
		if (task.getId() == 0) {
			return;
		}
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
		history.setCreateTime(task.getCreateTime());
		
		session.save(history);
		session.delete(task);
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
		task.setCreateTime(hTask.getCreateTime());
		return task;
	}
	
	@Override
	public void startService() {
		this.workingTasks.clear();
		this.futures.clear();
		
		String masterNode = IServerServiceManager.INSTANCE.getMasterNodeName();
		this.setAppService(IServerServiceManager.INSTANCE.getApplication(masterNode));
		AppContext.get().register(this);
		
		// make this shared
		this.pool = IServerServiceManager.INSTANCE.getSchedulerService()
				.createScheduler("system", "wf-coordinator", Runtime.getRuntime().availableProcessors() * 2);
		
		if (testCaseFlag) {
			return;
		}
		// load all pending tasks when system up.
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
		
		if (AppContext.isMasterNode()) {
			ServerNodeInfoImpl sc = new ServerNodeInfoImpl();
			this.serverNodes = CoordinatorModel.INSTANCE.searchServerNodes(sc, null, 0, -1);
			
			// clean all notifications daily for saving memory.
			this.pool.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					allNotifications.clear();
				}
			}, 1, 1, TimeUnit.DAYS);
		}
	}

	@Override
	public boolean readyToStop() {
		return true;
	}

	@Override
	public void stopService() {
		pool.shutdown();
		workingTasks.clear();
		listeners.clear();
		if (httpSender != null) {
			httpSender.shutdown();
		}
		
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
		NotificationImpl notifier = new NotificationImpl();
		if (t.getExpiredTime() != null) {
			notifier.setSubject("[" + t.getExpiredTime().toString() 
				+ "/" + t.getStatus().getDisplayName() + "] " + t.getSubject());
		} else {
			notifier.setSubject("[" + t.getStatus().getDisplayName() + "] " + t.getSubject());
		}
		notifier.setDescription(t.getDescription());
		notifier.setPartyId(t.getPartyId());
		addNotification(notifier, false);
	}
	
	@Override
	public void addNotification(INotification message, boolean needRemoted) {
		if (needRemoted) {
			// send to all nodes.
			byte[] raw = null;
			try {
				raw = SerializeUtil.serializeData(message);
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
				return;
			}
			IAppServiceManager serviceManager= IServerServiceManager.INSTANCE.getApplication(
					IServerServiceManager.INSTANCE.getMasterNodeName());
			ICoordinatorService coordinator = serviceManager.getService(ICoordinatorService.class);
			List<IServerNodeInfo> nodes = coordinator.getServerNodes();
			for (IServerNodeInfo serverNode : nodes) {
				if (!sendNotification(serverNode, raw)) {
					logger.warn("Failed to send the notification to {0}",
							toURL(serverNode));
				}
			}
		} else {
			long partyId = message.getPartyId();
			if (!allNotifications.containsKey(partyId)) {
				allNotifications.put(partyId, new ArrayList<INotification>());
			}
			allNotifications.get(partyId).add(message);
			
			for (INotificationListener listener : listeners) {
				listener.received(message);
			}
		}
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
	public List<INotification> pullNotifications(long userId) {
		if (allNotifications.contains(userId)) {
			return allNotifications.remove(userId);
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	/**
	 * the common notifications must be refreshed every hour.
	 * @return
	 */
	public List<INotification> pullCommonNotifications() {
		return allNotifications.get(0);
	}
	
	private HttpSender httpSender;
	
	private boolean sendNotification(IServerNodeInfo serverNode, byte[] rawMessage) {
		if (httpSender == null) {
			httpSender = new HttpSender();
		}
		return httpSender.post(toURL(serverNode), rawMessage);
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
