package qa.app;

import qa.connection.Parameter;
import qa.service.UserService;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;

public class StreamListener implements StatusListener{
	
	private UserService userService = new UserService();
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
				Answer.reply(status);
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
