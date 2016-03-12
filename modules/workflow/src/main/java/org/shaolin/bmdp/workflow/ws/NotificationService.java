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
package org.shaolin.bmdp.workflow.ws;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.workflow.be.INotification;
import org.shaolin.bmdp.workflow.coordinator.ICoordinatorService;
import org.shaolin.uimaster.page.ajax.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wushaol
 */
@ServerEndpoint("/wsnotificator")
public class NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	private static final ConcurrentHashMap<String, Session> sessoins = new ConcurrentHashMap<String, Session>();
	
	public NotificationService() {
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		if (logger.isDebugEnabled()) { 
			logger.debug("{0} Client connected.", session.getId());
		}
		sessoins.put(session.getId(), session);
	}

	@OnClose
	public void onClose(Session session) {
		if (logger.isDebugEnabled()) { 
			logger.debug("{0} Client closed.", session.getId());
		}
		sessoins.remove(session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		sessoins.remove(session.getId());
		logger.debug("Client error: " + session.getId(), throwable);
	}

	@OnMessage
	public void onMessage(String jsonMsg, Session session) throws IOException,
			InterruptedException {
		if (logger.isDebugEnabled()) { 
			logger.info("Received a message: {0}", jsonMsg);
		}
		try {
			AppContext.register(IServerServiceManager.INSTANCE.getApplication(
					IServerServiceManager.INSTANCE.getMasterNodeName()));
			
			JSONObject data = new JSONObject(jsonMsg);
			String action = data.getString("action");
			
			if ("register".equals(action)) {
				session.getUserProperties().put("partyId", data.getLong("partyId"));
				session.getBasicRemote().sendText("_register_confirmed");
			} else if ("poll".equals(action)) {
				poll(session);
			}
		} catch (Exception e) {
			logger.warn("Parsing client data error! session id: " + session, e);
		}
	}
	
	private void poll(final Session session) {
		if (!session.getUserProperties().containsKey("partyId")) {
			return;
		}
		Long userId = (Long)session.getUserProperties().get("partyId");
		Date queryDate = null;
		if (session.getUserProperties().containsKey("LastQueryDate")) {
			queryDate = (Date)session.getUserProperties().get("LastQueryDate");
		}
		session.getUserProperties().put("LastQueryDate", new Date());
		ICoordinatorService service = (ICoordinatorService)
				IServerServiceManager.INSTANCE.getService(ICoordinatorService.class);
		List<INotification> items = service.pullNotifications(userId, queryDate);
		
		for (INotification item : items) {
			StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"uimaster_noti_item\"><div style=\"color:blue;\">");
			sb.append("[").append(item.getCreateDate()).append("] ").append(item.getSubject());
			sb.append("</div><div style=\"width:100%;\">");
			sb.append(item.getDescription()).append("</div></div>");
			try {
				session.getBasicRemote().sendText(sb.toString());
			} catch (IOException e) {
				logger.warn("Error sending the notifications! session id: " + session, e);
			}
		}
	}
}
