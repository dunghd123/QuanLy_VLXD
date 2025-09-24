package com.example.quanly_vlxd.help;

import com.example.quanly_vlxd.enums.InvoiceTypeEnums;

import java.util.Random;

public class InvoiceHelper {
    public static String generateCode(InvoiceTypeEnums type) {
        Random random = new Random();
        String prefix = switch (type) {
            case INPUT -> "INP-";
            case OUTPUT -> "OUT-";
            default -> throw new IllegalArgumentException("Unknown code type: " + type);
        };

        int number = random.nextInt(1_000_000);
        String paddedNumber = String.format("%06d", number);
        return prefix + paddedNumber;
    }

}
