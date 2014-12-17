package com.anderl.hibernate.ext.filters;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dasanderl on 12.12.14.
 */
class CriteriaBuilder {

    private static Logger log = LoggerFactory.getLogger(CriteriaBuilder.class);

    private CriteriaBuilder() {
    }

    public static Criteria forCounting(SearchFilter searchFilter, Session session) {
        Criteria criteria = session.createCriteria(searchFilter.getType());
        addAliases(searchFilter, criteria);
        addCountDistinctIdProjections(criteria);
        addCriterions(searchFilter, criteria);
        return criteria;
    }

    public static Criteria forPaging(SearchFilter searchFilter, Session session) {
        Criteria criteria = session.createCriteria(searchFilter.getType());
        addAliases(searchFilter, criteria);
        addCriterions(searchFilter, criteria);
        addDistinctIdAndOrderProjections(criteria, searchFilter);
        return criteria;
    }

    public static Criteria forIds(SearchFilter searchFilter, Session session, List<Object> entityIds) {
        Criteria criteria = session.createCriteria(searchFilter.getType());
        addAliasForOrder(searchFilter.getOrder(), criteria);
        criteria.add(Restrictions.in("id", entityIds));
        if (searchFilter.getOrder() != null) {
            criteria.addOrder(searchFilter.getOrder().getOrderCriterion());
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    private static void addAliases(SearchFilter searchFilter, Criteria criteria) {
        List<AliasUtils.SubAlias> aliasesForQuery = AliasRetriever.getDistinctAliases(searchFilter);
        aliasesForQuery.stream().forEach(subAlias -> criteria.createAlias(subAlias.getPath(), subAlias.getName(), subAlias.getJoinType()));
    }

    private static void addAliasForOrder(Order order, Criteria criteria) {
        if(order == null || order.getFilterMapping().getAlias() == null)return;
        order.getFilterMapping().getAlias().getSubAliases().forEach(subAlias -> criteria.createAlias(subAlias.getPath(), subAlias.getName(), subAlias.getJoinType()));
    }

    private static void addCriterions(SearchFilter searchFilter, Criteria criteria) {
        List<Criterion> criterions = searchFilter.getFilters().stream().filter(filter -> filter.isValid()).map(filter -> filter.getCriterion()).collect(Collectors.toList());
        criterions.forEach(criterion -> criteria.add(criterion));
    }

    /**
     * Adds distinct id projection to criteria.
     * Also adds projection for sorted column, which is needed to make ordering working.
     * BE VERY CAREFUL CHANGING THIS.
     *
     * @param criteria
     * @param searchFilter
     */
    private static void addDistinctIdAndOrderProjections(Criteria criteria, SearchFilter searchFilter) {
        ProjectionList projectionList = Projections.projectionList();
        projectionList
                .add(Projections.distinct
                        (Projections.property("id")));
        Order order = searchFilter.getOrder();
        if (order != null) {
            projectionList.add(Projections.property(order.getFilterMapping().getCriterionPath()));
            criteria.addOrder(searchFilter.getOrder().getOrderCriterion());
        }
        else log.info("no order given for this query");
        criteria.setProjection(projectionList);
    }

    /**
     * Adds rowcount and distinct id projection to criteria.
     * BE VERY CAREFUL CHANGING THIS.
     *
     * @param criteria
     * @return Criteria
     */
    private static Criteria addCountDistinctIdProjections(Criteria criteria) {
        return criteria.setProjection(Projections.rowCount())
                .setProjection(Projections.distinct
                        (Projections.countDistinct("id")));
    }
}
