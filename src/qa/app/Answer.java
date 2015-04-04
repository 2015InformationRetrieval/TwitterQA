package qa.app;

import org.omg.CORBA.PUBLIC_MEMBER;

import qa.connection.Parameter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Answer {
	private static Twitter twitter = null;
	
	public static void init(){
		if(twitter == null){
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true);
			cb.setOAuthConsumerKey(Parameter.CONSUMER_KEY);
		    cb.setOAuthConsumerSecret(Parameter.CONSUMER_KEY_SECRET);
		    cb.setOAuthAccessToken(Parameter.ACCESS_TOKEN);
		    cb.setOAuthAccessTokenSecret(Parameter.ACESS_TOKEN_SECRET);
		    twitter= new TwitterFactory(cb.build()).getInstance();
		} 		
	}
	public static void reply(Status status){ 
		 init();
		 
		 StatusUpdate statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName()+ " " + getAnswer(status.getText().replaceAll(Parameter.USER_NAME, "").toLowerCase()));
		 statusUpdate.setInReplyToStatusId(status.getId());
		 try {
			twitter.updateStatus(statusUpdate);
		}catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getAnswer(String question){
		String answer = "I got you";
		if(question.contains("changjiang"))
			answer = "3,915 miles (6,300 km)";
		
		return answer;
	}
}
