import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class UserRobot extends TimerTask {
	private ChatFrame chat;
	
	public UserRobot(ChatFrame chat){
		this.chat = chat;
	}
	
	/**
	 * Activate the robot!
	 */
	
	public void activate() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	@Override
	public void run(){
		try {String responseBody = Request.Get("http://chitchat.andrej.com/users")
				.execute()
				.returnContent()
				.asString();


		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());

		TypeReference<List<User>> typeRef = new TypeReference<List<User>>() {};

		List<User>loggedIn = mapper.readValue(responseBody, typeRef);

		chat.activeUsers.setText(null);

		if (chat.getStatus()){
			for (User somebody : loggedIn ){
				String username = somebody.getUsername();
				Date lastActive = somebody.getLastActive();
				String response = username  +  "(" +  lastActive + ")" + "\n";
				chat.activeUsers.append(response);
			}
		}
		else {chat.activeUsers.setText(null);
		//System.out.println("Na serverju ni aktivnih uporabnikov.");
		}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}