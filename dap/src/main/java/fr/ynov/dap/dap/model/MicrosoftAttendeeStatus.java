package fr.ynov.dap.dap.model;

import java.util.Date;

public class MicrosoftAttendeeStatus {

	/**
	 * Current user status. e.g. None, Organizer, TentativelyAccepted, Accepted,
	 * Declined, NotResponded
	 */
	private String response;

	/**
	 * Store response time.
	 */
	private Date time;

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

}
