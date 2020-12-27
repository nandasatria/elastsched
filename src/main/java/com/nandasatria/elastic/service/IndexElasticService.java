package com.nandasatria.elastic.service;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nandasatria.elastic.helper.IndexElasticHelper;
import com.nandasatria.util.UUIDUtil;

@Service
public class IndexElasticService {

	@Autowired
	IndexElasticHelper indexElasticHelper;

	/**
	 * Get List of header from specific index
	 * 
	 * @param indexName input {@link String}
	 * @return {@link List<String>} listHeader
	 */
	public List<String> getHeaderIndex(String indexName) {

		String transactionId = UUIDUtil.getUUIDtoString();
		return this.getHeaderIndex(transactionId, indexName);
	}

	/**
	 * Get List of header from specific index
	 * 
	 * @param transactionId input {@link String}
	 * @param indexName     input {@link String}
	 * @return {@link List<String>} listHeader
	 */
	public List<String> getHeaderIndex(String transactionId, String indexName) {
		return indexElasticHelper.getHeaderIndex(indexName);
	}

	/**
	 * Get List of mapping properties from specific index
	 * 
	 * @param index input {@link String}
	 * @return {@link List<String>} listHeader
	 */
	public List<String> getListMappingIndex(String index) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return getListMappingIndex(transactionId, index);
	}

	/**
	 * Get List of mapping properties from specific index
	 * 
	 * @param transactionId input {@link String}
	 * @param index         input {@link String}
	 * @return {@link List<String>} listHeader
	 */
	public List<String> getListMappingIndex(String transactionId, String index) {
		return indexElasticHelper.getHeaderIndex(index);
	}

	/**
	 * Checking index name, exists or not in elastic
	 * 
	 * @param transactionId input {@link String}
	 * @param index         input {@link String}
	 * @return {@link boolean}
	 * @throws SocketTimeoutException
	 */
	public HttpStatus isIndexEsists(String transactionId, String index) {
		return indexElasticHelper.isIndexExists(transactionId, index);
	}

	/**
	 * Checking index name, exists or not in elastic
	 * 
	 * @param index input {@link String}
	 * @return {@link boolean}
	 * @throws SocketTimeoutException
	 */
	public HttpStatus isIndexEsists(String index) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return this.isIndexEsists(transactionId, index);
	}

	/**
	 * create new index with spesific index name, and index mapping properties
	 * 
	 * @param transactionId input {@link String}
	 * @param index         input {@link String}
	 * @param mapping       input {@link String} Json Mapping properties
	 * @return {@link boolean}
	 */
	public boolean createIndex(String transactionId, String index, String mapping) {
		return indexElasticHelper.createIndex(transactionId, index, mapping);
	}

	/**
	 * create new index with spesific index name, and index mapping properties
	 * 
	 * @param index   input {@link String}
	 * @param mapping input {@link String} Json Mapping properties
	 * @return {@link boolean}
	 */
	public boolean createIndex(String index, String mapping) {
		String transactionId = UUIDUtil.getUUIDtoString();
		return this.createIndex(transactionId, index, mapping);
	}
}
