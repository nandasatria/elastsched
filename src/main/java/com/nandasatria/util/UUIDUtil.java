package com.nandasatria.util;

import java.security.MessageDigest;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

public class UUIDUtil {

	private static final UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
	
	/** 
	 * Return timebased generated uuid 
	 * @return uuid
	 */
	public static UUID getUUID() {
		return uuidGenerator.generateTimeBasedUUID();
	}
	
	/**
	 * Return string object from {@link #getUUID()}
	 * @return string
	 */
	public static String getUUIDtoString() {
		return getUUID().toString();
	}
	
	/**
	 * Return namebased generated uuid
	 * 
	 * @param uuid input {@link UUID} param
	 * @param string input {@link String} param
	 * @param md input {@link MessageDigest} param
	 * @return uuid
	 */
	public static UUID getNameBasedUUID(UUID uuid, String string, MessageDigest md) {
		return uuidGenerator.generateNameBasedUUID(uuid, string, md);
	}
	
}
