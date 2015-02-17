package qa.datahelper;

import twitter4j.User;

public  class UserHelper {
	
	/**
	  * find user whether the user exist in the database
	  * @param id: the twitter_id of the user
	  * @return if there is the user exist in the database, return true.  Else return false 
	  */
	public boolean isExistByUserId(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	/**
	  * add the a term under a specific user
	  * @param token: the term add to the index
 	  * @param id: the twitter_id of the user
	  */
	public void addIndex(String token, long id) {
		// TODO Auto-generated method stub
		
	}

	/**
	  * add the follower of the user
	  * @param userId: the twitter_id of the user
	  * @param followerId: the twitter_id of the follower of the user
	  */
	public void addFollower(long userId, long followerId) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	  * add the following of the user
	  * @param userId: the twitter_id of the user
	  * @param followeingId: the twitter_id of the following of the user
	  */
	public void addFollowing(long userId, long followingId) {
		// TODO Auto-generated method stub
		
	}

}
