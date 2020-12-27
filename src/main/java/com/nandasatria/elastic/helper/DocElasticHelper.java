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
import com.nandasatria.util.JsonUtil;

@Service
public class DocElasticHelper extends BaseElasticHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocElasticHelper.class);

	@Value("${elastic.api.baseurl}")
	private String baseUrl;

	private final String URLPATH = "/_doc";

	@Autowired
	@Qualifier("elasticRestTemplate")
	private RestTemplate restTemplate;

	public ResponseEntity<String> searchByIndexAndId(String index, String id) {
		String url = baseUrl + "/" + index + URLPATH + "/" + id;

		HttpHeaders header = createHeaderRequest();

		HttpEntity<String> requestEntity = new HttpEntity<String>(header);
		ResponseEntity<String> response = null;
		LOGGER.info("Request to " + url);
		try {

			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
//			String jsonString = response.getBody();

			LOGGER.info("Response : " + JsonUtil.writeToString(response));
			return response;
		} catch (Exception e) {

			LOGGER.info("Error while searching Data : " + e.getMessage());
			e.printStackTrace();
			return response;
		}

	}

	public ResponseEntity<String> updateDocByIndexAndId(String index, String id, RequestAlert request) {

		String url = baseUrl + "/" + index + "/_update" + "/" + id;

		HttpHeaders header = createHeaderRequest();

		String requestJson  = "{ \"doc\" : " +JsonUtil.writeToString(request)+ " }";
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestJson, header);
		ResponseEntity<String> response = null;
		LOGGER.info("Request to " + url + " body: "+requestJson);
		try {

			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//			String jsonString = response.getBody();

			LOGGER.info("Response : " + JsonUtil.writeToString(response));
			return response;
		} catch (Exception e) {

			LOGGER.info("Error while searching Data : " + e.getMessage());
			e.printStackTrace();
			return response;
		}

	}

}
