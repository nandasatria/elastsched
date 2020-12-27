package com.nandasatria.elastsched;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

class ElastschedApplicationTests {

	public static void main(String[] args) {
		LocalDateTime nextRunning = updateRunningTime("5m");
		getDateTimeTZ(nextRunning);
	}
	
	public static LocalDateTime updateRunningTime(String runEvery) {
		TemporalUnit unit = ChronoUnit.MINUTES;
		String unitString = runEvery.substring(runEvery.length()-1,runEvery.length());
		Long amount = Long.valueOf(runEvery.replace(unitString, ""));
		switch (unitString) {
		case "m":
			unit = ChronoUnit.MINUTES;
			break;
		case "d":
			unit = ChronoUnit.DAYS;
			break;
		case "w":
			unit = ChronoUnit.WEEKS;
			break;
		case "M":
			unit = ChronoUnit.MONTHS;
			break;
		case "Y":
			unit = ChronoUnit.YEARS;
			break;
		default:
			unit = ChronoUnit.MINUTES;
			break;
		}
		LocalDateTime nextRunning = LocalDateTime.now().plus(amount, unit);
		return nextRunning;
	}
	
	public static String getDateTimeTZ(LocalDateTime dateTime) {
		String dateTimeString = dateTime.toString();
		return dateTimeString+"+07:00";
	}

}
