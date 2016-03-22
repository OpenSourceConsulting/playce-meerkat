package com.athena.meerkat.controller.web.provisioning;

import java.io.BufferedReader;

/**
 * <pre>
 * Process 에 대한 측정 작업을 처리하는 클래스.
 * </pre>
 * @author Bong-Jin Kwon
 */
public abstract class ProcessAction {

	/**
	 * <pre>
	 * create ProcessAction instance
	 * </pre>
	 */
	public ProcessAction() {
		
	}
	
	/**
	 * <pre>
	 * Process 에 대한 처리 작업을 정의.
	 * </pre>
	 * @param p
	 * @param br
	 * @return
	 * @throws Exception
	 */
	public abstract String actionPerform(Process p, BufferedReader br) throws Exception;

}
//end of ProcessAction.java