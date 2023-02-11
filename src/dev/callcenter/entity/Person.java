package dev.callcenter.entity;

import java.util.StringJoiner;

/**
 * Абстрактный класс-сущность для описания человека
 * @version 1.0
 */
public abstract class Person {
    private long id;    // идентификатор
    private String name;    // имя

    /**
     * Конструктор для создания нового объекта типа Person
     * @param id - идентификатор
     * @param name - имя
     */
    public Person(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        return (this.id == person.id) && ((this.name == person.name) || ((name != null) && name.equals(person.name)));
    }

    @Override
    public int hashCode() {
        final int CODE = (int) (31 * 1 + id + (name == null ? 0 : name.hashCode()));
        return CODE;
    }

    @Override
    public String toString() {
        return new StringJoiner(",", this.getClass().getCanonicalName() + "{", "}")
                .add("id=" + id).add("name=" + name).toString();
    }
}
