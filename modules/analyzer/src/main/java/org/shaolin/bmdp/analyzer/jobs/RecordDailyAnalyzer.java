package org.shaolin.bmdp.analyzer.jobs;

import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The statistic on the records created daily per user.
 * 
 * @author wushaol
 *
 */
public class RecordDailyAnalyzer {

	private static final Logger logger = LoggerFactory
			.getLogger(RecordDailyAnalyzer.class);

	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf().setAppName("SparkJdbc").setMaster("local[*]"));
	private static final SQLContext sqlContext = new SQLContext(sc);

	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String MYSQL_CONNECTION_URL = "jdbc:mysql://localhost:13306/test";
	private static final String MYSQL_USERNAME = "test";
	private static final String MYSQL_PWD = "test";

	public static void main(String[] args) {
		Properties options = new Properties();
		options.put("driver", MYSQL_DRIVER);
		// Load MySQL query result as DataFrame
		DataFrame jdbcDF = sqlContext.read().jdbc(MYSQL_CONNECTION_URL + "?user=" + MYSQL_USERNAME + "&password=" + MYSQL_PWD,
				"order_saleorder", options);
		List<Row> employeeFullNameRows = jdbcDF.collectAsList();

		for (Row employeeFullNameRow : employeeFullNameRows) {
			logger.warn("--------->" + employeeFullNameRow);
		}
	}
}
