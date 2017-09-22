package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		String result = null;
		try {
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(
						"SELECT * FROM responses;");			
			
			PreparedStatement updateHit = connection.prepareStatement(
						"UPDATE responses SET hits = ? WHERE keyword = ?");
						
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				String keyword = rs.getString(1);
				String response = rs.getString(2);
				int hits = rs.getInt(3);
				if(text.toLowerCase().contains(keyword.toLowerCase())) {
					result = response + " " + Integer.toString(hits);
					updateHit.setInt(1, hits + 1);
					updateHit.setString(2, keyword);
					break;
				}
			}
			
			rs.close();
			statement.close();
			connection.close();
		}
		catch(Exception e) {
			log.info("Exception while querying the database: {}", e.toString());
		}
		if(result != null)
			return result;
		throw new Exception("Not found");
	}	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
