package org.shaolin.bmdp.analyzer.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.shaolin.bmdp.analyzer.be.ChartPointDataImpl;
import org.shaolin.bmdp.analyzer.be.IChartPointData;
import org.shaolin.bmdp.analyzer.be.ITableColumnStatistic;
import org.shaolin.bmdp.analyzer.be.ITableStatistic;
import org.shaolin.bmdp.analyzer.be.TableStatisticImpl;
import org.shaolin.bmdp.analyzer.ce.StatisticChartType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.dao.AanlysisModelCust;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.ExpressionPropertyType;
import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.widgets.HTMLChartBarType;
import org.shaolin.uimaster.page.widgets.HTMLChartDoughnutType;
import org.shaolin.uimaster.page.widgets.HTMLChartLinearType;
import org.shaolin.uimaster.page.widgets.HTMLChartPolarPieType;
import org.shaolin.uimaster.page.widgets.HTMLChartRadarType;
import org.shaolin.uimaster.page.widgets.HTMLChartSuper;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrgCharts extends HTMLWidgetType {

	private static final Logger logger = LoggerFactory.getLogger(OrgCharts.class);
	
	public OrgCharts()
    {
    }

    public OrgCharts(HTMLSnapshotContext context)
    {
        super(context);
    }

    public OrgCharts(HTMLSnapshotContext context, String id)
    {
        super(context, id);
    }
	
	@Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
    	generateWidget(context);
    	
    	TableStatisticImpl condition = new TableStatisticImpl();
        List<ITableStatistic> result = AanlysisModel.INSTANCE.searchTableStatsDefinition(condition, null, 0, -1);
    	
        StringBuilder sb = new StringBuilder();
    	for (ITableStatistic group : result) {
    		context.generateHTML("<div class=\"uimaster_orgcharts\">");
    		HTMLUtil.generateTab(context, depth + 1);
    		List<Object[]> list = loadOrgData(group);
    		if (list == null || list.isEmpty()) {
    			continue;
    		}
    		List<IChartPointData> chartPointData = toChartPoints(list);
    		HashMap<String, Integer> chartSum = toSumPoints(list, group);
    		
    		DefaultParsingContext localP = new DefaultParsingContext();
    		localP.setVariableClass("rowBE", IChartPointData.class);
    		
    		List<ITableColumnStatistic> definedColumns = group.getColumns();
    		List<UITableColumnType> columns = new ArrayList<UITableColumnType>();
    		for (ITableColumnStatistic definedCol : definedColumns) {
    			UITableColumnType col = new UITableColumnType();
    			StringPropertyType title = new StringPropertyType();
    			title.setValue(definedCol.getName());
    			if (definedCol.getDescription() != null) {
    				title.setValue(definedCol.getDescription());
    			}
    			col.setTitle(title);
    			String realColumnId = definedCol.getName();
    			col.setBeFieldId("rowBE." + realColumnId);
    			if (group.getChartType() == StatisticChartType.LINEAR
    					|| group.getChartType() == StatisticChartType.RADAR
    					|| group.getChartType() == StatisticChartType.BAR) {
    				String color = genRGB();
    				col.setCssStype("backgroundColor: 'rgba("+color+",0.2)',pointBackgroundColor: 'rgba("+color+",1)',hoverPointBackgroundColor: 'rgba("+color+",1)',pointHighlightStroke: '#1CBC5F'");
    			} else {
    				col.setCssStype(genHexColor());
    			}
    			ExpressionPropertyType expr = new ExpressionPropertyType();
    			ExpressionType exprValue = new ExpressionType();
    			if (columns.size() == 0) {
    				exprValue.setExpressionString("{return rowBE.getDataset();}");
    			} else {
    				exprValue.setExpressionString("{return rowBE.getDataset"+columns.size()+"();}");
    			}
    			expr.setExpression(exprValue);
        		try {
					exprValue.parse(localP);
				} catch (ParsingException e) {
					logger.warn(e.getMessage(), e);
				}
        		
    			col.setRowExpression(expr);
    			columns.add(col);
    		}
    		
    		context.generateHTML("<div class=\"uimaster_orgcharts_title\">");
    		context.generateHTML(group.getDescription());
    		context.generateHTML("</div>");
    		HTMLUtil.generateTab(context, depth + 1);
    		context.generateHTML("<div class=\"uimaster_orgcharts_content\" ");
    		if (UserContext.isMobileRequest()) {
    			context.generateHTML(" style=\"width:100%;\">");
    		} else {
    			context.generateHTML(" style=\"width:30%;\">");
    		}
    		HTMLUtil.generateTab(context, depth + 2);
    		HTMLChartSuper chartWidget = null;
    		String uiid = group.getTableName() + (int)(Math.random() * 1000);
			if (group.getChartType() == StatisticChartType.LINEAR) {
    			chartWidget = new HTMLChartLinearType(context, uiid);
    			chartWidget.addAttribute("query", chartPointData);
    			chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartPointData));
        	} else if (group.getChartType() == StatisticChartType.BAR) {
        		chartWidget = new HTMLChartBarType(context, uiid);
    			chartWidget.addAttribute("query", chartPointData);
    			chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartPointData));
        	} else if (group.getChartType() == StatisticChartType.RADAR) {
        		chartWidget = new HTMLChartRadarType(context, uiid);
    			chartWidget.addAttribute("query", chartPointData);
    			chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartPointData));
        	} else if (group.getChartType() == StatisticChartType.PIE) {
        		chartWidget = new HTMLChartDoughnutType(context, uiid);
        		chartWidget.addAttribute("query", chartSum);
        		chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartSum));
        	} else if (group.getChartType() == StatisticChartType.POLARPIE) {
        		chartWidget = new HTMLChartPolarPieType(context, uiid);
        		chartWidget.addAttribute("query", chartSum);
        		chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartSum));
        	} else {
        		context.generateHTML(">");
        	}
    		chartWidget.setPrefix(context.getHTMLPrefix());
    		chartWidget.setFrameInfo(context.getFrameInfo());
    		chartWidget.addAttribute("columns", columns);
    		chartWidget.addAttribute("width", "400px");
    		chartWidget.addAttribute("height", "300px");
    		
    		chartWidget.generateBeginHTML(context, ownerEntity, depth);
    		chartWidget.generateEndHTML(context, ownerEntity, depth);
    		
    		if (group.getChartType() == StatisticChartType.LINEAR
    				|| group.getChartType() == StatisticChartType.BAR) {
    			List<UITableColumnType> subColumns = new ArrayList<UITableColumnType>();
        		for (ITableColumnStatistic definedCol : definedColumns) {
        			UITableColumnType col = new UITableColumnType();
        			StringPropertyType title = new StringPropertyType();
        			title.setValue(definedCol.getName());
        			if (definedCol.getDescription() != null) {
        				title.setValue(definedCol.getDescription());
        			}
        			col.setTitle(title);
        			String realColumnId = definedCol.getName();
        			col.setBeFieldId("rowBE." + realColumnId);
        			col.setCssStype(genHexColor());
        			subColumns.add(col);
        		}
    			
        		String sumWidth = "200px";
        		if (UserContext.isMobileRequest()) {
        			sumWidth = "150px";
        		}
	    		if (group.getNeedSum()) {
	    			HTMLUtil.generateTab(context, depth + 1);
	    			HTMLChartDoughnutType sumChart = generateSumPie(context, subColumns, chartSum, sb, uiid+"sum", "\u603B\u6570");
	    			
	    			context.generateHTML("<div class=\"uimaster_orgcharts_content\"  style=\"width:"+sumWidth+";float:right;\">");
	    			sumChart.generateBeginHTML(context, ownerEntity, depth);
	    			sumChart.generateEndHTML(context, ownerEntity, depth);
	    			context.generateHTML("</div>");
	    		}
	    		if (group.getNeedAverage()) {
	    			HTMLUtil.generateTab(context, depth + 1);
	    			HashMap<String, Integer> chartAve = toAvePoints(list, group);
	    			HTMLChartDoughnutType aveChart = generateSumPie(context, subColumns, chartAve, sb, uiid+"ave", "\u5E73\u5747\u503C");
	    			
	    			context.generateHTML("<div class=\"uimaster_orgcharts_content\"  style=\"width:"+sumWidth+";\">");
	    			aveChart.generateBeginHTML(context, ownerEntity, depth);
	    			aveChart.generateEndHTML(context, ownerEntity, depth);
	    			context.generateHTML("</div>");
	    		}
    		}
    		sb.append("UIMaster.pageInitFunctions.push(function(){\n");
    		String jsvar = "defaultname." + uiid;
    		if (group.getChartType() == StatisticChartType.LINEAR) {
    			sb.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
        	} else if (group.getChartType() == StatisticChartType.RADAR) {
        		sb.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
        	} else if (group.getChartType() == StatisticChartType.BAR) {
        		sb.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
        	} else if (group.getChartType() == StatisticChartType.PIE) {
        		sb.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
        	} else if (group.getChartType() == StatisticChartType.POLARPIE) {
        		sb.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
        	} 
    		sb.append(chartWidget.getId()).append("\"]});\n");
    		sb.append(jsvar).append(".init();\n");
    		sb.append("});\n");
    		HTMLUtil.generateTab(context, depth + 1);
    		context.generateHTML("</div>");
    		HTMLUtil.generateTab(context, depth);
    		context.generateHTML("</div>");
    		HTMLUtil.generateTab(context, depth);
    	}
    	context.generateHTML("<script type=\"text/javascript\">\n");
    	context.generateHTML(sb.toString());
    	context.generateHTML("</script>");
    	
    	generateEndWidget(context);
    }
    
    private HTMLChartDoughnutType generateSumPie(HTMLSnapshotContext context, List<UITableColumnType> columns, 
    		HashMap<String, Integer> chartSum, StringBuilder jsBuffer, String uiid, String title) {
		HTMLChartDoughnutType chartWidget = new HTMLChartDoughnutType(context, uiid);
		chartWidget.addAttribute("columns", columns);
		chartWidget.addAttribute("query", chartSum);
		chartWidget.addAttribute("labels", AanlysisModelCust.getLabels(chartSum));
		chartWidget.setPrefix(context.getHTMLPrefix());
		chartWidget.setFrameInfo(context.getFrameInfo());
		chartWidget.addAttribute("width", "400px");
		chartWidget.addAttribute("height", "300px");
		chartWidget.addAttribute("title", title);
		
		jsBuffer.append("UIMaster.pageInitFunctions.push(function(){\n");
		String jsvar = "defaultname." + uiid;
		jsBuffer.append(jsvar).append(" = new UIMaster.ui.chart({ui: elementList[\"");
		jsBuffer.append(chartWidget.getId()).append("\"]});\n");
		jsBuffer.append(jsvar).append(".init();\n");
		
		jsBuffer.append("});\n");
		return chartWidget;
    }

	public List<Object[]> loadOrgData(ITableStatistic group) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT CREATEDATE, ");
		List<ITableColumnStatistic> columns = group.getColumns();
		for (ITableColumnStatistic col : columns) {
			String realColumnId = col.getName();
			sb.append(realColumnId).append(',');
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(" FROM STATS_").append(group.getTableName());
		if (group.getNeedOrgStats()) {
			sb.append(" WHERE ORGID=? ");
		}
		sb.append(";");
		
		Session session = HibernateUtil.getSession();
		try {
	    	SQLQuery sqlQuery = session.createSQLQuery(sb.toString());
	    	sqlQuery.setParameter(0, UserContext.getUserContext().getOrgId());
	    	return sqlQuery.list();
		} finally {
			HibernateUtil.releaseSession(session, true);
		}
	}
	
	private HashMap<String, Integer> toAvePoints(final List<Object[]> data, ITableStatistic group) {
		HashMap<String, Integer> result  = new HashMap<String, Integer>();
		List<ITableColumnStatistic> columns = group.getColumns();
		
		int colSize = data.get(0).length;
		while (--colSize > 0) {
			int sum = 0;
			for (Object[] row: data) {
				sum += (Integer)row[colSize];
			}
			int sumAverage = sum/data.size();
			result.put(columns.get(colSize-1).getDescription(), sumAverage);
		}
		return result;
	}
	
	private HashMap<String, Integer> toSumPoints(final List<Object[]> data, ITableStatistic group) {
		HashMap<String, Integer> result  = new HashMap<String, Integer>();
		List<ITableColumnStatistic> columns = group.getColumns();
		
		int colSize = data.get(0).length;
		while (--colSize > 0) {
			int sum = 0;
			for (Object[] row: data) {
				sum += (Integer)row[colSize];
			}
			result.put(columns.get(colSize-1).getDescription(), sum);
		}
		return result;
	}
	
	
	private List<IChartPointData> toChartPoints(final List<Object[]> data) {
		List<IChartPointData> points = new ArrayList<IChartPointData>();
		for (Object[] row: data) {
	 		ChartPointDataImpl item = new ChartPointDataImpl();
			item.setLabel(String.valueOf(row[0]));//the label(CREATEDATE) must be on the first column.
			
			item.setDataset(String.valueOf(row[1]));
			if (row.length >= 3) {
				item.setDataset1(String.valueOf(row[2]));
			}
			if (row.length >= 4) {
				item.setDataset2(String.valueOf(row[3]));
			}
			if (row.length >= 5) {
				item.setDataset3(String.valueOf(row[4]));
			}
			if (row.length >= 6) {
				item.setDataset4(String.valueOf(row[5]));
			}
			if (row.length >= 7) {
				item.setDataset5(String.valueOf(row[6]));
			}
			if (row.length >= 8) {
				item.setDataset6(String.valueOf(row[7]));
			}
			if (row.length >= 9) {
				item.setDataset7(String.valueOf(row[8]));
			}
			if (row.length >= 10) {
				item.setDataset8(String.valueOf(row[9]));
			}
			if (row.length >= 11) {
				item.setDataset9(String.valueOf(row[10]));
			}
			points.add(item);
		}
		return points;
	}
	
	private String genRGB() {
		return ((int) (Math.random() * 255)) + ","
				+ ((int) (Math.random() * 255)) + ","
				+ ((int) (Math.random() * 255));
	}
	
	private String genHexColor() {
		Random random = new Random();
		String red = Integer.toHexString(random.nextInt(256)).toUpperCase();
		String green = Integer.toHexString(random.nextInt(256)).toUpperCase();
		String blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

		red = red.length() == 1 ? "0" + red : red;
		green = green.length() == 1 ? "0" + green : green;
		blue = blue.length() == 1 ? "0" + blue : blue;
		return "'#" + red + green + blue + "'";
	}
	
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
      return null;
    }
}
