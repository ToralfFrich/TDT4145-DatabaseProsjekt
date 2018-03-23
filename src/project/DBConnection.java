package project;

import java.sql.*;

public class DBConnection {
	
	//Lager connection til Database som static etter å ha forhørt meg med Kissa. Da er det lett å kalle denne senere i alle de andre klassene

	public static Connection createDBConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		
		String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/tcfrich_treningsapp?autoReconnect=true&useSSL=false";
		String username = "tcfrich_database";
		String password = "toralf";
    	
		System.out.println("Connecting to Server");
            
        //1. Setting up driver
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
            
            
		//2. Creating connection
		Connection conn = DriverManager.getConnection (url, username, password); 
            
            
		System.out.println("Connected");
        
        return conn;
    }
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		createDBConnection();
	}

}
