package org.emergent.android.weave.client;

import org.apache.http.client.HttpResponseException;

public class WeaveException extends Exception {

	private final WeaveException.ExceptionType m_type;
	
	public WeaveException() {
		this(WeaveException.ExceptionType.GENERAL);
	}
	
	public WeaveException(WeaveException.ExceptionType type) {
		m_type = type;
	}
	
	public WeaveException(String message) {
		this(WeaveException.ExceptionType.GENERAL, message);
	}
	
	public WeaveException(WeaveException.ExceptionType type, String message) {
		super(message);
		m_type = type;
	}
	
	public WeaveException(Throwable cause) {
		this(WeaveException.ExceptionType.GENERAL, cause);
	}
	
	public WeaveException(WeaveException.ExceptionType type, Throwable cause) {
		super(cause);
		m_type = type;
	}
	
	public WeaveException(String message, Throwable cause) {
		this(WeaveException.ExceptionType.GENERAL, message, cause);
	}
	
	public WeaveException(WeaveException.ExceptionType type, String message, Throwable cause) {
		super(message, cause);
		m_type = type;
	}
	
	public WeaveException.ExceptionType getType() {
		return m_type;
	}
	
	public static boolean isAuthFailure(HttpResponseException e) {
		int statusCode = e.getStatusCode();
		if (WeaveConstants.UNAUTHORIZED_HTTP_STATUS_CODE == statusCode) {
			return true;
		}
		return false;
	}
	
	public enum ExceptionType {
		GENERAL,
		BACKOFF;
	}
}
