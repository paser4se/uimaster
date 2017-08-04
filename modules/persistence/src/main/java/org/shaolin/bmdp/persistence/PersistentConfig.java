package org.shaolin.bmdp.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@ConfigurationProperties("persistentConstant")
@EnableConfigurationProperties
public class PersistentConfig {

	private String hbmRoot;

	public String getHbmRoot() {
		return hbmRoot;
	}

	public void setHbmRoot(String hbmRoot) {
		this.hbmRoot = hbmRoot;
	}
	
}
