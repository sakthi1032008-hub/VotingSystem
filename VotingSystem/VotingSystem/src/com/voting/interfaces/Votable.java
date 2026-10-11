package com.voting.interfaces;

/** Anything that can take part in voting must define eligibility. */
public interface Votable {
    int MIN_VOTING_AGE = 18;      // constants in interfaces are public static final
    boolean isEligible();
}
