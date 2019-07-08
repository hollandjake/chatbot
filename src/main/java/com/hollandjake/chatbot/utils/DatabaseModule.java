package com.hollandjake.chatbot.utils;

import com.google.errorprone.annotations.ForOverride;
import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.messengerBotAPI.util.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DatabaseModule extends Module {
	protected final DatabaseController db;
	protected Integer modId;
	protected PreparedStatement RANDOM_IMAGE_STMT;
	protected PreparedStatement RANDOM_TEXT_STMT;

	protected DatabaseModule(Chatbot chatbot) {
		super(chatbot);
		this.db = chatbot.getDb();
	}

	@ForOverride
	public void prepareStatements(Connection connection) throws SQLException {
		updateModuleId(connection);
		prepareRandomImage(connection);
		prepareRandomText(connection);
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
				"   image_id," +
				"   data " +
				"FROM image " +
				"JOIN response_image ri on image.image_id = ri.image_id " +
				"WHERE module_id = " + modId +
				" ORDER BY RAND() " +
				"LIMIT 1");
	}

	private void prepareRandomText(Connection connection) throws SQLException {
		RANDOM_TEXT_STMT = connection.prepareStatement("" +
				"SELECT" +
				"   text " +
				"FROM text " +
				"JOIN response_text rt on text.text_id = rt.text_id " +
				"WHERE module_id = " + modId +
				" ORDER BY RAND() " +
				"LIMIT 1");
	}

	public Integer getModId() {
		return modId;
	}
}
