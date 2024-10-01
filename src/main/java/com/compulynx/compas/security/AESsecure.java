package com.compulynx.compas.security;

import com.compulynx.compas.security.model.EncryptionPayloadResp;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


@Component
public class AESsecure {
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	public static SecretKey generateRandomKey() throws NoSuchAlgorithmException {
		javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
		keyGenerator.init(256); // Set the key size, such as 128, 192, or 256
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}
	public static String encrypt(String strToEncrypt) {
		try {
			SecretKey secretKey = null;
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String encString = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			return encString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encryptTemp(String strToEncrypt,SecretKey secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String encString = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			return encString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String strToDecrypt) {
		try {
			SecretKey secretKey = null;
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String s = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String decryptTemp(String strToDecrypt,SecretKey secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String s = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}
	public static String encryptWithPublic(byte[] secretKey) throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		byte[] ciphertext = null;
		Resource resource = resourceLoader.getResource("classpath:keys/public_key.pem");
		File file = resource.getFile();
		try (PemReader pemReader = new PemReader(new FileReader(file.getAbsolutePath()))) {
			PemObject pemObject = pemReader.readPemObject();
			byte[] publicKeyData = pemObject.getContent();
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyData));
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			ciphertext = cipher.doFinal(secretKey);
			String encryptedWithPulblic = Base64.getEncoder().encodeToString(ciphertext);
			return encryptedWithPulblic;
		} catch (NoSuchPaddingException e) {

			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException | FileNotFoundException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}
	public static byte[] decryptWithPrivateKey(byte[] ciphertext) throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		Resource resource = resourceLoader.getResource("classpath:keys/private_key.pem");
		File file = resource.getFile();
		try (PemReader pemReader = new PemReader(new FileReader(file.getAbsolutePath()))) {
			PemObject pemObject = pemReader.readPemObject();
			byte[] privateKeyData = pemObject.getContent();
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			byte[] decryptedKey = cipher.doFinal(ciphertext);
			return decryptedKey;
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException | FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}

	public static EncryptionPayloadResp integratedDataEncryption(String payloadToEncrypt) throws NoSuchAlgorithmException, IOException {
		EncryptionPayloadResp encryptionPayloadResp = new EncryptionPayloadResp();
		SecretKey secretKey = generateRandomKey();
		String encryptedRandomKey = encryptWithPublic(secretKey.getEncoded());
		String encryptedPayload = encryptTemp(payloadToEncrypt,secretKey);

		encryptionPayloadResp.setEncryptedPayload(encryptedPayload);
		encryptionPayloadResp.setEncryptedKey(encryptedRandomKey);

		return encryptionPayloadResp;
	}

	public static String integratedDataDecryption(String encryptedKey, String encryptedPayload) throws NoSuchAlgorithmException, IOException {
		byte[] decryptedRandomKey = decryptWithPrivateKey(Base64.getDecoder().decode(encryptedKey));

		String ps = Base64.getEncoder().encodeToString(decryptedRandomKey);
		SecretKey secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(ps), "AES");

		String decryptedPayload = decryptTemp(encryptedPayload,secretKeySpec);
		return decryptedPayload;
	}
	public String HmacHash(String content, String key, String encodingMAC) {
		Mac enctype = null;
		String result = null;
		try {
			enctype = Mac.getInstance(encodingMAC);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), encodingMAC);
			enctype.init(keySpec);
			byte[] mac_data = enctype.doFinal(content.getBytes("UTF-8"));
			result = convertToHex(mac_data);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException
				 | IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String convertToHex(byte[] raw) {
		int substring = 1;
		int length = 16;
		int size = 200;
		StringBuilder stringBuilder = new StringBuilder(size);
		for (int i = 0; i < raw.length; i++) {

			stringBuilder.append(Integer.toString((raw[i] & 0xff) + 0x100, length).substring(substring));
		}
		return stringBuilder.toString();
	}
	public String generateEncryptionKey() {
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		int count = 16;

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}