package com.hollandjake.chatbot.utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public interface CONSTANTS {
	Random RANDOM = new Random();

	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

	static String ACTIONIFY(String arg) {
		return "(?i)^!\\s*" + arg + "$";
	}

	static String ACTIONIFY_CASE(String arg) {
		return "^!\\s*" + arg + "$";
	}

	static String GET_PAGE_SOURCE(String url) {
		try {
			return Unirest.get(url).header("User-agent", "Chatbot").asString().getBody();
		} catch (UnirestException e) {
			System.out.println("Page doesn't exist");
			e.printStackTrace();
			return "";
		}
	}

	static <T> T GET_RANDOM(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}
}
