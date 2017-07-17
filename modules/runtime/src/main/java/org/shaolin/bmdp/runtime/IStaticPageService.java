package org.shaolin.bmdp.runtime;

import java.io.File;

import org.shaolin.bmdp.runtime.spi.IServiceProvider;

public interface IStaticPageService extends IServiceProvider {

	public void generatePageHTML(String pageEntity, File dest);
	
	public void generateFormHTML(String formEntity, File dest);
	
}
