package com.nandasatria.elastic.sync;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.nandasatria.elastic.service.IndexElasticService;
import com.nandasatria.util.UUIDUtil;

@Component
public class SyncIndexAlert {

	@Autowired
	IndexElasticService indexElasticService;

	@Value("${elastic.alert.indexname}")
	private String alertIndex;

	@Value("${elastic.json.mapping.elastsced}")
	private String mappingIndex;

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncIndexAlert.class);

	@PostConstruct
	public void constructIndexALert() {
		String transactionId = UUIDUtil.getUUIDtoString();
		LOGGER.info(transactionId + "| Checking Index Alert...");
		String index = alertIndex;
		String mapping = mappingIndex;
		HttpStatus indexExists = indexElasticService.isIndexEsists(index);
		
		if (indexExists == HttpStatus.OK) {
			LOGGER.info(transactionId + "| Index for elasticsched : " + index + " OK already exits");
		} else if (indexExists == HttpStatus.NOT_FOUND) {
			LOGGER.info(transactionId + "| Index Not Found - Register new index to elastic with index name : " + alertIndex);
			LOGGER.info(transactionId + "| Mapping of " + alertIndex + "");
			boolean isSuccessCreateIndex = indexElasticService.createIndex(transactionId, index, mapping);
			if (isSuccessCreateIndex) {
				LOGGER.info(transactionId + "| Success Create index :" + index);
			} else {
				LOGGER.info(transactionId + "| Failed Create index :" + index);
			}
		} else {

			LOGGER.info(
					transactionId + " | Error when checking index exists or not. Please check elastic search service "
							+ indexExists);
			
		}

	}

}
