package com.compulynx.compas.security.crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    public static SecretKey generateKey() throws NoSuchAlgorithmException {

        javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Set the key size, such as 128, 192, or 256
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static IvParameterSpec generateIV() {

        // Generate a random IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        return ivSpec;
    }
}