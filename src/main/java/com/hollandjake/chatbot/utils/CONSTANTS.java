package com.hollandjake.chatbot.utils;

import java.time.format.DateTimeFormatter;

public interface CONSTANTS {

	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

	static String ACTIONIFY(String arg) {
		return "(?i)^!\\s*" + arg + "$";
	}

	static String ACTIONIFY_CASE(String arg) {
		return "^!\\s*" + arg + "$";
	}

	static String DEACTIONIFY(String regex) {
		return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
	}

	static String DEACTIONIFY_CASE(String regex) {
		return regex.replaceAll("\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
	}
}
