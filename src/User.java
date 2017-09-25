import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String username;
	private Date lastActive;

	private User() {}

	@Override
	public String toString() {
		return "User [username=" + username + ", lastActive=" + lastActive + "]";
	}

	public String getUsername() {
		return username;
	}

	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("last_active")
	public Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
}
