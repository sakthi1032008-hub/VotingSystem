package com.voting.service;

import com.voting.dao.CandidateDAO;
import com.voting.dao.VoteDAO;
import com.voting.dao.VoterDAO;
import com.voting.db.DBConnection;
import com.voting.exception.*;
import com.voting.model.Candidate;
import com.voting.model.Voter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class VotingService {
    private final VoterDAO voterDAO = new VoterDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    public void registerVoter(Voter voter) throws VotingException {
        if (!voter.isEligible())
            throw new InvalidVoterException("Voter must be at least " + Voter.MIN_VOTING_AGE + " years old.");
        try (Connection con = DBConnection.getConnection()) {
            voterDAO.add(con, voter);
        } catch (SQLException e) {
            throw new VotingException("Could not register voter (duplicate ID?): " + e.getMessage(), e);
        }
    }

    public void addCandidate(Candidate c) throws VotingException {
        try (Connection con = DBConnection.getConnection()) {
            candidateDAO.add(con, c);
        } catch (SQLException e) {
            throw new VotingException("Could not add candidate (duplicate ID?): " + e.getMessage(), e);
        }
    }

    /** Casts a vote inside a single DB transaction (all-or-nothing). */
    public void castVote(int voterId, int candidateId) throws VotingException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Voter voter = voterDAO.findById(con, voterId);
                if (voter == null)
                    throw new InvalidVoterException("No voter found with ID " + voterId);
                if (!voter.isEligible())
                    throw new InvalidVoterException("Voter is not eligible to vote.");
                if (voter.hasVoted())
                    throw new DuplicateVoteException(voter.getName() + " has already voted!");

                if (candidateDAO.findById(con, candidateId) == null)
                    throw new CandidateNotFoundException("No candidate found with ID " + candidateId);

                voteDAO.record(con, voterId, candidateId);
                candidateDAO.incrementVotes(con, candidateId);
                voterDAO.markVoted(con, voterId);
                con.commit();
            } catch (VotingException e) {
                con.rollback();
                throw e;
            } catch (SQLException e) {
                con.rollback();
                throw new VotingException("Vote failed: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new VotingException("Database connection error: " + e.getMessage(), e);
        }
    }

    public List<Voter> getAllVoters() throws VotingException {
        try (Connection con = DBConnection.getConnection()) {
            return voterDAO.findAll(con);
        } catch (SQLException e) {
            throw new VotingException("Could not fetch voters: " + e.getMessage(), e);
        }
    }

    /** Candidates sorted by votes (uses Comparable + Collections.sort). */
    public List<Candidate> getResults() throws VotingException {
        try (Connection con = DBConnection.getConnection()) {
            List<Candidate> list = candidateDAO.findAll(con);
            Collections.sort(list);
            return list;
        } catch (SQLException e) {
            throw new VotingException("Could not fetch results: " + e.getMessage(), e);
        }
    }

    /** Party-wise totals using a Map. */
    public Map<String, Integer> getPartyTotals() throws VotingException {
        Map<String, Integer> totals = new TreeMap<>();
        for (Candidate c : getResults())
            totals.merge(c.getParty(), c.getVotes(), Integer::sum);
        return totals;
    }

    public Optional<Candidate> getWinner() throws VotingException {
        List<Candidate> results = getResults();
        if (results.isEmpty() || results.get(0).getVotes() == 0) return Optional.empty();
        return Optional.of(results.get(0));
    }
}
