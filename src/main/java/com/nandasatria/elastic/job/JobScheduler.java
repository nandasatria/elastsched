package com.nandasatria.elastic.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.nandasatria.elastic.service.SearchElasticService;
import com.nandasatria.util.DateTimeUtil;
import com.nandasatria.util.JsonUtil;
import com.nandasatria.util.UUIDUtil;

import net.minidev.json.JSONArray;

@Component("JobScheduler")
public class JobScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

	@Value("${elastic.json.request.getalert}")
	private String requestGetAlert;

	@Autowired
	SearchElasticService searchElasticService;

	@Autowired
	JobSchedulerProcessor jobSchedulerProcessor;

	@Value("${elastic.alert.indexname}")
	private String alertIndex;

	@Value("${elastic.jsonpath.search.ids}")
	private String jsonSearchIds;

	@Scheduled(cron = "${elastic.alert.cronjob.check}")
	public Object jobRunner() {

		String transactionId = UUIDUtil.getUUIDtoString();
		LOGGER.info(transactionId + "| Running Job to Check alert..");
		String query = requestGetAlert.replace("#next_running", DateTimeUtil.getDateTimeTZNow());
		String jsonData = searchElasticService.searchElastic(alertIndex, query);
		
		JSONArray listHits = JsonPath.read(jsonData, jsonSearchIds);
		LOGGER.info(transactionId + "| Found Match Job :" + JsonUtil.writeToString(listHits));
		listHits.forEach(idAlert -> jobSchedulerProcessor.process(idAlert.toString()));
		return listHits;
	}
}
