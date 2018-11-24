package fr.ynov.dap.dap.model;

import java.util.Date;

public class DateTimeTimeZone {

	/**
	 * Current datetime.
	 */
	private Date dateTime;

	/**
	 * Current timezone.
	 */
	private String timeZone;

	/**
	 * @return the dateTime
	 */
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
