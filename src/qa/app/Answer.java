package qa.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
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
	private static UserHelper userHelper = new UserHelper();
	 //private static final String DB_PATH = "neo4j-community-2.2.0/test";
	 //static GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	
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
		 String question = status.getText().replaceAll(Parameter.USER_NAME, "");
		 String answer = getAnswerer(question.toLowerCase(),status.getUser().getId());
		 StatusUpdate statusUpdate = null;
		 if(answer.length() == 0){
			 statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + "No one can answer your question in your friend circle..");
		 }else{
			 statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName()+ "For your question: " + question + " @" + answer + " can answer your question");
		 }
		
		
		 statusUpdate.setInReplyToStatusId(status.getId());
		 try {
			twitter.updateStatus(statusUpdate);
			System.out.println("------DONE-----");
		}catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static String getAnswerer(String question, long Uid){
		//return users' nickname
		System.out.println("QUESTION :"+question);
		System.out.println("UID :"+Uid);
		
		TextTokenizer token=new TextTokenizer(question);
		List<String> query=new ArrayList<String>();
		String nickname = "";
		Set<String> answerer=new HashSet<>();
		
		
		//System.out.println(token);
		String temp;

		while((temp=token.nextWord())!=null){
			System.out.println("-----");
			System.out.println(temp);
			query.add(temp);
		}
		
		//find user by question
		int i=0;
		while(i<query.size()){
			String index=query.get(i);
			System.out.println(index);
			answerer.addAll(userHelper.findAnswer(index,Uid));
			i++;
		}
		System.out.println("This is the results: "+answerer);
		
			for(String name:answerer){
				nickname+=name;
				nickname+=",";	
			}
			
	
		if(nickname.length() != 0){
			nickname = nickname.substring(0, nickname.length()-1);
		}
		
		
		System.out.println("nicke name :" + nickname);
	
		return nickname;
	}
}
