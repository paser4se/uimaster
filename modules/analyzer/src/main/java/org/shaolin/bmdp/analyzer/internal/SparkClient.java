package org.shaolin.bmdp.analyzer.internal;

import org.apache.spark.launcher.SparkLauncher;
import org.shaolin.bmdp.analyzer.be.IJob;
import org.shaolin.bmdp.runtime.Registry;

public class SparkClient {

	private final String serverHost;
	
	private final String port;
	
	public SparkClient() {
		this.serverHost = Registry.getInstance().getNodeItems("/System/spark").get("serverAddress");
		this.port = Registry.getInstance().getNodeItems("/System/spark").get("serverPort");
	}
	
	public SparkClient(String serverHost, String port) {
		this.serverHost = serverHost;
		this.port = port;
	}
	
	public void uploadJob(IJob job) throws Exception {
		Process spark = new SparkLauncher()
		  .setAppResource(job.getJarPath())
		  .setMainClass(job.getMainClass())
		  .setMaster("master spark://"+this.serverHost + ":" + this.port)
		  .launch();
		spark.waitFor();
	}
	
	public void queryJob() {
		//TODO:
	}
}
