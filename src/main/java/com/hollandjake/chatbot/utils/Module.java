package com.hollandjake.chatbot.utils;

import com.hollandjake.chatbot.Chatbot;

public abstract class Module {
	protected final Chatbot chatbot;

	protected Module(Chatbot chatbot) {
		this.chatbot = chatbot;
	}
}
