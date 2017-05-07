package org.shaolin.bmdp.i18n;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.shaolin.bmdp.exceptions.ExceptionData;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.bmdp.runtime.Registry;

public class ResourceUtilTest {

	@Test
	public void test() {
		Registry.getInstance().initRegistry();
		
		Assert.assertEquals("zh_CN", ResourceUtil.getDefaultLocale());
		Assert.assertEquals("default", ResourceUtil.getDefaultConfig());
		Assert.assertEquals("zh_CN", ResourceUtil.getLocale("Chinese"));

		Locale chineseLocale = ResourceUtil.getLocaleObject("Chinese");
		Assert.assertEquals("zh_CN", chineseLocale.toString());

		String msg = ResourceUtil.getResource("i18n.constant",
				"bmiasia.ebos.constant.test.ce.Sex1._NOT_SPECIFIED");
		Assert.assertEquals("not_specified_sex_en_US", msg);

	}

}
