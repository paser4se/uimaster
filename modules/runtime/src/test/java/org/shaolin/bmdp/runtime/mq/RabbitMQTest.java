package org.shaolin.bmdp.runtime.mq;

import org.junit.Test;
import org.shaolin.bmdp.runtime.mq.RabbitMQReceiver.ReceivingHandler;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQTest {

	@Test
    public void testEmpty() {}
	
	public void testExFanoutSending() throws Exception {
		
		RabbitMQExchanger exchanger = new RabbitMQExchanger("127.0.0.1", 5672, "order1", BuiltinExchangeType.FANOUT);
		for (int i=0; i<10; i++) {
			exchanger.send("hello: "+i, MessageProperties.MINIMAL_PERSISTENT_BASIC);
			Thread.sleep(100);
		}
		exchanger.close();
	}
	
	public void testExMutipleReceiversWithFanout() throws Exception {
		RabbitMQReceiver receiver = new RabbitMQReceiver("127.0.0.1", 5672, "order1", BuiltinExchangeType.FANOUT, new ReceivingHandler(){
			@Override
			public void receive(String message) {
				System.out.println("Received1 a message: " + message);
			}
		});
		RabbitMQReceiver receiver1 = new RabbitMQReceiver("127.0.0.1", 5672, "order1", BuiltinExchangeType.FANOUT, new ReceivingHandler(){
			@Override
			public void receive(String message) {
				System.out.println("Received2 a message: " + message);
			}
		});
		Thread.sleep(2000);
		receiver.close();
		receiver1.close();
	}
	
	public void testExTopicSending() throws Exception {
		
		RabbitMQExchanger exchanger = new RabbitMQExchanger("127.0.0.1", 5672, "order", BuiltinExchangeType.TOPIC);
		for (int i=0; i<10; i++) {
			exchanger.send(("hello: "+i).getBytes(), ((i%2==0)?"topicA.hahah":"topicB.hahah"), MessageProperties.MINIMAL_PERSISTENT_BASIC);
			Thread.sleep(100);
		}
		exchanger.close();
	}
	
	public void testExMutipleReceiversWithTopic() throws Exception {
		RabbitMQReceiver receiver = new RabbitMQReceiver("127.0.0.1", 5672, "order", "topicA.*", BuiltinExchangeType.TOPIC, new ReceivingHandler(){
			@Override
			public void receive(String message) {
				System.out.println("Received a message: " + message);
			}
		});
		RabbitMQReceiver receiver1 = new RabbitMQReceiver("127.0.0.1", 5672, "order", "topicB.*", BuiltinExchangeType.TOPIC, new ReceivingHandler(){
			@Override
			public void receive(String message) {
				System.out.println("Received2 a message: " + message);
			}
		});
		Thread.sleep(2000);
		receiver.close();
		receiver1.close();
	}
	
}
