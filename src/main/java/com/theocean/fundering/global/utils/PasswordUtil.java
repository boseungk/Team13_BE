package com.theocean.fundering.global.utils;

import java.util.Random;

public enum PasswordUtil {
    ;

    public static String generateRandomPassword() {
        int index = 0;
        final char[] charSet = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };

        final StringBuffer password = new StringBuffer();
        final Random random = new Random();

        for (int i = 0; 8 > i; i++) {
            final double rd = random.nextDouble();
            index = (int) (charSet.length * rd);

            password.append(charSet[index]);
        }
        return password.toString();
    }
}
