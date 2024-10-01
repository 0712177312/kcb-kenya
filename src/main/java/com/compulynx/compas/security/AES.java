package com.compulynx.compas.security;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AES {
	private final static String ALGORITHM = "AES";

	private static Scanner scanner;

	public static void main(String[] args) {
		print("1) For Encrypt, 2) For Decrypt>>");
		scanner = new Scanner(System.in);
		String sAction = scanner.nextLine();

		if (sAction.toLowerCase().equalsIgnoreCase("1")) {
			print("Enter String To Encrypt");
			scanner = new Scanner(System.in);
			String enStr = scanner.nextLine();

			try {
				print("Encrypted String Is >> " + encrypt(enStr));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (sAction.toLowerCase().equalsIgnoreCase("2")) {
			print("Enter String To decrypt");
			scanner = new Scanner(System.in);
			String decStr = scanner.nextLine();
			try {
				print("Decrypted String in main>> " + decrypt(decStr));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			print("Invalid Action Selected");
		}
	}

	public static void print(String value) {
		System.out.println(value);
	}

	public static String encrypt(String input) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey());
			return encodeString(cipher.doFinal(input.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String decrypt(String input) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, generateKey());
			String value = new String(cipher.doFinal(decodeString(input)));
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// generates a secret key
	private static Key generateKey() throws Exception {
		try {
			byte[] keyval = "@compulynx#54321".getBytes();
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			keyval = digest.digest(keyval);
			keyval = Arrays.copyOf(keyval, 16);
			Key key = new SecretKeySpec(keyval, ALGORITHM);
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encodeString(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	public static byte[] decodeString(String output) {
		return Base64.decodeBase64(output);
	}
}

