import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.modules.OneLinkCommand;
import com.hollandjake.messenger_bot_api.util.Config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

class ChatbotTest extends Chatbot {
	public ChatbotTest(Config config) throws SQLException {
		super(config);
	}

	public static void main(String[] args) throws SQLException {
		String configFile = args.length > 0 ? args[0] : null;
		new ChatbotTest(new Config(configFile));
	}

	@Override
	protected void loadModules(Connection connection) throws SQLException {
		modules.put("Google", new OneLinkCommand(this,
				Arrays.asList("google"),
				"Google",
				"https://www.google.com"
		));
	}
}