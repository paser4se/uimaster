package org.shaolin.bmdp.runtime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The runtime context of Spring boot.
 * 
 * @author wushaol
 *
 */
@SpringBootApplication(scanBasePackages={"org.shaolin.*"})
@Configuration
@ComponentScan(basePackages = {"org.shaolin.*"})
//"org.shaolin.bmdp.runtime", "org.shaolin.uimaster.uipage", "org.shaolin.bmdp.workflow", "org.shaolin.vogerp"
public class SpringBootstrapper extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootstrapper.class);
	
	private ApplicationContext ctx;
	
	public SpringBootstrapper() {
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootstrapper.class);
    }
	
	@Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		this.ctx = ctx;
        return this;
    }
	
	@Override
	public void run(String... args) throws Exception {
		if (this.ctx == null || !"/uimaster".equals(this.ctx.getApplicationName())) {
			return;
		}
		logger.info("UIMaster("+ this.ctx.getApplicationName() +") SpringBoot Integration start... ");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
        	if (beanName.startsWith("org.springframework")) {
        		logger.debug("Registered spring bean: " + beanName);
        	} else {
        		logger.info("Registered bean name: " + beanName);
        	}
        }
        logger.info("\n\n");

        IServerServiceManager.setSpringContext(ctx);
		// initial system configuration.
		Registry registry = Registry.getInstance();
		registry.initRegistry();
		
		List<String> cacheItems = registry.getNodeChildren("/System/caches");
	    	for (String cacheName: cacheItems) {
	    		Map<String, String> config = registry.getNodeItems("/System/caches/" + cacheName);
	    		String maxSizeStr = config.get("maxSize");
	    		String minutesStr = config.get("refreshTimeInMinutes");
	    		String description = config.get("description");
	    		int maxSize;
	    		long minutes;
			try {
				maxSize = Integer.parseInt(maxSizeStr);
			} catch (NumberFormatException e) {
				maxSize = -1;
				logger.warn("maxSize format error, now use the default -1");
			}
			try {
				minutes = Long.parseLong(minutesStr);
			} catch (NumberFormatException e) {
				minutes = -1;
				logger.warn("refresh interval error, now use the default -1");
			}
	    		ICache<String, ConcurrentHashMap> cache = CacheManager.getInstance().getCache(
	    						cacheName, maxSize, false, String.class, ConcurrentHashMap.class);
	    		cache.setRefreshInterval(minutes);
	    		cache.setDescription(description);
	    	}
	}

    public static void main(String[] args) {
//    	(new SpringApplicationBuilder()).listeners(listeners)
        SpringApplication.run(SpringBootstrapper.class, args);
        
    }

}
