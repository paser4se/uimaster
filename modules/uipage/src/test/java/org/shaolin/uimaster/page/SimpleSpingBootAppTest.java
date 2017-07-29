package org.shaolin.uimaster.page;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class SimpleSpingBootAppTest implements CommandLineRunner {

	@Autowired
	private WebConfigSpringInstance instance;
	
	private ApplicationContext appCtx;
	
	public static void main(String[] args) {
//    	(new SpringApplicationBuilder()).listeners(listeners)
        SpringApplication.run(SimpleSpingBootAppTest.class, args);
        
    }
	
	@Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		this.appCtx = ctx;
		return this;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = this.appCtx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println("Registered bean name: " + beanName);
        }

        System.out.println("instance.getResourceServer(): " + instance.getResourceServer());
        System.out.println("instance.getActionPath(): " + instance.getActionPath());
        System.out.println("instance.getAjaxServiceURL(): " + instance.getAjaxServiceURL());
        System.out.println("instance.getContextRoot(): " + instance.getContextRoot());
        System.out.println("instance.getCommoncss(): " + Arrays.toString(instance.getCommoncss()));
        System.out.println("instance.getResourceServer(): " + Arrays.toString(instance.getCommonjs()));
	}
	
}
