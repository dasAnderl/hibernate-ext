package com.anderl.hibernate.ext.filters;

import com.anderl.hibernate.ext._helper.domain.Entity;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SearchFilterTest {

    public static class TestSearchFilter implements SearchFilter<Entity> {

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

    private static String filterValue = "s";
    private static Class valueType = String.class;
    private static Integer nestedFilterValue = 1;
    private static Class nestedValueType = Integer.class;

    private SearchFilter<Entity> searchFilter = new TestSearchFilter();

    @Test
    public void tesSearchFilter() {

        Assertions.assertThat(searchFilter.getFilters()).isNotNull();
        Assertions.assertThat(searchFilter.getFilters()).hasSize(2);
        Assertions.assertThat(searchFilter.getPagingHelper()).isNotNull();
        Assertions.assertThat(searchFilter.getType()).isEqualTo(Entity.class);
    }


}