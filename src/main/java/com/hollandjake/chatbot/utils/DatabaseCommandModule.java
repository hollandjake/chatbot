package com.hollandjake.chatbot.utils;

import com.hollandjake.chatbot.Chatbot;

public abstract class DatabaseCommandModule extends DatabaseModule implements CommandableModule {
	public DatabaseCommandModule(Chatbot chatbot) {
		super(chatbot);
	}
}
