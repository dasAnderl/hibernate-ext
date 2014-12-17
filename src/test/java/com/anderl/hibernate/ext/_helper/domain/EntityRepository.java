package com.anderl.hibernate.ext._helper.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by dasanderl on 07.09.14.
 */
public interface EntityRepository extends CrudRepository<Entity, Long> {

    List<Entity> findByName(String name);

    List<Entity> findByAge(int age);

    List<Entity> findByNameAndAge(String name, int age);

    List<Entity> findByNameOrAge(String name, int age);


}
