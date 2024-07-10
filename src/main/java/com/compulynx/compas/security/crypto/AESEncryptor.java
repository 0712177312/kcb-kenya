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

public class AESEncryptor {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";//"AES";
    private static final int AES_BLOCK_SIZE = 16;

    public static String encrypt(String plainText, SecretKey secretKey, IvParameterSpec iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {

        // Get the plain text as a byte array.
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        // Encrypt the payload.
        byte[] encryptedPayloadBytes = cipher.doFinal(plainTextBytes);

        // Convert the encrypted payload bytes to a string.
        String encryptedPayload = Base64.getEncoder().encodeToString(encryptedPayloadBytes);

        return encryptedPayload;
    }

    public static String decrypt(String encryptedPayload, SecretKey secretKey, IvParameterSpec iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Get the encrypted payload as a byte array.
        byte[] encryptedPayloadBytes = Base64.getDecoder().decode(encryptedPayload);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // Initialize the cipher with the encryption key and IV.
        cipher.init(Cipher.DECRYPT_MODE, secretKey,iv);

        // Decrypt the payload.
        byte[] decryptedPayloadBytes = cipher.doFinal(encryptedPayloadBytes);

        // Convert the decrypted payload bytes to a string.
        String decryptedPayload = Base64.getEncoder().encodeToString(decryptedPayloadBytes);
        String decryptedString = new String(decryptedPayloadBytes, StandardCharsets.UTF_8);

        return decryptedString;
    }

}
