import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.modules.OneLinkCommand;
import com.hollandjake.messenger_bot_api.util.Config;

import java.util.Arrays;

class ChatbotTest extends Chatbot {
	public ChatbotTest(Config config) {
		super(config);
	}

	public static void main(String[] args) {
		String configFile = args.length > 0 ? args[0] : null;
		new ChatbotTest(new Config(configFile));
	}

	@Override
	protected void loadModules() {
		modules.put("Github", new OneLinkCommand(this,
				Arrays.asList("github", "repo"),
				"Github repository",
				"https://github.com/hollandjake/Chatbot"
		));
	}
}