package org.shaolin.bmdp.analyzer;

import org.shaolin.bmdp.analyzer.be.IJavaCCJob;

public interface IAnalyzerService {

	public void startJob(IJavaCCJob job);
	
	public void stopJob(IJavaCCJob job);
}
