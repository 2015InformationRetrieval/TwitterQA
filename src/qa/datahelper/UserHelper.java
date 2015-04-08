package qa.datahelper;
 
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import twitter4j.User;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.kernel.TopLevelTransaction;
import org.neo4j.tooling.GlobalGraphOperations;
import org.neo4j.graphdb.DynamicLabel; // add a label
import org.neo4j.graphdb.Label;

public  class UserHelper {
	
	// there are two labels in this Neo4j. 
	// (1) User(Property:id)  (2) Index (Property: token)
	
	 ExecutionEngine engine;	
	 
	 private static String DB_PATH;
	 private static GraphDatabaseService db;
	 //private static final String DB_PATH = "ir";
	 //GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	 
	 public UserHelper () {
		 DB_PATH = "neo4j-community-2.2.0/ir";
		 if (db == null) {
			 db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		 }
		 
	 }
	 
	 // two labels in neo4j, Index and User
	 org.neo4j.graphdb.Label Index = DynamicLabel.label("Index"); 
	 org.neo4j.graphdb.Label User = DynamicLabel.label("User");

	 public static enum RelTypes implements RelationshipType{
		 
		 Indexed,
		 Following, // Following shows that a user is following other users
		 Followed   // Followed shows that a user is followed by other users
		 
	}
	 
	 /**
	  * check whether the network of userId is in neo4j or not
	  * @param id
	  * @return
	  */
	 @SuppressWarnings("deprecation")
	public boolean IsExistUserNetwork(long id) {
		 
		 Set<String> container = new HashSet<String>();
		 //Set<RelationshipType> contain = new HashSet<RelationshipType>();
		 try ( Transaction tx = db.beginTx()) {
			 if ( isExistByUserId(id) ) {
				 //Iterator<RelationshipType> itea_type = db.getRelationshipTypes();
				 Iterable<RelationshipType> itea_type = db.getRelationshipTypes();
				 Iterator<RelationshipType> itea = itea_type.iterator();
				 while( itea.hasNext()) {
					 String name = itea.next().name();
					 container.add(name);
				 }
			 }
		 } // end try
		 
		 if ( container.contains("Indexed") && container.contains("Following") && container.contains("Followed") ) {
			 return true;
		 }

		 return false;
	 }
	 
	 /**
	  * at first, crawling data from twitter, just put the user who asks the question into neo4j
	  * @param userId
	  * @param Name
	  */
	 public void addUser(long userId, String Name) {
		 
		 if ( !isExistByUserId(userId) ) {
			 
			 try( Transaction tx =  db.beginTx()) {
				 
				 Node user = db.createNode();
				 user.addLabel(User);
				 user.setProperty("ID", userId);
				 user.setProperty("name", Name);
				 System.out.println("addUser is done");
				 tx.success();
				 	 
			 }
			 
		 } else {
			 System.out.println("errors: " + userId + "is already in neo4j");
		 }
		 
	 } // end addUser
	 
	 
	/**
	  * find user whether the user exist in the main user list
	  * @param id: the twitter_id of the user
	  * @return if there is the user exist in the database, return true.  Else return false 
	  */
	public boolean isExistByUserId(long id) {
		// TODO Auto-generated method stub
        // START SNIPPET: execute
		try( Transaction tx =  db.beginTx()) {			
			ResourceIterator<Node> iterator1 = db.findNodesByLabelAndProperty(User, "ID", id).iterator();			
		    while (iterator1.hasNext()) {		    	
		    	return true;		    	
		    }		    
		    tx.success();		
		}
		return false;
	}
	
	/**
	  * find index whether the token exist in the main index list
	  * @param id: the token of index
	  * @return if there exists token in the database, return true.  Else return false 
	  */
	public boolean isExistByIndex(String token) {	
		try( Transaction tx =  db.beginTx()) {		
			ResourceIterator<Node> iterator1 = db.findNodesByLabelAndProperty(Index, "token", token).iterator();			
		    while (iterator1.hasNext()) {	    
		    	return true;			
		    }		    
		    tx.success();			
		}	    
	    return false;
	} 
	
	/**
	  * add the a term under a specific user
	  * @param token: the term add to the index
 	  * @param id: the twitter_id of the user
	  */
	public void addIndex(String token, long id) {
		// TODO Auto-generated method stub
		
		String match;
		
		// check the id if id is in neo4j or not
		if (isExistByUserId(id)) {
			
			if (isExistByIndex(token)) { // token is already in neo4j
				
				 try( Transaction tx =  db.beginTx()) { // create relationship betwen token and id
					 
					 
					 ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", id).iterator();
					 
					 ResourceIterator<Node> iterator_index= db.findNodesByLabelAndProperty(Index, "token", token).iterator();
					 
					 // there is only one node in iterator1
					 Node user_node = iterator_user.next();
					 // there is only one node in iterator2
					 Node index_node = iterator_index.next();
					 
					 Relationship relationship = user_node.createRelationshipTo(index_node, RelTypes.Indexed);
					 
					 //match = (String)user_node.getProperty("ID") + ": " + (String)index_node.getProperty("token");
					 System.out.println("addIndex is done");
					 
					 tx.success();
					 
				 }
				 
				 //System.out.println(match);
				
				
			} else { // create a node with property token: token
				
				 try( Transaction tx =  db.beginTx()) { // create relationship betwen token and id
					 
					 Node index_node = db.createNode();
					 index_node.addLabel(Index);
					 index_node.setProperty("token", token);
					 
					 ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", id).iterator();
					 Node user_node = iterator_user.next();
					 
					 Relationship relationship = user_node.createRelationshipTo(index_node, RelTypes.Indexed);
					 
					 //match = (String)user_node.getProperty("ID") + ": " + (String)index_node.getProperty("token");
					 System.out.println("Index this Word--------"+index_node.getProperty("token"));
					 System.out.println("addIndex is done");
					 
					 tx.success();
					 
				 }
				 
				 //System.out.println(match);
								
			}		
			
		} else { // user id is not in neo4j, should send errors
			System.out.println("errors user: " + id + "is not in neo4j");
		}
		
		
	} // end addIndex


	/**
	  * add the follower of the user
	  * @param string 
	  * @param userId: the twitter_id of the user
	  * @param followerId: the twitter_id of the follower of the user
	  */
	public void addFollower(long userId, long followerId, String name) {
		// TODO Auto-generated method stub
		
		if (isExistByUserId(userId)) { // check userId is in neo4j or not
			// check followerId is in neo4j or not
			if ( isExistByUserId(followerId)) {  
				// followerId is in neo4j, we only need to find this person
				try ( Transaction tx = db.beginTx() ) {
					
					ResourceIterator<Node> itea_follower = db.findNodesByLabelAndProperty(User,"ID",followerId).iterator();
					Node follower_node = itea_follower.next();
						
					ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", userId).iterator();
					Node user_node = iterator_user.next();
					
					// user is followed by follower
					Relationship relationship1 = user_node.createRelationshipTo(follower_node, RelTypes.Followed);
					 
					// follower is following user
					Relationship relationship2 = follower_node.createRelationshipTo(user_node, RelTypes.Following);
					 
					tx.success();
				}

			} else { // followerId is not in the neo4j
				try( Transaction tx =  db.beginTx()) {
					 
					 // create a follower node
					 Node follower_node = db.createNode();
					 follower_node.addLabel(User);
					 follower_node.setProperty("ID", followerId);
					 follower_node.setProperty("name", name);
					 
					 // create a user node
					 ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", userId).iterator();
					 Node user_node = iterator_user.next();
					 
					 // user is followed by follower
					 Relationship relationship1 = user_node.createRelationshipTo(follower_node, RelTypes.Followed);
					 
					 // follower is following user
					 Relationship relationship2 = follower_node.createRelationshipTo(user_node, RelTypes.Following);
					 
					 System.out.println("followerId: "+ followerId +" is not in neo4j. AddFollower is done");	 
					 tx.success();
				 }
			}
			 
		} else { 
			// there is no user with userId
			System.out.println("errors userId" + userId + " is not in the database");
		}
		
	} // end addFollower
	
	/**
	  * add the following of the user
	 * @param string 
	  * @param userId: the twitter_id of the user
	  * @param followeingId: the twitter_id of the following of the user
	  */
	public void addFollowing(long userId, long followingId, String name ) {
		// TODO Auto-generated method stub
		 
		if (isExistByUserId(userId)) {
			
			if (isExistByUserId(followingId)) {
				
				try ( Transaction tx = db.beginTx()) {
					// followingId is in neo4j
					 ResourceIterator<Node> iterator_following = db.findNodesByLabelAndProperty(User, "ID", userId).iterator();
					 Node following_node = iterator_following.next();
					 
					 ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", userId).iterator();
					 Node user_node = iterator_user.next();
					 
					 // following_node is following user_node
					 Relationship relationship1 = following_node.createRelationshipTo(user_node, RelTypes.Followed);
					 
					 // user_node is followed by following_node
					 Relationship relationship2 = user_node.createRelationshipTo(following_node, RelTypes.Following);
					 
					 System.out.println("addFollowing is done");
					 
					 tx.success();
				}

			} else { // followingId is not in neo4j
				 try( Transaction tx =  db.beginTx()) { 
					 // create a following node
					 Node following_node = db.createNode();
					 following_node.addLabel(User);
					 following_node.setProperty("ID", followingId);
					 following_node.setProperty("name", name);
					 
					 // create a user node
					 ResourceIterator<Node> iterator_user = db.findNodesByLabelAndProperty(User, "ID", userId).iterator();
					 Node user_node = iterator_user.next();
					 
					 // following_node is following user_node
					 Relationship relationship1 = following_node.createRelationshipTo(user_node, RelTypes.Followed);
					 
					 // user_node is followed by following_node
					 Relationship relationship2 = user_node.createRelationshipTo(following_node, RelTypes.Following);
					 
					 System.out.println("addFollowing is done");
					 
					 tx.success();
				 }
			}

			
		} else { // userId is not in neo4j
			System.out.println("errors: " + userId + "is not in neo4j");
		}
		

	}
	
	/**
	  * find whether the query word is a index or not
	  * @param word: the query word 
	  * 
	  */
	public Node findIndex(String word){
	
		Node node = null;
		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result;
		
		try(Transaction transction=db.beginTx()){
			//suppose it's called value
			result=engine.execute("MATCH (a) Where a.value='"+ word + "' RETURN  a");
			node=(Node) result.columnAs("a");
			transction.success();
		}
		 return node;	
	}
	
	/**
	  * find the followers of the questioner who has the index terms
	  * @param index: the index node
	  * @param Uid: the questioner's id
	  */
	public Set<String> findAnswer(String index,Long Uid){
		
		 Set<String> users = new HashSet<>();
		 ExecutionEngine engine = new ExecutionEngine(db);
		 ExecutionResult result;
		 
		 try(Transaction transction=db.beginTx()){
			  
			 result=engine.execute("MATCH (a)-[:`Followed`]->(b)-[:`Indexed`]-(c) where c.token='"+index+"' and a.ID="+Uid+" RETURN b");
			//result=engine.execute("MATCH (b:`User`) where b.ID="+Uid+" RETURN b");
			 //System.out.println("Check Uid: "+Uid);
			 System.out.println("Check index: "+index);
			 //result=engine.execute("MATCH (a)-[:`Followed`]-(b) where a.ID="+Uid+" RETURN b");
			 //result=engine.execute("MATCH (a)-[:`Indexed`]-(b:`User`) where a.token='"+index+"' RETURN b");
			 //result=engine.execute("MATCH (a)-[:`Indexed`]-(b:`User`) where a.token='pittsburgh' RETURN b");
			 for(Map<String,Object> map : result){
				 Node temp=(Node) map.get("b");
				 String name=(String) temp.getProperty("name");
				 users.add(name);	 
			 } 
			 System.out.println("This is the partial results: "+users);
			 transction.success();
		 }
		 return users;
	}
	
	/**
	  * find the followers of the user
	  * @param id: the id of the user
	  * @param graphDataService: the address of database
	  */
	public Set<Node> findFollowed(Long id){
		
		Set<Node> users = new HashSet<>();
		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result;
		
		try(Transaction transction=db.beginTx()){
			//suppose the relationship named 'FOLLOWED', and the property called id
			result=engine.execute("MATCH (a)-[:`FOLLOWED`]->(b) where b.id='"+id+"' RETURN a");
			 for(Map<String,Object> map : result){
				 Node temp=(Node) map.get("a");
				 users.add(temp);	 
			 }
			 transction.success();
		}	
		return users;
	}
	
	

	 // end addFollowing
	
	/**
	 * shut down the database
	 * no parameter
	 * return none
	 */
    public void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        db.shutdown();
        // END SNIPPET: shutdownServer
    } // end shutdown
    


}
