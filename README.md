# Chatbot [![Release](https://jitpack.io/v/hollandjake/chatbot.svg)](https://jitpack.io/#hollandjake/chatbot) [![](https://jitci.com/gh/hollandjake/chatbot/svg)](https://jitci.com/gh/hollandjake/chatbot)

Facebook messenger group chat bot. Expands upon the [MessengerBotAPI](https://github.com/hollandjake/messenger-bot-api) system.

## Usage
While this class is not instantiable it provides a base class to extend.

```java
class ChatbotTest extends Chatbot {
	public ChatbotTest(Config config) throws SQLException {
		super(config);
	}

	public static void main(String[] args) throws SQLException {
		new ChatbotTest(new Config());
	}

	@Override
	protected void loadModules(Connection connection) {
		modules.put("Github", new OneLinkCommand(this,
				Arrays.asList("github", "repo", "git"),
				"Github repository",
				"https://github.com/hollandjake/chatbot"
		));
	}
}
```

## Making modules
You can make your own custom modules using the `CommandModule` and `DatabaseModule` interfaces. `CommandModule` is called every time a new message arrives and its method `process(Message message)` is called. If the program required Database access then it must implement the DatabaseModule and its method `prepareStatements(Connection connection)`. 