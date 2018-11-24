package fr.ynov.dap.dap.model;

public class MicrosoftAttendee {

	private String type;

	/**
	 * Current attendee status.
	 */
	private MicrosoftAttendeeStatus status;

	/**
	 * Current attendee information.
	 */
	private EmailAddress emailAddress;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public MicrosoftAttendeeStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(MicrosoftAttendeeStatus status) {
		this.status = status;
	}

	/**
	 * @return the emailAddress
	 */
	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(EmailAddress emailAddress) {
		this.emailAddress = emailAddress;
	}

}
