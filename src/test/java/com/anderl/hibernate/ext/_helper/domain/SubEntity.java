package com.anderl.hibernate.ext._helper.domain;

import javax.persistence.ManyToOne;

/**
 * Created by dasanderl on 07.09.14.
 */
@javax.persistence.Entity
public class SubEntity extends _AbstractEntity {

    private String name;
    private int age;
    @ManyToOne
    private Entity entity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "SubEntity{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
