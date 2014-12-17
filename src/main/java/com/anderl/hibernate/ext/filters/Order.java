package com.anderl.hibernate.ext.filters;

import org.hibernate.criterion.Criterion;

/**
 * Created by dasanderl on 11.12.14.
 */
public interface Order {
    public org.hibernate.criterion.Order getOrderCriterion();
    public boolean isAsc();
    public void setAsc(boolean asc);
    public void setFilterMapping(AliasUtils.FilterMapping filterMapping);
    public AliasUtils.FilterMapping getFilterMapping();
}
