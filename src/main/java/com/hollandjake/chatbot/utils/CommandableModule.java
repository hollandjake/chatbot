package com.hollandjake.chatbot.utils;

import com.hollandjake.messenger_bot_api.message.Message;
import com.hollandjake.messenger_bot_api.message.MessageComponent;

public interface CommandableModule {
	/**
	 * Used to apply the module to the message and act upon its response
	 *
	 * @param message {@link Message}
	 * @return {@link Boolean}
	 */
	boolean process(Message message) throws Exception;

	/**
	 * Used to apply the module to the message and return which regex matches
	 *
	 * @param component {@link MessageComponent}
	 * @return {@link String}
	 */
	String getMatch(MessageComponent component);
}
