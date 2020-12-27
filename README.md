# elastsched 0.0.1 
Scheduler for elastic

## Configuration

Configuration File :

1. Elastic : `config/elastic.properties` 
used for configure elasticsearch, from url, credential, dan json mapping 

2. Email : `config/email.properties`
used for configure email to notify user

## API

### Register New Alert

Sample Request: [url : `http://host:8080/v1/alert` ]

1. Using query string (based on lucene query)

```json
{
      "alert_name" : "Alert with execution_time more than 1000",
      "index_name": "siem",
      "use_query_string": "true",
      "query": "execution_time:>1000 and event:login",
      "run_every": "1d",
      "time_field": "@timestamp",
      "email": ["test@gmail.com"],
      "show_field" : ["label","startdate","enddate","execution_time","status","detailedstatus"],
      "next_running" : "2020-12-27T02:00:00+07:00"
}
```

2. Using query elastic


```json
{
      "alert_name" : "Alert with execution_time more than 1000",
      "index_name": "siem",
      "use_query_string": "false",
      "query": "{\"from\":0,\"size\":2,\"query\":{\"bool\":{\"filter\":[{\"bool\":{\"filter\":[{\"bool\":{\"should\":[{\"range\":{\"execution_time\":{\"gt\":2000}}}],\"minimum_should_match\":1}},{\"bool\":{\"must_not\":{\"bool\":{\"should\":[{\"match_phrase\":{\"status\":\"OK\"}}],\"minimum_should_match\":1}}}},{\"bool\":{\"should\":[{\"query_string\":{\"fields\":[\"label\"],\"query\":\"*Login*\"}}],\"minimum_should_match\":1}}]}}]}}}",
      "run_every": "1d",
      "time_field": "@timestamp",
      "email": ["test@gmail.com"],
      "show_field" : ["label","startdate","enddate","execution_time","status","detailedstatus"],
      "next_running" : "2020-12-27T02:00:00+07:00"
}
```

### Manual test running alert

Sample Request: [ url : `http://host:8080/v1/alert?id=alert-id` ]

```text
http://localhost:8080//v1/run-alert?id=tGJuUXYBTgclbUJMplph
```