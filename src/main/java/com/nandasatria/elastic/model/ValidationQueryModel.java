package com.nandasatria.elastic.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ValidationQueryModel {

	@JsonProperty("_shards")
	Shards shards;

	boolean valid;

	List<Explanation> explanations;
}
