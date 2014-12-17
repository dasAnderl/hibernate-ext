package com.anderl.hibernate.ext.filters;


import org.hibernate.sql.JoinType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ga2unte on 12/2/13.
 */
public class AliasUtils {

    public interface Alias {

        public String getFieldPath();
        public JoinType getJoinType();

//        calculated each time -> flaw
        // aliases with same name dont work (e.g. NESTED_NESTED)
        default List<SubAlias> getSubAliases() {
            List<SubAlias> subAliases = new ArrayList<>();
            String aliasPath = "";
            for (String s : getFieldPath().split("\\.")) {
                if (!StringUtils.isEmpty(aliasPath)) {
                    aliasPath = aliasPath + ".";
                }
                aliasPath = aliasPath + s;
                subAliases.add(new SubAlias(aliasPath, s, this.getJoinType()));
            }
            return subAliases;
        }
    }

    static final class SubAlias {

        private final String path;
        private final String name;
        private final JoinType joinType;

        public SubAlias(String path, String name, JoinType joinType) {
            this.path = path;
            this.name = name;
            this.joinType = joinType;
        }

        //we override equals and hashcode to forPaging easy access to distinct subaliases
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SubAlias)) return false;

            SubAlias subAlias = (SubAlias) o;

            if (joinType != subAlias.joinType) return false;
            if (!name.equals(subAlias.name)) return false;
            if (!path.equals(subAlias.path)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + joinType.hashCode();
            return result;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public JoinType getJoinType() {
            return joinType;
        }
    }

    public final static class FilterMapping {
        private final Alias alias;
        private final String property;

        public static FilterMapping get(String property, Alias alias) {
            return new FilterMapping(property, alias);
        }

        public static FilterMapping get(String property) {
            return new FilterMapping(property);
        }

        private FilterMapping(String property, Alias alias) {
            this.property = property;
            this.alias = alias;
        }

        private FilterMapping(String property) {
            this.property = property;
            alias = null;
        }

        public Alias getAlias() {
            return alias;
        }

        public String getCriterionPath() {
            if (alias == null) return property;
            String fieldPath = alias.getFieldPath();
            fieldPath = fieldPath.substring(fieldPath.lastIndexOf(".") + 1);
            return fieldPath + "." + property;
        }

        public String getFieldPath() {
            return alias == null ? property : alias.getFieldPath() + "." + property;
        }

    }
}
