package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.exceptions.MalformedCommandException;
import com.hollandjake.chatbot.utils.CONSTANTS;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.message.Text;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Shutdown extends CommandModule {
	private static final String SHUTDOWN_REGEX = CONSTANTS.ACTIONIFY("shutdown (\\d*)");
	private final String code = Integer.toString((new Random()).nextInt(99999));

	public Shutdown(Chatbot chatbot) {
		super(chatbot);
	}

	@Override
	public boolean process(Message message) throws MalformedCommandException {
		for (MessageComponent component : message.getComponents()) {
			if (component instanceof Text) {
				String text = ((Text) component).getText();
				if (text.matches(SHUTDOWN_REGEX)) {
					Matcher matcher = Pattern.compile(SHUTDOWN_REGEX).matcher(text);
					if (matcher.find() && matcher.group(1).equals(code)) {
						chatbot.quit();
						return true;
					} else {
						throw new MalformedCommandException();
					}
				}
			}
		}
		return false;
	}
}
