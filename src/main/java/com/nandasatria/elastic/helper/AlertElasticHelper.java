package com.nandasatria.elastic.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nandasatria.api.model.RequestAlert;
import com.nandasatria.elastic.model.ValidationQueryModel;
import com.nandasatria.elastic.service.SearchElasticService;
import com.nandasatria.util.DateTimeUtil;
import com.nandasatria.util.JsonUtil;

@Service
public class AlertElasticHelper extends BaseElasticHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertElasticHelper.class);

	@Value("${elastic.api.baseurl}")
	private String baseUrl;

	@Value("${elastic.alert.indexname}")
	private String alertIndex;

	private final String URLPATH = "/_doc";

	@Autowired
	SearchElasticService searchElasticService;

	@Autowired
	@Qualifier("elasticRestTemplate")
	private RestTemplate restTemplate;

	public String registerAlert(String transactionId, RequestAlert request) {
		if (request == null) {
			LOGGER.info(transactionId + "| Request is null");
			return "Request is Null";
		} else {
			request.setStatusAlert("Active");
			String index = request.getIndex();

			String query = request.getQuery();
			
			request.setTimestamp(DateTimeUtil.getDateTimeTZNow());
			if (request.getUseQueryString() == null) {
				request.setUseQueryString(false);
			}
			boolean useQueryString = request.getUseQueryString();

			ValidationQueryModel validationQueryModel = searchElasticService.validateSearch(transactionId, index, query, useQueryString);

			if (validationQueryModel.isValid()) {
				return this.postNewAlert(transactionId, request);
			} else {
				return "Query Not Valid : " + validationQueryModel.getExplanations().get(0).getError();
			}
		}

	}

	public String postNewAlert(String transactionId, RequestAlert requestAlert) {

		String url = baseUrl + "/" + alertIndex + URLPATH;
		HttpHeaders header = createHeaderRequest();
		HttpEntity<RequestAlert> requestEntity = new HttpEntity<RequestAlert>(requestAlert, header);
		ResponseEntity<String> response = null;
		LOGGER.info("Request to " + url + " with request body : " + requestEntity);
		try {

			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			LOGGER.info("Response : " + JsonUtil.writeToString(response));
			return response.getBody();

		} catch (Exception e) {

			LOGGER.info("Error while register new alert : " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}

	}

}
