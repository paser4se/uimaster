package org.shaolin.bmdp.runtime.spi;

public interface IShortMsgService {

	public boolean sendMsgToPhone(String message, String tempId, String phoneNumber);
	
	public boolean sendMsgToEmail(String message, String tempId, String email);
	
}
