package fr.ynov.dap.dap.model;

import java.util.ArrayList;

public class MicrosoftEvent {

	/**
	 * Store event id.
	 */
	private String id;

	/**
	 * Store event subject.
	 */
	private String subject;

	/**
	 * Store event organizer.
	 */
	private Recipient organizer;

	/**
	 * Store starting date.
	 */
	private DateTimeTimeZone start;

	/**
	 * Store ending date.
	 */
	private DateTimeTimeZone end;

	/**
	 * List of every attendee for current event.
	 */
	private ArrayList<MicrosoftAttendee> attendees;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the organizer
	 */
	public Recipient getOrganizer() {
		return organizer;
	}

	/**
	 * @param organizer the organizer to set
	 */
	public void setOrganizer(Recipient organizer) {
		this.organizer = organizer;
	}

	/**
	 * @return the start
	 */
	public DateTimeTimeZone getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(DateTimeTimeZone start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public DateTimeTimeZone getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(DateTimeTimeZone end) {
		this.end = end;
	}

	/**
	 * @return the attendees
	 */
	public ArrayList<MicrosoftAttendee> getAttendees() {
		return attendees;
	}

	/**
	 * @param attendees the attendees to set
	 */
	public void setAttendees(ArrayList<MicrosoftAttendee> attendees) {
		this.attendees = attendees;
	}

}
