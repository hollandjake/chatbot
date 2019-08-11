package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CONSTANTS;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messenger_bot_api.message.Message;
import com.hollandjake.messenger_bot_api.message.MessageComponent;
import com.hollandjake.messenger_bot_api.message.Text;

import java.util.List;
import java.util.stream.Collectors;

public class OneLinkCommand extends CommandModule {
	private final List<String> COMMAND_REGEXES;
	private final String url;
	private final String message;

	public OneLinkCommand(Chatbot chatbot, List<String> commands, String message, String link) {
		super(chatbot);
		COMMAND_REGEXES = commands.stream().map(CONSTANTS::ACTIONIFY).collect(Collectors.toList());
		url = link;
		this.message = message;
	}

	@Override
	public boolean process(Message message) {
		for (MessageComponent component : message.getComponents()) {
			String match = getMatch(component);
			if (!match.isEmpty()) {
				for (String command : COMMAND_REGEXES) {
					if (match.equals(command)) {
						chatbot.sendMessage(this.message + (url.isEmpty() ? "" : (":\n" + url)));
					}
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
			for (String command : COMMAND_REGEXES) {
				if (text.matches(command)) {
					return command;
				}
			}
		}
		return "";
	}
}