package org.shaolin.bmdp.runtime.mq;

import org.shaolin.bmdp.runtime.mq.RabbitMQReceiver.ReceivingHandler;

public class RabbitMQReceivers {

	private RabbitMQReceiver[] receivers;
	
	public RabbitMQReceivers(String host, int port, String queueName, 
			ReceivingHandler receiver, int count) throws Exception {
		receivers = new RabbitMQReceiver[count];
		for (int i=0; i<count; i++) {
			receivers[i] = new RabbitMQReceiver(host, port, queueName, receiver);
		}
	}
	
	public void close() {
		for (RabbitMQReceiver r: receivers) {
			r.close();
		}
	}
}
