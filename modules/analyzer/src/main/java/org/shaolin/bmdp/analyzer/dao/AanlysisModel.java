package org.shaolin.bmdp.analyzer.dao;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import org.hibernate.criterion.Order;

import org.shaolin.bmdp.persistence.BEEntityDaoObject;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.persistence.query.operator.Operator;

/**
 * This code is generated automatically, any change will be replaced after rebuild.
 */
public class AanlysisModel extends BEEntityDaoObject {

    public static final AanlysisModel INSTANCE = new AanlysisModel();

    private AanlysisModel() {
    }

    public List<org.shaolin.bmdp.analyzer.be.IClientDBInfo> searchClientDBInfo(org.shaolin.bmdp.analyzer.be.ClientDBInfoImpl scObject,
           List<Order> orders, int offset, int count) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.ClientDBInfoImpl.class, "inObject");
            if (orders == null) {
            } else {
                this._addOrders(inObjectCriteria, orders);
            }


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        List result = this._list(offset, count, inObjectCriteria);
        return result;
    }

    public long searchClientDBInfoCount(org.shaolin.bmdp.analyzer.be.ClientDBInfoImpl scObject) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.ClientDBInfoImpl.class, "inObject");


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        return this._count(inObjectCriteria);
    }

    public List<org.shaolin.bmdp.analyzer.be.IJob> searchJob(org.shaolin.bmdp.analyzer.be.JobImpl scObject,
           List<Order> orders, int offset, int count) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.JobImpl.class, "inObject");
            if (orders == null) {
            } else {
                this._addOrders(inObjectCriteria, orders);
            }


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        List result = this._list(offset, count, inObjectCriteria);
        return result;
    }

    public long searchJobCount(org.shaolin.bmdp.analyzer.be.JobImpl scObject) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.JobImpl.class, "inObject");


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        return this._count(inObjectCriteria);
    }

    public List<org.shaolin.bmdp.analyzer.be.IChartStatistic> searchChartStats(org.shaolin.bmdp.analyzer.be.ChartStatisticImpl scObject,
           List<Order> orders, int offset, int count) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.ChartStatisticImpl.class, "inObject");
            if (orders == null) {
            } else {
                this._addOrders(inObjectCriteria, orders);
            }


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        List result = this._list(offset, count, inObjectCriteria);
        return result;
    }

    public long searchChartStatsCount(org.shaolin.bmdp.analyzer.be.ChartStatisticImpl scObject) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.ChartStatisticImpl.class, "inObject");


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        return this._count(inObjectCriteria);
    }

}

