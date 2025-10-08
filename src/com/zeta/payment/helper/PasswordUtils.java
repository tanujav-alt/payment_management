package com.z.payment.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.logging.Logger;

/** Simple SHA-256 hashing helper. In production add per-user salt & stronger KDF. */
public class PasswordUtils {
    private static final Logger logger = Logger.getLogger(PasswordUtils.class.getName());

    public static String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(dig);
        } catch (Exception e) {
            logger.severe("Password hash failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
