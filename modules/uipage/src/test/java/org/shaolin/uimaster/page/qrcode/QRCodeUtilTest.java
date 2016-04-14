package org.shaolin.uimaster.page.qrcode;

import org.junit.Test;

public class QRCodeUtilTest {

	@Test
	public void test() throws Exception {
		String text = "www.vogerp.com";
		QRCodeUtil.encode(text, "C:\\uimaster\\deploy\\apache-tomcat-8.0.9\\webapps\\uimaster\\images\\qd-shortlogo.png", "c:/", true);
	}

}
