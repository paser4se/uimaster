package org.shaolin.bmdp.designtime.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.WebConfigSpringInstance;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * SpringBootstrapper Mojo
 * 
 * @author Shaolin
 */
@SpringBootApplication(scanBasePackages={"org.shaolin.*"}, 
		exclude={HibernateJpaAutoConfiguration.class,
				XADataSourceAutoConfiguration.class,
				JdbcTemplateAutoConfiguration.class,
				JndiDataSourceAutoConfiguration.class,
				DataSourceAutoConfiguration.class, 
				DataSourceTransactionManagerAutoConfiguration.class})
@EnableAutoConfiguration(
		exclude={HibernateJpaAutoConfiguration.class,
				XADataSourceAutoConfiguration.class,
				JdbcTemplateAutoConfiguration.class,
				JndiDataSourceAutoConfiguration.class,
				DataSourceAutoConfiguration.class, 
				DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan(basePackages = {"org.shaolin.*"})
@Profile("default")
public abstract class SpringBootstrapperMojo extends AbstractMojo implements CommandLineRunner {
	
	protected static final ThreadLocal<SpringBootstrapperMojo> contextObject = new ThreadLocal<SpringBootstrapperMojo>();
	
	protected ApplicationContext ctx;
	
	// read-only parameters ---------------------------------------------------
    /**
     * The maven project.
     * @parameter property="project"
     * @required
     */
    protected MavenProject project;
	
    public void setProject(MavenProject project) {
		this.project = project;
	}

	/**
     * Gets the Maven project.
     * 
     * @return the project
     */
    protected MavenProject getProject() {
        return project;
    }
    
    public void execute() throws MojoExecutionException, MojoFailureException {
    	contextObject.set(this);
    	try {
	    	SpringApplication app = new SpringApplication(SpringBootstrapperMojo.class);
	    	app.setWebEnvironment(false);
	    	app.setBannerMode(Banner.Mode.CONSOLE);
	        app.run(new String[0]);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		this.ctx = ctx;
        return this;
    }
    
    @Override
	public void run(String... args) throws Exception {
    	// initialize registry
		Registry.getInstance().initRegistry();
		IServerServiceManager.setSpringContext(this.ctx);
		IServerServiceManager.INSTANCE.setRunningEnv(IAppServiceManager.Env.Testing);
		WebConfigSpringInstance instance = ctx.getBean(WebConfigSpringInstance.class);
		this.getLog().info("UIMaster contextRoot: " + instance.getContextRoot());
		WebConfig.setSpringInstance(instance);
    	contextObject.get().invoke();
    }
	
    public abstract void invoke() throws Exception;
}
