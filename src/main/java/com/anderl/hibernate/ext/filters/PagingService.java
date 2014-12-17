package com.anderl.hibernate.ext.filters;


import com.anderl.hibernate.ext.helper.LogTimer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ga2unte on 12/20/13.
 */
public abstract class PagingService<T> {

    protected abstract Session getSession();

    /**
     * Getting the correct number of results when joining multiple tables and using sort orders,
     * is very complex in hibernate.
     * To return distinct accountsetups, we first forPaging the distinct accountsetup ids matching our
     * criteria. On that id list we return the accountsetups.
     * BE VERY CAREFUL WHEN CHANINGING THIS.
     *
     * @param searchFilter
     * @return
     */
    @Transactional(readOnly = true)
    public <T> List<T> page(SearchFilter searchFilter) {
        final int firstResultIndex = searchFilter.getPagingHelper().getIndex();
        final int maxResults = searchFilter.getPagingHelper().getPageSize();
        LogTimer logTimer = new LogTimer().enter("page start firstResultIndex: {} maxResults {} for {}", firstResultIndex, maxResults, searchFilter.getType().getSimpleName());
        //      First retrieve distinct list of ids
        Criteria criteria = CriteriaBuilder.forPaging(searchFilter, getSession());
        List result = criteria.setFirstResult(firstResultIndex).setMaxResults(maxResults).list();
        List<Object> entityIds = getIdsFromResultSet(searchFilter.getOrder() != null, result);
        if (CollectionUtils.isEmpty(entityIds)) {
            logTimer.exit("returning 0 entities");
            return new ArrayList<>();
        }
        //        Then return entities with ids in list
        criteria = CriteriaBuilder.forIds(searchFilter, getSession(), entityIds);
        List<T> entities = criteria.list();
        logTimer.exit("returning {} entities", entities.size());
        return entities;
    }

    @Transactional(readOnly = true)
    public <T> int count(SearchFilter searchFilter) {
        LogTimer logTimer = new LogTimer().enter("count start for entity {}", searchFilter.getType().getSimpleName());
        Criteria criteria = CriteriaBuilder.forCounting(searchFilter, getSession());
        Long count = (Long) criteria.uniqueResult();
        int intCount = count.intValue();
        logTimer.exit("search count is {}", count);
        return intCount;
    }

    private static List<Object> getIdsFromResultSet(boolean hasOrder, List result) {
        List<Object> ids = new ArrayList<Object>();
        //if we have an order, resultset is different, because of projection needed to sort.
        if (hasOrder) {
            for (Object resultEntry : result) {
                Object[] resultEntryArray = (Object[]) resultEntry;
                ids.add(resultEntryArray[0]);
            }

        }
        //if we dont have an order, resultset is list of ids, because we added no projection to be able to sort
        else {
            ids.addAll(result);
        }
        return ids;
    }

}

