package com.voting.exception;

public class DuplicateVoteException extends VotingException {
    public DuplicateVoteException(String message) { super(message); }
}
