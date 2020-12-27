package com.nandasatria.elastic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nandasatria.elastic.helper.SearchElasticHelper;
import com.nandasatria.elastic.helper.ValidateElasticHelper;
import com.nandasatria.elastic.model.ValidationQueryModel;
import com.nandasatria.util.UUIDUtil;


@Service
public class SearchElasticService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchElasticService.class);

	@Autowired
	SearchElasticHelper searchElasticHelper;

	@Autowired
	ValidateElasticHelper validateElasticHelper;

	/**
	 * Get Json String based on _search from index
	 * 
	 * @param index input {@link String}
	 * @param query input Json {@link String}
	 * @return {@link String} Json String
	 */
	public String searchElastic(String index, String query) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return this.searchElastic(transactionId, index, query);
	}

	/**
	 * Get Json String based on _search from index
	 * 
	 * @param transactionId input {@link String}
	 * @param index         input {@link String}
	 * @param query         input Json {@link String}
	 * @return {@link String} Json String
	 */
	public String searchElastic(String transactionId, String index, String query) {
		LOGGER.info(transactionId + "| search elastic from index : " + index + " with query : " + query);
		return searchElasticHelper.queryData(transactionId, index, query);
	}

	/**
	 * validate query _search from index we can use elastic query or lucene, for
	 * lucene set userQueryString=true
	 * 
	 * @param index          input {@link String}
	 * @param query          input Json {@link String}
	 * @param useQueryString input {@link boolean}
	 * @return {@link ValidationQueryModel} validationresult
	 */
	public ValidationQueryModel validateSearch(String index, String query, boolean useQueryString) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return this.validateSearch(transactionId, index, query, useQueryString);
	}

	/**
	 * validate query _search from index we can use elastic query or lucene, for
	 * lucene set userQueryString=true
	 * 
	 * @param transactionId  input {@link String}
	 * @param index          input {@link String}
	 * @param query          input Json {@link String}
	 * @param useQueryString input {@link boolean}
	 * @return {@link ValidationQueryModel} validationresult
	 */
	public ValidationQueryModel validateSearch(String transactionId, String index, String query,
			boolean useQueryString) {
		LOGGER.info(transactionId + "| Validate search index : " + index + " with query : " + query);
		return validateElasticHelper.validateQuery(index, query, useQueryString);
	}

}
