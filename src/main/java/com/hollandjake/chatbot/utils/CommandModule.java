package com.hollandjake.chatbot.utils;

import com.hollandjake.chatbot.Chatbot;

public abstract class CommandModule extends Module implements CommandableModule {
	protected CommandModule(Chatbot chatbot) {
		super(chatbot);
	}
}
