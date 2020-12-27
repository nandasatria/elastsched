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

import com.nandasatria.elastic.model.ValidationQueryModel;
import com.nandasatria.util.JsonUtil;

@Service
public class ValidateElasticHelper extends BaseElasticHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidateElasticHelper.class);

	@Value("${elastic.api.baseurl}")
	private String baseUrl;

	private final String URLPATH = "/_validate/query";

	@Autowired
	@Qualifier("elasticRestTemplate")
	private RestTemplate restTemplate;

	public ValidationQueryModel validateQuery(String index, String query, boolean useQueryString) {

		HttpHeaders header = createHeaderRequest();
		ValidationQueryModel validationQuery = new ValidationQueryModel();
		validationQuery.setValid(false);

		String url;
		HttpEntity<String> requestEntity;
		if (useQueryString) {
			url = baseUrl + "/" + index + URLPATH + "?explain=true&q=" + query;
			requestEntity = new HttpEntity<String>(header);

		} else {
			url = baseUrl + "/" + index + URLPATH + "?explain=true";
			requestEntity = new HttpEntity<String>(query, header);
		}

		ResponseEntity<String> response = null;
		LOGGER.info("Request to " + url);
		try {

			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			LOGGER.debug("Response : " + JsonUtil.writeToPrettyString(response));

			validationQuery = JsonUtil.readFromString(response.getBody(), ValidationQueryModel.class);

			LOGGER.debug("Response : " + JsonUtil.writeToString(validationQuery));
			
			return validationQuery;
		} catch (Exception e) {

			LOGGER.info("Error while searching Data : " + e.getMessage());
			e.printStackTrace();
			return validationQuery;
		}

	}

}
