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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public XMLUtilException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public XMLUtilException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XMLUtilException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated constructor stub
	}

}
//end of XMLUtilException.java