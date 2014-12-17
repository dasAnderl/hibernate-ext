package com.anderl.hibernate.ext._helper.domain

/**
 * Created by dasanderl on 09.12.14.
 */
class DomainProvider {

    private DomainProvider() {
    }

    static String name1 = "name1"
    static String name2 = "name2"
    static String name3 = "name3"
    static int age1 = 1
    static int age2 = 2
    static int age3 = 3

    static Entity e1 = new Entity(name: name1, age: age1)
    static Entity e2 = new Entity(name: name2, age: age2)
    static Entity e3 = new Entity(name: name3, age: age3)

    static {
        e1.subEntities.add(new SubEntity(name: name1, age: age1, entity: e1))
        e1.subEntities.add(new SubEntity(name: name1, age: age1, entity: e1))
        e1.subEntities.add(new SubEntity(name: "a different name", age: age1, entity: e1))

        e2.subEntities.add(new SubEntity(name: name2, age: age2, entity: e2))

        e3.subEntities.add(new SubEntity(name: name3, age: age3, entity: e3))
    }

    def static List<Entity> getEntities() {
        return [e1, e2, e3]
    }
}
