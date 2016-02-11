package com.athena.meerkat.controller;

/**
 * Represent the status and message that will be returned from service
 * 
 * @author Tran Ho
 * 
 */
public class ServiceResult {
	private Status status;
	private String message;
	private Object returnedVal;

	public ServiceResult(Status s, String msg) {
		status = s;
		message = msg;
	}

	public ServiceResult(Status s, String msg, Object rVal) {
		status = s;  
		message = msg;
		returnedVal = rVal;
	}

	public Object getReturnedVal() {
		return returnedVal;
	}

	public String getMessage() {
		return message;
	}

	public Status getStatus() {
		return status;
	}

	public enum Status {
		DONE, FAILED, ERROR,
	}
}
