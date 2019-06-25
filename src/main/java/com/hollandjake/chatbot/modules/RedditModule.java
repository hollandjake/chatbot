package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.chatbot.utils.DatabaseModule;
import com.hollandjake.messengerBotAPI.util.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.hollandjake.chatbot.utils.CONSTANTS.GET_RANDOM;

public abstract class RedditModule implements CommandModule, DatabaseModule {

	//region Constants
	protected final Chatbot chatbot;
	protected final DatabaseController db;
	protected int MOD_ID;
	//endregion
	//region Database statements
	protected PreparedStatement GET_SUBREDDITS_STMT;
	protected PreparedStatement GET_RESPONSE_STMT;
	//endregion

	public RedditModule(Chatbot chatbot) {
		this.chatbot = chatbot;
		this.db = chatbot.getDb();
	}

	public void setModId(Connection connection) throws SQLException {
		final PreparedStatement GET_MOD_ID = connection.prepareStatement("SELECT module_id FROM module WHERE module_name = ?");
		GET_MOD_ID.setString(1, getClass().getSimpleName());
		ResultSet resultSet = GET_MOD_ID.executeQuery();
		if (resultSet.next()) {
			MOD_ID = resultSet.getInt("module_id");
		}
	}

	protected String getImage() {
		List<String> subreddits = getSubreddits();
		while (subreddits != null && subreddits.size() > 0) {
			String image = Reddit.getSubredditPicture(GET_RANDOM(subreddits));
			if (image != null) {
				return image;
			}
		}
		return null;
	}

	protected String getResponse() {
		try {
			ResultSet resultSet = GET_RESPONSE_STMT.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("text");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getSubreddits() {
		List<String> subreddits = new ArrayList<>();
		try {
			ResultSet resultSet = GET_SUBREDDITS_STMT.executeQuery();
			while (resultSet.next()) {
				subreddits.add(resultSet.getString("link"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subreddits;
	}
}