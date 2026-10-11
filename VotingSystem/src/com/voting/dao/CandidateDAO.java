package com.voting.dao;

import com.voting.model.Candidate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {

    public void add(Connection con, Candidate c) throws SQLException {
        String sql = "INSERT INTO candidates (candidate_id, name, age, party) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getAge());
            ps.setString(4, c.getParty());
            ps.executeUpdate();
        }
    }

    public Candidate findById(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM candidates WHERE candidate_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void incrementVotes(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE candidates SET votes = votes + 1 WHERE candidate_id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Candidate> findAll(Connection con) throws SQLException {
        List<Candidate> list = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM candidates")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Candidate map(ResultSet rs) throws SQLException {
        return new Candidate(rs.getInt("candidate_id"), rs.getString("name"),
                rs.getInt("age"), rs.getString("party"), rs.getInt("votes"));
    }
}
