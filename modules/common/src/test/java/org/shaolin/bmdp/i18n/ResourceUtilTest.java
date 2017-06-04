package org.shaolin.bmdp.i18n;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.shaolin.bmdp.exceptions.I18NRuntimeException;
import org.shaolin.bmdp.utils.StringUtil;
import org.shaolin.bmdp.utils.UserDefinedBase64;

public class ResourceUtilTest {

	@Test
	public void testGetResourceStringString() {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String day = simpleDateFormat.format(new Date());
		System.out.println(day);
		System.out.println(new Date());
		
		Assert.assertNotNull(ResourceUtil.getResource("Common", "OKbtn"));
		Assert.assertNotNull(ResourceUtil.getResource("Common", "Cancelbtn"));
		
		Assert.assertNotNull(ResourceUtil.getResource("org_shaolin_bmdp_common_i18n", "a"));
		
		try {
			throw new I18NRuntimeException(ExceptionConstants.UIMASTER_ODMAPPER_056,
					new Object[]{"hello."});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testHtmlBase64() {
		String str = "<div class='swiper-slide'><div class='p'><div><img src=\"/uimaster\\product\\uimaster\\5\\images\\thumbnail\\IMG_47ÄãºÃ24.JPG\" style=\"width:100px;height:100px;\"></div></div><div class='di'>ÖÐ¹ú:(PSN-20150613-005)</div><div class='di'>IPod4 all series</div><div class='di'>:org.shaolin.vogerp.productmodel.ce.ProductType</div><div class='di'>:2016-01-12 23:55:10.0</div></div>";
		byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
		System.out.println("ecncoded value is " + new String(bytesEncoded));
		byte[] valueDecoded= Base64.decodeBase64(bytesEncoded);
		System.out.println("Decoded value is " + new String(valueDecoded));
		
		
		String encode = UserDefinedBase64.encode(str);
		System.out.println("\n\necncoded value is " + encode);
		String decoded = UserDefinedBase64.decode(encode);
		System.out.println("Decoded value is " + decoded);
		
		encode = UserDefinedBase64.encode("2016-01-17 11:50:45");
		System.out.println("\n\necncoded value is " + encode);
		decoded = UserDefinedBase64.decode(encode);
		System.out.println("Decoded value is " + decoded);
	}

}
