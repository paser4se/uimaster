package org.shaolin.bmdp.i18n;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.shaolin.bmdp.utils.ImageUtil;

public class ImageUtilTest {
	
	@Test
	public void testImageActions() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("iphoneapk.png");
		ImageUtil.resizeImage(new File(url.getFile()), 100, 100, 
				new File(url.getPath().substring(0, url.getPath().lastIndexOf('/')), "resize.png"));
		
		ImageUtil.createThumbnail(new File(url.getFile()), 50, 
				new File(url.getPath().substring(0, url.getPath().lastIndexOf('/')), "thumbnail.png"));
		
		ImageUtil.cropImage(new File(url.getFile()), 200, 200);
	}

	@Test
	public void testSearchImageOnInternet() {
		ImageUtil.searchImageOnInternet("·ÊÔí");
	}
}
