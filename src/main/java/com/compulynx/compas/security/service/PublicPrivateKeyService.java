//package com.compulynx.compas.security.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.Key;
//import java.security.KeyFactory;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//@Component
//public class PublicPrivateKeyService {
//
//    public PublicPrivateKeyService() {}
//
//    public byte[] loadPrivateKey() throws IOException {
//        return readKeyFile("keys/id_rsa");
//    }
//    public byte[] loadPublicKey() throws IOException {
//        return readKeyFile("keys/id_rsa.pub");
//    }
//
//    public String encrypt(String plaintext) throws Exception {
//        System.out.println("String to encrypt::"+plaintext);
//        Key publicKey = loadPublicKeyFromBytes(loadPublicKey());
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
//        String s = Base64.getEncoder().encodeToString(encryptedBytes);
//       // decrypt(s);
//        return s;
//    }
//
//    public String decrypt(String ciphertext) throws Exception {
//
//        Key privateKey = loadPrivateKeyFromBytes(loadPrivateKey());
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
//        String s = new String(decryptedBytes);
//        System.out.println("Decrypted String "+s);
//        return s;
//    }
//
//    private Key loadPublicKeyFromBytes (byte[] publicKeyBytes) throws Exception {
//        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePublic(publicKeySpec);
//    }
//    private Key loadPrivateKeyFromBytes(byte[] privateKeyBytes) throws Exception {
//        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePrivate(privateKeySpec);
//    }
//    private byte[] readKeyFile(String filePath) throws IOException {
//        InputStream inputStream = new ClassPathResource(filePath).getInputStream();
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[4096]; // Choose an appropriate buffer size
//
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//            //byte[] bytes = outputStream.toByteArray();
//            // Process the byte array as needed
//            return outputStream.toByteArray();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//       return null;
//    }
//
//    public static void main(String[] args) throws Exception {
//        PublicPrivateKeyService publicPrivateKeyService = new PublicPrivateKeyService();
//        System.out.println("{}{}");
//        System.out.println("Encrypted String:::::::::"+publicPrivateKeyService.encrypt("Edward"));
//        //System.out.println(publicPrivateKeyService.loadPublicKey());
//    }
//}
package com.compulynx.compas.security.service;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class PublicPrivateKeyService {

    public PublicPrivateKeyService() {
    }
    public PublicKey loadPublicKey() throws Exception {
        try (InputStream inputStream = new ClassPathResource("keys/public_key.pem").getInputStream()) {
            byte[] publicKeyBytes = IOUtils.toByteArray(inputStream);
            System.out.println();
            PublicKey publicKeyFromBytes = getPublicKeyFromBytes(publicKeyBytes);
            System.out.println();
            return publicKeyFromBytes;
        }
    }
    public PrivateKey loadPrivateKey() throws IOException {
        try (InputStream inputStream = new ClassPathResource("keys/private_key.pem").getInputStream()) {
            byte[] privateKeyBytes = IOUtils.toByteArray(inputStream);
            return getPrivateKeyFromBytes(privateKeyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String plaintext) throws Exception {
        System.out.println("String to encrypt: " + plaintext);
        PublicKey publicKey = loadPublicKey();

        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        return encryptedString;
    }

    public String decrypt(String ciphertext) throws Exception {
        PrivateKey privateKey = loadPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Decrypted String: " + decryptedString);
        return decryptedString;
    }

    private PublicKey getPublicKeyFromBytes(byte[] publicKeyBytes) throws Exception {
        try{
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            System.out.println();
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            System.out.println();
            return publicKey;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    private PrivateKey getPrivateKeyFromBytes(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
       // RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePublic(privateKeySpec);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    private byte[] readKeyFile(String filePath) throws IOException {
        InputStream inputStream = new ClassPathResource(filePath).getInputStream();
        try {
            return IOUtils.toByteArray(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static void main(String[] args) throws Exception {
        com.compulynx.compas.security.service.PublicPrivateKeyService publicPrivateKeyService = new com.compulynx.compas.security.service.PublicPrivateKeyService();
//        PublicKey publicKey = publicPrivateKeyService.loadPublicKey();
//        System.out.println(publicKey);
        String encryptedString = publicPrivateKeyService.encrypt("Edward");
        System.out.println("Encrypted String: " + encryptedString);
        String decryptedString = publicPrivateKeyService.decrypt(encryptedString);
        System.out.println("Decrypted String: " + decryptedString);
    }
}
