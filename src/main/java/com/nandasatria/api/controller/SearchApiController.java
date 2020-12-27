package com.nandasatria.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nandasatria.api.model.RequestSearch;
import com.nandasatria.elastic.model.ValidationQueryModel;
import com.nandasatria.elastic.service.SearchElasticService;

@Service
@RestController
public class SearchApiController {

	@Autowired
	SearchElasticService searchElasticService;

	@GetMapping(value = "/v1/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchData(@RequestBody RequestSearch request) {
		String response = null;
		try {
			response = searchElasticService.searchElastic(request.getIndex(), request.getQuery());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/v1/validate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateQuery(@RequestParam("query") String query, @RequestParam("index") String index) {
		ValidationQueryModel response = new ValidationQueryModel();
		try {
			response = searchElasticService.validateSearch(index, query, true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
