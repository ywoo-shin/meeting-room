package com.pay.room.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@ToString
@Accessors(chain = true)
public enum PayResultCode {
	SUCCESS(OK, 0, "SUCCESS"),
	FAIL(INTERNAL_SERVER_ERROR, -1, "FAIL"),
	UNKNOWN_EXCEPTION(INTERNAL_SERVER_ERROR, -2, "UNKNOWN_EXCEPTION"),
	JSON_PARSE_FAIL(INTERNAL_SERVER_ERROR, -3, "JSON_PARSE_FAIL"),
	PERMISSION_DENIED(UNAUTHORIZED, -4, "Permission denied."),

	REQUEST_PARAMETER_CONVERT_FAIL(BAD_REQUEST, 1000, "Request parameter convert fail. Check your content body."),
	INVALID_PARAMETER(BAD_REQUEST, 1001, "Parameter is not valid: {}"),

	RESPONSE_INSTANCE_CREATE_FAIL(INTERNAL_SERVER_ERROR, 2000, "Fail to create the instance of response."),

	REPOSITORY_DATA_NOT_EXIST(NO_CONTENT, 3000, "Data does not exist. Check your input."),
	RESERVE_FAIL_ALREADY_RESERVATION(ALREADY_REPORTED, 3001, "Already reserved."),
	RESERVATION_TIME_INVALID_FORMAT(BAD_REQUEST, 3002, "The format of reservation time is invalid. It has to be like 09:00."),
	RESERVATION_TIME_INVALID_INTERVAL(BAD_REQUEST, 3003, "The interval of reservation time is invalid."),
	RESERVATION_TIME_INVALID(BAD_REQUEST, 3004, "The reservation time is invalid. Check your input.");

	private HttpStatus httpStatus;
	private int code;
	private String message;

	@Setter
	private Object[] objects;

	PayResultCode(HttpStatus httpStatus, int code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return MessageFormatter.arrayFormat(message, objects).getMessage();
	}
}
