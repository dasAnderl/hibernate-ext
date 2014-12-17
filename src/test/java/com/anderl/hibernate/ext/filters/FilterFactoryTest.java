package com.anderl.hibernate.ext.filters;

import org.hibernate.criterion.SimpleExpression;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FilterFactoryTest {

    private static String property = "property";
    private static String filterValue = "s";
    private static Class valueType = String.class;
    private static Integer nestedFilterValue = 1;
    private static Class nestedValueType = Integer.class;

    private Filter<String> filter = FilterFactory.getFilter(AliasUtils.FilterMapping.get(property), RestrictionsExt.equal, valueType, filterValue);
//    private Filter<String> nestedFilter = FilterFactory.getFilter(AliasUtils.FilterMapping.forPaging("nested", TestAlias.Alias.NESTED), nestedValueType, nestedFilterValue);

    @Test
    public void testFilterValue() throws Exception {

        assertNotNull(filter);
        assertThat(filter.getValue())
                .isNotNull()
                .isEqualTo(filterValue);

        String newValue = filterValue + filterValue;
        filter.setValue(newValue);

        assertThat(filter.getValue())
                .isNotNull()
                .isEqualTo(newValue);
    }

    @Test
    public void testFilterMapping() throws Exception {

        assertThat(filter.getFilterMapping())
                .isNotNull()
                .isInstanceOf(AliasUtils.FilterMapping.class);
    }

    @Test
    public void testFilterWithoutValue() throws Exception {

        assertThat(FilterFactory.getFilter(AliasUtils.FilterMapping.get("sth"), RestrictionsExt.equal, String.class).getValue())
                .isNull();
    }


    @Test
    public void testFilterCriterion() throws Exception {

        assertNotNull(filter);
        SimpleExpression criterion = (SimpleExpression) filter.getCriterion();
        assertThat(criterion)
                .isNotNull();
        assertThat(criterion.getValue())
                .isEqualTo(filterValue);
        assertThat(criterion.getPropertyName())
                .isEqualTo(property);
    }

    @Test(expected = AssertionError.class)
    public void testIllegalSetValue() throws Exception {
        Filter untypedFilter = filter;
        untypedFilter.setValue(1);
    }

    @Test(expected = AssertionError.class)
    public void testIllegalGet1() throws Exception {
        FilterFactory.getFilter(null, RestrictionsExt.equal, String.class, "");
    }

    @Test(expected = AssertionError.class)
    public void testIllegalGet2() throws Exception {
        FilterFactory.getFilter(AliasUtils.FilterMapping.get("sth"), null, String.class, "");
    }

    @Test(expected = AssertionError.class)
    public void testIllegalGet3() throws Exception {
        FilterFactory.getFilter(AliasUtils.FilterMapping.get("sth"), RestrictionsExt.equal, null, "");
    }


}