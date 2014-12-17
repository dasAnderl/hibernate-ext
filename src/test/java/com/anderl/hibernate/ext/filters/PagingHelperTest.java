package com.anderl.hibernate.ext.filters;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assertions.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.greaterThan;

public class PagingHelperTest {

    @Test
    public void testPagingHelper() throws Exception {

        PagingHelper pagingHelper = new PagingHelper();

        assertThat(pagingHelper.getIndex()).isGreaterThanOrEqualTo(0);
        assertThat(pagingHelper.getPageSize()).isGreaterThanOrEqualTo(0);

        try {
            pagingHelper.setIndex(-1);
            fail("setting negative index must result in exception");
        } catch (AssertionError assertionError) {}

        try {
            pagingHelper.setPageSize(-1);
            fail("setting negative pagesize must result in exception");
        } catch (AssertionError assertionError) {}

        try {
            new PagingHelper(-1, 1);
            fail("setting negative index must result in exception");
        } catch (AssertionError assertionError) {}
        try {
            new PagingHelper(1, -1);
            fail("setting negative pagesize must result in exception");
        } catch (AssertionError assertionError) {}
    }
}