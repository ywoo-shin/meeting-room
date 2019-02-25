package com.pay.room.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
	public static String convertDateToString(Date date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return formatter.format(localDate);
	}

	public static Date convertStringToDate(String date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDate = LocalDate.parse(date, formatter);
		return convertLocalDateToDate(localDate);
	}

	public static Date convertStringToDate(String date) {
		return convertStringToDate(date, "yyyy-MM-dd");
	}

	public static LocalDate convertStringToLocalDate(String date) {
		return convertDateToLocalDate(convertStringToDate(date));
	}

	public static Date convertLocalDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static Date convertLocalDateToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convertDateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date getNow() {
		return convertLocalDateToDate(LocalDate.now());
	}
}
