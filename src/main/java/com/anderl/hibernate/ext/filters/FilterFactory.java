package com.anderl.hibernate.ext.filters;

import org.hibernate.criterion.Criterion;

import java.util.ArrayList;
import java.util.List;

import static com.anderl.hibernate.ext.filters.AliasUtils.*;

/**
 * Created by dasanderl on 11.12.14.
 */
public final class FilterFactory {

    private FilterFactory() {
    }

    public static <T> Filter<T> getFilter(FilterMapping filterMapping, RestrictionsExt restriction, Class<T> valueType, T value) {
        FilterImpl<T> filter = new FilterImpl<>(filterMapping, restriction, valueType);
        filter.setValue(value);
        return filter;
    }

    public static <T> Filter<T> getFilter(FilterMapping filterMapping, RestrictionsExt restriction, Class<T> valueType) {
        return new FilterImpl<>(filterMapping, restriction, valueType);
    }

    public static class FilterImpl<T> implements Filter<T> {

        private final FilterMapping filterMapping;
        private final RestrictionsExt restriction;
        private final Class<T> valueType;
        private T value;
        private boolean enabled;

        private FilterImpl(FilterMapping filterMapping, RestrictionsExt restriction, Class<T> valueType) {
            if(filterMapping == null || restriction == null || valueType == null)
                throw new AssertionError(String.format("none of the following may be null: filterMapping=%s rstriction=%s valueType=%s",
                filterMapping, restriction, valueType));

            this.filterMapping = filterMapping;
            this.restriction = restriction;
            this.valueType = valueType;
        }

        @Override
        public Criterion getCriterion() {
            return restriction.get(filterMapping.getCriterionPath(), getValue());
        }

        @Override
        public boolean isValid() {
            return restriction.isNullValueAllowed() ? true : value != null;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            if(value != null && value.getClass() != valueType)
                throw new AssertionError(String.format("%s is not of type %s", value, valueType));
            this.value = value;
        }

        public FilterMapping getFilterMapping() {
            return filterMapping;
        }

    }


}
