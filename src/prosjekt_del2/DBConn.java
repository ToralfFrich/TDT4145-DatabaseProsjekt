package prosjekt_del2;

import java.sql.*;

public abstract class DBConn {
	
    protected Connection conn;

    public DBConn () {
    }
    
    
	public void connect() {
    		try {
    			System.out.println("hei1");
    			Class.forName("com.mysql.jdbc.Driver").newInstance();
    			String url =  "jdbc:mysql://mysql.stud.ntnu.no:3306/idamen_treningsdagbok?autoReconnect=true&useSSL=false";
    			System.out.println("hei2");
    			conn = DriverManager.getConnection(url, "idamen_dbgruppe123", "hello123");
    			System.out.println("hei3");
    		} catch (Exception e) { System.out.println(e); }
	}
    
  
}
