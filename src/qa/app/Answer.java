package qa.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
 
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.omg.CORBA.PUBLIC_MEMBER;

import qa.analysis.TextTokenizer;
import qa.connection.Parameter;
import qa.datahelper.UserHelper;
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
	public static void reply(Status status,GraphDatabaseService graphDataService){ 
		 init();
		 
		 StatusUpdate statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName()+ " " + getAnswerer(status.getText().replaceAll(Parameter.USER_NAME, "").toLowerCase(),status.getId(),graphDataService));
		 statusUpdate.setInReplyToStatusId(status.getId());
		 try {
			twitter.updateStatus(statusUpdate);
		}catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static String getAnswerer(String question, long Uid,GraphDatabaseService graphDataService){
		//return users' nickname
		TextTokenizer token=new TextTokenizer(question);
		List<String> query=new ArrayList<String>();
		String nickname = null;
		Set<Node> answerer=new HashSet<>();
		UserHelper find=new UserHelper();
		
		while(token.nextWord()!=null){
			query.add(token.nextWord());
		}
		
		//find user by question
		int i=0;
		while(i<query.size()){
			String index=query.get(i);
			answerer=find.findAnswer(index,Uid,graphDataService);
			i++;
		}
		
		for(Node node:answerer){
			nickname+=node.getProperty("Nick_Name").toString();
			nickname+=",";	
		}
		
		nickname = nickname.substring(0, nickname.length()-1);
		/*String answer = "I got you";
		if(question.contains("changjiang"))
			answer = "3,915 miles (6,300 km)";*/
	
		return nickname;
	}
}
