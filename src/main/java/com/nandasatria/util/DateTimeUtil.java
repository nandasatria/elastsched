package com.nandasatria.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class DateTimeUtil {

	public static String getDateTimeTZ(LocalDateTime dateTime) {
		String dateTimeString = dateTime.toString();
		return dateTimeString + "+07:00";
	}

	public static String getDateTimeTZNow() {
		return getDateTimeTZ(LocalDateTime.now());
	}

	public static LocalDateTime getNextRunning(String addition) {
		TemporalUnit unit = ChronoUnit.MINUTES;
		String unitString = addition.substring(addition.length() - 1, addition.length());
		Long amount = Long.valueOf(addition.replace(unitString, ""));
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
		LocalDateTime lastRunning = LocalDateTime.now();
		LocalDateTime nextRunning = lastRunning.plus(amount, unit);
		return nextRunning;
	}
}
