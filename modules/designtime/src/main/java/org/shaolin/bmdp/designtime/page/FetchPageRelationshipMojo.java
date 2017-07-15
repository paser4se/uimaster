package org.shaolin.bmdp.designtime.page;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.shaolin.bmdp.datamodel.common.DiagramType;
import org.shaolin.bmdp.datamodel.page.UIEntity;
import org.shaolin.bmdp.datamodel.page.UIPage;
import org.shaolin.bmdp.datamodel.page.WebService;
import org.shaolin.bmdp.datamodel.pagediagram.WebChunk;
import org.shaolin.bmdp.runtime.entity.EntityAddedEvent;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityUpdatedEvent;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.bmdp.runtime.spi.IEntityManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFlowCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @goal fetch-pagerelation
 * @author Shaolin
 *
 */
public class FetchPageRelationshipMojo extends AbstractMojo {
	
	private static final Logger logger = LoggerFactory.getLogger(UIFlowCacheManager.class);
	
	// read-only parameters ---------------------------------------------------
    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * 
     * @parameter expression="frontendpage.info"
     */
    private File frontendPageInfo;
    
    private List<String> pageInfo = new ArrayList<String>();
    
    /**
     * 
     * @parameter expression="entities/"
     */
    private File entitiesDirectory;
    
    /**
     * 
     * @parameter expression=""
     */
    private File dest;
    

    /**
     * Gets the Maven project.
     * 
     * @return the project
     */
    protected MavenProject getProject() {
        return project;
    }
    
    public void execute() throws MojoExecutionException, MojoFailureException  {
    	if (frontendPageInfo == null || !frontendPageInfo.exists()) {
    		this.getLog().info("the frontendPageInfo file does not exist!");
    		return;
    	}
    	if (dest == null || !dest.exists()) {
    		this.getLog().info("the entity directory does not exist!");
    		return;
    	}
    	
    	this.getLog().info("entitiesDirectory: " + frontendPageInfo.getAbsolutePath());
    	this.getLog().info("Destination: " + dest.getAbsolutePath());
    	try {
	    	Files.lines(Paths.get(new URI(frontendPageInfo.getAbsolutePath()))).forEach(new Consumer<String>() {
				@Override
				public void accept(String t) {
					pageInfo.add(t);
				}
	    	});
	    	
	    	
	    	IEntityManager entityManager = IServerServiceManager.INSTANCE.getEntityManager();
			addEntityListeners(entityManager);
			entityManager.reloadDir(entitiesDirectory, new String[] {"websvis", "pageflow", "page", "form", "workflow"});
		} catch (Exception e) {
			this.getLog().error(e);
		}
	}
    
    static void addEntityListeners(IEntityManager entityManager) {
		
		entityManager.addEventListener(new IEntityEventListener<WebChunk, DiagramType>() {
			@Override
			public void setEntityManager(EntityManager entityManager) {
			}

			@Override
			public void notify(
					EntityAddedEvent<WebChunk, DiagramType> event) {
			}

			@Override
			public void notify(
					EntityUpdatedEvent<WebChunk, DiagramType> event) {
			}

			@Override
			public void notifyLoadFinish(DiagramType diagram) {
			}

			@Override
			public void notifyAllLoadFinish() {
			}

			@Override
			public Class<WebChunk> getEventType() {
				return WebChunk.class;
			}

		});
		entityManager.addEventListener(new IEntityEventListener<UIPage, DiagramType>() {
			@Override
			public void setEntityManager(EntityManager entityManager) {
			}

			@Override
			public void notify(
					EntityAddedEvent<UIPage, DiagramType> event) {
			}

			@Override
			public void notify(
					EntityUpdatedEvent<UIPage, DiagramType> event) {
			}

			@Override
			public void notifyLoadFinish(DiagramType diagram) {
			}

			@Override
			public void notifyAllLoadFinish() {
			}

			@Override
			public Class<UIPage> getEventType() {
				return UIPage.class;
			}
		});
		entityManager.addEventListener(new IEntityEventListener<UIEntity, DiagramType>() {
			@Override
			public void setEntityManager(EntityManager entityManager) {
			}

			@Override
			public void notify(
					EntityAddedEvent<UIEntity, DiagramType> event) {
			}

			@Override
			public void notify(
					EntityUpdatedEvent<UIEntity, DiagramType> event) {
			}

			@Override
			public void notifyLoadFinish(DiagramType diagram) {
			}

			@Override
			public void notifyAllLoadFinish() {
			}

			@Override
			public Class<UIEntity> getEventType() {
				return UIEntity.class;
			}
		});
		entityManager.addEventListener(new IEntityEventListener<WebService, DiagramType>() {
			@Override
			public void setEntityManager(EntityManager entityManager) {
			}

			@Override
			public void notify(
					EntityAddedEvent<WebService, DiagramType> event) {
			}

			@Override
			public void notify(
					EntityUpdatedEvent<WebService, DiagramType> event) {
			}

			@Override
			public void notifyLoadFinish(DiagramType diagram) {
			}

			@Override
			public void notifyAllLoadFinish() {
			}

			@Override
			public Class<WebService> getEventType() {
				return WebService.class;
			}
		});
	}

	
}
