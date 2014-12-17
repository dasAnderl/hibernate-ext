package com.anderl.hibernate.ext._helper;

import com.anderl.hibernate.ext._helper.domain.Entity;
import com.anderl.hibernate.ext.filters.*;

/**
 * Created by dasanderl on 12.12.14.
 */
public class TestSearchFilter implements SearchFilter<Entity> {


    private static String filterValue = "s";
    private static Class valueType = String.class;
    private static Integer nestedFilterValue = 1;
    private static Class nestedValueType = Integer.class;

    private Filter<String> filter = FilterFactory.getFilter(AliasUtils.FilterMapping.get("property"), RestrictionsExt.equal, valueType, filterValue);
    private Filter<String> nestedFilter = FilterFactory.getFilter(AliasUtils.FilterMapping.get("nested", AliasUtilsTest.Alias.NESTED), RestrictionsExt.equal, nestedValueType, nestedFilterValue);
    private PagingHelper pagingHelper = new PagingHelper();

    public Filter<String> getFilter() {
        return filter;
    }

    public Filter<String> getNestedFilter() {
        return nestedFilter;
    }

    @Override
    public PagingHelper getPagingHelper() {
        return pagingHelper;
    }

    @Override
    public Order getOrder() {
        return null;
    }
}
