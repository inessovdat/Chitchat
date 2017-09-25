public class ChitChat {

	public static void main(String[] args) {
		ChatFrame chatFrame = new ChatFrame();
		UserRobot onlineUsers = new UserRobot(chatFrame);
		onlineUsers.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
