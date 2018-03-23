package project;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import prosjekt_del2.InsertIntoDatabase;
    
    public class DatabaseOperations {
        
    	
    	/////////KRAV 1 - Legge til alt i Database///////////
    	
    	
    	//Legg til apparat
    	public static void addApparat(Connection connection, String navn, String beskrivelse) throws SQLException{
    		
    		//Gjør klar query, ? er det som skal legges til i etterkant av prepstat.
    		String queryStatement = "INSERT INTO apparat (navn, beskrivelse) VALUES(?,?)";
    		
    		//PreparedStatement er en klasse som kompilerer SQL statements. 
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		//setString fordi navn og beskrivelse er String
    		prepStat.setString(1, navn);
    		prepStat.setString(2, beskrivelse);
    		
    		//Execute for å kjøre kode.
    		prepStat.execute();
    		System.out.println("Apparat lagt til");
    	}
    	
    	
    	//Legg til apparatøvelse
    	public static void addApparatØvelse(Connection connection, String ovelsesnavn, String antallKilo, String antallSett, String apparatNavn) throws SQLException{
    		String queryStatement = "INSERT INTO apparatøvelse(øvelsesnavn, antallKilo, antallSett, apparatNavn) VALUES(?,?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, antallKilo);
    		prepStat.setString(3, antallSett);
    		prepStat.setString(4, apparatNavn);
    		prepStat.execute();
    		System.out.println("Apparatøvelse lagt til ");
    	}
    	
    	
    	//Legg til friøvelse
    	public static void addFriØvelse(Connection connection, String ovelsesnavn, String beskrivelse) throws SQLException{
    		String queryStatement = "INSERT INTO friøvelse(øvelsesnavn, beskrivelse) VALUES(?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, beskrivelse);
    		prepStat.execute();
    		System.out.println("Fri øvelse lagt til");
    	}
    	
    	
    	//Legg til treningsøkt
    	public static void addTreningsØkt(Connection connection, Date dato, Time time, int duration, int personligForm, int prestasjon, String notat ) throws SQLException{
    		String preQueryStatement = "INSERT INTO treningsøkt(dato, tidspunkt, varighet, personligform, prestasjon, notat) VALUES (?,?,?,?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(preQueryStatement);
    		
    		prepStat.setDate(1,dato);
    		prepStat.setTime(2, time);
    		prepStat.setInt(3, duration);
    		prepStat.setInt(4, personligForm);
    		prepStat.setInt(5, prestasjon);
    		prepStat.setString(6, notat);
    		
    		prepStat.execute();
    		System.out.println("Treningsøkt lagt til");
    		
    	}
    	
    	
    	//Legg til øvelse i treningsøkt
    	public static void addØvelseITreningsøkt(Connection connection, Date dato, Time tidspunkt, String øvelsesnavn) throws SQLException{
    		String queryStatement = "INSERT INTO øvelseITreningsøkt(dato, tidspunkt, øvelsesnavn) VALUES (?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setDate(1, dato);
    		prepStat.setTime(2, tidspunkt);
    		prepStat.setString(3, øvelsesnavn);
    		
    		prepStat.execute();
    		System.out.println("Øvelse lagt i treningsøkt");
    	}
    	
    	
    	//Legg til øvelse i øvelsesgruppe
    	public static void addØvelseIØvelsesgruppe(Connection connection, String øvelsesnavn, String øvelsesgruppe) throws SQLException{
    		String queryStatement = "INSERT INTO øvelseIØvelsesgruppe(dato, tidspunkt, øvelsesnavn) VALUES (?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, øvelsesnavn);
    		prepStat.setString(2, øvelsesgruppe);
    		
    		prepStat.execute();
    		System.out.println("Øvelse lagt i Øvelsesgruppe");
    	}
    	
    	
    	///////////////////////ALLE ADDS LAGT TIL////////////////////////
    	
    	
    	//Hente de n siste Treningsøkter med all informasjon
    	public static List<TreningsOkt> getNSisteTreningsøkter(Connection connection, int n) throws SQLException{
    		
    		List<TreningsOkt> treningsøkter = new ArrayList<TreningsOkt>();
    		
    		//First find a list over the N last workouts
    		String stmt = "select * from treningsøkt order by dato desc limit ?";
    		PreparedStatement prepStat = connection.prepareStatement(stmt);
    		prepStat.setInt(1, n);
    		ResultSet rs = prepStat.executeQuery();
    		
    		//Legger inn de n siste treningsøkter inn i en liste med treningsøkter.
    		while(rs.next()) {
    			TreningsOkt t = new TreningsOkt(rs.getDate("dato"), rs.getTime("tidspunkt"), 
    			rs.getInt("varighet"), rs.getInt("personligForm"), rs.getInt("prestasjon"), rs.getString("notat"));
    			treningsøkter.add(t);
    		}
    		
    		return treningsøkter;
    	}

    	
    	
    	
    	
    	
    	
    	
    	
    	
    	public void printNTreningsokter(Connection conn, int n) { //skriver ut de n siste treningsøktene
    		try{
    			Statement stmt = conn.createStatement();
    			String quary = 
    			"SELECT * FROM "+db+".Treningsokt "+
    			"ORDER BY Dato ASC";
    			
    			ResultSet rs = null;
    			if(stmt.execute(quary)){
    				rs = stmt.getResultSet();
    			}
    			int i = 0;
    			while (rs.next() && i < n) {
    				i ++;
    				String dato = rs.getString(1);
    				String tidspunkt = rs.getString(2);
    				String varighet = rs.getString(3);
    				String personligform = rs.getString(4);
    				String prestasjon = rs.getString(5);
    				String notat = rs.getString(6);
    				System.out.println("Økt " + i +  "/n"
    						+ "Dato: " + dato +"\n"
    						+ "Tidspunkt: " + tidspunkt +"\n"
    						+ "Varighet: " + varighet +"\n"
    						+ "Personlig form: " + personligform +"\n"
    						+ "Prestasjon: " + prestasjon +"\n"
    						+ "Notat: " + notat +"\n\n");
    			}
    		} catch (SQLException ex) {
    			System.out.println("SQLExcetion:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    	}
    	
    	public void printResultatlogg(Connection conn, String ovelsesnavn, String startdato, String sluttdato) { //skriver ut resultatlogg for apparatovelser
    		try{
    			Statement stmt = conn.createStatement();
    			String quary = 
    			"SELECT Ovelsesnavn, AntallKilo, AntallSett, Dato, Tidspunkt "
    			+ "FROM ("+db+".Apparatovelse NATURAL JOIN "+db+".OvelseITreningsokt)"
    			+ " INNER JOIN "+db+".Treningsokt ON (OvelseInTreningsokt.Dato = Treningsokt.Dato AND OvelseInTreningsokt.Tidspunkt = Treningsokt.Tidspunkt)"+
    			" WHERE (ovelsesnavn = " + ovelsesnavn + " AND dato >= " + startdato + " AND dato <= " + sluttdato + ")";
    			
    			ResultSet rs = null;
    			if(stmt.execute(quary)){
    				rs = stmt.getResultSet();
    			}
    			System.out.println("Resultatlogg for " + ovelsesnavn);
    			while (rs.next()) {
    				String antallKilo = rs.getString(2);
    				String antallSett = rs.getString(3);
    				String dato = rs.getString(4);
    				String tidspunkt = rs.getString(5);
    				System.out.println("Dato " + dato +  "/n"
    						+ "Tidspunkt: " + tidspunkt +"\n"
    						+ "Antall kilo: " + antallKilo +"\n"
    						+ "Antall sett: " + antallSett +"\n\n");
    			}
    		} catch (SQLException ex) {
    			System.out.println("SQLExcetion:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    	}
    	
    	public void findOvelseInOvelsesgruppe(Connection conn, String ovelsesgruppenavn) {
    		try{
    			Statement stmt = conn.createStatement();
    			String quary = 
    			"SELECT Ovelsesnavn "
    			+ "FROM ("+db+".Ovelsesgruppe NATURAL JOIN "+db+".InngårI)"
    			+ " NATURAL JOIN "+db+".Ovelse"+
    			" WHERE (ovelsesgruppenavn = " + ovelsesgruppenavn + ")";
    			
    			ResultSet rs = null;
    			if(stmt.execute(quary)){
    				rs = stmt.getResultSet();
    			}
    			System.out.println("Ovelser i " + ovelsesgruppenavn + ":");
    			while (rs.next()) {
    				String navn = rs.getString(1);
    				System.out.println(navn);
    			}
    		} catch (SQLException ex) {
    			System.out.println("SQLExcetion:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    	}
       
    }