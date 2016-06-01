package org.shaolin.bmdp.persistence;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.shaolin.bmdp.persistence.query.operator.Operator;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.runtime.be.IPersistentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BEEntityDaoObject {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final BEEntityDaoObject DAOOBJECT = new BEEntityDaoObject();

	public void addResource(String hbmMapping) {
		HibernateUtil.getConfiguration().addResource(hbmMapping);
	}

	/**
	 * Insert a business entity record to database.
	 * 
	 * @param entity
	 */
	public void create(IPersistentEntity entity, boolean commit) {
		if (logger.isDebugEnabled()) {
			logger.debug("Insert an entity: {}", entity);
		}
		if (entity.getCreateDate() == null) {
			entity.setCreateDate(new Date());
		}

		Session session = HibernateUtil.getSession();
		session.save(entity);
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	public void create(IPersistentEntity entity) {
		this.create(entity, false);
	}

	/**
	 * Delete a business entity record to database.
	 * 
	 * @param entity
	 */
	public void delete(IPersistentEntity entity, boolean commit) {
		if (entity.getId() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Remove an entity: {}", entity);
		}

		Session session = HibernateUtil.getSession();
		session.delete(entity);
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void delete(IPersistentEntity entity) {
		this.delete(entity, false);
	}

	/**
	 * Update a business entity record to database.
	 * 
	 * @param entity
	 */
	public void update(IPersistentEntity entity, boolean commit) {
		if (logger.isDebugEnabled()) {
			logger.debug("Update an entity: {}", entity);
		}

		try {
			Session session = HibernateUtil.getSession();
			session.update(entity);
		} catch (NonUniqueObjectException e) {
			// try committing session again.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);

			Session session = HibernateUtil.getSession();
			session.update(entity);
		}
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void update(IPersistentEntity entity) {
		this.update(entity, false);
	}

	/**
	 * Reload the entity
	 * 
	 * @param entity
	 */
	public void reload(IPersistentEntity entity) {
		if (entity.getId() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Reload an entity: {}", entity);
		}

		Session session = HibernateUtil.getSession();
		session.load(entity, entity.getId());
		HibernateUtil.releaseSession(session, true);
	}

	public void cascadingUpdate(IPersistentEntity entity) {
		if (entity.getId() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Cascading update an entity: {}", entity);
		}

		Session session = HibernateUtil.getSession();
		session.merge(entity);
		HibernateUtil.releaseSession(session, true);
	}

	public void batchInsert(List<IPersistentEntity> entities) {
		this.batchInsert(entities, false);
	}

	/**
	 * Batch insert for multiple entities.
	 * 
	 * @param entities
	 */
	public void batchInsert(List<IPersistentEntity> entities, boolean commit) {
		if (entities == null || entities.size() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Insert a batch of entities: {}", entities);
		}

		Session session = HibernateUtil.getSession();
		for (IPersistentEntity entity : entities) {
			if (entity.getCreateDate() == null) {
				entity.setCreateDate(new Date());
			}

			if (entity.getId() > 0) {
				session.update(entity);
			} else {
				session.save(entity);
			}
		}
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	public void batchUpdate(List<IPersistentEntity> entities) {
		this.batchUpdate(entities, false);
	}

	/**
	 * Batch update for multiple entities.
	 * 
	 * @param entities
	 */
	public void batchUpdate(List<IPersistentEntity> entities, boolean commit) {
		if (entities == null || entities.size() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Update a batch of entities: {}", entities);
		}

		Session session = HibernateUtil.getSession();
		for (IPersistentEntity entity : entities) {
			if (entity.getId() == 0) {
				session.save(entity);
			} else {
				session.update(entity);
			}
		}
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	public void batchDelete(List<IPersistentEntity> entities) {
		this.batchDelete(entities, false);
	}

	/**
	 * Batch delete for multiple entities.
	 * 
	 * @param entities
	 */
	public void batchDelete(List<IPersistentEntity> entities, boolean commit) {
		if (entities == null || entities.size() == 0) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Remove a batch of entities: {}", entities);
		}

		Session session = HibernateUtil.getSession();
		for (IPersistentEntity entity : entities) {
			if (entity.getId() > 0) {
				session.delete(entity);
			}
		}
		if (commit) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	/**
	 * Delete a business entity record to database.
	 * 
	 * @param entity
	 */
	public void disable(IPersistentEntity entity) {
		if (logger.isDebugEnabled()) {
			logger.debug("Disable an entity: {}", entity);
		}

		Session session = HibernateUtil.getSession();
		entity.setEnabled(false);
		session.update(entity);

		HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
	}

	/**
	 * Enable a business entity record to database.
	 * 
	 * @param entity
	 */
	public void enable(IPersistentEntity entity) {
		if (logger.isDebugEnabled()) {
			logger.debug("Enable an entity: {}", entity);
		}

		Session session = HibernateUtil.getSession();

		entity.setEnabled(true);
		session.update(entity);

		HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
	}

	/**
	 * Query for the statistic tables which analyzed by Scala engine
	 * 
	 * @param offset
	 * @param count
	 * @param conditions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listStatistic(String table, int offset, int count,
			Map<String, Object> conditions) {
		try {
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery("select * from " + table).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> statsPerDayByOrgId(String table) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String day = simpleDateFormat.format(new Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT ORGID, COUNT(1) FROM " + table
					+ " WHERE CREATEDATE between '" + day + " 00-00-00' and '"
					+ day + " 23:59:59' GROUP BY ORGID");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> statsPerDay(String table) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
			String day = simpleDateFormat.format(new Date());
			StringBuilder sb = new StringBuilder();
	
			sb.append("SELECT COUNT(1) FROM " + table
					+ " WHERE CREATEDATE between '" + day + " 00-00-00' and '"
					+ day + " 23:59:59' GROUP BY DAY(CREATEDATE)");
			Session session = HibernateUtil.getSession();
			return session.createSQLQuery(sb.toString()).list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	/**
	 * Query for the normal tables.
	 * 
	 * @param offset
	 * @param count
	 * @param elementType
	 * @param persistentClass
	 * @return
	 */
	public <T> List<T> list(int offset, int count, Class<T> elementType,
			Class<?> persistentClass) {
		return list(offset, count, elementType, persistentClass, null, null);
	}

	public <T> List<T> list(int offset, int count, Class<T> elementType,
			String alias) {
		return list(offset, count, elementType, elementType, alias, null);
	}

	public <T> List<T> list(int offset, int count, Class<T> elementType,
			Class<?> persistentType, String alias) {
		return list(offset, count, elementType, persistentType, alias, null,
				null);
	}

	public <T> List<T> list(int offset, int count, Class<T> elementType,
			Class<?> persistentType, String alias, List<Criterion> criterions) {
		return list(offset, count, elementType, persistentType, alias,
				criterions, null);
	}

	/**
	 * Single table query with returning a list of the business entity recorded
	 * by conditions.
	 * 
	 * @param offset
	 * @param count
	 * @param elementType
	 * @param tableInfo
	 * @param criterions
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(int offset, int count, Class<T> elementType,
			Class<?> persistentType, String alias, List<Criterion> criterions,
			List<Order> orders) {
		Session session = HibernateUtil.getSession();
		try {
			Criteria criteria = null;
			if (alias != null) {
				criteria = session.createCriteria(persistentType, alias);
			} else {
				criteria = session.createCriteria(persistentType);
			}
			if (count > 0) {
				criteria.setFirstResult(offset);
				criteria.setMaxResults(count);
			}

			if (criterions != null) {
				for (Criterion c : criterions) {
					criteria.add(c);
				}
				// Restrictions
			}
			if (orders != null) {
				for (Order c : orders) {
					criteria.addOrder(c);
				}
			}

			return criteria.list();
		} finally {
			// release session ASAP. but it's an issue for transaction
			// manipulation.
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
		}
	}

	/**
	 * 
	 * Create Criteria. internal only.
	 * 
	 * @param session
	 * @param persistentType
	 * @param alias
	 * @return
	 */
	protected Criteria _createCriteria(Class<?> persistentType, String alias) {
		Session session = HibernateUtil.getSession();
		return session.createCriteria(persistentType, alias);
	}

	/**
	 * Join table query support. internal only.
	 * 
	 * @param criteria
	 * @param refField
	 * @param alias
	 * @return
	 */
	protected Criteria _createJoinAlias(Criteria criteria, String refField,
			String alias) {
		return criteria.createAlias(refField, alias);
	}

	protected void _addOrders(Criteria criteria, List<Order> orders) {
		if (orders != null && !orders.isEmpty()) {
			for (Order r : orders) {
				criteria.addOrder(r);
			}
		}
	}

	/**
	 * Does query. internal only.
	 * 
	 * @param offset
	 * @param count
	 * @param criteria
	 * @param criterions
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> _list(int offset, int count, Criteria criteria) {
		try {
			if (count > 0) {
				criteria.setFirstResult(offset);
				criteria.setMaxResults(count);
			}
			List<T> result = criteria.list();
			if (result != null && result.size() > 0) {
				if (result.get(0) instanceof IBusinessEntity) {
					IBusinessEntity be = (IBusinessEntity)result.get(0);
					criteria.setFirstResult(0);
					criteria.setMaxResults(1);
					be.get_extField().put("count", _count(criteria, result.size()));
				}
			}
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
			return result;
		} catch (Exception e) {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), false);
			logger.warn(criteria.toString() + " " + e.getMessage(), e);
			return Collections.emptyList();
		} 
	}
	
	private long _count(Criteria criteria, long rowSize) {
		criteria.setProjection(Projections.rowCount());
		@SuppressWarnings("unchecked")
		// return criteria.uniqueResult();
		List<Long> r = criteria.list();
		for (Long v : r) {
			if (v >= rowSize) {
				return v;
			}
		}
		return 0;
	}

	/**
	 * Does count query. internal only.
	 * 
	 * @param criteria
	 * @param criterions
	 * @return
	 */
	protected long _count(Criteria criteria) {
		criteria.setProjection(Projections.rowCount());
//		try {
//			result = criteria.uniqueResult();
//		} catch (NonUniqueResultException e) {
		@SuppressWarnings("unchecked")
		List<Long> r = criteria.list();
		for (Long v : r) {
			if (v > 0) {
				return v;
			}
		}
		return 0;
	}

	public long count(Class<?> persistentClass) {
		return count(persistentClass, null, null);
	}

	public long count(Class<?> persistentClass, String alias) {
		return count(persistentClass, alias, null);
	}

	/**
	 * Query the total count of the statistic tables which analyzed by Scala
	 * engine
	 * 
	 * @param offset
	 * @param count
	 * @param elementType
	 * @param persistentClass
	 * @return
	 */
	public long countStatistic(String table, Map<String, Object> condition) {
		Session session = HibernateUtil.getSession();
		return (Long) session.createSQLQuery("select count(1) from " + table)
				.uniqueResult();
	}

	/**
	 * Single table query with returning the total count of current business
	 * entities.
	 * 
	 * @param queryStr
	 * @return
	 */
	public long count(Class<?> persistentType, String alias,
			List<Criterion> criterions) {
		Session session = HibernateUtil.getSession();
		Criteria criteria = null;
		if (alias != null) {
			criteria = session.createCriteria(persistentType, alias);
		} else {
			criteria = session.createCriteria(alias);
		}
		if (criterions != null && !criterions.isEmpty()) {
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}

		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	/**
	 * 
	 * @param offset
	 * @param count
	 * @param sql
	 * @param condition
	 * @return
	 */
	public <T> List<T> sqlList(int offset, int count, StringBuffer sql,
			List<Object> condition, List<SQLTableInfo> tableInfo) {
		Session session = HibernateUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
		sqlQuery.setFirstResult(offset);
		sqlQuery.setMaxResults(count);

		for (int i = 0; i < condition.size(); i++) {
			Object value = condition.get(i);
			if (value instanceof String) {
				sqlQuery.setString(i, value.toString());
			} else if (value instanceof Integer) {
				sqlQuery.setInteger(i, (Integer) value);
			} else if (value instanceof Long) {
				sqlQuery.setLong(i, (Long) value);
			} else if (value instanceof Boolean) {
				sqlQuery.setBoolean(i, (Boolean) value);
			} else if (value instanceof Date) {
				sqlQuery.setDate(i, (Date) value);
			} else if (value instanceof Calendar) {
				sqlQuery.setCalendar(i, (Calendar) value);
			} else {
				sqlQuery.setString(i, value.toString());
			}
		}
		for (SQLTableInfo info : tableInfo) {
			sqlQuery.addEntity(info.tableAlias, info.pElementType);
		}

		return sqlQuery.list();
	}

	/**
	 * 
	 * @param sql
	 * @param condition
	 * @return
	 */
	public long sqlCount(StringBuffer sql, List<Object> condition) {
		Session session = HibernateUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql.toString());

		for (int i = 0; i < condition.size(); i++) {
			Object value = condition.get(i);
			if (value instanceof String) {
				sqlQuery.setString(i, value.toString());
			} else if (value instanceof Integer) {
				sqlQuery.setInteger(i, (Integer) value);
			} else if (value instanceof Long) {
				sqlQuery.setLong(i, (Long) value);
			} else if (value instanceof Boolean) {
				sqlQuery.setBoolean(i, (Boolean) value);
			} else if (value instanceof Date) {
				sqlQuery.setDate(i, (Date) value);
			} else if (value instanceof Calendar) {
				sqlQuery.setCalendar(i, (Calendar) value);
			} else {
				sqlQuery.setString(i, value.toString());
			}
		}

		BigInteger result = (BigInteger) sqlQuery.uniqueResult();
		return result.longValue();
	}

	protected class SQLTableInfo {
		String tableAlias;
		Class<?> pElementType;

		public SQLTableInfo(String tableAlias, Class<?> elementType) {
			this.tableAlias = tableAlias;
			this.pElementType = elementType;
		}
	}

	protected void endParameters(StringBuffer sb) {
		int sbLength = sb.length();
		if (sbLength > 0) {
			sb.delete(sbLength - " AND ".length(), sbLength);
			sb.insert(0, '(');
			sb.append(')');
		}
	}

	protected void addTableInfo(ArrayList<SQLTableInfo> tableInfo,
			String tableAlias, Class<?> elementType) {
		SQLTableInfo inFileInfo = new SQLTableInfo(tableAlias, elementType);
		tableInfo.add(inFileInfo);
	}

	/**
	 * Create criterions.
	 * 
	 * @param type
	 * @param propertyName
	 * @param value
	 * @return
	 */
	protected Criterion createCriterion(final Operator operator,
			String propertyName, Object... value) {
		if (Operator.IS_NULL == operator && value == null) {
			throw new IllegalStateException("PropertyName [" + propertyName
					+ "], OperatorType[" + operator
					+ "] the value can't be null.");
		}

		if (Operator.EQUALS == operator) {
			return Restrictions.eq(propertyName, value[0]);
		} else if (Operator.EQUALS_IGNORE_CASE == operator) {
			return Restrictions.eq(propertyName, value[0]);
		} else if (Operator.IS_NULL == operator) {
			return Restrictions.isNull(propertyName);
		} else if (Operator.GREATER_THAN == operator) {
			return Restrictions.gt(propertyName, value[0]);
		} else if (Operator.GREATER_THAN_OR_EQUALS == operator) {
			return Restrictions.ge(propertyName, value[0]);
		} else if (Operator.LESS_THAN == operator) {
			return Restrictions.lt(propertyName, value[0]);
		} else if (Operator.LESS_THAN_OR_EQUALS == operator) {
			return Restrictions.le(propertyName, value[0]);
		} else if (Operator.IN == operator) {
			return Restrictions.in(propertyName, value);
		} else if (Operator.BETWEEN == operator) {
			if (value.length < 2) {
				throw new IllegalStateException(
						"Two value required for SQL Between Operator.");
			}
			return Restrictions.between(propertyName, value[0], value[1]);
		} else if (Operator.EXISTS == operator) {
			// TODO:

		} else if (Operator.START_WITH == operator
				|| Operator.START_WITH_LEFT == operator) {
			return Restrictions.ilike(propertyName, value[0].toString(),
					MatchMode.START);
		} else if (Operator.START_WITH_IGNORE_CASE == operator) {
			return Restrictions.like(propertyName, value[0].toString(),
					MatchMode.START);
		} else if (Operator.START_WITH_RIGHT == operator) {
			return Restrictions.ilike(propertyName, value[0].toString(),
					MatchMode.END);
		} else if (Operator.CONTAINS_PARTIAL == operator) {
			return Restrictions.ilike(propertyName, value[0].toString(),
					MatchMode.ANYWHERE);
		} else if (Operator.CONTAINS_PARTIAL_IGNORE_CASE == operator) {
			return Restrictions.like(propertyName, value[0].toString(),
					MatchMode.ANYWHERE);
		} else if (Operator.CONTAINS_WORD == operator) {
			return Restrictions.ilike(propertyName, value[0].toString(),
					MatchMode.EXACT);
		} else if (Operator.CONTAINS_WORD_IGNORE_CASE == operator) {
			return Restrictions.like(propertyName, value[0].toString(),
					MatchMode.EXACT);
		}

		throw new IllegalStateException("Unsupported SQL OperatorType : "
				+ operator);
	}

}
