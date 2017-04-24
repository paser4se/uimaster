package org.shaolin.bmdp.workflow.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.shaolin.bmdp.persistence.BEEntityDaoObject;
import org.shaolin.bmdp.persistence.query.operator.Operator;
import org.shaolin.bmdp.workflow.be.ISession;
import org.shaolin.bmdp.workflow.be.SessionImpl;

public class CustCoordinatorModel extends BEEntityDaoObject {
	public static final CustCoordinatorModel INSTANCE = new CustCoordinatorModel();

	private CustCoordinatorModel() {
	}

	public List<ISession> searchSessions(org.shaolin.bmdp.workflow.be.TaskImpl scFlow, int offset, int count) {
		Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");
		inFlowCriteria.setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("sessionId"), "sessionId")));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("createDate"));
		this._addOrders(inFlowCriteria, orders);
		if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
			inFlowCriteria.add(createCriterion(Operator.START_WITH, "inFlow.sessionId", scFlow.getSessionId()));
		}
		if (scFlow.getOrgId() > 0) {
			inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
		}
		inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

		List<String> result0 = this._list(offset, count, inFlowCriteria);
		List<ISession> result = new ArrayList<ISession>(result0.size());
		boolean flag = false;
		for (String task : result0) {
			ISession s = new SessionImpl();
			if (!flag) {
				flag = true;				
				s.get_extField().put("count", searchSessionCount(scFlow));
			}
			s.setSessionId(task);
			result.add(s);
		}
		
		
		return result;
	}
	
	public long searchSessionCount(org.shaolin.bmdp.workflow.be.TaskImpl scFlow) {
		Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");
		inFlowCriteria.setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("sessionId"), "sessionId")));
		if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
			inFlowCriteria.add(createCriterion(Operator.START_WITH, "inFlow.sessionId", scFlow.getSessionId()));
		}
		if (scFlow.getOrgId() > 0) {
			inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
		}
		inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

		return this._count(inFlowCriteria);
	}
	
	public List<ISession> searchSessionHistory(org.shaolin.bmdp.workflow.be.TaskImpl scFlow, int offset, int count) {
		Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskHistoryImpl.class, "inFlow");
		inFlowCriteria.setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("sessionId"), "sessionId")));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("createDate"));
		this._addOrders(inFlowCriteria, orders);
		if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
			inFlowCriteria.add(createCriterion(Operator.START_WITH, "inFlow.sessionId", scFlow.getSessionId()));
		}
		if (scFlow.getOrgId() > 0) {
			inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
		}
		inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

		List<String> result0 = this._list(offset, count, inFlowCriteria);
		List<ISession> result = new ArrayList<ISession>(result0.size());
		boolean flag = false;
		for (String task : result0) {
			ISession s = new SessionImpl();
			if (!flag) {
				flag = true;				
				s.get_extField().put("count", searchSessionHistoryCount(scFlow));
			}
			s.setSessionId(task);
			result.add(s);
		}
		return result;
	}
	
	public long searchSessionHistoryCount(org.shaolin.bmdp.workflow.be.TaskImpl scFlow) {
		Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskHistoryImpl.class, "inFlow");
		inFlowCriteria.setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("sessionId"), "sessionId")));
		if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
			inFlowCriteria.add(createCriterion(Operator.START_WITH, "inFlow.sessionId", scFlow.getSessionId()));
		}
		if (scFlow.getOrgId() > 0) {
			inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
		}
		inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

		return this._count(inFlowCriteria);
	}

}
