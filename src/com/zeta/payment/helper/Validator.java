package com.zeta.payment.helper;

import com.zeta.payment.entity.enums.*;
import java.util.Objects;

/** Basic input validation; throws IllegalArgumentException for invalid inputs. */
public class Validator {
    public static void requirePositiveAmount(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
    }

    public static void requireNotNull(Object o, String name) {
        if (Objects.isNull(o)) throw new IllegalArgumentException(name + " is required");
    }

    public static void requireValidStatus(String s) {
        PaymentStatus.valueOf(s.toUpperCase()); // will throw if invalid
    }
}
