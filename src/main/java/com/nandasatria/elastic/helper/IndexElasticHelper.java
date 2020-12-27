package com.nandasatria.elastic.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.nandasatria.util.JsonUtil;

import net.minidev.json.JSONArray;

@Component("IndexElasticHelper")
public class IndexElasticHelper extends BaseElasticHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexElasticHelper.class);

	@Value("${elastic.api.baseurl}")
	private String baseUrl;

	@Autowired
	@Qualifier("elasticRestTemplate")
	private RestTemplate restTemplate;

	public HttpStatus isIndexExists(String transactionId, String index) {

		String url = baseUrl + "/" + index;
		HttpHeaders header = createHeaderRequest();
		HttpEntity<String> requestEntity = new HttpEntity<String>(header);
		ResponseEntity<String> response = null;
		LOGGER.info(transactionId + "| Request to " + url + " with method : " + HttpMethod.HEAD);
		try {

			response = restTemplate.exchange(url, HttpMethod.HEAD, requestEntity, String.class);

			LOGGER.info(transactionId + "| Response : " + JsonUtil.writeToString(response));

			return response.getStatusCode();
		} catch (HttpClientErrorException ec) {
			return ec.getStatusCode();

		} catch (HttpServerErrorException es) {
			return es.getStatusCode();
		} catch (Exception e) {
			LOGGER.info(transactionId + "| Error while checking index : " + e.getMessage());
			e.printStackTrace();
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

	}

	public boolean createIndex(String transactionId, String index, String mapping) {

		String url = baseUrl + "/" + index;
		HttpHeaders header = createHeaderRequest();
		HttpEntity<String> requestEntity = new HttpEntity<String>(mapping, header);
		ResponseEntity<String> response = null;
		LOGGER.info(transactionId + "| Request to " + url + " with method : " + HttpMethod.PUT + " and mapping index : "
				+ mapping);
		try {

			response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

			LOGGER.info(transactionId + "| Response : " + JsonUtil.writeToString(response));

			if (response.getStatusCode() == HttpStatus.OK) {
				return true;
			} else {
				LOGGER.info(transactionId + "| Failed Create index " + index + " reason :" + response.getBody());
			}

		} catch (Exception e) {

			LOGGER.info(transactionId + "| Error while creating Index : " + e.getMessage());
			e.printStackTrace();

		}

		return false;
	}

	public String getMapping(String index) {

		String url = baseUrl + "/" + index + "/_mapping";
		HttpHeaders header = createHeaderRequest();
		HttpEntity<String> requestEntity = new HttpEntity<String>(header);
		ResponseEntity<String> response = null;
		LOGGER.info("Request to " + url + " with method : " + HttpMethod.GET);
		try {

			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

			LOGGER.info("Response : " + JsonUtil.writeToString(response));

			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody();

			} else {
				return response.getBody();
			}

		} catch (Exception e) {

			LOGGER.info("Error while searching Data : " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}

	}

	public List<String> getHeaderIndex(String index) {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonPath = "$..mappings.properties";
		String mappingJsonString = this.getMapping(index);
		JSONArray mappings = JsonPath.read(mappingJsonString, jsonPath);

		List<String[]> listObjectMapping = mappings.stream()
				.map(mapping -> objectMapper.convertValue(
						objectMapper.convertValue(mapping, LinkedHashMap.class).keySet().toArray(), String[].class))
				.collect(Collectors.toList());

		List<String> listHeaders = new ArrayList<String>();

		for (String[] listStringHeader : listObjectMapping) {
			for (String header : listStringHeader) {
				if (listHeaders.contains(header)) {
					continue;
				} else {
					listHeaders.add(header);
				}

			}
		}
		return listHeaders;
	}

}
