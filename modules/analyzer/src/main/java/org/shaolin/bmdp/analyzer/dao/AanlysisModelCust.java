package org.shaolin.bmdp.analyzer.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.shaolin.bmdp.analyzer.be.ChartPointDataImpl;
import org.shaolin.bmdp.analyzer.be.IChartPointData;
import org.shaolin.bmdp.persistence.BEEntityDaoObject;

/**
 */
public class AanlysisModelCust extends BEEntityDaoObject {

	public static final AanlysisModelCust INSTANCE = new AanlysisModelCust();

	private AanlysisModelCust() {
	}

	public List<org.shaolin.bmdp.analyzer.be.IChartPointData> stats(
			String tableName, Map<String, Object> condition) {
		List result = this.listStatistic(tableName, 0, -1, condition);
		List<IChartPointData> data = new ArrayList<IChartPointData>(result.size());
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();

			ChartPointDataImpl item = new ChartPointDataImpl();
			//object[0] the primary key.
			item.setLabel(String.valueOf(objects[1]));//the label must be on the first column.
			
			item.setDataset(String.valueOf(objects[2]));
			if (objects.length >= 4) {
				item.setDataset1(String.valueOf(objects[3]));
			}
			if (objects.length >= 5) {
				item.setDataset2(String.valueOf(objects[4]));
			}
			if (objects.length >= 6) {
				item.setDataset3(String.valueOf(objects[5]));
			}
			if (objects.length >= 7) {
				item.setDataset4(String.valueOf(objects[6]));
			}
			data.add(item);
		}
		return data;
	}
	
	public static String getLabels(List<IChartPointData> data) {
		StringBuffer sb = new StringBuffer("[");
		for (IChartPointData item: data) {
			sb.append("'");
			sb.append(item.getLabel());
			sb.append("',");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	public static String getLabels(HashMap<String, Integer>  listData) {
		StringBuffer sb = new StringBuffer("[");
		Set<String> keys = listData.keySet();
		for (String key : keys) {
			sb.append("'");
			sb.append(key);
			sb.append("',");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		
		return sb.toString();
	}
}
