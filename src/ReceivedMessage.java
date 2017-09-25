import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReceivedMessage {
	private Boolean global;
	private String recipient;
	private  String sender;
	private String text;
	private Date sentAt;

	public ReceivedMessage() {}

	@Override
	public String toString() {
		return "ReceivedMessage [global=" + global + ", recipient=" + recipient + ", message=" + text + ", sentAt="
				+ sentAt + "]";
	}
	@JsonProperty("global")
	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@JsonProperty("text")
	public String getMessage() {
		return text;
	}

	public void setMessage(String message) {
		this.text = message;
	}

	@JsonProperty("sent_at")
	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

}
