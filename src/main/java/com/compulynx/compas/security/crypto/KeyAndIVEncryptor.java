package com.compulynx.compas.security.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KeyAndIVEncryptor {

    public static void encryptKeyAndIV(PublicKey publicKey, String key, String iv, OutputStream outputStream) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        // Create a new instance of the Cipher using the RSA algorithm.
        Cipher cipher = Cipher.getInstance("RSA");

        // Initialize the cipher for encryption.
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the key and IV.
        byte[] encryptedKey = cipher.doFinal(key.getBytes());
        byte[] encryptedIV = cipher.doFinal(iv.getBytes());

        // Write the encrypted key and IV to the output stream.
        outputStream.write(encryptedKey);
        outputStream.write(encryptedIV);
    }
}