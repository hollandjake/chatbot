package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CONSTANTS;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.message.Text;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hollandjake.chatbot.utils.CONSTANTS.ACTIONIFY;

public class Reddit implements CommandModule {
	//region Constants
	private final String REDDITS_REGEX = ACTIONIFY("reddits");
	private final Chatbot chatbot;
	//endregion

	public Reddit(Chatbot chatbot) {
		this.chatbot = chatbot;
	}

	public static String getSubredditPicture(String subreddit) {
		//Get reddit path
		String redditPath = "https://www.reddit.com/r/" + subreddit + "/random.json";

		String data = CONSTANTS.GET_PAGE_SOURCE(redditPath);
		Matcher matcher = Pattern.compile("https://i\\.redd\\.it/\\S+?\\.jpg").matcher(data);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}

	//region Overrides
	@Override
	public boolean process(Message message) {
		for (MessageComponent component : message.getComponents()) {
			if (component instanceof Text) {
				String text = ((Text) component).getText();
				if (text.matches(REDDITS_REGEX)) {
					StringBuilder response = new StringBuilder("Reddits currently in use\n");
					HashMap<String, CommandModule> modules = chatbot.getModules();
					for (CommandModule module : modules.values()) {
						if (module instanceof RedditModule) {
							response.append("\n");
							for (String subreddit : ((RedditModule) module).getSubreddits()) {
								response.append("\thttps://www.reddit.com/r/").append(subreddit).append("\n");
							}
						}
					}
					chatbot.sendMessage(response.toString());
					return true;
				}
			}
		}
		return false;
	}
	//endregion
}