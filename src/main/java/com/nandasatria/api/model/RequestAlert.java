package com.nandasatria.api.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nandasatria.elastic.model.StatusJob;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class RequestAlert {

	@JsonProperty("_id")	
	String id;
	
	@JsonProperty("@timestamp")
	String timestamp;
	
	@NotNull(message="Alert Name is mandatory")
	@JsonProperty("alert_name")
	String alertName;
	
	@NotNull(message="Index Name is mandatory")
	@JsonProperty("index_name")
	String index;
	
	@NotNull(message="Query is mandatory")
	String query;

	@JsonProperty("use_query_string")
	Boolean useQueryString;
	
	@NotNull(message="Run Every is mandatory")
	@JsonProperty("run_every")
	String runEvery;
	
	@NotBlank(message="Time Field is mandatory")
	@JsonProperty("time_field")
	String timeField;
	
	@NotNull(message="Email is mandatory for notification")
	String[] email;
	
	@JsonProperty("show_field")
	List<String> showField;
	
	@JsonProperty("status_alert")
	String statusAlert;
	
	@JsonProperty("status_job_running")
	StatusJob statusJobRunning;
	
	@JsonProperty("last_running")
	String lastRunning;
	
	@JsonProperty("next_running")
	String nextRunning;
	
	String error;
}
