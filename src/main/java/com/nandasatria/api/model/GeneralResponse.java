package com.nandasatria.api.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeneralResponse {

	
	@JsonProperty("valid")
	boolean valid;
	
	@JsonProperty("error_code")
	HttpStatus errorCode;
	
	@JsonProperty("error_description")
	String errorDescription;
	
}
