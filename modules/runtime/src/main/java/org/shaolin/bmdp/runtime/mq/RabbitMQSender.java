package org.shaolin.bmdp.runtime.mq;

import java.io.IOException;

import org.shaolin.bmdp.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQSender {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);
	
	private final String host;
	
	private final int port;
	
	private final String queueName;
	
	private Channel channel;
	
	public RabbitMQSender(String host, int port, String queueName) throws Exception {
		this.host = host;
		this.port = port;
		this.queueName = queueName;
		init();
	}
	
	private void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, true, false, false, null);
		this.channel = channel;
	}
	
	public void send(String message) throws Exception {
		channel.basicPublish("", queueName, MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes("UTF-8"));
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message);
		}
	}
	
	public void send(byte[] message) throws Exception {
		channel.basicPublish("", queueName, MessageProperties.MINIMAL_PERSISTENT_BASIC, message);
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message);
		}
	}
	
	public void send(JSONObject json) throws Exception {
		send(json.toString());
	}
	
	public void close() {
		try {
			channel.getConnection().close();
		} catch (IOException e) {
			logger.warn("Failed to close RabbitMQ channel! msg: " + e.getMessage(), e);
		}
	}
}
