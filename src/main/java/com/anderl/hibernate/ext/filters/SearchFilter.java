package com.anderl.hibernate.ext.filters;


import com.anderl.hibernate.ext.helper.ReflectionHelper;

import java.util.List;

/**
 * Created by dasanderl on 11.12.14.
 */
public interface SearchFilter<T> extends HasFilters, HasOrder, HasPagingHelper {

    default Class<T> getType() {
        return ReflectionHelper.getGenericInterfaceType(this.getClass(), 0);
    }
}
