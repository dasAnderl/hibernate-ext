package com.anderl.hibernate.ext.filters;


/**
 * Created by ga2unte on 12/2/13.
 */
public final class OrderFactory {

    private OrderFactory() {
    }

    public static Order asc(AliasUtils.FilterMapping filterMapping) {
        return new OrderImpl(filterMapping, true);
    }

    public static Order desc(AliasUtils.FilterMapping filterMapping) {
        return new OrderImpl(filterMapping, false);
    }

    public static class OrderImpl implements Order {

        private boolean asc = true;
        private AliasUtils.FilterMapping filterMapping;

        private OrderImpl(AliasUtils.FilterMapping filterMapping, boolean asc) {
            this.filterMapping = filterMapping;
            this.asc = asc;
        }

        @Override
        public org.hibernate.criterion.Order getOrderCriterion() {
            if (asc) {
                return org.hibernate.criterion.Order.asc(filterMapping.getCriterionPath());
            }
            return org.hibernate.criterion.Order.desc(filterMapping.getCriterionPath());
        }

        @Override
        public void setFilterMapping(AliasUtils.FilterMapping filterMapping) {
            this.filterMapping = filterMapping;
        }

        @Override
        public boolean isAsc() {
            return asc;
        }

        @Override
        public void setAsc(boolean asc) {
            this.asc = asc;
        }

        @Override
        public AliasUtils.FilterMapping getFilterMapping() {
            return filterMapping;
        }
    }
}
