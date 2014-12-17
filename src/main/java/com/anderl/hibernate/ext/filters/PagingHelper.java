package com.anderl.hibernate.ext.filters;

/**
 * Created by dasanderl on 11.12.14.
 */
public class PagingHelper {

    private int index = 0;
    private int pageSize = 20;

    public PagingHelper(int index, int pageSize) {
        setIndex(index);
        setPageSize(pageSize);
    }

    public PagingHelper() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if(index<0)throw new AssertionError("index must be > 0");
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize<0)throw new AssertionError("pageSize must be > 0");
        this.pageSize = pageSize;
    }
}
