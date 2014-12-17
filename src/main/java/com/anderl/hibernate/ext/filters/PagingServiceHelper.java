package com.anderl.hibernate.ext.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ga2unte on 12/19/13.
 */
public class PagingServiceHelper {


    /**
     * Adds rowcount and distinct id projection to criteria.
     * BE VERY CAREFUL CHANGING THIS.
     *
     * @param criteria
     * @return
     */
    public static Criteria addCountDistinctIdProjections(Criteria criteria) {
        return criteria.setProjection(Projections.rowCount())
                .setProjection(Projections.distinct
                        (Projections.countDistinct("id")));
    }


}
