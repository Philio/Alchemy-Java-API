package com.techlightenment.alchemy.api.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OAuthUtils {

	/**
	 * Simple string validity check
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isString(String str) {
		if (str != null && str != "") {
			return true;
		}
		return false;
	}

	/**
	 * Encode data into OAuth url format
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeString(String str)
			throws UnsupportedEncodingException {
		// Standard URL encoding
		String encoded = URLEncoder.encode(str, "UTF-8");

		// Make some replacements for chars which don't encode as we want
		encoded = encoded.replace("+", "%20");
		encoded = encoded.replace("%7E", "~");

		return encoded;
	}

	/**
	 * Generate a random hash key
	 * 
	 * @return String
	 */
	public static String generateKey() {
		// Generate a random byte sequence
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[64];
		random.nextBytes(bytes);

		// Generate SHA1 of the random bytes
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			// Return null as could not generate a SHA1
			return null;
		}
		md.reset();
		md.update(bytes);
		byte digest[] = md.digest();

		// Convert bytes to hash
		String s = "";
		for (int i = 0; i < digest.length; i++) {
			s += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
		}
		return s;
	}

}