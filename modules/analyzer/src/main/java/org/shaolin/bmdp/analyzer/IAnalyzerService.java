package org.shaolin.bmdp.analyzer;

import java.util.List;

import org.shaolin.bmdp.analyzer.be.IJavaCCJob;

public interface IAnalyzerService {

	public void startJob(IJavaCCJob job);
	
	public void stopJob(IJavaCCJob job);
	
	public List<String> getAllTableList();
	
	public List<String> getTableColumns(String tableName);
	
	public List<String> getTableColumnIds(String tableName);
}
