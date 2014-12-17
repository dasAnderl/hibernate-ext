package com.anderl.hibernate.ext.filters;

import org.hibernate.criterion.Criterion;

/**
 * Created by dasanderl on 11.12.14.
 */
public interface Filter<T> extends HasFilterMapping{
    public T getValue();
    public void setValue(T value);
    public Criterion getCriterion();
    public boolean isValid();
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
}
