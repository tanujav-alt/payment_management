package com.zeta.payment.dao;

import com.zeta.payment.entity.User;
import com.zeta.payment.entity.enums.UserRole;
import com.zeta.payment.helper.DbConnector;

import java.sql.*;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public boolean addUser(User user) {
        String sql = "INSERT INTO users ( username, password, role) VALUES ( ?, ?, ?)";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //stmt.setString(1, user.getUserId());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());

            stmt.executeUpdate();
            logger.info("User added: " + user.getUsername());
            return true;

        } catch (SQLException e) {
            logger.severe("Error adding user: " + e.getMessage());
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            logger.severe("Error fetching user: " + e.getMessage());
        }
        return null;
    }
}
