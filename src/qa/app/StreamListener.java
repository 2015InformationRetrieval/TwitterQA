package qa.app;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import qa.app.Answer;
import qa.connection.Parameter;
import qa.service.UserService;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;

public class StreamListener implements StatusListener{
	
	String Neo4j_Path="/ir";
	private UserService userService = new UserService();
	GraphDatabaseService graphDataService=new GraphDatabaseFactory().newEmbeddedDatabase(Neo4j_Path);
	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(Status status) {
		// TODO Auto-generated method stub
		System.out.println(status.getUser().getName() + " : " + status.getText());
		System.out.println(status.getUser().getId());
		
		if(status.getUser().getId() != Long.parseLong(Parameter.USER_ID )){
			if(userService.isExist(status.getUser())){
				Answer.reply(status,graphDataService);
			}else{
				userService.createIndex(status.getUser());
			}
			
		}
			
		
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}
