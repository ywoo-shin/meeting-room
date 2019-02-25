package com.pay.room.web;

import com.pay.room.common.exception.PayException;
import com.pay.room.web.protocol.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.pay.room.common.exception.PayResultCode.FAIL;
import static com.pay.room.common.exception.PayResultCode.UNKNOWN_EXCEPTION;
import static com.pay.room.web.protocol.PayResponse.fail;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(PayException.class)
	public PayResponse handleToastCloudException(PayException ex) {
		log.error(ex.toString(), ex);
		return fail(ex);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public PayResponse handleException(MethodArgumentNotValidException ex) {
		log.error(ex.toString(), ex);
		return bindingResultResponse(ex.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	public PayResponse handleException(BindException ex) {
		log.error(ex.toString(), ex);
		return bindingResultResponse(ex.getBindingResult());
	}

	private PayResponse bindingResultResponse(BindingResult bindingResult) {
		StringBuilder builder = new StringBuilder();
		for (FieldError error : bindingResult.getFieldErrors()) {
			builder.append(String.format("%s: %s. ", error.getField(), error.getDefaultMessage()));
		}

		for (ObjectError error : bindingResult.getGlobalErrors()) {
			builder.append(String.format("%s: %s. ", error.getObjectName(), error.getDefaultMessage()));
		}
		return fail(HttpStatus.BAD_REQUEST, FAIL, builder.toString());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public PayResponse handleException(MissingServletRequestParameterException ex) {
		log.error(ex.toString(), ex);
		return fail(HttpStatus.BAD_REQUEST, FAIL, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public PayResponse handleException(Exception ex) {
		log.error(ex.toString(), ex);
		return fail(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_EXCEPTION, ex.getMessage());
	}
}
