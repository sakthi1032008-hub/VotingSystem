package com.voting.model;

public class Candidate extends Person implements Comparable<Candidate> {
    private String party;
    private int votes;

    public Candidate(int id, String name, int age, String party, int votes) {
        super(id, name, age);
        this.party = party;
        this.votes = votes;
    }

    public String getParty() { return party; }
    public int getVotes()    { return votes; }

    @Override
    public String getRole()  { return "Candidate"; }

    @Override
    public void displayInfo() {
        System.out.printf("[%s] ID: %d | Name: %s | Party: %s | Votes: %d%n",
                getRole(), getId(), getName(), party, votes);
    }

    /** Sort candidates by votes, highest first. */
    @Override
    public int compareTo(Candidate other) {
        return Integer.compare(other.votes, this.votes);
    }
}
