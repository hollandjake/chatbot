package com.hollandjake.chatbot.utils;

import com.google.errorprone.annotations.ForOverride;
import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.messengerBotAPI.message.Image;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.util.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DatabaseModule extends Module {
	protected final DatabaseController db;
	protected Integer modId;
	private PreparedStatement RANDOM_IMAGE_STMT;
	private PreparedStatement RANDOM_RESPONSE_STMT;

	protected DatabaseModule(Chatbot chatbot) {
		super(chatbot);
		this.db = chatbot.getDb();
	}

	@ForOverride
	public void prepareStatements(Connection connection) throws SQLException {
		updateModuleId(connection);
		prepareRandomImage(connection);
		prepareRandomResponse(connection);
	}

	private void updateModuleId(Connection connection) throws SQLException {
		PreparedStatement GET_MOD_ID = connection.prepareStatement("SELECT module_id FROM module WHERE module_name = ?");
		GET_MOD_ID.setString(1, getClass().getSimpleName());
		ResultSet resultSet = GET_MOD_ID.executeQuery();
		if (resultSet.next()) {
			modId = resultSet.getInt("module_id");
		}
	}

	private void prepareRandomImage(Connection connection) throws SQLException {
		RANDOM_IMAGE_STMT = connection.prepareStatement("" +
				"SELECT" +
				"   i.image_id," +
				"   data " +
				"FROM image i " +
				"JOIN response_image ri on i.image_id = ri.image_id " +
				"WHERE module_id = " + modId +
				" ORDER BY RAND() " +
				"LIMIT 1");
	}

	private void prepareRandomResponse(Connection connection) throws SQLException {
		RANDOM_RESPONSE_STMT = connection.prepareStatement("" +
				"SELECT" +
				"   text " +
				"FROM text t " +
				"JOIN response_text rt on t.text_id = rt.text_id " +
				"WHERE module_id = " + modId +
				" ORDER BY RAND() " +
				"LIMIT 1");
	}

	public Integer getModId() {
		return modId;
	}

	protected MessageComponent getRandomImage() {
		try {
			chatbot.checkDbConnection();
			ResultSet resultSet = RANDOM_IMAGE_STMT.executeQuery();
			if (resultSet.next()) {
				return Image.fromResultSet(chatbot.getConfig(), resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getRandomResponse() {
		try {
			db.checkConnection();
			ResultSet resultSet = RANDOM_RESPONSE_STMT.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("text");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
