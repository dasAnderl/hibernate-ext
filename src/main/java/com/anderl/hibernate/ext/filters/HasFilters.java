package com.anderl.hibernate.ext.filters;

import com.anderl.hibernate.ext.helper.ReflectionHelper;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by dasanderl on 11.12.14.
 */
interface HasFilters {

    static Logger log = org.slf4j.LoggerFactory.getLogger(HasFilters.class);

    default List<Filter> getFilters() {
        List<Filter> filters = ReflectionHelper.invokeGettersByReturnType(Filter.class, this);
        if(filters.isEmpty()) {
            log.error("no getter with type {} found on this({})", Filter.class, this);
            return null;
        }
        return filters;
    }
}
