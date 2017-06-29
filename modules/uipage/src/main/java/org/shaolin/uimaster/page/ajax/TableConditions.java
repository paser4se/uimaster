/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.uimaster.page.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.utils.StringUtil;

public class TableConditions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IBusinessEntity condition;
	private Object additionalCondition;

	private List<Order> orders;
	private int offset;
	private int count = 10;//10 records per query by default.
	private String pullAction = "new";//mobile pull. we have new or history actions.
	private long pullId;
	
	private int currentSelectedIndex = -1;
	
	private Integer[] selectedIndexs;

	public TableConditions() {
	}
	
	public static final TableConditions createCondition() {
		return new TableConditions();
	}
	
	public static final TableConditions createCondition(IBusinessEntity be) {
		TableConditions condition = new TableConditions();
		condition.setBECondition(be);
		return condition;
	}
	
	public static final TableConditions createCondition(Object object) {
		TableConditions condition = new TableConditions();
		condition.setAdditionalCondition(object);
		return condition;
	}
	
	public Object getAdditionalCondition() {
		return additionalCondition;
	}

	public void setAdditionalCondition(Object additionalCondition) {
		this.additionalCondition = additionalCondition;
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		if (this.orders == null) {
			this.orders = new ArrayList<Order>();
		}
		this.orders.add(order);
	}
	
	public void addOrder(String name, boolean asc) {
		if (this.orders == null) {
			this.orders = new ArrayList<Order>();
		}
		for (int i=0; i<orders.size(); i++) {
			if (orders.get(i).getPropertyName().equalsIgnoreCase(name)) {
				orders.remove(i);
				break;
			}
		}
		this.orders.add(asc ? Order.asc(name) : Order.desc(name));
	}
	
	public void clearOrder() {
		if (this.orders != null) {
			this.orders.clear();
		}
	}
	
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public IBusinessEntity getCondition() {
		return condition;
	}

	public IBusinessEntity getBECondition() {
		return condition;
	}
	
	public void setBECondition(IBusinessEntity condition) {
		this.condition = condition;
	}

	public int getCurrentSelectedIndex() {
		return currentSelectedIndex;
	}

	public void setCurrentSelectedIndex(int currentSelectedIndex) {
		this.currentSelectedIndex = currentSelectedIndex;
	}

	public Integer[] getSelectedIndex() {
		return selectedIndexs;
	}

	public void setSelectedIndex(Integer[] selectedIndex) {
		this.selectedIndexs = selectedIndex;
	}

	public String getPullAction() {
		return pullAction;
	}

	public void setPullAction(String pullAction) {
		this.pullAction = pullAction;
	}
	
	public long getPullId() {
		return pullId;
	}

	public void setPullId(long pullId) {
		this.pullId = pullId;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("condition", condition.toJSON());
		json.put("condiType", condition.getClass().getName());
		
		json.put("currentSelectedIndex", this.currentSelectedIndex);
		json.put("offset", this.offset);
		json.put("count", this.count);
		json.put("pullAction", this.pullAction);
		json.put("pullId", this.pullId);
		if (this.orders != null && this.orders.size() > 0) {
			JSONArray ors = new JSONArray();
			for (Order o : this.orders) {
				JSONObject item = new JSONObject();
				item.put("name", o.getPropertyName());
				item.put("o", o.isAscending());
				ors.put(item);
			}
			json.put("orders", ors);
		}
		if (this.selectedIndexs != null && this.selectedIndexs.length > 0) {
			
		}
		return json;
	}
	
	public static TableConditions fromJson(JSONObject json) throws JSONException {
		TableConditions condition = TableConditions.createCondition();
		try {
			IBusinessEntity beObject = (IBusinessEntity)Class.forName(json.getString("condiType")).newInstance();
			beObject.fromJSON(json.getJSONObject("condition"));
			condition.setBECondition(beObject);
		} catch (Exception e) {
			Logger.getLogger(TableConditions.class).warn(e);
		} 
		condition.setCount(json.getInt("count"));
		condition.setCurrentSelectedIndex(json.getInt("currentSelectedIndex"));
		condition.setOffset(json.getInt("offset"));
		condition.setPullAction(json.getString("pullAction"));
		condition.setPullId(json.getLong("pullId"));
		if (json.has("orders")) {
			JSONArray items = json.getJSONArray("orders");
			List<Order> orders = new ArrayList<Order>();
			for (int i=0; i<items.length(); i++) {
				String propertyName = items.getJSONObject(i).getString("name");
				if (items.getJSONObject(i).getBoolean("o")) {
					orders.add(Order.asc(propertyName));
				} else {
					orders.add(Order.desc(propertyName));
				}
			}
			condition.setOrders(orders);
		}
		if (json.has("selectedIndex")) {
			String v = json.getString("selectedIndex");
			if (v != null) {
				List<Integer> t = StringUtil.splitAsInt(v, ",");
				Integer[] indices = new Integer[t.size()];
				t.toArray(indices);
				condition.setSelectedIndex(indices);
			}
		}
		return condition;
	}

}
