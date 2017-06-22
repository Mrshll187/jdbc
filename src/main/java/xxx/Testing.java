package xxx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("deprecation")
public class Testing {

	public static void main(String...args) throws Exception{
		
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
		
		String createTable = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("create-db.sql"));		
		connection.createStatement().executeUpdate(createTable);
		
		String payload = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("example.json"));
		
		String sql = "INSERT INTO events (id, name) VALUES (1, ?) ";
		
	    PreparedStatement statement = connection.prepareStatement(sql);
	    statement.setString(1, payload);
	    statement.executeUpdate();
	    statement.close();
		
		ResultSet results = connection.createStatement().executeQuery("SELECT * FROM events");
		
		while(results.next()){
			
			System.out.println(results.getString("name"));
			System.out.println("ID "+results.getLong("id"));
		}
	}
}
