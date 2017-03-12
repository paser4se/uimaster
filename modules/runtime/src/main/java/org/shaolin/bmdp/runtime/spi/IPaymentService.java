package org.shaolin.bmdp.runtime.spi;

public interface IPaymentService {

	void pay();
	
	void transfer();
	
	void refund();
	
	Object query(String orderId);
}
