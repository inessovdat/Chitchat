import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * Hello ChitChat!
 */
public class App {
	
	public static ArrayList<User> usersList() throws ClientProtocolException, IOException{
		Random r = new Random();
		int s = r.nextInt(600);
		String users = Request.Get("http://chitchat.andrej.com/users?stopcache=" + s )
				.execute()
				.returnContent().asString();

		System.out.println(users);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());

		TypeReference<ArrayList<User>> typeRef = new TypeReference<ArrayList<User>>(){};
		ArrayList<User> users1 = mapper.readValue(users, typeRef);
		return users1;
	}
       
	
	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		try {
			String hello = Request.Get("http://chitchat.andrej.com")
					.execute()
					.returnContent().asString();
			System.out.println(hello);
		} catch (IOException e) {
			e.printStackTrace();
		}

		usersList();

	}

   
    // Prijava na strežnik
    public static void login(String name) throws URISyntaxException, ClientProtocolException, IOException {
    	URI uri = new URIBuilder("http://chitchat.andrej.com/users")
    			.addParameter("username", name)
    			.build();

    	String responseBody = Request.Post(uri)
    			.execute()
    			.returnContent()
    			.asString();

    	System.out.println(responseBody);
    }
    
    // Odjava s strežnika
    public static void logout(String name) throws URISyntaxException, ClientProtocolException, IOException{
    	URI uri = new URIBuilder("http://chitchat.andrej.com/users")
    			.addParameter("username", name)
    			.build();

    	String responseBody = Request.Delete(uri)
    			.execute()
    			.returnContent()
    			.asString();

    	System.out.println(responseBody);
    }
    
    
    // Pošiljanje sporoèil vsem prijavljenim uporabnikom
    public static void sendMessage(String name, Boolean global, String text) throws URISyntaxException, ClientProtocolException, IOException {
    	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
    			.addParameter("username", name)
    			.build();

    	ObjectMapper mapper = new ObjectMapper();
    	mapper.setDateFormat(new ISO8601DateFormat());

    	ReceivedMessage message = new ReceivedMessage();
    	message.setGlobal(true);
    	message.setMessage(text);

    	String msg = mapper.writeValueAsString(message);

    	String responseBody = Request.Post(uri)
    			.bodyString(msg, ContentType.APPLICATION_JSON)
    			.execute()
    			.returnContent()
    			.asString();

    	System.out.println(responseBody);
    }
    
    // Pošiljanje zasebnih sporoèil izbranemu uporabniku
    public static void sendPrivateMessage(String name, Boolean global, String recipient, String text) throws URISyntaxException, ClientProtocolException, IOException {
    	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
    			.addParameter("username", name)
    			.build();

    	ObjectMapper mapper = new ObjectMapper();
    	mapper.setDateFormat(new ISO8601DateFormat());

    	ReceivedMessage message = new ReceivedMessage();
    	message.setGlobal(false);
    	message.setMessage(text);
    	message.setRecipient(recipient);

    	String msg = mapper.writeValueAsString(message);

    	String responseBody = Request.Post(uri)
    			.bodyString(msg, ContentType.APPLICATION_JSON)
    			.execute()
    			.returnContent()
    			.asString();

    	System.out.println(responseBody);
    }
    
    // Sprejemanje sporoèil
    public static void receiveMessage (String name) throws URISyntaxException, ClientProtocolException, IOException{
    	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
    			.addParameter("username", name)
    			.build();

    	String responseBody = Request.Get(uri)
    			.execute()
    			.returnContent()
    			.asString();

    	System.out.println(responseBody);
    }

}


