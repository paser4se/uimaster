package org.shaolin.bmdp.analyzer.dao;

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
public class HelperModel extends BEEntityDaoObject {

    public static final HelperModel INSTANCE = new HelperModel();

    private HelperModel() {
    }

    public List<org.shaolin.bmdp.analyzer.be.IPageHelper> searchPageHelper(org.shaolin.bmdp.analyzer.be.PageHelperImpl scObject,
           List<Order> orders, int offset, int count) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.PageHelperImpl.class, "inObject");
            if (orders == null) {
            } else {
                this._addOrders(inObjectCriteria, orders);
            }


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        List result = this._list(offset, count, inObjectCriteria);
        return result;
    }

    public long searchPageHelperCount(org.shaolin.bmdp.analyzer.be.PageHelperImpl scObject) {
            Criteria inObjectCriteria = this._createCriteria(org.shaolin.bmdp.analyzer.be.PageHelperImpl.class, "inObject");


        inObjectCriteria.add(createCriterion(Operator.EQUALS, "inObject._enable", scObject.isEnabled()));

        return this._count(inObjectCriteria);
    }

}

