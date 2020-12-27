package com.nandasatria.util;

import java.util.Base64;

public class Base64Util {

	/**
	 * Construct {@link Base64} encoded string based on given plain text
	 * @param plainText
	 * @return encodedString
	 */
	public static String encode(String plainText) {
		return Base64.getEncoder().encodeToString(plainText.getBytes());
	}
	
	/**
	 * Construct decoded {@link String} based on given encoded string
	 * @param encodedText
	 * @return decodedText
	 */
	public static String decode(String encodedText) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
		return new String(decodedBytes);
	}
	
}
