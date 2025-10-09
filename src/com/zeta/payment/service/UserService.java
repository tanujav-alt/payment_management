
package com.zeta.payment.service;

import com.zeta.payment.dao.UserDAO;
import com.zeta.payment.entity.User;
import com.zeta.payment.entity.enums.UserRole;

import java.util.List;
import java.util.logging.Logger;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //To register a new user
        public boolean registerUser(String username, String password, UserRole role) {
            if (userDAO.getUserByUsername(username) != null) {
                logger.warning("User already exists: " + username);
                return false;
            }
            // Call DAO with individual fields
            return userDAO.addUser(username, password, role);
        }



    // To login an existing user
    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }


    // To get the UserRole
    public UserRole getUserRole(String username) {
        User user = userDAO.getUserByUsername(username);
        return user != null ? user.getRole() : null;
    }

    //For the admin to remove the existing user
    public boolean removeUser(String username) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            logger.warning("User not found: " + username);
            return false;
        }
        return userDAO.deleteUser(username);
    }

    // For the admin to view the users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
