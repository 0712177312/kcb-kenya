package com.compulynx.compas.security.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class AESDecryptor {
    private static final String AES_ALGORITHM = "AES";
    private static final int AES_BLOCK_SIZE = 16;

    public static String decrypt(String encryptedPayload, String encryptionKey, String iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Get the encrypted payload as a byte array.
        byte[] encryptedPayloadBytes = Base64.getDecoder().decode(encryptedPayload);

        // Get the encryption key and IV as byte arrays.
        byte[] encryptionKeyBytes = Base64.getDecoder().decode(encryptionKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        // Create a new instance of the AES cipher using the encryption algorithm.
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // Initialize the cipher with the encryption key and IV.
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(encryptionKeyBytes), new IvParameterSpec(ivBytes));

        // Decrypt the payload.
        byte[] decryptedPayloadBytes = cipher.doFinal(encryptedPayloadBytes);

        // Convert the decrypted payload bytes to a string.
        String decryptedPayload = new String(decryptedPayloadBytes, StandardCharsets.UTF_8);

        return decryptedPayload;
    }

    private static SecretKey getSecretKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {

        javax.crypto.KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Set the key size, such as 128, 192, or 256
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }
}
