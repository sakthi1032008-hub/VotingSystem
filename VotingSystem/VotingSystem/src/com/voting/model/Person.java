package com.voting.model;

/** Abstract parent class - Voter and Candidate inherit from it. */
public abstract class Person {
    private int id;
    private String name;
    private int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId()       { return id; }
    public String getName()  { return name; }
    public int getAge()      { return age; }

    // Polymorphic methods - each subclass provides its own version
    public abstract String getRole();
    public abstract void displayInfo();
}
