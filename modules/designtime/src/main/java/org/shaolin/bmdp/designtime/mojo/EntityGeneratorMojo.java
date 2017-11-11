package org.shaolin.bmdp.designtime.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.maven.model.Dependency;
import org.shaolin.bmdp.datamodel.common.EntityType;
import org.shaolin.bmdp.designtime.bediagram.BESourceGenerator;
import org.shaolin.bmdp.designtime.bediagram.CESourceGenerator;
import org.shaolin.bmdp.designtime.orm.DaoGenerator;
import org.shaolin.bmdp.designtime.orm.HibernateMappingGenerator;
import org.shaolin.bmdp.designtime.orm.MySQLSchemaGenerator;
import org.shaolin.bmdp.designtime.orm.RbdDiagramGenerator;
import org.shaolin.bmdp.designtime.page.UIFormJSGenerator;
import org.shaolin.bmdp.designtime.page.UIPageGenerator;
import org.shaolin.bmdp.designtime.page.UIPageJSGenerator;
import org.shaolin.bmdp.designtime.tools.GeneratorOptions;
import org.shaolin.bmdp.persistence.provider.DBMSProviderFactory;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.bmdp.runtime.internal.AppServiceManagerImpl;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.CloseUtil;

/**
 * @goal generate-entity
 * @phase generate-sources
 * @author Shaolin
 *
 */
public class EntityGeneratorMojo extends SpringBootstrapperMojo {
	
	// read-only parameters ---------------------------------------------------

	/**
	 * @parameter property="project.build.outputDirectory"
     * @readonly
     */
    private File targetClasses;
	
    /**
     * @parameter property="project.build.sourceDirectory"
     * @readonly
     */
    private File srcDirectory;
    
    /**
     * @parameter default-value="${basedir}/src/test/java"
     * @readonly
     */
    private File testDirectory;
    
    /**
     * @parameter default-value="${basedir}/src/main/resources/entities"
     * @readonly
     */
    private File entitiesDirectory;
    
    /**
     * @parameter property="generate-entity.systemEntityPath"
     * @readonly
     */
    private String systemEntityPath;
    
    /**
     * @parameter property="settings.localRepository"
     * @readonly
     */
    private File localRepository;
    
    /**
     * @parameter default-value="${basedir}/src/main/resources"
     * @readonly
     */
    private File resourcesDir;
    
    /**
     * @parameter default-value="${basedir}/src/other/web"
     * @readonly
     */
    private File webDirectory;
    
    /**
     * @parameter default-value="${basedir}/src/other/sql"
     * @readonly
     */
    private File sqlDirectory;
    
    /**
     * @parameter default-value="${basedir}/src/other/hbm"
     * @readonly
     */
    private File hbmDirectory;

    /**
     * @parameter property="generate-entity.genUIComponents"
     * @readonly
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
    
    public File getTargetClasses() {
		return targetClasses;
	}

	public void setTargetClasses(File targetClasses) {
		this.targetClasses = targetClasses;
	}

	public File getSrcDirectory() {
		return srcDirectory;
	}

	public void setSrcDirectory(File srcDirectory) {
		this.srcDirectory = srcDirectory;
	}

	public File getTestDirectory() {
		return testDirectory;
	}

	public void setTestDirectory(File testDirectory) {
		this.testDirectory = testDirectory;
	}

	public File getEntitiesDirectory() {
		return entitiesDirectory;
	}

	public void setEntitiesDirectory(File entitiesDirectory) {
		this.entitiesDirectory = entitiesDirectory;
	}

	public String getSystemEntityPath() {
		return systemEntityPath;
	}

	public void setSystemEntityPath(String systemEntityPath) {
		this.systemEntityPath = systemEntityPath;
	}

	public File getLocalRepository() {
		return localRepository;
	}

	public void setLocalRepository(File localRepository) {
		this.localRepository = localRepository;
	}

	public File getResourcesDir() {
		return resourcesDir;
	}

	public void setResourcesDir(File resourcesDir) {
		this.resourcesDir = resourcesDir;
	}

	public File getWebDirectory() {
		return webDirectory;
	}

	public void setWebDirectory(File webDirectory) {
		this.webDirectory = webDirectory;
	}

	public File getSqlDirectory() {
		return sqlDirectory;
	}

	public void setSqlDirectory(File sqlDirectory) {
		this.sqlDirectory = sqlDirectory;
	}

	public File getHbmDirectory() {
		return hbmDirectory;
	}

	public void setHbmDirectory(File hbmDirectory) {
		this.hbmDirectory = hbmDirectory;
	}

	public boolean isGenUIComponents() {
		return genUIComponents;
	}

	public void setGenUIComponents(boolean genUIComponents) {
		this.genUIComponents = genUIComponents;
	}

	public void setClasspathElements(List<String> classpathElements) {
		this.classpathElements = classpathElements;
	}

	@Override
	public void invoke() throws Exception {
    	if (!entitiesDirectory.exists()) {
    		return;
    	}
    	if (!srcDirectory.exists()) {
    		srcDirectory.mkdirs();
    	}
    	if (!webDirectory.exists()) {
    		webDirectory.mkdirs();
    	}
    	if (!sqlDirectory.exists()) {
    		sqlDirectory.mkdirs();
    	}
    	
    	this.getLog().info("SrcDirectory: " + srcDirectory.getAbsolutePath());
    	this.getLog().info("EntitiesDirectory: " + entitiesDirectory.getAbsolutePath());
    	this.getLog().info("WebDirectory: " + webDirectory.getAbsolutePath());
    	this.getLog().info("SqlDirectory: " + sqlDirectory.getAbsolutePath());
    	
		GeneratorOptions options = new GeneratorOptions(project.getGroupId(),
				project.getArtifactId(), entitiesDirectory, resourcesDir,
				srcDirectory, testDirectory, webDirectory, 
				hbmDirectory, sqlDirectory, targetClasses, 
				DBMSProviderFactory.MYSQL);
		options.setI18nProperty(readProperties(options)); 
		
		if (classpathElements.size() == 1 && 
				(Thread.currentThread().getContextClassLoader() instanceof URLClassLoader)) {
			try {
				List<String> findjars = new ArrayList<String>();
				List<Dependency> dependencies = this.getProject().getDependencies();
				for (Dependency d : dependencies) {
					if ("org.shaolin.vogerp".equals(d.getGroupId()) 
							|| "org.shaolin.bmdp".equals(d.getGroupId())) {
						String jarpath = "file:///" + localRepository.getAbsolutePath() + File.separatorChar 
								+ d.getGroupId().replace(".", File.separatorChar + "") + File.separatorChar
								+ d.getArtifactId() + File.separatorChar + d.getVersion() + File.separatorChar
								+ d.getArtifactId() + "-" + d.getVersion() + ".jar";
						findjars.add(jarpath);
					}
				}
				if (!findjars.isEmpty()) {
					URL[] urls = new URL[findjars.size()];
					for (int i = 0; i < findjars.size(); i++) {
						urls[i] = new URL(findjars.get(i));
					}
					URLClassLoader urlCL = ((URLClassLoader) (Thread.currentThread().getContextClassLoader()));
					URLClassLoader newCL = URLClassLoader.newInstance(urls, urlCL);
					Thread.currentThread().setContextClassLoader(newCL);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		List<IEntityEventListener<? extends EntityType, ?>> listeners 
			= new ArrayList<IEntityEventListener<? extends EntityType, ?>>();
		// core.
		CESourceGenerator ceGenerator = new CESourceGenerator(options);
		BESourceGenerator beGenerator = new BESourceGenerator(options, ceGenerator, classpathElements);
		listeners.add(ceGenerator);
		listeners.add(beGenerator);
		
		// add more DB schemas support.
		listeners.add(new RbdDiagramGenerator(options));
		listeners.add(new MySQLSchemaGenerator(options));
		listeners.add(new HibernateMappingGenerator(options));
		listeners.add(new DaoGenerator(options));
		
		// ui parts.
		if (genUIComponents) { // easy for manual dev.
			listeners.add(new UIPageGenerator(options));
			listeners.add(new UIFormJSGenerator(options));
			listeners.add(new UIPageJSGenerator(options));
		}
		
		// initialize entity manager.
		String[] filters = new String[] {project.getName() + "/"};
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			// merge the compiled classes to current class loader.
			Set<URL> urls = new HashSet<>();
		    List<String> elements = project.getRuntimeClasspathElements();
		    for (String element : elements) {
		        urls.add(new File(element).toURI().toURL());
		    }
		    this.getLog().info("CompileClasspathElements: " + elements);
		    ClassLoader contextClassLoader = URLClassLoader.newInstance(
		            urls.toArray(new URL[urls.size()]),
		            Thread.currentThread().getContextClassLoader());
		    Thread.currentThread().setContextClassLoader(contextClassLoader);
		    
		    	AppContext.register(new AppServiceManagerImpl("build_app", loader));
		    	//IServerServiceManager.INSTANCE = IServerServiceManager.createMockServiceManager();
				// Classloader will be switched in designtime
		    	EntityManager entityManager = (EntityManager)IServerServiceManager.INSTANCE.getEntityManager();
			if (systemEntityPath != null) {
				ArrayList<File> files = new ArrayList<File>();
				if (systemEntityPath.indexOf(";") != -1) {
					String[] paths = systemEntityPath.split(";");
					for (String p : paths) {
						files.add(new File(p));
					}
				} else {
					files.add(new File(systemEntityPath));
				}
				files.add(entitiesDirectory);
				entityManager.init(listeners, filters, files.toArray(new File[files.size()]));
			} else {
				entityManager.init(listeners, filters, new File[]{entitiesDirectory});
			}
		} finally {
			storeProperties(options.getI18nProperty(), options);
			Thread.currentThread().setContextClassLoader(loader);
		}
	}
	
	private Properties readProperties(GeneratorOptions options) {
		if (!resourcesDir.exists()) {
			resourcesDir.mkdirs();
		}
		File resource_en = new File(resourcesDir, options.geti18nFileName());
		Properties property = new Properties();
		if (resource_en.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(resource_en);
				property.load(in);
			} catch (IOException e) {
			} finally {
				CloseUtil.close(in);
			}
		}
		return property;
	}
	
	private void storeProperties(Properties property, GeneratorOptions options) {
		File resource_en = new File(resourcesDir, options.geti18nFileName());
		OutputStream out = null;
		try {
			property = resort(property);
			out = new FileOutputStream(resource_en);
			property.store(out, "English localization file!");
		} catch (IOException e) {
		} finally {
			CloseUtil.close(out);
		}
	}
	
	public Properties resort(Properties map) {
		SortedProperties newP = new SortedProperties();
		Set<Entry<Object,Object>> set = map.entrySet();
		for (Map.Entry<Object, Object> entry : set) {
			newP.setProperty(entry.getKey().toString(), entry.getValue().toString());
		}
		return newP;
	}

	class SortedProperties extends Properties {
		public Enumeration keys() {
			Enumeration keysEnum = super.keys();
			Vector<String> keyList = new Vector<String>();
			while (keysEnum.hasMoreElements()) {
				keyList.add((String) keysEnum.nextElement());
			}
			Collections.sort(keyList);
			return keyList.elements();
		}
	}
	
	
}
