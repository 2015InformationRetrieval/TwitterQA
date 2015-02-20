package qa.datahelper;

import twitter4j.User;

import java.io.File;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import static org.neo4j.kernel.impl.util.FileUtils.deleteRecursively;

public  class UserHelper {
	
	private static final String DB_PATH = "/Users/fujun/Desktop/graph";

    private void clearDbPath()
    {
        try
        {
            deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
}


	
	/**
	  * find user whether the user exist in the main user list
	  * @param id: the twitter_id of the user
	  * @return if there is the user exist in the database, return true.  Else return false 
	  */
	public boolean isExistByUserId(long id) {
		// TODO Auto-generated method stub
		
	    String resultString;
	    String columnsString;
	    String nodeResult;
	    String rows = "";
	
		clearDbPath();
		
		// START SNIPPET: addData
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        
        // START SNIPPET: execute
        ExecutionEngine engine = new ExecutionEngine( db );
        
        ExecutionResult result;
        try ( Transaction ignored = db.beginTx() )
        {
            result = engine.execute( "match (n:Person {UserID:id}) return n, n.UserID" );
            // END SNIPPET: execute
            // START SNIPPET: items
            Iterator<Node> n_column = result.columnAs( "n" );
            for ( Node node : IteratorUtil.asIterable( n_column ) )
            {
                // note: we're grabbing the name property from the node,
                // not from the n.name in this case.
                nodeResult = node + ": " + node.getProperty( "UserID" );
            }
            // END SNIPPET: items
        }
        
        // START SNIPPET: columns
        List<String> columns = result.columns();
        // END SNIPPET: columns

        // the result is now empty, get a new one
        result = engine.execute( "match (n:Person {UserID: id}) return n, n.UserID" );
        // START SNIPPET: rows
        for ( Map<String, Object> row : result )
        {
            for ( Entry<String, Object> column : row.entrySet() )
            {
                rows += column.getKey() + ": " + column.getValue() + "; ";
            }
            rows += "\n";
        }
        // END SNIPPET: rows
        resultString = engine.execute( "match (n {UserID: 'my node'}) return n, n.UserID" ).dumpToString();
        columnsString = columns.toString();
        
        if (resultString != null) {
        	return true;
        }
        
        db.shutdown();
		
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
	 * @param string 
	  * @param userId: the twitter_id of the user
	  * @param followerId: the twitter_id of the follower of the user
	  */
<<<<<<< Updated upstream
	public void addFollower(long userId, long followerId, String string) {
=======
	public void addFollower(long userId, long followerId) {
		// relationship: Follower
>>>>>>> Stashed changes
		// TODO Auto-generated method stub
		
	}
	
	/**
	  * add the following of the user
	 * @param string 
	  * @param userId: the twitter_id of the user
	  * @param followeingId: the twitter_id of the following of the user
	  */
<<<<<<< Updated upstream
	public void addFollowing(long userId, long followingId, String string) {
=======
	public void addFollowing(long userId, long followingId) {
		// relationship: Following
>>>>>>> Stashed changes
		// TODO Auto-generated method stub
		
	}

}
