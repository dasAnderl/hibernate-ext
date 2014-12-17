package com.anderl.hibernate.ext.filters;

import org.hibernate.sql.JoinType;
import org.junit.Test;

import static com.anderl.hibernate.ext.filters.AliasUtils.*;
import static org.assertj.core.api.Assertions.*;

public class AliasUtilsTest {

    private static final String _NESTED = "nested";
    private static final String _NESTED1 = "nested1";
    private static final String _NESTED_NESTED1 = _NESTED+"."+_NESTED1;
    private static final JoinType _joinType = JoinType.LEFT_OUTER_JOIN;

    public enum Alias implements AliasUtils.Alias {

        NESTED(_NESTED, _joinType),
        NESTED_NESTED1(_NESTED_NESTED1, _joinType);
        private final String fieldPath;
        private final JoinType joinType;

        private Alias(String nested, JoinType jointype) {
            this.fieldPath = nested;
            this.joinType = jointype;
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

    @Test
    public void testAliasNested() {
        Alias alias = Alias.NESTED;
        assertThat(alias.getSubAliases())
                .hasSize(1);
        SubAlias subAlias = alias.getSubAliases().get(0);
        assertThat(subAlias.getName())
                .isEqualTo(_NESTED);
        assertThat(subAlias.getPath())
                .isEqualTo(_NESTED);
        assertThat(subAlias.getJoinType())
                .isEqualTo(_joinType);
    }

    @Test
    public void testAliasNestedNested() {
        Alias alias = Alias.NESTED_NESTED1;
        assertThat(alias.getSubAliases())
                .hasSize(2)
                .extracting("name")
                .containsExactly(_NESTED, _NESTED1);
        assertThat(alias.getSubAliases())
                .extracting("path")
                .containsExactly(_NESTED, _NESTED_NESTED1);
        assertThat(alias.getSubAliases())
                .hasSize(2)
                .extracting("joinType")
                .containsExactly(_joinType, _joinType);
    }

    @Test
    public void testFilterMapping() throws Exception {

        String property = "property";
        Alias alias = Alias.NESTED;
        FilterMapping filterMapping = FilterMapping.get(property, alias);
        assertThat(filterMapping.getFieldPath())
                .isEqualTo(alias.getFieldPath()+"."+property)
                .isEqualTo(_NESTED+"."+property);
    }


}