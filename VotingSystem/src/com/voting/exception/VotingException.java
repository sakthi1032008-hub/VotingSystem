package com.voting.exception;

/** Base checked exception for the application. */
public class VotingException extends Exception {
    public VotingException(String message) { super(message); }
    public VotingException(String message, Throwable cause) { super(message, cause); }
}
