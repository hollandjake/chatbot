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

	protected DatabaseModule(Chatbot chatbot) {
		super(chatbot);
		this.db = chatbot.getDb();
	}

	@ForOverride
	public void prepareStatements(Connection connection) throws SQLException {
		updateModuleId(connection);
	}

	protected void updateModuleId(Connection connection) throws SQLException {
		PreparedStatement GET_MOD_ID = connection.prepareStatement("SELECT module_id FROM module WHERE module_name = ?");
		GET_MOD_ID.setString(1, getClass().getSimpleName());
		ResultSet resultSet = GET_MOD_ID.executeQuery();
		if (resultSet.next()) {
			modId = resultSet.getInt("module_id");
		}
	}

	public Integer getModId() {
		return modId;
	}
}
