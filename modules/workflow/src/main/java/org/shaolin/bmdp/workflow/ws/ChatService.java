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
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.workflow.be.ChatHistoryImpl;
import org.shaolin.bmdp.workflow.be.IChatHistory;
import org.shaolin.bmdp.workflow.be.NotificationImpl;
import org.shaolin.bmdp.workflow.dao.CoordinatorModel;
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

	private static final String TALK_PREFIX = " : ";

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
			AppContext.register(IServerServiceManager.INSTANCE);
			
			JSONObject data = new JSONObject(jsonMsg);
			String action = data.getString("action");
			
			if ("register".equals(action)) {
				if (UserContext.isOnlineUser(data.getLong("partyId"))) {
					session.getUserProperties().put("partyId", data.getLong("partyId"));
					if (data.has("isAbc") && data.getBoolean("isAbc") == Boolean.TRUE) {//is admin
						session.getUserProperties().put("admin", data.getBoolean("isAbc"));
					}
					session.getBasicRemote().sendText("_register_confirmed");
				} else {
					session.getBasicRemote().sendText("_register_failed");
					throw new IllegalStateException("User offline!");
				}
			} else if ("chating".equals(action)) {
				Long taskId = data.getLong("taskId");
				Long orgId = data.getLong("orgId");
				Long fromId = data.getLong("fromPartyId");
				Long toId = data.getLong("toPartyId");
				String message = data.getString("content");
				Boolean toAdmin = Boolean.FALSE;
				if (data.has("toAdmin")) {
					toAdmin = data.getBoolean("toAdmin");
				}
				if (!data.has("sessionId") || data.getString("sessionId").trim().length() == 0) {
					logger.warn("Chat session id must be specified!");
					return;
				}
				boolean sentTo = false;
				boolean sentFrom = false;
				Session sentFromSession = null;
				Set<Entry<String, Session>> entries = sessoins.entrySet();
				for (Entry<String, Session> entry : entries) {
					Session s = entry.getValue();
					if (s.isOpen()) {
						if (toAdmin == Boolean.TRUE && s.getUserProperties().containsKey("admin")
								&& s.getUserProperties().containsKey("admin") == Boolean.TRUE) {
							sentTo = true;
							send(message, s);
						} else if (s.getUserProperties().containsKey("partyId")) {
							long partyId = ((Long)s.getUserProperties().get("partyId") ).longValue();
							// online user.
							if (toId.longValue() == partyId) {
								sentTo = true;
								send(message, s);
							} else if(fromId.longValue() == partyId) {
								sentFromSession = s;
								sentFrom = true;
								send(message, s);
							}
						}
						if (sentTo && sentFrom) {
							break;
						}
					}
				}
				
				if (!sentTo && sentFromSession != null) {
					send("\u7528\u6237\u79BB\u7EBF\u4E2D\uFF0C\u8BF7\u7A0D\u540E\u8054\u7CFB!", sentFromSession);
					// save to DB as history as well.
					ChatHistoryImpl item = new ChatHistoryImpl();
					item.setTaskId(taskId);
					item.setSentPartyId(fromId);
					item.setReceivedPartyId(toId);
					item.setMessage(message);
					item.setRead(false);
					String sessionId = "";
					sessionId = data.getString("sessionId");
					item.setSessionId(sessionId);
					CoordinatorModel.INSTANCE.create(item, true);
					
					//send notification
					NotificationImpl notifier = new NotificationImpl();
					notifier.setSubject("\u60A8\u6709\u65B0\u7684\u6D88\u606F!");
					notifier.setDescription("<div><span>"+message+"</span><button onclick=\"javascript:defaultname.showHelp(defaultname.helpIcon,'"+fromId+"','"+sessionId+"');\">\u9A6C\u4E0A\u8054\u7CFB</button></div>");
					notifier.setPartyId(toId);
					notifier.setRead(false);
					notifier.setSessionId(sessionId);
					CoordinatorModel.INSTANCE.create(notifier, true);
				} else {
					
				}
				
			} else if ("history".equals(action)) {
				ChatHistoryImpl condition = new ChatHistoryImpl();
				condition.setEnabled(true);
				if (data.has("sessionId")) {
					condition.setSessionId(data.getString("sessionId").trim());
				}
				if (condition.getSessionId() != null && condition.getSessionId().length() > 0) {
					ArrayList<Order> orders = new ArrayList<Order>();
					orders.add(Order.asc("createDate"));
					List<IChatHistory> result = CoordinatorModel.INSTANCE.searchChatHistory(condition, orders, 0, -1);
					for (IChatHistory item: result) {
						send(item, session);
					}
				}
			} else if ("allhistory".equals(action)) {
				//TODO:
//				Long fromId = data.getLong("fromPartyId");
//				
//				ChatHistoryImpl condition = new ChatHistoryImpl();
//				condition.setEnabled(true);
//				condition.setSentPartyId(fromId);
//				if (data.has("sessionId")) {
//					condition.setSessionId(data.getString("sessionId"));
//				}
//				
//				ArrayList<Order> orders = new ArrayList<Order>();
//				orders.add(Order.asc("createDate"));
//				List<IChatHistory> result = CoordinatorModel.INSTANCE.searchChatHistory(condition, orders, 0, -1);
//				for (IChatHistory item: result) {
//					send(item, session);
//				}
			}
		} catch (JSONException e) {
			logger.warn("Parsing client data error! session id: " + session, e);
		}
	}

	public void send(String message, Session s) throws IOException {
		if (!checkSendPolicy(s)) {
			return;
		}
		String date;
		try {
			date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, (new Date()), null, null);
		} catch (FormatException e) {
			date = (new Date()).toString();
		}
		int prefix = message.indexOf(TALK_PREFIX);
		if (prefix != -1) {
			date = "[" + date + "] " + message.substring(0, prefix + TALK_PREFIX.length());
			message = message.substring(prefix + TALK_PREFIX.length());
		}
		s.getBasicRemote().sendText("<div class=\"swiper-slide uimaster_chat_item_to "+calculateBGStyle(s)+"\"><div><div class=\"uimaster_chat_time\">"
				 + date + "</div><div class=\"uimaster_chat_message\"> " + message + "</div></div></div>");
	}
	
	public void send(IChatHistory item, Session s) throws IOException {
		String date;
		try {
			date = FormatUtil.convertDataToUI(FormatUtil.DATE_TIME, (item.getCreateDate()), null, null);
		} catch (FormatException e) {
			date = (new Date()).toString();
		}
		String message = item.getMessage();
		int prefix = message.indexOf(TALK_PREFIX);
		if (prefix != -1) {
			date = "[" + date + "] " + message.substring(0, prefix + TALK_PREFIX.length());
			message = message.substring(prefix + TALK_PREFIX.length());
		}
		s.getBasicRemote().sendText("<div class=\"swiper-slide uimaster_chat_item_to "+calculateBGStyle(s)+"\"><div><div class=\"uimaster_chat_time\">"
				 + date + "</div><div class=\"uimaster_chat_message\"> " + message + "</div></div></div>");
	}

	public static void sendOffLine(String message, long from, long to, String sessionId) {
		ChatHistoryImpl item = new ChatHistoryImpl();
		item.setCreateDate(new Date());
		item.setMessage(message);
		item.setSentPartyId(from);
		item.setReceivedPartyId(to);
		item.setSessionId(sessionId);
		CoordinatorModel.INSTANCE.create(item);
	}
	
	public static void sendOffLine(IChatHistory item) {
		CoordinatorModel.INSTANCE.create(item);
	}
	
	public boolean checkSendPolicy(Session s) {
		if (s.getUserProperties().containsKey("sendTime")) {
			long lastTime = (Long)s.getUserProperties().get("sendTime");
			long rate = (System.currentTimeMillis() - lastTime);
			if (rate <= 500) {// 2 messages per second. too frequently.
				s.getUserProperties().put("sendTime", System.currentTimeMillis());
				return false; 
			}
		} 
		s.getUserProperties().put("sendTime", System.currentTimeMillis());
		return true;
	}
	
	private String calculateBGStyle(Session s) {
		String backgroundStyle;
		if (s.getUserProperties().containsKey("isodd")) {
			s.getUserProperties().remove("isodd");
			backgroundStyle = "uimaster_chat_item_even";
		} else {
			backgroundStyle = "uimaster_chat_item_old";
			s.getUserProperties().put("isodd",1);
		}
		return backgroundStyle;
	}
	
}
