package com.anderl.hibernate.ext.filters;

import com.anderl.hibernate.ext.helper.ReflectionHelper;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by dasanderl on 12.12.14.
 */
public interface HasFilterMapping {

    static Logger log = org.slf4j.LoggerFactory.getLogger(HasFilterMapping.class);

    default AliasUtils.FilterMapping getFilterMapping() {
        List<AliasUtils.FilterMapping> filterMappings = ReflectionHelper.invokeGettersByReturnType(AliasUtils.FilterMapping.class, this);
        if(filterMappings.isEmpty()) {
            log.error("no getter with type {} found on this({})", AliasUtils.FilterMapping.class, this);
            return null;
        }
        return filterMappings.get(0);
    }
}
