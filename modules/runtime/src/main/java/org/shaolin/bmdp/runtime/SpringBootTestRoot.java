package org.shaolin.bmdp.runtime;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * The test root of spring boot context.
 * 
 * @author wushaol
 *
 */
@SpringBootTest(classes=SpringBootTestRoot.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"org.shaolin.*"})
@Profile("default")
public class SpringBootTestRoot {

	//extends SpringBootServletInitializer 
	//@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootTestRoot.class);
    }
	
	@Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args->{
        	LocaleContext.createLocaleContext("default");
        	Registry.getInstance().initRegistry();
    		IServerServiceManager.setSpringContext(ctx);
    		IServerServiceManager.INSTANCE.setAppClassLoader(this.getClass().getClassLoader());
    		
    		IServerServiceManager.INSTANCE.configureLifeCycleProviders();
    		IEntityManager entityManager = IServerServiceManager.INSTANCE.getEntityManager();
    		((EntityManager)entityManager).initRuntime();
        };
    }
	
	@BeforeClass
	public static void setup() {
		SpringApplication app = new SpringApplication(SpringBootTestRoot.class);
    	app.setWebEnvironment(false);
    	app.setBannerMode(Banner.Mode.OFF);
        app.run(new String[0]);
	}
	
	@AfterClass
	public static void teardown() {
		
	}
}
