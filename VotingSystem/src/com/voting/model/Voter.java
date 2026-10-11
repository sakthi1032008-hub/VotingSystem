package com.voting.model;

import com.voting.interfaces.Votable;

public class Voter extends Person implements Votable {
    private String city;
    private boolean hasVoted;

    public Voter(int id, String name, int age, String city, boolean hasVoted) {
        super(id, name, age);
        this.city = city;
        this.hasVoted = hasVoted;
    }

    public String getCity()      { return city; }
    public boolean hasVoted()    { return hasVoted; }

    @Override
    public boolean isEligible()  { return getAge() >= MIN_VOTING_AGE; }

    @Override
    public String getRole()      { return "Voter"; }

    @Override
    public void displayInfo() {
        System.out.printf("[%s] ID: %d | Name: %s | Age: %d | City: %s | Voted: %s%n",
                getRole(), getId(), getName(), getAge(), city, hasVoted ? "Yes" : "No");
    }
}
