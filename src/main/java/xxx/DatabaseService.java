package xxx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatabaseService {

	public static final String EVENT_TABLE = "EVENT";
	
	public static final String ID = "ID";
	public static final String DATE = "DATE";
	public static final String JSON = "JSON";
	
	Connection connection = null;
	
	public DatabaseService(){
		
		init();
	}
	
	public void init(){
		
		try{
			
			connection = DriverManager.getConnection("jdbc:h2:mem:test");
	
			String createTable = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("create-db.sql"));
			connection.createStatement().executeUpdate(createTable);
	
			String payload = IOUtils.toString(Testing.class.getClassLoader().getResourceAsStream("example.json"));
			
			insert(payload);
		}
		catch(Exception e){
			
		}
	}
	
	public String getEntry(UUID uuid) throws SQLException{
		
		ResultSet results = connection.createStatement().executeQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+ID+"='"+uuid.toString()+"'");
		
		if(results.next()) {

			JsonObject result = new JsonObject();
			result.addProperty(ID, results.getString(ID));
			result.addProperty(DATE, new Date(results.getLong(DATE)).toString());
			result.addProperty(JSON, results.getString(JSON));
			
			return result.toString();
		}
		
		return null;
	}
	
	public JsonObject getJson(UUID uuid) throws SQLException{
		
		ResultSet results = connection.createStatement().executeQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+ID+"='"+uuid.toString()+"'");
		
		if(results.next()) {

			String result = results.getString(JSON);
			JsonObject json = new JsonParser().parse(result).getAsJsonObject();
			
			return json;
		}
		
		return null;
	}
	
	public boolean exists(UUID uuid) throws SQLException{
		
		ResultSet results = connection.createStatement().executeQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+ID+"='"+uuid.toString()+"'");
		
		if(results.next())
			return true;
		else
			return false;
	}
	
	public UUID insert(String payload) throws SQLException{
		
		UUID uuid = UUID.randomUUID();
		long date = new Date().getTime();		
		
		String sql = "INSERT INTO "+EVENT_TABLE+" ( " + ID + "," + JSON + ", "+DATE+") VALUES ( '"+uuid.toString()+"' , ? , " +date+ " ) ";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, payload);
		statement.executeUpdate();
		statement.close();
		
		return uuid;
	}
	
	public boolean remove(UUID uuid) throws SQLException{
		
		String sql = "DELETE FROM "+EVENT_TABLE+" WHERE "+ID+"='"+uuid.toString()+"'";
		connection.createStatement().executeUpdate(sql);
		
		if(exists(uuid))
			return false;
		else
			return true;
	}
}
