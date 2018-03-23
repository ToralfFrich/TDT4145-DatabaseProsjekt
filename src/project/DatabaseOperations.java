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
    	public static void addApparatOvelse(Connection connection, String ovelsesnavn, String antallKilo, String antallSett, String apparatNavn) throws SQLException{
    		String queryStatement = "INSERT INTO apparatovelse(ovelsesnavn, antallKilo, antallSett, apparatNavn) VALUES(?,?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, antallKilo);
    		prepStat.setString(3, antallSett);
    		prepStat.setString(4, apparatNavn);
    		prepStat.execute();
    		System.out.println("ApparatOvelse lagt til ");
    	}
    	
    	
    	//Legg til friøvelse
    	public static void addFriOvelse(Connection connection, String ovelsesnavn, String beskrivelse) throws SQLException{
    		String queryStatement = "INSERT INTO friovelse(ovelsesnavn, beskrivelse) VALUES(?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, beskrivelse);
    		prepStat.execute();
    		System.out.println("Fri Ovelse lagt til");
    	}
    	
    	
    	//Legg til treningsøkt
    	public static void addTreningsOkt(Connection connection, Date dato, Time time, int duration, int personligForm, int prestasjon, String notat ) throws SQLException{
    		String preQueryStatement = "INSERT INTO treningsokt(dato, tidspunkt, varighet, personligform, prestasjon, notat) VALUES (?,?,?,?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(preQueryStatement);
    		
    		prepStat.setDate(1,dato);
    		prepStat.setTime(2, time);
    		prepStat.setInt(3, duration);
    		prepStat.setInt(4, personligForm);
    		prepStat.setInt(5, prestasjon);
    		prepStat.setString(6, notat);
    		
    		prepStat.execute();
    		System.out.println("TreningsOkt lagt til");
    		
    	}
    	
    	
    	//Legg til øvelse i treningsøkt
    	public static void addOvelseITreningsOkt(Connection connection, Date dato, Time tidspunkt, String ovelsesnavn) throws SQLException{
    		String queryStatement = "INSERT INTO ovelseITreningsokt(dato, tidspunkt, ovelsesnavn) VALUES (?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setDate(1, dato);
    		prepStat.setTime(2, tidspunkt);
    		prepStat.setString(3, ovelsesnavn);
    		
    		prepStat.execute();
    		System.out.println("Ovelse lagt i treningsokt");
    	}
    	
    	
    	//Legg til øvelse i øvelsesgruppe
    	public static void addOvelseIOvelsesgruppe(Connection connection, String ovelsesnavn, String ovelsesgruppe) throws SQLException{
    		String queryStatement = "INSERT INTO ovelseIOvelsesgruppe(dato, tidspunkt, ovelsesnavn) VALUES (?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, ovelsesgruppe);
    		
    		prepStat.execute();
    		System.out.println("Ovelse lagt i ovelsesgruppe");
    	}
    	
    	///////////////////////ALLE ADDS LAGT TIL////////////////////////
    	
    	
    	//Hente de n siste Treningsøkter med all informasjon
    	public static List<TreningsOkt> getNSisteTreningsOkter(Connection connection, int n) throws SQLException{
    		
    		List<TreningsOkt> treningsOkter = new ArrayList<TreningsOkt>();
    		
    		//First find a list over the N last workouts
    		String stmt = "select * from treningsokt order by dato desc limit ?";
    		PreparedStatement prepStat = connection.prepareStatement(stmt);
    		prepStat.setInt(1, n);
    		ResultSet rs = prepStat.executeQuery();
    		
    		
    		
    		//Legger inn de n siste treningsøkter inn i en liste med treningsøkter.
    		while(rs.next()) {
    			System.out.println(rs.getDate("dato"));
        		System.out.println(rs.getTime("tidspunkt"));
    			System.out.println(rs.getInt("varighet"));
    			System.out.println(rs.getInt("personligForm"));
    			System.out.println(rs.getInt("prestasjon"));
    			System.out.println(rs.getString("notat"));
    			TreningsOkt t = new TreningsOkt(rs.getDate("dato"), rs.getTime("tidspunkt"), 
    			rs.getInt("varighet"), rs.getInt("personligForm"), rs.getInt("prestasjon"), rs.getString("notat"));
    			treningsOkter.add(t);
    		}
    		
    		return treningsOkter;
    	}
    	
    	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			//System.out.println(getNSisteTreningsøkter(DBConnection.createDBConnection(), 3));
    		//DatabaseOperations.addApparatØvelse(DBConnection.createDBConnection(), "hei", "90", "5", "hola");

    		Statement prepStatement = DBConnection.createDBConnection().createStatement();
    		ResultSet rs = prepStatement.executeQuery("select count(*) from apparatovelse where ovelsesnavn like 'hei'");
    		System.out.println(rs);
    		rs.next();
    		System.out.println(rs.getInt(1));
		}

    	
    	
    	
    	///////////Hente ut siste N treningsøkter lagt til///////////////
    	
    	////////Hente ut øvelse x mellom alle datoer 
    	
    	public List<Ovelse> getInfoAboutOvelseInTimeInterval(Connection connection, String ovelsesnavn, Date startsDato, Date sluttdato) throws SQLException{
    		
    		//Sjekker først om det finnes noen ovelsesnavn i apparatøvelse. Hvis det gjør det så execute kode
    		Statement prepStatement = connection.createStatement();
    		
    		//Dette querystatementet er gjennomtestet, og det funker gull
    		ResultSet rs = prepStatement.executeQuery("select count(*) from apparatovelse where ovelsesnavn like" + ovelsesnavn);
    		//Må ha rs.next() fordi den nullindekserer.
    		rs.next();
    		if (rs.getInt(1) > 0){
    			ResultSet resultat = prepStatement.executeQuery("select * from ovelseITreningsokt JOIN apparatovelse on (ovelseITreningsOkt.ovelsesnavn = apparatovelse.ovelsesnavn)");
    		}
    		
    		//Sjekker deretter for det samme i friøvelse
    		rs = prepStatement.executeQuery("select count(*) from friovelse where ovelsesnavn like" + ovelsesnavn);
    		rs.next();
    		if (rs.getInt(1) > 0){
    			
    		}
    		
    		
    		String checkApparatOvelse = "SELECT ovelsesnavn(COUNT(1) AS BIT) FROM apparatovelse WHERE (ovelsesnavn == ovelsesnavn)";
    		String checkFriOvelse = "SELECT ovelsesnavn(COUNT(1) AS BIT) FROM friovelse WHERE (ovelsesnavn == ovelsesnavn)";
    		//String friØvelseQueryStatement = "select ovelsesnavn from friøvelse where  "startsDato" and "sluttDato"";
    		//String apparatØvelseQueryStatement = "select "
    		
			return null;
    		
    	}
    	
    	
    	
    	
    	

 /*

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
       */
    }