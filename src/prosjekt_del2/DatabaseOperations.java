package prosjekt_del2;

    import java.sql.*;
    
    public class DatabaseOperations {
        
        protected Connection conn;
        
        public DatabaseOperations() {
        }
        
        public void connect(){
            try { 
                System.out.println("hei");
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tcfrich_treningsapp?autoReconnect=true&useSSL=false", "tcfrich_database", "toralf"
                        );
                System.out.println("Connected");
            } catch (Exception e){
                throw new RuntimeException("Unable to connect", e);
            }
        }
        
        public static void main(String[] args) {
            DatabaseOperations data = new DatabaseOperations();
            data.connect();
        }
        
    }