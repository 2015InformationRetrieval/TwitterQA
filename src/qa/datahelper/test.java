package qa.datahelper;

public class test {
	
	public static void main(String[] args) {
		
		UserHelper uh = new UserHelper();
		uh.addUser(25,"Fu_Jun");
		uh.addIndex("Sichuan", 25);
		uh.addIndex("ChengDu", 25);
		uh.addFollower(25, 24, "Cheng_Ang");
		uh.shutDown();
		
	}
	
}
