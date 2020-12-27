package com.nandasatria.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class JsonUtil {

	/**
	 * Write the json string representation of particular object
	 * @param object input {@link Object}
	 * @return jsonString
	 */
	public static String writeToString(Object object) {
		String payload = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			// prevent having duplicate object being serialized with lower-case
			mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
	                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
	                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			// to follows ISO 8601 format of "yyyy-MM-dd'T'HH:mm:ss.SSSZ" open commented below
			mapper.setDateFormat(new StdDateFormat());
			payload = mapper.writeValueAsString(object);
		} catch (Exception e) {
		}
		return payload;
	}
	
	/**
	 * Write the json (pretty) string representation of particular object
	 * @param object input {@link Object}
	 * @return jsonString
	 */
	public static String writeToPrettyString(Object object) {
		String payload = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			// prevent having duplicate object being serialized with lower-case
			mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
	                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
	                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			// to follows ISO 8601 format of "yyyy-MM-dd'T'HH:mm:ss.SSSZ" open commented below
			mapper.setDateFormat(new StdDateFormat());
			payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {
		}
		return payload;
	}
	
	/**
	 * Construct an object based on given jsonString and particular object target
	 * @param payload input {@link String}
	 * @param clazz input {@link Class} target
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readFromString(String payload, Class<?> clazz) {
		T object = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			object = (T) mapper.readValue(payload, clazz);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return object;
	}
	
	/**
	 * Construct an object based on given jsonString and particular object target
	 * @param payload input {@link String}
	 * @param valueTypeRef input {@link TypeReference} target
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readFromString(String payload, TypeReference<?> valueTypeRef) {
		T object = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			object = (T) mapper.readValue(payload, valueTypeRef);
		} catch (Exception e) {
		}
		return object;
	}
	
}
