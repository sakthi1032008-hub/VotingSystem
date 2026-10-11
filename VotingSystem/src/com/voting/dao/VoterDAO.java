package com.voting.dao;

import com.voting.model.Voter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoterDAO {

    public void add(Connection con, Voter v) throws SQLException {
        String sql = "INSERT INTO voters (voter_id, name, age, city) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getId());
            ps.setString(2, v.getName());
            ps.setInt(3, v.getAge());
            ps.setString(4, v.getCity());
            ps.executeUpdate();
        }
    }

    public Voter findById(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM voters WHERE voter_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void markVoted(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE voters SET has_voted = TRUE WHERE voter_id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Voter> findAll(Connection con) throws SQLException {
        List<Voter> list = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM voters ORDER BY voter_id")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Voter map(ResultSet rs) throws SQLException {
        return new Voter(rs.getInt("voter_id"), rs.getString("name"),
                rs.getInt("age"), rs.getString("city"), rs.getBoolean("has_voted"));
    }
}
