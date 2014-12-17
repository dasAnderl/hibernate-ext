package com.anderl.hibernate.ext.filters;

import com.anderl.hibernate.ext._helper.domain.Entity;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AliasRetrieverTest {

    public static class TestSearchFilter implements SearchFilter<Entity> {

        private Filter<String> filter1 = FilterFactory.getFilter(AliasUtils.FilterMapping.get("sth", AliasUtilsTest.Alias.NESTED), RestrictionsExt.equal, String.class);
        private Filter<String> filter2 = FilterFactory.getFilter(AliasUtils.FilterMapping.get("does", AliasUtilsTest.Alias.NESTED), RestrictionsExt.equal, String.class);
        private Filter<String> filter3 = FilterFactory.getFilter(AliasUtils.FilterMapping.get("not", AliasUtilsTest.Alias.NESTED_NESTED1), RestrictionsExt.equal, String.class);

        private PagingHelper pagingHelper = new PagingHelper();

        @Override
        public PagingHelper getPagingHelper() {
            return pagingHelper;
        }

        public Filter<String> getFilter1() {
            return filter1;
        }

        public Filter<String> getFilter2() {
            return filter2;
        }

        public Filter<String> getFilter3() {
            return filter3;
        }

        public void setFilter1(Filter<String> filter1) {
            this.filter1 = filter1;
        }

        public void setFilter2(Filter<String> filter2) {
            this.filter2 = filter2;
        }

        public void setFilter3(Filter<String> filter3) {
            this.filter3 = filter3;
        }

        @Override
        public Order getOrder() {
            return null;
        }
    }

    @Test
    public void testGetDistinctAliases() throws Exception {

        TestSearchFilter searchFilter = new TestSearchFilter();

        Assertions.assertThat(AliasRetriever.getDistinctAliases(searchFilter))
                .containsExactly(
                AliasUtilsTest.Alias.NESTED_NESTED1.getSubAliases().get(0),
                AliasUtilsTest.Alias.NESTED_NESTED1.getSubAliases().get(1)
        );

        searchFilter.setFilter3(null);

        Assertions.assertThat(AliasRetriever.getDistinctAliases(searchFilter))
                .containsExactly(
                        AliasUtilsTest.Alias.NESTED.getSubAliases().get(0)
                );

        searchFilter.setFilter1(null);
        searchFilter.setFilter2(null);

        Assertions.assertThat(AliasRetriever.getDistinctAliases(searchFilter))
                .hasSize(0);
    }
}