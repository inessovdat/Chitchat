import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class ReceivedMessageRobot extends TimerTask {
	private ChatFrame chat;

	public ReceivedMessageRobot(ChatFrame chat) {
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
	public void run() {
		//URI uri = null;
		if (chat.getStatus()){
			try{ URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
					.addParameter("username", chat.textField.getText())
					.build();
			//String responseBody = null;

			String responseBody = Request.Get(uri)
					.execute()
					.returnContent()
					.asString();

			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new ISO8601DateFormat());

			TypeReference<ArrayList<ReceivedMessage>> typeRef = new TypeReference<ArrayList<ReceivedMessage>>() {};


			ArrayList<ReceivedMessage> received = mapper.readValue(responseBody, typeRef);

			if (!received.isEmpty()){
				for (ReceivedMessage msg  :  received){
					String sender = msg.getSender();
					String text = msg.getMessage();		
					chat.output.append(sender + ":" +  text  + "\n");
					System.out.println("Prejeli ste novo sporoèilo.");
				}
			}
			else {System.out.println("Prejeli niste nobenih novih sporoèil.");

			}
			}catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}


		}
	}
}
