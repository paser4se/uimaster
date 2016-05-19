package org.shaolin.bmdp.analyzer.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.shaolin.bmdp.analyzer.IAnalyzerService;
import org.shaolin.bmdp.analyzer.be.ITableColumnStatistic;
import org.shaolin.bmdp.analyzer.be.ITableStatistic;
import org.shaolin.bmdp.analyzer.be.JavaCCJobImpl;
import org.shaolin.bmdp.analyzer.be.TableStatisticImpl;
import org.shaolin.bmdp.analyzer.ce.DataStatisticType;
import org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.internal.AnalyzerServiceImpl;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.slf4j.LoggerFactory;

public class AnalyzerUtil {

	public static final String StatsTablePrefix = "STATS_";
	
	public static String genSQL(ITableStatistic tableStat) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(AnalyzerUtil.StatsTablePrefix).append(tableStat.getTableName()).append("\n");
		sb.append("(ID BIGINT(38) NOT NULL AUTO_INCREMENT,").append("\n");
		sb.append("ORGID BIGINT(38) NOT NULL default 0,").append("\n");
		List<ITableColumnStatistic> columns = tableStat.getColumns();
		for (ITableColumnStatistic c : columns) {
			sb.append("").append(c.getName()).append(" INT(12) NOT NULL default 0,").append("\n");
		}
		sb.append("PRIMARY KEY(ID)").append("\n");
		sb.append(");").append("\n");
		return sb.toString();
	}
	
	public static void genAndStartTableJob(ITableStatistic tableStat) throws Exception {
		IAnalyzerService service = AppContext.get().getService(IAnalyzerService.class);
		if (service.hasJob(tableStat.getTableName())) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("import org.shaolin.bmdp.analyzer.util.AnalyzerUtil;\n{\nAnalyzerUtil.executeStatsPerDay(\"");
		sb.append(tableStat.getTableName());
		sb.append("\");\n}");
		
		JavaCCJobImpl job = new JavaCCJobImpl();
		job.setScript(sb.toString());
		job.setCronExp("0 0 1 1/1 * ?");//executed in the middle night.
		job.setDescription(tableStat.getTableName() + " table statistic.");
		job.setName(tableStat.getTableName());
		job.setStatus(JavaCCJobStatusType.START);
		
		OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
        DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
        ooeeContext.setDefaultEvaluationContext(evaContext);
        ExpressionType expr = new ExpressionType();
        expr.setExpressionString(job.getScript());
        expr.parse(ooeeContext);
        
        AanlysisModel.INSTANCE.create(job, true);
        service.startJob(job);
	}
	
	public static void executeStatsPerDay(String tableName) {
		//DATE(CREATEDATE) AS 'DATE',
		TableStatisticImpl scObject = new TableStatisticImpl();
		scObject.setTableName(tableName);
		List<ITableStatistic> tableDefinitions = AanlysisModel.INSTANCE.searchTableStatsDefinition(scObject, null, 0, -1);
		if (tableDefinitions == null || tableDefinitions.size() == 0) {
			return;
		}
		
		final List<List<Object>> totalResult = new ArrayList<List<Object>>();
		ITableStatistic definition = tableDefinitions.get(0);
		List<ITableColumnStatistic> columns = definition.getColumns();
		for (ITableColumnStatistic column : columns) {
			List<Object> row = new ArrayList<Object>();
			totalResult.add(row);//fill up empty structure.
		}
		for (ITableColumnStatistic column : columns) {
			if (column.getStatsType() == DataStatisticType.COUNT) {
				List<Object[]> sresult = AnalyzerUtil.countPerDayByOrgId(tableName, column.getName());
				if (sresult == null || sresult.size() == 0) {
					return;
				}
				mergeRows(totalResult, sresult);
			} else if (column.getStatsType() == DataStatisticType.SUM) {
				List<Object[]> sresult = AnalyzerUtil.sumPerDayByOrgId(tableName, column.getName());
				if (sresult == null || sresult.size() == 0) {
					return;
				}
				mergeRows(totalResult, sresult);
			} else if (column.getStatsType() == DataStatisticType.AVERAGE) {
				List<Object[]> sresult = AnalyzerUtil.avePerDayByOrgId(tableName, column.getName());
				if (sresult == null || sresult.size() == 0) {
					return;
				}
				mergeRows(totalResult, sresult);
			}
		}
		List<String> columnIds = new ArrayList<String>();
		for (ITableColumnStatistic col : columns) {
			columnIds.add(col.getName());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(AnalyzerUtil.StatsTablePrefix).append(tableName);
		sb.append(" (`CREATEDATE`,`ORGID`,");
		for (String col : columnIds) {
			sb.append("`").append(col).append("`,");
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(") VALUES (?,?,");
		for (String col : columnIds) {
			sb.append("?,");
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(");");
		final String insertSQL = sb.toString();
		Session session = HibernateUtil.getSession();
		try {
			final int s = columnIds.size();
			session.doWork(new Work() {
				public void execute(Connection connection)
						throws SQLException {
					PreparedStatement ps = connection.prepareStatement(insertSQL);
					for (List<Object> row : totalResult) {
						ps.setDate(1, new Date(System.currentTimeMillis()));
						ps.setInt(2, ((Number)row.get(0)).intValue());
						for (int i=0; i<s; i++) {
							ps.setInt(i+3, ((Number)row.get(i+1)).intValue());
				    	}
						ps.executeUpdate();
					}
				}
			});
			HibernateUtil.releaseSession(session, true);
		} catch (Exception e) {
			LoggerFactory.getLogger(AnalyzerServiceImpl.class).warn("Error to execute Table Statistic: " + e.getMessage(), e);
			HibernateUtil.releaseSession(session, false);
		} 
	}
	
	private static void mergeRows(List<List<Object>> totalResult, List<Object[]> singleResult) {
		int rowIndex = 0;
		for (Object[] values : singleResult) {
			List<Object> row = totalResult.get(rowIndex);
			int colIndex = 0;
			if (row.size() > 0) {
				colIndex = 1;
			}
			while (colIndex < values.length) {
				row.add(values[colIndex++]);
			}
			rowIndex++;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> sumPerDayByOrgId(String table, String column) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String day = simpleDateFormat.format(new java.util.Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT ORGID, SUM(").append(column).append(") FROM ").append(table);
			sb.append(" WHERE CREATEDATE between '").append(day).append(" 00-00-00' and '");
			sb.append(day).append(" 23:59:59' GROUP BY ORGID");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> avePerDayByOrgId(String table, String column) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String day = simpleDateFormat.format(new java.util.Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT ORGID, AVG(").append(column).append(") FROM ");
			sb.append(table).append(" WHERE CREATEDATE between '").append(" 00-00-00' and '");
			sb.append(day).append(" 23:59:59' GROUP BY ORGID");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> countPerDayByOrgId(String table, String column) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String day = simpleDateFormat.format(new java.util.Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT ORGID, COUNT(").append(column).append(") FROM ").append(table);
			sb.append(" WHERE CREATEDATE between '").append(day).append(" 00-00-00' and '");
			sb.append(day).append(" 23:59:59' GROUP BY ORGID");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> statsPerDay(String table) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
			String day = simpleDateFormat.format(new java.util.Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT COUNT(1) FROM " + table
					+ " WHERE CREATEDATE between '" + day + " 00-00-00' and '"
					+ day + " 23:59:59' GROUP BY DAY(CREATEDATE)");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
}
