package com.athena.meerkat.controller.common;

public class State {
	/* State of machine */
	public static int MACHINE_STATE_STARTED = 0;
	public static int MACHINE_STATE_SHUTDOWN = 1;
	public static int MACHINE_STATE_IDLE = 2;

	/* State of application */
	public static int APP_STATE_STARTED = 1;
	public static int APP_STATE_STOPPED = 2;

	/* State of application */
	public static int TOMCAT_STATE_STARTED = 1;
	public static int TOMCAT_STATE_STOPPED = 2;
}
