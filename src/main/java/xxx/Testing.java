package xxx;

import java.util.UUID;

public class Testing {
	
	public static void main(String... args) throws Exception {
		
		DatabaseService databaseService = new DatabaseService();
		
		UUID uuid = databaseService.insert("{ok : nope}");
		System.out.println(uuid.toString());
		
		String result = databaseService.getJson(uuid).toString();
		System.out.println(result);
		
		boolean success = databaseService.remove(uuid);
		System.out.println("Removed : "+success);
		
		String searchResult = databaseService.getEntry(uuid);
		System.out.println("Search Result : "+searchResult);
	}
}
