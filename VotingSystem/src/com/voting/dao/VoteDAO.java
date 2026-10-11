package com.voting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteDAO {
    public void record(Connection con, int voterId, int candidateId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)")) {
            ps.setInt(1, voterId);
            ps.setInt(2, candidateId);
            ps.executeUpdate();
        }
    }
}
