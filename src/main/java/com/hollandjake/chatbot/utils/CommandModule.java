package com.hollandjake.chatbot.utils;

import com.hollandjake.chatbot.exceptions.MalformedCommandException;
import com.hollandjake.messengerBotAPI.message.Message;

public interface CommandModule {
	/**
	 * Used to apply the module to the message and act upon its response
	 *
	 * @param message {@link Message}
	 * @return {@link Boolean}
	 */
	boolean process(Message message) throws MalformedCommandException;
}
