package com.hollandjake.chatbot.utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseModule {
	void prepareStatements(Connection connection) throws SQLException;
}