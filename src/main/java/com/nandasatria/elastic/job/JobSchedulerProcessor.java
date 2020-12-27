package com.nandasatria.elastic.job;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.nandasatria.api.model.RequestAlert;
import com.nandasatria.elastic.helper.DocElasticHelper;
import com.nandasatria.elastic.model.StatusJob;
import com.nandasatria.elastic.service.IndexElasticService;
import com.nandasatria.elastic.service.SearchElasticService;
import com.nandasatria.notif.service.EmailService;
import com.nandasatria.util.DateTimeUtil;
import com.nandasatria.util.JsonUtil;
import com.nandasatria.util.UUIDUtil;

import net.minidev.json.JSONArray;

@Component("JobSchedulerProcessor")
public class JobSchedulerProcessor {

	@Autowired
	DocElasticHelper docElasticHelper;

	@Value("${elastic.alert.indexname}")
	private String alertIndex;

	@Value("${elastic.jsonpath.doc}")
	private String jsonPathDoc;

	@Value("${elastic.json.request.querystring}")
	private String templateRequestQueryString;

	@Value("${elastic.jsonpath.search.resultquery}")
	private String jsonSearchResultquery;

	@Value("${elastic.jsonpath.search.resultcount}")
	private String jsonSearchResultCount;	
	
	@Autowired
	IndexElasticService indexElasticService; 
	
	@Autowired
	SearchElasticService searchElasticService; 

	@Autowired
	EmailService emailService;

	public RequestAlert process(String id) {
		RequestAlert updateAlert = new RequestAlert();
		try {
			String transactionId = UUIDUtil.getUUIDtoString();
			ResponseEntity<String> responseDoc = docElasticHelper.searchByIndexAndId(alertIndex, id);
			if (responseDoc.getStatusCode() == HttpStatus.OK) {
				HashMap<String, String> paramNotif = new HashMap<String, String>();

				String jsonAlert = JsonUtil.writeToString(JsonPath.read(responseDoc.getBody(), jsonPathDoc));
				RequestAlert requestAlert = JsonUtil.readFromString(jsonAlert, RequestAlert.class);
				if(requestAlert.getShowField() == null) {
					requestAlert.setShowField(indexElasticService.getHeaderIndex(transactionId, requestAlert.getIndex()));
				}
				
				String query;
				if (requestAlert.getUseQueryString()) {
					query = templateRequestQueryString.replace("#querystring", requestAlert.getQuery())
							.replace("#triggertime", requestAlert.getRunEvery())
							.replace("#showfields", JsonUtil.writeToString(requestAlert.getShowField()))
							.replace("#timefield", requestAlert.getTimeField());
					paramNotif.put("query", requestAlert.getQuery());
					
				} else {
					query = requestAlert.getQuery();
					paramNotif.put("query", requestAlert.getQuery());
				}

				
				String queryResult = searchElasticService.searchElastic(transactionId, requestAlert.getIndex(), query);
				Object jsonResult = JsonPath.read(queryResult, jsonSearchResultquery);
				Integer totalHits = JsonPath.read(queryResult, jsonSearchResultCount);
				
				if (totalHits > 0) {
					paramNotif.put("total_alert", Integer.toString(totalHits));
					List<LinkedHashMap<String, Object>> alerts = this.convertJsonArrayToHashMap((JSONArray) jsonResult);
					String additionalContent = this.constructTableAlert(requestAlert.getShowField(), alerts);
					
					if(requestAlert.getAlertName() == null) {
						requestAlert.setAlertName("Elastic Alert");
					}else {
						requestAlert.setAlertName("Elastic Alert : "+ requestAlert.getAlertName());
					}
					emailService.sendNotif(transactionId, requestAlert.getEmail(),requestAlert.getAlertName(), paramNotif, additionalContent);
				}
				
				
				updateAlert = this.requestUpdateRunningTime(requestAlert.getRunEvery());
				updateAlert.setStatusJobRunning(StatusJob.OK);
				
			}else {
				updateAlert.setStatusJobRunning(StatusJob.ERROR);
				updateAlert.setError("Failed to get detail Alert data HttpStatus : "+responseDoc.getStatusCode()+" : "+responseDoc.getBody());
			}
			
		}catch (Exception e) {
			updateAlert.setStatusJobRunning(StatusJob.ERROR);
			updateAlert.setError("Failed to process Alert : "+e.getMessage());
			e.printStackTrace();
		}
		
		docElasticHelper.updateDocByIndexAndId(alertIndex, id, updateAlert);
		return updateAlert;
	}

	@SuppressWarnings("unchecked")
	public List<LinkedHashMap<String, Object>> convertJsonArrayToHashMap(JSONArray jsonArray) {
		List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String, Object>>();

		Iterator<Object> iterator = jsonArray.iterator();

		while (iterator.hasNext()) {
			result.add((LinkedHashMap<String, Object>) iterator.next());
		}

		return result;
	}

	public String constructTableAlert(List<String> headers, List<LinkedHashMap<String, Object>> contents) {
		String result = "<table class='table-alert' ><tr>";

		result += headers.stream().map(header -> new String("<th>").concat(header).concat("</th>"))
				.collect(Collectors.joining());

		result = result.concat("</tr>");

		for (int i = 0; i < contents.size(); i++) {
			LinkedHashMap<String, Object> templinkedHashMap = contents.get(i);
			result += "<tr>";
			result += headers.stream()
					.map(header ->  
						new String("<td>").concat( 
								(templinkedHashMap.get(header) !=null && templinkedHashMap.containsKey(header)) ? 
										templinkedHashMap.get(header).toString() : "" )
						.concat("</td>"))
					.collect(Collectors.joining());
			result += "</tr>";
		}

		result = result.concat("</table>");

		return result;
	}

	public RequestAlert requestUpdateRunningTime(String runEvery) {
		
		RequestAlert updateDoc = new RequestAlert();
		updateDoc.setNextRunning(DateTimeUtil.getDateTimeTZ(DateTimeUtil.getNextRunning(runEvery)));
		updateDoc.setLastRunning(DateTimeUtil.getDateTimeTZ(LocalDateTime.now()));
		
		return updateDoc;
	}
	
	
}
