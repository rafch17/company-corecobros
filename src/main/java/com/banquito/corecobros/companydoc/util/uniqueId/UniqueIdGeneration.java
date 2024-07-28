package com.banquito.corecobros.companydoc.util.uniqueId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UniqueIdGeneration {
	private String uniqueId;

	public UniqueIdGeneration() {
		this.uniqueId = generateUniqueId();
	}

	private String generateUniqueId() {
		String letters = generateRandomLetters(3);
		String numbers = generateRandomNumbers(5);
		String rawId = letters + "00" + numbers;
		return encryptWithMD5(rawId);
	}

	private String generateRandomLetters(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char letter = (char) ('A' + random.nextInt(26));
			sb.append(letter);
		}
		return sb.toString();
	}

	private String generateRandomNumbers(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);
			sb.append(number);
		}
		return sb.toString();
	}

	private String encryptWithMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not found", e);
		}
	}

	public String getUniqueId() {
		return uniqueId;
	}
}
