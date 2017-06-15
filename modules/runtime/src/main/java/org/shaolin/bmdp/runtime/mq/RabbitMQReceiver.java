package org.shaolin.bmdp.runtime.mq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQReceiver implements Consumer {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQReceiver.class);
	
	private final String host;
	
	private final int port;
	
	private final String queueName;
	
	private final String exchangeName;
	
	private final BuiltinExchangeType policy;
	
	private Channel channel;
	
	private ReceivingHandler receiver;
	
	public RabbitMQReceiver(String host, int port, String queueName, ReceivingHandler receiver) throws Exception {
		super();
		this.host = host;
		this.port = port;
		this.queueName = queueName;
		this.exchangeName = null;
		this.policy = null;
		this.receiver = receiver;
		
		init();
	}
	
	public RabbitMQReceiver(String host, int port, String exchangeName, BuiltinExchangeType policy, ReceivingHandler receiver) throws Exception {
		super();
		this.host = host;
		this.port = port;
		this.queueName = null;
		this.exchangeName = exchangeName;
		this.policy = policy;
		this.receiver = receiver;
		
		init();
	}
	
	private void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		this.channel = channel;
		if (policy != null) {
			channel.exchangeDeclare(exchangeName, policy, true, false, null);
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, exchangeName, "");
		    channel.basicConsume(queueName, true, this);
		} else {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicConsume(queueName, true, this);
			/**
			 * In order to defeat that we can use the basicQos method with the prefetchCount = 1 setting. 
			 * This tells RabbitMQ not to give more than one message to a worker at a time. 
			 * Or, in other words, don't dispatch a new message to a worker until it has processed and 
			 * acknowledged the previous one. Instead, it will dispatch it to the next worker that is not still busy.
			 */
			this.channel.basicQos(1);// accept only one unacked message at a time (see below)
		}
	}
	
	public void close() {
		try {
			channel.getConnection().close();
		} catch (IOException e) {
			logger.warn("Failed to close RabbitMQ channel! msg: " + e.getMessage(), e);
		}
	}
	
	public interface ReceivingHandler {
		public void receive(String message);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
	}

	@Override
	public void handleCancelOk(String consumerTag) {
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
		String message = new String(body, "UTF-8");
		if (logger.isDebugEnabled()) {
			logger.debug("Received a message: " + message);
		}
		receiver.receive(message);
		if (policy == null) {
			channel.basicAck(envelope.getDeliveryTag(), false);
		}
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
	}
}
