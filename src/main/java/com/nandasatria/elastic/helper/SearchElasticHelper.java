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

import com.nandasatria.util.JsonUtil;


@Service
public class SearchElasticHelper extends BaseElasticHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchElasticHelper.class);

	@Value("${elastic.api.baseurl}")
	private String baseUrl;

	private final String URLPATH = "/_search";

	@Autowired
	@Qualifier("elasticRestTemplate")
	private RestTemplate restTemplate;

	public String queryData(String transactionId, String index, String query) {

	
		String url = baseUrl + "/" + index + URLPATH;
		
		HttpHeaders header = createHeaderRequest();

		HttpEntity<String> requestEntity = new HttpEntity<String>(query, header);
		ResponseEntity<String> response = null;
		LOGGER.info(transactionId+ "| Request to "+url +" with method " + HttpMethod.POST+" with request body : "+requestEntity) ;
		try {

			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			LOGGER.debug("Response : "+ JsonUtil.writeToString(response));
			return response.getBody();
		} catch (Exception e) {
			LOGGER.info(transactionId +"| Error while searching Data : " + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}
	
	
}
