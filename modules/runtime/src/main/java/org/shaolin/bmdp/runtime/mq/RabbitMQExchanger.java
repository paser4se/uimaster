package org.shaolin.bmdp.runtime.mq;

import java.io.IOException;

import org.shaolin.bmdp.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQExchanger {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQExchanger.class);
	
	private final String host;
	
	private final int port;
	
	private final String exchangeName;
	
	private final BuiltinExchangeType policy;
	
	private Channel channel;
	
	public RabbitMQExchanger(String host, int port, String exchangeName, BuiltinExchangeType policy) throws Exception {
		this.host = host;
		this.port = port;
		this.exchangeName = exchangeName;
		this.policy = policy;
		init();
	}
	
	private void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName, policy, true, false, null);
		this.channel = channel;
	}
	
	public void send(String message, BasicProperties property) throws Exception {
		channel.basicPublish(exchangeName, "", property, message.getBytes("UTF-8"));
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message);
		}
	}
	
	public void send(byte[] message, BasicProperties property) throws Exception {
		channel.basicPublish("", exchangeName, property, message);
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message);
		}
	}
	
	public void send(JSONObject json, BasicProperties property) throws Exception {
		send(json.toString(), property);
	}
	
	public void close() {
		try {
			channel.getConnection().close();
		} catch (IOException e) {
			logger.warn("Failed to close RabbitMQ channel! msg: " + e.getMessage(), e);
		}
	}
}
