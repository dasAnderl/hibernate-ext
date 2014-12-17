package com.anderl.hibernate.ext.integration;

import com.anderl.hibernate.ext._helper.Application;
import com.anderl.hibernate.ext._helper.TestPagingService;
import com.anderl.hibernate.ext._helper.domain.DomainProvider;
import com.anderl.hibernate.ext._helper.domain.Entity;
import com.anderl.hibernate.ext._helper.domain.EntityRepository;
import com.anderl.hibernate.ext.filters.*;
import org.assertj.core.api.Assertions;
import org.hibernate.sql.JoinType;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


/**
 *  This serves as integration test. must be in a package which only exists in test src, because this is an api
 *  and we want to make sure that the api is usable outside the package.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class PagingServiceTest {

    @Autowired
    private EntityRepository entityRepository;
    @Autowired
    private TestPagingService<Entity> pagingService;

    public enum Alias implements AliasUtils.Alias {

        SUBENTITIES("subEntities", JoinType.LEFT_OUTER_JOIN);

        private final String fieldPath;
        private final JoinType joinType;

        Alias(String fieldPath, JoinType joinType) {

            this.fieldPath = fieldPath;
            this.joinType = joinType;
        }

        @Override
        public String getFieldPath() {
            return fieldPath;
        }

        @Override
        public JoinType getJoinType() {
            return joinType;
        }
    }

    public static class EntityFilter implements SearchFilter<Entity> {

        private Filter nameFilter = FilterFactory.getFilter(AliasUtils.FilterMapping.get("name"), RestrictionsExt.like, nameValueType, nameValue);
        private Filter subAgeFilter = FilterFactory.getFilter(AliasUtils.FilterMapping.get("age", Alias.SUBENTITIES), RestrictionsExt.greaterOrEqual, ageValueType, ageValue);
        private PagingHelper pagingHelper = new PagingHelper();
        private Order order = OrderFactory.asc(nameFilter.getFilterMapping());

        public Filter getNameFilter() {
            return nameFilter;
        }

        public Filter getSubAgeFilter() {
            return subAgeFilter;
        }

        @Override
        public Order getOrder() {
            return order;
        }

        @Override
        public PagingHelper getPagingHelper() {
            return pagingHelper;
        }
    }

    private static String nameValue = "name";
    private static Class nameValueType = String.class;
    private static Integer ageValue = -100;
    private static Class ageValueType = Integer.class;

    private final List<Entity> entities = DomainProvider.getEntities();
    private EntityFilter entityFilter = new EntityFilter();

    @Before
    public void before() {
        entityRepository.save(entities);
        assertThat(entityRepository.findAll())
                .hasSize(entities.size());
    }

    @Test
    public void testAPageAndCount() throws Exception {

        List<Object> entities = pagingService.page(entityFilter);
        assertThat(entities).isNotNull()
                .hasSize(entities.size());
        assertThat(entities.size()).isEqualTo(pagingService.count(entityFilter));
    }

    @Test
    public void testBPageAndCount() throws Exception {

        String name = entities.get(0).getName();
        entityFilter.getNameFilter().setValue(name);
        List<Object> entities = pagingService.page(entityFilter);
        assertThat(entities).isNotNull()
                .hasSize(1);
        assertThat(entities)
                .extracting("name")
                .containsExactly(name);
        assertThat(1).isEqualTo(pagingService.count(entityFilter));
    }

    @Test
    public void testCPageAndCount() throws Exception {

        entities.get(0).getSubEntities().get(0).setAge(Integer.MAX_VALUE);
        entityRepository.save(entities.get(0));

        entityFilter.getNameFilter().setValue(nameValue);
        entityFilter.getSubAgeFilter().setValue(Integer.MAX_VALUE);

        List<Entity> entities = pagingService.page(entityFilter);
        entities.get(0).getSubEntities().size();
        assertThat(entities).isNotNull()
                .hasSize(1);
        List<Integer> subAges = entities.get(0).getSubEntities().stream().map(entity -> entity.getAge()).collect(Collectors.toList());
        assertThat(subAges)
                .contains(Integer.MAX_VALUE);
        assertThat(entities.get(0).getId())
                .isEqualTo(entities.get(0).getId());
        assertThat(1).isEqualTo(pagingService.count(entityFilter));
    }

    @Test
    public void testDPageAndCount() throws Exception {

        entityFilter.getNameFilter().setValue("nonExistingName");
        List<Object> entities = pagingService.page(entityFilter);
        assertThat(entities).isNotNull()
                .hasSize(0);
        assertThat(0).isEqualTo(pagingService.count(entityFilter));
    }

    @Test
    public void testFPageAndCount() throws Exception {

        entityFilter.getNameFilter().setValue(null);
        entityFilter.getSubAgeFilter().setValue(null);
        List<Object> entities = pagingService.page(entityFilter);
        assertThat(entities).isNotNull()
                .hasSize(entities.size());
        assertThat(entities.size()).isEqualTo(pagingService.count(entityFilter));
    }

    @Test
    public void testGOrFilters() throws Exception {

        //todo implement me
    }

    @Test
    public void testHPaging() {

        entityFilter.getPagingHelper().setPageSize(1);
        List<Entity> entities1 = pagingService.page(entityFilter);
        assertThat(entities1)
                .hasSize(1);

        entityFilter.getPagingHelper().setIndex(1);
        List<Entity> entities2 = pagingService.page(entityFilter);
        assertThat(entities1.get(0).getId())
                .isNotEqualTo(entities2.get(0).getId());

        entityFilter.getPagingHelper().setPageSize(2);
        assertThat(pagingService.page(entityFilter))
                .hasSize(2);

    }

    @Test
    public void testIOrder() {
        entityFilter.getOrder().setAsc(true);
        entityFilter.getOrder().setFilterMapping(AliasUtils.FilterMapping.get("id"));
        List<Entity> entities = pagingService.page(entityFilter);
        assertThat(entities.size())
                .isGreaterThan(1)
                .isLessThan(10); //if we have more than ten the reversal of the id string wont work
        List<Long> ascIds = entities.stream().map(entity -> entity.getId()).collect(Collectors.toList());
        entityFilter.getOrder().setAsc(false);
        entities = pagingService.page(entityFilter);
        List<Long> descIds = entities.stream().map(entity -> entity.getId()).collect(Collectors.toList());
        assertThat(descIds)
                .isNotEqualTo(ascIds);
        Collections.sort(ascIds, Collections.reverseOrder());
        assertThat(descIds)
                .isEqualTo(ascIds);
    }

    @Test
    public void testJOrderNested() {
        entityFilter.getOrder().setAsc(true);
        entityFilter.getOrder().setFilterMapping(AliasUtils.FilterMapping.get("id", Alias.SUBENTITIES));
        List<Entity> entities = pagingService.page(entityFilter);
        assertThat(entities.size())
                .isGreaterThan(1);
        List<Long> ascIds = entities.stream().map(entity -> entity.getId()).collect(Collectors.toList());
        entityFilter.getOrder().setAsc(false);
        entities = pagingService.page(entityFilter);
        List<Long> descIds = entities.stream().map(entity -> entity.getId()).collect(Collectors.toList());
        assertThat(descIds)
                .isNotEqualTo(ascIds);
        Collections.sort(ascIds, Collections.reverseOrder());
        assertThat(descIds)
                .isEqualTo(ascIds);
    }

    @Test
    public void testKNullValues() {
        entityFilter.getNameFilter().setValue(null);
        entityFilter.getSubAgeFilter().setValue(null);
        List<Entity> entities = pagingService.page(entityFilter);
        assertThat(entities)
                .hasSize(3);
    }

    @After
    public void after() {
        entityRepository.deleteAll();
    }
}