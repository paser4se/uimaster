package org.shaolin.bmdp.workflow.dao;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import org.hibernate.criterion.Order;

import org.hibernate.criterion.Projections;

import org.shaolin.bmdp.persistence.BEEntityDaoObject;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.persistence.query.operator.Operator;

import org.shaolin.bmdp.workflow.be.IFlowEntity;
import org.shaolin.bmdp.workflow.be.FlowEntityImpl;
/**
 * This code is generated automatically, any change will be replaced after rebuild.
 */
public class WorkflowModel extends BEEntityDaoObject {

    public static final WorkflowModel INSTANCE = new WorkflowModel();

    private WorkflowModel() {
    }

    public List<IFlowEntity> listIFlowEntitys(int offset, int count) {
        return list(offset, count, IFlowEntity.class, FlowEntityImpl.class);
    }

    public long listIFlowEntityCount() {
        return count(IFlowEntity.class);
    }

    public List<org.shaolin.bmdp.workflow.be.IFlowEntity> searchFlowEntities(org.shaolin.bmdp.workflow.be.FlowEntityImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.FlowEntityImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getEntityName() != null && scFlow.getEntityName().trim().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.entityName", scFlow.getEntityName()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchFlowEntitiesCount(org.shaolin.bmdp.workflow.be.FlowEntityImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.FlowEntityImpl.class, "inFlow");

            if (scFlow.getEntityName() != null && scFlow.getEntityName().trim().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.entityName", scFlow.getEntityName()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.IUIFlows> searchFlows(org.shaolin.bmdp.workflow.be.UIFlowsImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.UIFlowsImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getModuleType() != null && scFlow.getModuleType() != org.shaolin.bmdp.workflow.ce.ModuleType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.moduleTypeInt", scFlow.getModuleType().getIntValue()));
            }
            if (scFlow.getModuleItemId() != -1) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.moduleItemId", scFlow.getModuleItemId()));
            }
            if (scFlow.getName() != null && !scFlow.getName().isEmpty()) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.name", scFlow.getName()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchFlowsCount(org.shaolin.bmdp.workflow.be.UIFlowsImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.UIFlowsImpl.class, "inFlow");

            if (scFlow.getModuleType() != null && scFlow.getModuleType() != org.shaolin.bmdp.workflow.ce.ModuleType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.moduleTypeInt", scFlow.getModuleType().getIntValue()));
            }
            if (scFlow.getModuleItemId() != -1) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.moduleItemId", scFlow.getModuleItemId()));
            }
            if (scFlow.getName() != null && !scFlow.getName().isEmpty()) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.name", scFlow.getName()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

}

