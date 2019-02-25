package com.pay.room.web.protocol;

import com.pay.room.common.exception.PayResultCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static com.pay.room.common.exception.PayResultCode.REQUEST_PARAMETER_CONVERT_FAIL;
import static com.pay.room.common.exception.PayResultCode.SUCCESS;

@Getter
@NoArgsConstructor
@ToString
public class PayResponseHeader {
	private int httpStatus;
	private int code;
	private String message;

	private PayResponseHeader(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private PayResponseHeader(PayResultCode resultCode) {
		this.httpStatus = resultCode.getHttpStatus().value();
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	private PayResponseHeader(HttpStatus httpStatus, int code, String message) {
		this(code, message);
		this.httpStatus = httpStatus.value();
	}

	protected static PayResponseHeader toSuccess() {
		return new PayResponseHeader(SUCCESS);
	}

	protected static PayResponseHeader toFail(HttpStatus httpStatus, int code, String message) {
		return new PayResponseHeader(httpStatus, code, message);
	}

	protected static PayResponseHeader toFail(PayResultCode resultCode) {
		return new PayResponseHeader(resultCode);
	}

	protected static PayResponseHeader toFail(int code, String message) {
		return new PayResponseHeader(code, message);
	}

	protected static PayResponseHeader toFail(BindingResult bindingResult) {
		return new PayResponseHeader(REQUEST_PARAMETER_CONVERT_FAIL.getCode(), getBindErrorMessage(bindingResult));
	}

	private static String getBindErrorMessage(BindingResult bindingResult) {
		List<String> messages = new ArrayList<>();
		List<FieldError> errors = bindingResult.getFieldErrors();
		int errorIndex = 1;
		for (FieldError fieldError : errors) {
			messages.add("[Error " + errorIndex + "] " + "Request parameter '" + fieldError.getRejectedValue() + "' is invalid data type or format for '" + fieldError.getField() + "' field.");
			errorIndex++;
		}
		return REQUEST_PARAMETER_CONVERT_FAIL.getMessage() + " " + StringUtils.join(messages, ", ");
	}
}
