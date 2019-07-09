package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messenger_bot_api.message.Message;
import com.hollandjake.messenger_bot_api.message.MessageComponent;
import com.hollandjake.messenger_bot_api.message.Text;

import static com.hollandjake.chatbot.utils.CONSTANTS.ACTIONIFY;

public class Ping extends CommandModule {
	private final String PING_REGEX = ACTIONIFY("ping");

	public Ping(Chatbot chatbot) {
		super(chatbot);
	}

	@Override
	public boolean process(Message message) {
		for (MessageComponent component : message.getComponents()) {
			String match = getMatch(component);
			if (match.equals(PING_REGEX)) {
				if (Math.random() < 0.3) {
					chatbot.sendMessageWithImage("Pong! \uD83C\uDFD3", "https://www.rightthisminute.com/sites/default/files/styles/twitter_card/public/videos/images/munchkin-teddy-bear-dog-ping-pong-video.jpg?itok=ajJWbxY6");
				} else {
					chatbot.sendMessage("Pong! \uD83C\uDFD3");
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String getMatch(MessageComponent component) {
		if (component instanceof Text) {
			String text = ((Text) component).getText();
			if (text.matches(PING_REGEX)) {
				return PING_REGEX;
			}
		}
		return "";
	}
}