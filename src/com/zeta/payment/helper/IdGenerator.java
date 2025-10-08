package com.zeta.payment.helper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Logger;

/** Secure 5-character alphanumeric generator with prefix (e.g. TRAN-A1F4P). */
public class IdGenerator{
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public static String generateTransactionId(){
        StringBuilder sb = new StringBuilder("TRANS");
        for(int i=0; i<5; i++){
            sb.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }

        return sb.toString();
    }
}
