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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.hibernate.criterion.Order;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.workflow.be.ChatHistoryImpl;
import org.shaolin.bmdp.workflow.be.IChatHistory;
import org.shaolin.bmdp.workflow.dao.CoordinatorModel;
import org.shaolin.uimaster.page.ajax.json.JSONException;
import org.shaolin.uimaster.page.ajax.json.JSONObject;
import org.shaolin.uimaster.page.exception.FormatException;
import org.shaolin.uimaster.page.od.formats.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wushaol
 */
@ServerEndpoint("/wschart")
public class ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	private static final ConcurrentHashMap<String, Session> sessoins = new ConcurrentHashMap<String, Session>();
	
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
		logger.debug("Client error! session id: " + session, throwable);
	}

	@OnMessage
	public void onMessage(String jsonMsg, Session session) throws IOException,
			InterruptedException {
		if (logger.isDebugEnabled()) { 
			logger.debug("Received a message: {}", jsonMsg);
		}
		try {
			AppContext.register(IServerServiceManager.INSTANCE.getApplication(
					IServerServiceManager.INSTANCE.getMasterNodeName()));
			
			JSONObject data = new JSONObject(jsonMsg);
			String action = data.getString("action");
			
			if ("register".equals(action)) {
				session.getUserProperties().put("partyId", data.getLong("partyId"));
				session.getBasicRemote().sendText("_register_confirmed");
			} else if ("chating".equals(action)) {
				Long taskId = data.getLong("taskId");
				Long orgId = data.getLong("orgId");
				Long fromId = data.getLong("fromPartyId");
				Long toId = data.getLong("toPartyId");
				String message = data.getString("content");
				
				boolean sentTo = false;
				boolean sentFrom = false;
				Set<Entry<String, Session>> entries = sessoins.entrySet();
				for (Entry<String, Session> entry : entries) {
					Session s = entry.getValue();
					if (s.isOpen()) {
						if (s.getUserProperties().containsKey("partyId")) {
							if (toId.longValue() == ((Long)s.getUserProperties().get("partyId") ).longValue()) {
								sentTo = true;
							    // online user.
								String date;
								try {
									date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, (new Date()), null, null);
								} catch (FormatException e) {
									date = (new Date()).toString();
								}
								s.getBasicRemote().sendText("<div class=\"uimaster_chat_item_to\"><div class=\"uimaster_chat_time\">["
										 + date + "]:</div><div class=\"uimaster_chat_message\">" + message + "</div><div>");
							} else if(fromId.longValue() == ((Long)s.getUserProperties().get("partyId") ).longValue()) {
								sentFrom = true;
								String date;
								try {
									date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, (new Date()), null, null);
								} catch (FormatException e) {
									date = (new Date()).toString();
								}
								s.getBasicRemote().sendText("<div class=\"uimaster_chat_item_from\"><div class=\"uimaster_chat_time\">["
										 + date + "]:</div><div class=\"uimaster_chat_message\">" + message + "</div><div>");
							}
							
							if (sentTo && sentFrom) {
								break;
							}
						}
					}
				}
			} else if ("leavewords".equals(action)) {
				Long taskId = data.getLong("taskId");
				Long orgId = data.getLong("orgId");
				Long fromId = data.getLong("fromPartyId");
				Long toId = data.getLong("toPartyId");
				String message = data.getString("content");
				// save to DB as history as well.
				ChatHistoryImpl item = new ChatHistoryImpl();
				item.setTaskId(taskId);
				item.setOrgId(orgId);
				item.setSentPartyId(fromId);
				item.setReceivedPartyId(toId);
				item.setMessage(message);
				item.setRead(false);
				CoordinatorModel.INSTANCE.create(item, true);
			} else if ("history".equals(action)) {
				Long fromId = data.getLong("fromPartyId");
				Long toId = data.getLong("toPartyId");
				
				ChatHistoryImpl condition = new ChatHistoryImpl();
				condition.setEnabled(true);
				condition.setSentPartyId(fromId);
				condition.setReceivedPartyId(toId);
				ArrayList<Order> orders = new ArrayList<Order>();
				orders.add(Order.asc("createDate"));
				List<IChatHistory> result = CoordinatorModel.INSTANCE.searchChatHistory(condition, orders, 0, -1);
				for (IChatHistory item: result) {
					String date;
					try {
						date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, item.getCreateDate(), null, null);
					} catch (FormatException e) {
						date = (item.getCreateDate()).toString();
					}
					session.getBasicRemote().sendText("<div class=\"uimaster_chat_item\"><div class=\"uimaster_chat_time\">[" + date + 
							"]:</div><div class=\"uimaster_chat_message\">" + item.getMessage() + "</div></div>");
				}
			} else if ("allhistory".equals(action)) {
				Long fromId = data.getLong("fromPartyId");
				
				ChatHistoryImpl condition = new ChatHistoryImpl();
				condition.setEnabled(true);
				condition.setSentPartyId(fromId);
				ArrayList<Order> orders = new ArrayList<Order>();
				orders.add(Order.asc("createDate"));
				List<IChatHistory> result = CoordinatorModel.INSTANCE.searchChatHistory(condition, orders, 0, -1);
				for (IChatHistory item: result) {
					String date;
					try {
						date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, item.getCreateDate(), null, null);
					} catch (FormatException e) {
						date = (item.getCreateDate()).toString();
					}
					session.getBasicRemote().sendText("<div class=\"uimaster_chat_item\"><div class=\"uimaster_chat_time\">[" + date + 
							"]:</div><div class=\"uimaster_chat_message\">" + item.getMessage() + "</div></div>");
				}
			}
		} catch (JSONException e) {
			logger.warn("Parsing client data error! session id: " + session, e);
		}
	}
	
}
