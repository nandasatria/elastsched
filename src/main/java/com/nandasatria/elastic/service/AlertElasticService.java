package com.nandasatria.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nandasatria.api.model.RequestAlert;
import com.nandasatria.elastic.helper.AlertElasticHelper;
import com.nandasatria.util.UUIDUtil;

@Service
public class AlertElasticService {

	@Autowired
	AlertElasticHelper alertElasticHelper;

	/**
	 * Register alert
	 * 
	 * @param request input {@link RequestAlert}
	 * @return {@link String} Json String
	 */
	public String registerAlert(RequestAlert request) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return this.registerAlert(transactionId, request);
	}

	/**
	 * Register alert
	 * 
	 * @param transactionId input {@link String}
	 * @param request       input {@link RequestAlert}
	 * @return {@link String} Json String
	 */
	public String registerAlert(String transactionId, RequestAlert request) {
		return alertElasticHelper.registerAlert(transactionId, request);
	}
}
