package com.anderl.hibernate.ext._helper;

import com.anderl.hibernate.ext._helper.domain.Entity;
import com.anderl.hibernate.ext.filters.PagingService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * Created by dasanderl on 12.12.14.
 */
@Service
public class TestPagingService<Entity> extends PagingService<Entity> {

    @Autowired
    private EntityManager entityManager;

    @Override
    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
