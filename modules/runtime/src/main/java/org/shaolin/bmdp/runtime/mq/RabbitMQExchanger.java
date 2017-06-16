package org.shaolin.bmdp.runtime.mq;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.shaolin.bmdp.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQExchanger implements Consumer {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQExchanger.class);
	
	private final String host;
	
	private final int port;
	
	private final String exchangeName;
	
	private final BuiltinExchangeType policy;
	
	private Channel channel;
	
	private final CopyOnWriteArrayList<String> requestAckQueue = new CopyOnWriteArrayList<String>();
	
	private RPCEventHandler eventHandler;
	
	public RabbitMQExchanger(String host, int port, String exchangeName, BuiltinExchangeType policy) throws Exception {
		this.host = host;
		this.port = port;
		this.exchangeName = exchangeName;
		this.policy = policy;
		init();
	}
	
	public RabbitMQExchanger(String host, int port, String exchangeName, BuiltinExchangeType policy, RPCEventHandler eventHandler) throws Exception {
		this.host = host;
		this.port = port;
		this.exchangeName = exchangeName;
		this.policy = policy;
		this.eventHandler = eventHandler;
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
	
	public void send(byte[] message, String topicKey, BasicProperties property) throws Exception {
		channel.basicPublish(exchangeName, topicKey, property, message);
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message + ",topicKey: " + topicKey);
		}
	}
	
	public void sendWithAck(String message, String topicKey, BasicProperties property) throws Exception {
		if (property.getCorrelationId() == null || property.getCorrelationId().length() ==0) {
			throw new IllegalArgumentException("CorrelationId must set for acknowlegal case!");
		}
		if (!requestAckQueue.addIfAbsent(property.getCorrelationId())) {
			throw new IllegalArgumentException("CorrelationId has duplicated! " + property.getCorrelationId());
		}
		
		channel.basicPublish(exchangeName, topicKey, property, message.getBytes("UTF-8"));
		if (logger.isDebugEnabled()) {
			logger.debug("Sent a message: " + message + ",topicKey: " + topicKey);
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

	@Override
	public void handleConsumeOk(String consumerTag) {
	}
	@Override
	public void handleCancelOk(String consumerTag) {
	}
	@Override
	public void handleCancel(String consumerTag) throws IOException {
		logger.warn("handleCancel event! tag: " + consumerTag);
	}
	@Override
	public void handleDelivery(String consumerTag,
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body) throws IOException {
		if (requestAckQueue.contains(properties.getCorrelationId())) {
			eventHandler.notifyAcked(properties.getCorrelationId(), body);
        } else {
        	eventHandler.notifyDiscard(properties.getCorrelationId(), body);
        	logger.warn("response mismatching due to request id does not exist! correction id: " + properties.getCorrelationId());
        }
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		
	}
}
