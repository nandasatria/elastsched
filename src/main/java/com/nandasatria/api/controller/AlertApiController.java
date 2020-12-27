package com.nandasatria.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nandasatria.api.model.GeneralResponse;
import com.nandasatria.api.model.RequestAlert;
import com.nandasatria.api.service.ValidationAlert;
import com.nandasatria.elastic.job.JobScheduler;
import com.nandasatria.elastic.job.JobSchedulerProcessor;
import com.nandasatria.elastic.service.AlertElasticService;
import com.nandasatria.elastic.service.IndexElasticService;

@RestController
@Service
public class AlertApiController {

	@Autowired
	JobSchedulerProcessor jobSchedulerProcessor;

	@Autowired
	AlertElasticService alertElasticService;

	@Autowired
	IndexElasticService indexElasticService;

	@Autowired
	JobScheduler jobScheduler;

	@Autowired
	ValidationAlert validationAlert;

	@PostMapping(value = "/v1/alert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchData(@RequestBody RequestAlert request) {
		GeneralResponse response = null;

		try {
			response = validationAlert.validate(request);
			if (response.isValid() == true) {

				String responseString = alertElasticService.registerAlert(request);
				response.setErrorDescription(responseString);
			}

			return new ResponseEntity<>(response, response.getErrorCode());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/v1/run-alert")
	public ResponseEntity<?> runAlert(@RequestParam("id") String id) {
		RequestAlert request = jobSchedulerProcessor.process(id);
		return new ResponseEntity<>(request, HttpStatus.OK);
	}

	@GetMapping(value = "/v1/run-all-alert")
	public ResponseEntity<?> runAlltAlert() {
		return new ResponseEntity<>(jobScheduler.jobRunner(), HttpStatus.OK);
	}

	@GetMapping(value = "/v1/mapping-index")
	public ResponseEntity<?> getListMappingIndex(@RequestParam("index") String index) {
		List<String> listHeaders = indexElasticService.getHeaderIndex(index);
		return new ResponseEntity<>(listHeaders, HttpStatus.OK);
	}
}
