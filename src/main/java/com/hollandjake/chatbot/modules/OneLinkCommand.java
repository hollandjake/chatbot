package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CONSTANTS;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.message.Text;

import java.util.List;
import java.util.stream.Collectors;

public class OneLinkCommand implements CommandModule {
	private final List<String> COMMAND_REGEXES;
	private final String url;
	private final String message;
	private final Chatbot chatbot;

	public OneLinkCommand(Chatbot chatbot, List<String> commands, String message, String link) {
		this.chatbot = chatbot;
		this.COMMAND_REGEXES = commands.stream().map(CONSTANTS::ACTIONIFY).collect(Collectors.toList());
		this.url = link;
		this.message = message;
	}

	@Override
	public boolean process(Message message) {
		for (MessageComponent component : message.getComponents()) {
			if (component instanceof Text) {
				String text = ((Text) component).getText();
				for (String command : COMMAND_REGEXES) {
					if (text.matches(command)) {
						chatbot.sendMessage(this.message + (url.isEmpty() ? "" : (":\n" + url)));
						return true;
					}
				}
			}
		}
		return false;
	}
}