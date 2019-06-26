package com.hollandjake.chatbot.modules;

import com.hollandjake.chatbot.Chatbot;
import com.hollandjake.chatbot.utils.CONSTANTS;
import com.hollandjake.chatbot.utils.CommandModule;
import com.hollandjake.chatbot.utils.CommandableModule;
import com.hollandjake.messengerBotAPI.message.Message;
import com.hollandjake.messengerBotAPI.message.MessageComponent;
import com.hollandjake.messengerBotAPI.message.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hollandjake.chatbot.utils.CONSTANTS.ACTIONIFY;

public class Reddit extends CommandModule {
	//region Constants
	private final String REDDITS_REGEX = ACTIONIFY("reddits");
	private final String REDDIT_REGEX = ACTIONIFY("reddit");
	//endregion

	public Reddit(Chatbot chatbot) {
		super(chatbot);
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
					int numReddits = 0;
					for (CommandableModule module : chatbot.getModules().values()) {
						if (module instanceof RedditModule) {
							response.append("\n");
							for (String subreddit : ((RedditModule) module).getSubreddits()) {
								numReddits++;
								response.append("\thttps://www.reddit.com/r/").append(subreddit).append("\n");
							}
						}
					}
					response.append("\n")
							.append(numReddits)
							.append(" reddit module")
							.append(numReddits != 1 ? "s" : "")
							.append(" being used");
					chatbot.sendMessage(response.toString());
					return true;
				} else if (text.matches(REDDIT_REGEX)) {
					chatbot.sendMessage("https://www.reddit.com/");
				}
			}
		}
		return false;
	}
	//endregion
}