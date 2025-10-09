package com.zeta.payment.dao;

import com.zeta.payment.entity.User;
import com.zeta.payment.entity.enums.UserRole;
import com.zeta.payment.helper.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    //to validate the user against the username and password
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //to add new user
    public boolean addUser(String username, String password, UserRole role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.name());


            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get role by username
    public UserRole getRoleByUsername(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return UserRole.valueOf(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // or throw exception
    }


    public UserRole getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username=?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UserRole.valueOf(rs.getString("role"));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching user role: " + e.getMessage());
        }
        return null;
    }

    // Remove user
    public boolean removeUser(String username) {
        String sql = "DELETE FROM users WHERE username=?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.severe("Error removing user: " + e.getMessage());
            return false;
        }
    }

    // Fetches all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, role FROM users";
        try (Connection conn = DbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.severe("Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT username, password, role FROM users WHERE username = ?";

        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }

        } catch (SQLException e) {
            logger.severe("Error fetching user: " + e.getMessage());
        }

        return null; // user not found
    }

    // Delete User (new)
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.severe("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    // Optional: getUserByUsername
    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            return stmt.executeQuery().next();

        } catch (SQLException e) {
            logger.severe("Error checking user: " + e.getMessage());
        }
        return false;
    }
}
