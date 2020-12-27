package com.nandasatria.elastic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Explanation {

	String index;
	boolean valid;
	String explanation;
	String error;

}
