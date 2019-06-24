package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.message.Text;

import static com.hollandjake.chatbot.utils.CONSTANTS.ACTIONIFY;

public class Ping implements CommandModule {
	private final String PING_REGEX = ACTIONIFY("ping");
	private final Chatbot chatbot;

	public Ping(Chatbot chatbot) {
		this.chatbot = chatbot;
	}

	@Override
	public boolean process(Message message) {
		for (MessageComponent component : message.getComponents()) {
			if (component instanceof Text) {
				String text = ((Text) component).getText();
				if (text.matches(PING_REGEX)) {
					if (Math.random() < 1) {
						chatbot.sendMessageWithImage("Pong! \uD83C\uDFD3", "https://www.rightthisminute.com/sites/default/files/styles/twitter_card/public/videos/images/munchkin-teddy-bear-dog-ping-pong-video.jpg?itok=ajJWbxY6");
					} else {
						chatbot.sendMessage("Pong! \uD83C\uDFD3");
					}
					return true;
				}
			}
		}
		return false;
	}
}