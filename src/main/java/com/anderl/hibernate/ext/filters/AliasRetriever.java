package com.anderl.hibernate.ext.filters;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ga2unte on 12/2/13.
 * <p/>
 * To forPaging a list of {@link org.hibernate.criterion.Criterion} for e.g. you searchcontroller:
 * <p/>
 * HibernateCriterionRetriever.getAllCriterionsFor(searchcontroller);
 * <p/>
 * To forPaging a list of {@link org.hibernate.criterion.Order} for e.g. you searchcontroller:
 * <p/>
 * HibernateCriterionRetriever.getAllOrdersFor(searchcontroller);
 */
public class AliasRetriever {

    public static List<AliasUtils.SubAlias> getDistinctAliases(SearchFilter searchFilter) {

        List<AliasUtils.SubAlias> aliasesNotNull = getAliasesForWrappers(searchFilter.getFilters());
////        aliasesNotNull = addAliasesForOrWrappers(searchFilter.getFilters(), aliasesNotNull);
//
        Order order = searchFilter.getOrder();
//        if (order != null && order.getFilterMapping().getAlias() != null) {
//            aliasesNotNull.addAll(order.getFilterMapping().getAlias().getSubAliases());
//        }

        return aliasesNotNull.stream().distinct().collect(Collectors.toList());
    }

    private static List<AliasUtils.SubAlias> getAliasesForWrappers(List<Filter> filters) {

        List<AliasUtils.SubAlias> subAliases = new ArrayList<>();
        if (filters == null) return subAliases;

        List<AliasUtils.FilterMapping> filterMappings = filters.stream().map(filter -> filter.getFilterMapping()).collect(Collectors.toList());

        for (AliasUtils.FilterMapping filterMapping : filterMappings) {
            if (filterMapping.getAlias() != null) {
                subAliases.addAll(filterMapping.getAlias().getSubAliases());
            }
        }
        return subAliases;
    }

//    private static List<AliasUtils.SubAlias> addAliasesForOrWrappers(List<OrFilter> orWrappers, List<AliasUtils.SubAlias> aliases) {
//
//        if (orWrappers == null) return new ArrayList<>();
//        List<List<Filter>> wrappersLists = orWrappers.stream().map(orWrapper -> orWrapper.getHibernateCriterionWrappers()).collect(Collectors.toList());
//
//        final List<Filter> filters = wrappersLists.stream().flatMap(innerList -> innerList.stream()).collect(Collectors.toList());
//
//        return getAliasesForWrappers(filters, aliases);
//    }
}
