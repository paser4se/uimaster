package org.shaolin.bmdp.analyzer.internal;

import java.io.Serializable;
import java.sql.ResultSet;

import org.apache.spark.rdd.JdbcRDD;

import scala.runtime.AbstractFunction1;

public class MapResult extends AbstractFunction1<ResultSet, Object[]> implements Serializable {

	private static final long serialVersionUID = 1L;

	public Object[] apply(ResultSet row) {
        return JdbcRDD.resultSetToObjectArray(row);
    }
}
