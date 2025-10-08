package com.zeta.payment.service;

import com.zeta.payment.dao.UserDAO;
import com.zeta.payment.entity.User;
import com.zeta.payment.entity.enums.UserRole;
import com.zeta.payment.helper.IdGenerator;
import com.zeta.payment.helper.LoggerHelper;

import java.util.logging.Logger;

public class UserService {
    private static final Logger logger = LoggerHelper.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerUser(String username, String password, UserRole role) {
        String userId = IdGenerator.generateTransactionId();
        User user = new User(userId, username, password, role); // ✅ fixed

        if (userDAO.addUser(user)) {
            logger.info("User registered successfully: " + username);
        } else {
            logger.warning("User registration failed for: " + username);
        }
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            logger.info("Login successful for user: " + username);
            return user;
        } else {
            logger.warning("Invalid credentials for user: " + username);
            return null;
        }
    }
}
