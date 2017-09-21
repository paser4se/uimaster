package org.shaolin.bmdp.designtime.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.shaolin.bmdp.datamodel.common.EntityType;
import org.shaolin.bmdp.designtime.page.UIFlowValidator;
import org.shaolin.bmdp.designtime.page.UIFormValidator;
import org.shaolin.bmdp.designtime.page.UIPageValidator;
import org.shaolin.bmdp.designtime.page.WebServiceValidator;
import org.shaolin.bmdp.designtime.page.WorkflowValidator;
import org.shaolin.bmdp.designtime.tools.GeneratorOptions;
import org.shaolin.bmdp.persistence.provider.DBMSProviderFactory;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.exception.ParsingException;

/**
 * @goal validate-entity
 * @phase package
 * @author Shaolin
 *
 */
public class EntityValidationMojo extends AbstractMojo {
	
	// read-only parameters ---------------------------------------------------
    /**
     * The maven project.
     * 
     * @parameter property="project"
     * @readonly
     */
    private MavenProject project;

	/**
     * project/target/classes
     * 
     * @parameter property="project.build.outputDirectory"
     */
    private File targetClasses;
	
    /**
     * project/src/main/java
     * 
     * @parameter property="project.build.sourceDirectory"
     */
    private File srcDirectory;
    
    /**
     * project/src/test/java
     * 
     * @parameter default-value="${basedir}/src/test/java"
     */
    private File testDirectory;
    
    /**
     * 
     * @parameter property="generate-entity.systemEntityPath"
     */
    private String systemEntityPath;
    
    /**
     * 
     * @parameter default-value="${basedir}/src/main/resources/entities"
     */
    private File entitiesDirectory;
    
    /**
     * 
     * @parameter default-value="${basedir}/src/main/resources"
     */
    private File resourcesDir;
    
    /**
     * 
     * @parameter default-value="${basedir}/src/other/web"
     */
    private File webDirectory;
    
    /**
     * 
     * @parameter default-value="${basedir}/src/other/sql"
     */
    private File sqlDirectory;
    
    /**
     * 
     * @parameter default-value="${basedir}/src/main/resources/hbm"
     */
    private File hbmDirectory;

    /**
     * 
     * @parameter property="generate-entity.genUIComponents" default-value="true"
     */
    private boolean genUIComponents = true;
    
    // Mojo methods -----------------------------------------------------------
    /**
     * The project's classpath.
     * 
     * @parameter property="project.compileClasspathElements"
     * @readonly
     */
    private List<String> classpathElements;

    // AbstractAptMojo methods ------------------------------------------------

    protected List<String> getClasspathElements() {
        return classpathElements;
    }
	
    /**
     * Gets the Maven project.
     * 
     * @return the project
     */
    protected MavenProject getProject() {
        return project;
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	if (!entitiesDirectory.exists()) {
    		return;
    	}
    	
    	this.getLog().info("EntitiesDirectory: " + entitiesDirectory.getAbsolutePath());
    	this.getLog().info("WebDirectory: " + webDirectory.getAbsolutePath());
    	
    	// initialize registry
		Registry.getInstance().initRegistry();
    	
		GeneratorOptions options = new GeneratorOptions(project.getGroupId(),
				project.getArtifactId(), entitiesDirectory, resourcesDir,
				srcDirectory, testDirectory, webDirectory, 
				hbmDirectory, sqlDirectory, targetClasses, 
				DBMSProviderFactory.MYSQL);
		
		// initialize entity manager.
		String[] filters = new String[] {project.getName() + "/", ""};
		
		ClassLoader currentCL = Thread.currentThread().getContextClassLoader();
		try {
			AppContext.register(new AppServiceManagerImpl("build_app", currentCL));
			
			// merge the compiled classes to current class loader.
			Set<URL> urls = new HashSet<>();
		    List<String> elements = project.getCompileClasspathElements();
		    for (String element : elements) {
		        urls.add(new File(element).toURI().toURL());
		    }
		    this.getLog().debug("CompileClasspathElements: " + elements);
		    ClassLoader contextClassLoader = URLClassLoader.newInstance(
		            urls.toArray(new URL[0]),
		            Thread.currentThread().getContextClassLoader());
		    Thread.currentThread().setContextClassLoader(contextClassLoader);
			
			// Classloader will be switched in designtime
		    List<IEntityEventListener<? extends EntityType, ?>> listeners 
				= new ArrayList<IEntityEventListener<? extends EntityType, ?>>();
			listeners.add(new UIPageValidator(options));
			listeners.add(new UIFormValidator(options));
			listeners.add(new UIFlowValidator(options));
			try {
				listeners.add(new WorkflowValidator(options));
			} catch (ParsingException e1) {
				e1.printStackTrace();
			}
			listeners.add(new WebServiceValidator(options));
			IEntityManager entityManager = IServerServiceManager.INSTANCE.getEntityManager();
			entityManager.addListeners(listeners);
			ArrayList<File> files = new ArrayList<File>();
			if (systemEntityPath != null) {
				if (systemEntityPath.indexOf(";") != -1) {
					String[] paths = systemEntityPath.split(";");
					for (String p : paths) {
						files.add(new File(p.trim()));
					}
				} else {
					files.add(new File(systemEntityPath));
				}
			}
			for (File f : files) {
				entityManager.reloadDir(f);
			}
			entityManager.reloadDir(entitiesDirectory, new String[] {"websvis", "pageflow", "page", "form", "workflow"});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DependencyResolutionRequiredException e) {
			e.printStackTrace();
		} finally {
			Thread.currentThread().setContextClassLoader(currentCL);
		}
	}
	
	
}
