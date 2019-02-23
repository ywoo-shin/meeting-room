package com.pay.room.common.exception;

import lombok.Getter;

public class PayException extends RuntimeException {
	private static final long serialVersionUID = 8678202868437386736L;

	@Getter
	private final PayResultCode resultCode;
	private Throwable cause;

	private PayException(PayResultCode resultCode, Object... objects) {
		this.resultCode = resultCode;
		this.resultCode.setObjects(objects);
	}

	private PayException(PayResultCode resultCode, Throwable cause) {
		this(resultCode);
		this.cause = cause;
		initCause(cause);
	}

	public static PayException getInstance(PayResultCode resultCode, Object... objects) {
		for (Object o : objects) {
			if ( o instanceof Throwable ) {
				return new PayException(resultCode, (Throwable)o);
			}
			break;
		}
		return new PayException(resultCode, objects);
	}

	@Override
	public String getMessage() {
		return resultCode.getMessage();
	}
}
