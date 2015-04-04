package qa.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This is for INFSCI 2140 in 2015
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class TextTokenizer {
	
	List<String> list = new ArrayList();
	ListIterator<String> listIterator;
	int index = 0;
	// YOU MUST IMPLEMENT THIS METHOD
	public TextTokenizer( String text ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		
		char[] texts = text.toCharArray();
		int j = 0;
		for(int i = 0;i < texts.length;i++){
			//if there is a white space, the word ends
			if(texts[i] == ' '){
				char[] word = new char[i - j];
				int poi = 0;
			    while(j < i){
			    	word[poi] = texts[j];
			    	poi++;
			    	j++;
			    }
			    String tmp = new String(word).toLowerCase();
			    if(!StopwordsRemover.isStopword(tmp)){
			    	 list.add(tmp);
					    j++;
			    }
			   
			}
			
		}
		
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public String nextWord() {
		// read and return the next word of the document; or return null if it is the end of the document
		if(listIterator == null)
			listIterator = list.listIterator();
		 
        if (listIterator.hasNext()) {
        	return listIterator.next();
        }
         
		return null;
	}
	
	public static void main(String args[]){
		TextTokenizer t = new TextTokenizer("I Think it as a good idea");
		String a;
		while((a = t.nextWord()) != null){
			System.out.println(a);
		}
	}
}
