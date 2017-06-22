package xxx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("deprecation")
public class Testing {

	public static final String EVENT_TABLE = "EVENT";
	public static final String ID = "ID";
	public static final String DATE = "DATE";
	public static final String JSON = "JSON";
	
	public static void main(String... args) throws Exception {
		
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");

		String createTable = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("create-db.sql"));
		connection.createStatement().executeUpdate(createTable);

		String payload = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("example.json"));

		String sql = "INSERT INTO "+EVENT_TABLE+" ("+JSON+") VALUES (?) ";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, payload);
		statement.executeUpdate();
		statement.close();

		ResultSet results = connection.createStatement().executeQuery("SELECT * FROM "+EVENT_TABLE+"");

		while (results.next()) {

			System.out.println(JSON + " " + results.getString(JSON));
			System.out.println(ID + " " + results.getLong(ID));
			System.out.println(DATE + " " + results.getDate(DATE));
		}
	}
}
