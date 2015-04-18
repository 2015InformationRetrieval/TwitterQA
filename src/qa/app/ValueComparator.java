package qa.app;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

//rank TreeMap by value
class ValueComparator implements Comparator<String> {

    Map<String, Float> base = new HashMap<>();
    
    public ValueComparator(Map<String, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
    	
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
