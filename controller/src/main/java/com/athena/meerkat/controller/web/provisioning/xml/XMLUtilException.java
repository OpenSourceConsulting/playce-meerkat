/**
 * 
 */
package com.athena.meerkat.controller.web.provisioning.xml;

/**
 * XMLUtil 에서 발생되는 Exception.
 * @author BongJin Kwon
 *
 */
public class XMLUtilException extends RuntimeException {

	/**
	 * 
	 */
	public XMLUtilException() {
	}

	/**
	 * @param message
	 */
	public XMLUtilException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public XMLUtilException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XMLUtilException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public XMLUtilException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
//end of XMLUtilException.java