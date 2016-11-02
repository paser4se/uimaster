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

/**
 * This code is generated automatically, any change will be replaced after rebuild.
 */
public class CoordinatorModel extends BEEntityDaoObject {

    public static final CoordinatorModel INSTANCE = new CoordinatorModel();

    private CoordinatorModel() {
    }

    public List<org.shaolin.bmdp.workflow.be.ITask> searchTasks(org.shaolin.bmdp.workflow.be.TaskImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (scFlow.getSubject() != null && scFlow.getSubject().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.START_WITH_RIGHT, "inFlow.subject", scFlow.getSubject()));
            }
            if (scFlow.getExpiredTimeStart() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeStart()));
            }
            if (scFlow.getExpiredTimeEnd() != null) {
                inFlowCriteria.add(createCriterion(Operator.LESS_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeEnd()));
            }
            if (scFlow.getStatus() != null && scFlow.getStatus() != org.shaolin.bmdp.workflow.ce.TaskStatusType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.statusInt", scFlow.getStatus().getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchTasksCount(org.shaolin.bmdp.workflow.be.TaskImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().trim().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (scFlow.getSubject() != null && scFlow.getSubject().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.START_WITH_RIGHT, "inFlow.subject", scFlow.getSubject()));
            }
            if (scFlow.getExpiredTimeStart() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeStart()));
            }
            if (scFlow.getExpiredTimeEnd() != null) {
                inFlowCriteria.add(createCriterion(Operator.LESS_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeEnd()));
            }
            if (scFlow.getStatus() != null && scFlow.getStatus() != org.shaolin.bmdp.workflow.ce.TaskStatusType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.statusInt", scFlow.getStatus().getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.ITask> searchPendingTasks(org.shaolin.bmdp.workflow.be.TaskImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (true) {
                inFlowCriteria.add(createCriterion(Operator.IN, "inFlow.statusInt", org.shaolin.bmdp.workflow.ce.TaskStatusType.NOTSTARTED.getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchPendingTasksCount(org.shaolin.bmdp.workflow.be.TaskImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskImpl.class, "inFlow");

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (true) {
                inFlowCriteria.add(createCriterion(Operator.IN, "inFlow.statusInt", org.shaolin.bmdp.workflow.ce.TaskStatusType.NOTSTARTED.getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.ITaskHistory> searchTasksHistory(org.shaolin.bmdp.workflow.be.TaskHistoryImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskHistoryImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getTaskId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.taskId", scFlow.getTaskId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (scFlow.getSubject() != null && scFlow.getSubject().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.START_WITH_RIGHT, "inFlow.subject", scFlow.getSubject()));
            }
            if (scFlow.getExpiredTimeStart() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeStart()));
            }
            if (scFlow.getExpiredTimeEnd() != null) {
                inFlowCriteria.add(createCriterion(Operator.LESS_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeEnd()));
            }
            if (scFlow.getStatus() != null && scFlow.getStatus() != org.shaolin.bmdp.workflow.ce.TaskStatusType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.statusInt", scFlow.getStatus().getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchTasksHistoryCount(org.shaolin.bmdp.workflow.be.TaskHistoryImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.TaskHistoryImpl.class, "inFlow");

            if (scFlow.getId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.id", scFlow.getId()));
            }
            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getTaskId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.taskId", scFlow.getTaskId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }
            if (scFlow.getSubject() != null && scFlow.getSubject().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.START_WITH_RIGHT, "inFlow.subject", scFlow.getSubject()));
            }
            if (scFlow.getExpiredTimeStart() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeStart()));
            }
            if (scFlow.getExpiredTimeEnd() != null) {
                inFlowCriteria.add(createCriterion(Operator.LESS_THAN_OR_EQUALS, "inFlow.expiredTime", scFlow.getExpiredTimeEnd()));
            }
            if (scFlow.getStatus() != null && scFlow.getStatus() != org.shaolin.bmdp.workflow.ce.TaskStatusType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.statusInt", scFlow.getStatus().getIntValue()));
            }
            if (scFlow.getPriority() != null && scFlow.getPriority() != org.shaolin.bmdp.workflow.ce.TaskPriorityType.NOT_SPECIFIED) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.priorityInt", scFlow.getPriority().getIntValue()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.INotification> searchNotification(org.shaolin.bmdp.workflow.be.NotificationImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.NotificationImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getCreateDate() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.createDate", scFlow.getCreateDate()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchNotificationCount(org.shaolin.bmdp.workflow.be.NotificationImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.NotificationImpl.class, "inFlow");

            if (scFlow.getPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.partyId", scFlow.getPartyId()));
            }
            if (scFlow.getCreateDate() != null) {
                inFlowCriteria.add(createCriterion(Operator.GREATER_THAN_OR_EQUALS, "inFlow.createDate", scFlow.getCreateDate()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.IChatHistory> searchChatHistory(org.shaolin.bmdp.workflow.be.ChatHistoryImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.ChatHistoryImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }

            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getSentPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sentPartyId", scFlow.getSentPartyId()));
            }
            if (scFlow.getReceivedPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.receivedPartyId", scFlow.getReceivedPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchChatHistoryCount(org.shaolin.bmdp.workflow.be.ChatHistoryImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.ChatHistoryImpl.class, "inFlow");

            if (scFlow.getOrgId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.orgId", scFlow.getOrgId()));
            }
            if (scFlow.getSentPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sentPartyId", scFlow.getSentPartyId()));
            }
            if (scFlow.getReceivedPartyId() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.receivedPartyId", scFlow.getReceivedPartyId()));
            }
            if (scFlow.getSessionId() != null && scFlow.getSessionId().length() > 0) {
                inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow.sessionId", scFlow.getSessionId()));
            }

        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

    public List<org.shaolin.bmdp.workflow.be.IServerNodeInfo> searchServerNodes(org.shaolin.bmdp.workflow.be.ServerNodeInfoImpl scFlow,
           List<Order> orders, int offset, int count) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.ServerNodeInfoImpl.class, "inFlow");
            if (orders == null) {
            } else {
                this._addOrders(inFlowCriteria, orders);
            }


        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        List result = this._list(offset, count, inFlowCriteria);
        return result;
    }

    public long searchServerNodesCount(org.shaolin.bmdp.workflow.be.ServerNodeInfoImpl scFlow) {
            Criteria inFlowCriteria = this._createCriteria(org.shaolin.bmdp.workflow.be.ServerNodeInfoImpl.class, "inFlow");


        inFlowCriteria.add(createCriterion(Operator.EQUALS, "inFlow._enable", scFlow.isEnabled()));

        return this._count(inFlowCriteria);
    }

}

