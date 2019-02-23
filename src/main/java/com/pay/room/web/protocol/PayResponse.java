package com.pay.room.web.protocol;

import com.pay.room.common.exception.PayException;
import com.pay.room.common.exception.PayResultCode;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

import static com.pay.room.common.exception.PayResultCode.RESPONSE_INSTANCE_CREATE_FAIL;
import static com.pay.room.common.exception.PayResultCode.SUCCESS;
import static com.pay.room.web.protocol.PayResponseHeader.toFail;
import static com.pay.room.web.protocol.PayResponseHeader.toSuccess;

@ToString
@NoArgsConstructor
public class PayResponse implements Serializable {
	private static final long serialVersionUID = 796123802118802413L;

	@Getter
	@Setter(AccessLevel.PROTECTED)
	private PayResponseHeader header;

	private PayResponse(PayResponseHeader header) {
		this.header = header;
	}

	public static PayResponse success() {
		return getResult(PayResponse.class, SUCCESS);
	}

	public static <T extends PayResponse> T success(Class<T> clazz) {
		return getResult(clazz, SUCCESS);
	}

	private static <T extends PayResponse> T getResult(Class<T> clazz, PayResultCode resultCode) {
		try {
			T result = clazz.newInstance();
			result.setHeader(resultCode == SUCCESS
							 ? toSuccess()
							 : toFail(resultCode));
			return result;

		} catch (Exception ex) {
			throw PayException.getInstance(RESPONSE_INSTANCE_CREATE_FAIL, ex);
		}
	}

	public static PayResponse fail(PayException payException) {
		return getResult(PayResponse.class, payException.getResultCode());
	}

	public static PayResponse fail(PayResultCode resultCode) {
		return getResult(PayResponse.class, resultCode);
	}

	public static <T extends PayResponse> T fail(Class<T> clazz, PayResultCode resultCode) {
		return getResult(clazz, resultCode);
	}

	public static PayResponse fail(BindingResult bindResult) {
		return new PayResponse(PayResponseHeader.toFail(bindResult));
	}

	public static PayResponse fail(HttpStatus httpStatus, PayResultCode resultCode, String message) {
		return new PayResponse(PayResponseHeader.toFail(httpStatus, resultCode.getCode(), message));
	}
}
