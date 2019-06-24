package com.hollandjake.chatbot;

import com.google.errorprone.annotations.ForOverride;
import com.hollandjake.chatbot.exceptions.MalformedCommandException;
import com.hollandjake.chatbot.modules.OneLinkCommand;
import com.hollandjake.chatbot.modules.Ping;
import com.hollandjake.chatbot.modules.Shutdown;
import com.hollandjake.chatbot.modules.Stats;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.chatbot.utils.DatabaseModule;
import com.hollandjake.messengerBotAPI.API;
import com.hollandjake.messengerBotAPI.message.Image;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.util.Config;
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
	protected HashMap<String, CommandModule> modules;
	private int numMessages = 0;

	public Chatbot(Config config) {
		super(config);
		startup = LocalDateTime.now();
	}

	protected abstract void loadModules();

	public void sendMessageWithImage(String text, String imageUrl) {
		Message message = Message.fromString(getThread(), getMe(), text);
		message.getComponents().add(Image.fromUrl(imageUrl));
		sendMessage(message);
	}

	@Override
	public void newMessage(Message message) {
		numMessages++;
		if (debugging()) {
			message.print();
		}
		try {
			for (CommandModule module : modules.values()) {
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
		System.out.println("Database reload");
		for (CommandModule module : modules.values()) {
			if (module instanceof DatabaseModule) {
				((DatabaseModule) module).prepareStatements(connection);
			}
		}
	}

	@Override
	public void loaded() {
		System.out.println("Loaded");
		modules = new HashMap<>();
		modules.put("Shutdown", new Shutdown(this));
		modules.put("Stats", new Stats(this));
		modules.put("Ping", new Ping(this));
		modules.put("Github", new OneLinkCommand(this,
				Arrays.asList("github", "repo"),
				"Github repository",
				"https://github.com/hollandjake/Chatbot"
		));
		modules.put("Commands", new OneLinkCommand(this,
				Arrays.asList("commands", "help"),
				"A list of commands can be found at",
				"https://github.com/hollandjake/Chatbot/blob/master/README.md"));

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

	public int getNumMessages() {
		return numMessages;
	}

	public LocalDateTime getStartup() {
		return startup;
	}
}