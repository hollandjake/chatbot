package com.hollandjake.chatbot;

import com.google.errorprone.annotations.ForOverride;
import com.hollandjake.chatbot.exceptions.MalformedCommandException;
import com.hollandjake.chatbot.modules.Shutdown;
import com.hollandjake.chatbot.modules.*;
import com.hollandjake.chatbot.utils.CommandableModule;
import com.hollandjake.chatbot.utils.DatabaseModule;
import com.hollandjake.messenger_bot_api.API;
import com.hollandjake.messenger_bot_api.message.Image;
import com.hollandjake.messenger_bot_api.message.Message;
import com.hollandjake.messenger_bot_api.message.MessageComponent;
import com.hollandjake.messenger_bot_api.util.Config;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Chatbot extends API {

	private final LocalDateTime startup;
	protected HashMap<String, CommandableModule> modules;
	private int numMessages = 0;

	public Chatbot(Config config) {
		super(config);
		startup = LocalDateTime.now();
	}

	protected abstract void loadModules();

	public void sendMessageWithImage(String text, String imageUrl) {
		sendMessageWithImage(text, Image.fromUrl(config, imageUrl));
	}

	public void sendMessageWithImage(String text, MessageComponent image) {
		Message message = Message.fromString(getThread(), getMe(), text);
		message.getComponents().add(image);
		sendMessage(message);
	}

	public boolean containsCommand(Message message) {
		for (MessageComponent component : message.getComponents()) {
			for (CommandableModule module : modules.values()) {
				if (!module.getMatch(component).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void newMessage(Message message) {
		numMessages++;
		try {
			for (CommandableModule module : modules.values()) {
				if (module.process(message)) {
					break;
				}
			}
		} catch (MalformedCommandException e) {
			sendMessage("There seems to be an issue with your command");
		}
	}

	@Override
	public void databaseReload(Connection connection) throws SQLException {
		if (debugging()) {
			System.out.println("Database reload");
		}
		for (CommandableModule module : modules.values()) {
			if (module instanceof DatabaseModule) {
				((DatabaseModule) module).prepareStatements(connection);
			}
		}
	}

	@Override
	public void loaded() {
		if (debugging()) {
			System.out.println("Loaded");
		}
		modules = new HashMap<>();
		modules.put("Commands", new OneLinkCommand(this,
				Arrays.asList("commands", "help"),
				"A list of commands can be found at",
				"https://github.com/hollandjake/Chatbot/blob/master/README.md"));
		modules.put("Github", new OneLinkCommand(this,
				Arrays.asList("github", "repo"),
				"Github repository",
				"https://github.com/hollandjake/Chatbot"
		));
		modules.put("Ping", new Ping(this));
		modules.put("Reddit", new Reddit(this));
		modules.put("Shutdown", new Shutdown(this));
		modules.put("Stats", new Stats(this));

		loadModules();
	}

	@ForOverride
	public String getVersion() {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = null;
		try {
			try {
				model = reader.read(new FileReader(new File("pom.xml")));
			} catch (IOException e) {
				model = reader.read(
						new InputStreamReader(
								getClass().getResourceAsStream(
										"/META-INF/maven/de.scrum-master.stackoverflow/aspectj-introduce-method/pom.xml"
								)
						)
				);
			}
		} catch (XmlPullParserException | IOException ignore) {
		}
		if (model != null) {
			return model.getVersion();
		}
		return "";
	}

	public HashMap<String, CommandableModule> getModules() {
		return modules;
	}

	public int getNumMessages() {
		return numMessages;
	}

	public LocalDateTime getStartup() {
		return startup;
	}
}