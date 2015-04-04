package qa.datahelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

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
	
	/**
	  * find whether the query word is a index or not
	  * @param word: the query word 
	  * @param graphDataService: the address of database
	  */
	public Node findIndex(String word, GraphDatabaseService graphDataService){
	
		Node node = null;
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		
		try(Transaction transction=graphDataService.beginTx()){
			//suppose it's called value
			result=engine.execute("MATCH (a) Where a.value='"+ word + "' RETURN  a");
			node=(Node) result.columnAs("a");
		}
		 return node;	
	}
	
	/**
	  * find the followers of the questioner who has the index terms
	  * @param node: the index node
	  * @param Uid: the questioner's id
	  * @param graphDataService: the address of database
	  */
	public Set<Node> findAnswer(String index,Long Uid,GraphDatabaseService graphDataService){
		
		 Set<Node> users = new HashSet<>();
		 ExecutionEngine engine = new ExecutionEngine(graphDataService);
		 ExecutionResult result;
		 
		 try(Transaction transction=graphDataService.beginTx()){
			 //suppose the relationship called 'BELONG_TO' and `FOLLOWING`, 
			 result=engine.execute("MATCH (a)-[:`BELONG_TO`]->(b)-[:`FOLLOWING`]->(c) where a.value='"+index+"' and c.id='"+Uid+"' RETURN b");
			 for(Map<String,Object> map : result){
				 Node temp=(Node) map.get("b");
				 users.add(temp);	 
			 }     
		 }
		 return users;
	}
	
	/**
	  * find the followers of the user
	  * @param id: the id of the user
	  * @param graphDataService: the address of database
	  */
	public Set<Node> findFollowed(Long id,GraphDatabaseService graphDataService){
		
		Set<Node> users = new HashSet<>();
		ExecutionEngine engine = new ExecutionEngine(graphDataService);
		ExecutionResult result;
		
		try(Transaction transction=graphDataService.beginTx()){
			//suppose the relationship named 'FOLLOWED', and the property called id
			result=engine.execute("MATCH (a)-[:`FOLLOWED`]->(b) where b.id='"+id+"' RETURN a");
			 for(Map<String,Object> map : result){
				 Node temp=(Node) map.get("a");
				 users.add(temp);	 
			 }
		}	
		return users;
	}
	
	
}
