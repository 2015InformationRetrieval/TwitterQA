package qa.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.omg.CORBA.PUBLIC_MEMBER;

import qa.analysis.TextTokenizer;
import qa.connection.Parameter;
import qa.datahelper.UserHelper;
import qa.util.QuestionUtil;
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
		 String question = QuestionUtil.removeUserName(status.getText()).toLowerCase();
		 String answer = getAnswererProb(question ,status.getUser().getId());
		 StatusUpdate statusUpdate = null;
		 if(answer.length() == 0){
			 statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " For your question: " + question +  " No one can answer your question in your friend circle..");
		 }else{
			 statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName()+ " For your question: " + question + answer + " can answer your question");
		 }
		
		
		 statusUpdate.setInReplyToStatusId(status.getId());
		 try {
			twitter.updateStatus(statusUpdate);
		}catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static Map<Long, String> getAnswererBL(String question, long Uid){
		TextTokenizer token=new TextTokenizer(question);
		List<String> query=new ArrayList<String>();
		String nickname = "";
		Map<Long, String> answerer = new HashMap<>();
		String temp;
		
		while((temp=token.nextWord())!=null){
			if(!temp.isEmpty()){
				query.add(temp);
			}	
		}
		//find user by question
		int i=0;
		while(i<query.size()){
			String index=query.get(i);
			System.out.println(index);
			answerer.putAll(userHelper.findAnswerBL(index,Uid));
			i++;
		}
		Iterator it = answerer.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next();
			String tmp = (String) pair.getValue();
			nickname+=tmp;
			nickname+=",";
		}
	
		if(nickname.length() != 0){
			nickname = nickname.substring(0, nickname.length()-1);
		}
	
		return answerer;
	}
	
	public static String getAnswererProb(String question, long Uid){
		//return users' nickname
		TextTokenizer token=new TextTokenizer(question);
		List<String> query=new ArrayList<String>();
		String nickname = "";
		Map<Long, String> answerer = new HashMap<>();
		Map<String, Float> unsort = new HashMap<>();
		
		String temp;
		while((temp=token.nextWord())!=null){
			if(!temp.isEmpty()){
				query.add(temp);
			}
		}
		//find user by question
		int i=0;
		while(i<query.size()){
			String index=query.get(i);
			answerer.putAll(userHelper.findAnswerBL(index,Uid));
			i++;
		}
		
		unsort = userHelper.findAnswerProb(answerer,query,Uid);
			
			System.out.println("---------The unsort result is : "+unsort);
			ValueComparator bvc =  new ValueComparator(unsort);
			TreeMap<String, Float> sort=new TreeMap<>(bvc);
			sort.putAll(unsort);
			System.out.println("This is the sorted results: ----------"+sort);
			Iterator itera = sort.entrySet().iterator();
			int count =0;
			while(itera.hasNext() && count<3){
				Map.Entry pair = (Map.Entry) itera.next();
				String name = (String) pair.getKey();
				nickname+= "@" + name + ",";
				count++;	
			}
			if(nickname.length() != 0){
				nickname = nickname.substring(0, nickname.length()-1);
			}
			//System.out.println(nickname);
		return nickname;
	}
	
}



