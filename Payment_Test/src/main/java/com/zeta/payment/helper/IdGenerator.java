package com.zeta.payment.helper;

import java.security.SecureRandom;

/** Generates fixed-length transaction IDs (e.g., 10 characters: TRANS + 5 random chars) */
public class IdGenerator {
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final String PREFIX = "TRANS";   // 5 chars
    private static final int TOTAL_LENGTH = 10;     // total ID length

    public static String generateTransactionId() {
        int randomLength = TOTAL_LENGTH - PREFIX.length(); // number of random chars needed
        StringBuilder sb = new StringBuilder(PREFIX);

        for (int i = 0; i < randomLength; i++) {
            sb.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }

        return sb.toString();
    }
}