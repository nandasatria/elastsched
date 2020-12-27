package com.nandasatria.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nandasatria.api.model.GeneralResponse;
import com.nandasatria.api.model.RequestAlert;
import com.nandasatria.elastic.service.IndexElasticService;

@Service
public class ValidationAlert {

	@Autowired
	IndexElasticService indexElasticService;

	public GeneralResponse validate(RequestAlert request) {
		GeneralResponse response = new GeneralResponse();
		response.setErrorCode(HttpStatus.BAD_REQUEST);
		response.setValid(false);
		String error = "";
		if (request.getAlertName() == null || request.getAlertName().trim().equals("")) {
			error = " alert_name is mandatory";
		} else if (request.getIndex() == null || request.getIndex().trim().equals("")) {
			error = " index_name is mandatory";
		} else if (request.getQuery() == null || request.getQuery().trim().equals("")) {
			error = " query is mandatory";
		} else if (request.getRunEvery() == null || request.getQuery().trim().equals("")) {
			error = " run_every is mandatory";
		} else if (request.getTimeField() == null || request.getTimeField().trim().equals("")) {
			error = " time_field is mandatory";
		} else if (request.getEmail() == null) {
			error = " email is mandatory";
		} else {
			response.setValid(true);
			response.setErrorCode(HttpStatus.OK);
		}
		List<String> listHeaderExists = indexElasticService.getHeaderIndex(request.getIndex());
		boolean validateTimeField = validateField(listHeaderExists, request.getTimeField());
		boolean validateShowField = validateField(listHeaderExists, request.getShowField());
		
		if(! validateShowField) {
			response.setValid(false);
			error = "One or More field in show_field not found in mapping index "+request.getIndex();
		}
		
		if(! validateTimeField) {
			response.setValid(false);
			error = "time_field not found in mapping index "+request.getIndex();
		}
		
		response.setErrorDescription(error);

		return response;
	}

	public boolean validateField(String indexName, String fieldName) {
		List<String> listHeaderExists = indexElasticService.getHeaderIndex(indexName);
		return this.validateField(listHeaderExists, fieldName);

	}

	public boolean validateField(String indexName, List<String> fieldNames) {
		List<String> listHeaderExists = indexElasticService.getHeaderIndex(indexName);
		return this.validateField(listHeaderExists, fieldNames);

	}

	public boolean validateField(List<String> allfields, List<String> fieldNames) {
		
		for(String fieldName : fieldNames) {
			if(! allfields.contains(fieldName)) {
				return false;
				
			}
		}
		return true;

	}

	public boolean validateField(List<String> allfields, String fieldName) {
		if (allfields.contains(fieldName)) {
			return true;
		} else {
			return false;
		}

	}

}
