package com.nandasatria.elastic.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.nandasatria.util.Base64Util;

public class BaseElasticHelper {
	
	@Value("${elastic.api.username}")
	private String username;
	
	@Value("${elastic.api.password}")
	private String password;
	
	public HttpHeaders createHeaderRequest() {

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		String basicAuth = "Basic " + Base64Util.encode(username + ":" + password);
		headers.set(HttpHeaders.AUTHORIZATION, basicAuth);
		return headers;

	}
}
