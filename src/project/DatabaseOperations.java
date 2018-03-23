package project;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import prosjekt_del2.InsertIntoDatabase;
    
    public class DatabaseOperations {
        
    	
    	//KRAV 1/////////Legge til alt i Database///////////
    	
    	
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
    	
    	
    	
    	//Legg til ovelse
    	public static void addOvelse(Connection connection, String ovelsesnavn) throws SQLException {
    		
    		List<Ovelse> ovelser = new ArrayList<>();
    		
    		String queryStatement = "select * from ovelse";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		ResultSet rs = prepStat.executeQuery();
    		
    		//Sjekker om ovelse allerede finnes i ovelse-db
    		while(rs.next()){
    			Ovelse ovelse = new Ovelse(rs.getString("ovelsesnavn"));
    			if (ovelse.getOvelsesnavn().equals(ovelsesnavn)){
    				System.out.println("Denne ovelsen finnes alt!");
    				return;
    			}
    		}
    		System.out.println("Havna inni her");
    		queryStatement = "insert into ovelse (ovelsesnavn) VALUES (?)";
          	prepStat = connection.prepareStatement(queryStatement);
                
           	prepStat.setString(1, ovelsesnavn);
                
            prepStat.executeUpdate();
            System.out.println("ovelse lagt til");
    		
        }
    	
    	
    	//Legg til apparatøvelse
    	public static void addApparatOvelse(Connection connection, String ovelsesnavn, 
    			String antallKilo, String antallSett, String apparatNavn) throws SQLException{
    		String queryStatement = "INSERT INTO apparatovelse(ovelsesnavn, antallKilo, "
    				+ "antallSett, apparatNavn) VALUES(?,?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, antallKilo);
    		prepStat.setString(3, antallSett);
    		prepStat.setString(4, apparatNavn);
    		prepStat.execute();
    		System.out.println("ApparatOvelse lagt til ");
    		
    		DatabaseOperations.addOvelse(connection, ovelsesnavn);
    		
    	}
    	
    	
    	
    	
    	//Legg til friøvelse
    	public static void addFriOvelse(Connection connection, String ovelsesnavn, String beskrivelse) throws SQLException{
    		String queryStatement = "INSERT INTO friovelse(ovelsesnavn, beskrivelse) VALUES(?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, beskrivelse);
    		prepStat.execute();
    		System.out.println("Fri Ovelse lagt til");
    		
    		DatabaseOperations.addOvelse(connection, ovelsesnavn);
    		
    	}
    	
    	
    	
    	//Legg til treningsøkt
    	public static void addTreningsOkt(Connection connection, Date dato, Time time, 
    			int duration, int personligForm, int prestasjon, String notat ) throws SQLException{
    		String preQueryStatement = "INSERT INTO treningsokt(dato, tidspunkt, varighet, "
    				+ "personligform, prestasjon, notat) VALUES (?,?,?,?,?,?)";
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
    	public static void addOvelseITreningsOkt(Connection connection, Date dato, 
    			Time tidspunkt, String ovelsesnavn) throws SQLException{
    		String queryStatement = "INSERT INTO ovelseITreningsokt(dato, tidspunkt, ovelsesnavn) VALUES (?,?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setDate(1, dato);
    		prepStat.setTime(2, tidspunkt);
    		prepStat.setString(3, ovelsesnavn);
    		
    		prepStat.execute();
    		System.out.println("Ovelse lagt i treningsokt");
    	}
    	
    	
    	
    	//Legg til øvelse i øvelsesgruppe
    	public static void addOvelseIOvelsesgruppe(Connection connection, String ovelsesnavn, 
    			String ovelsesgruppe) throws SQLException{
    		String queryStatement = "INSERT INTO ovelseIOvelsesgruppe(ovelsesnavn, ovelsesgruppe) VALUES (?,?)";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		
    		prepStat.setString(1, ovelsesnavn);
    		prepStat.setString(2, ovelsesgruppe);
    		
    		prepStat.execute();
    		System.out.println("Ovelse lagt i ovelsesgruppe");
    	}
    	
    	
    	
    	//Legg til ovelsesgruppe
    	public static void addOvelsesgruppe(Connection connection, String Ovelsesgruppenavn) throws SQLException {
            String queryStatement = "insert into ovelsesgruppe (ovelsesgruppenavn) VALUES (?)";
            PreparedStatement prepStat = connection.prepareStatement(queryStatement);
            
            prepStat.setString(1, Ovelsesgruppenavn);
            
            prepStat.executeUpdate();
            System.out.println("Ovelsesgruppe lagt til");
    	}
    	
    	
    	

    	//KRAV 2//////Hente de n siste Treningsøkter med all informasjon/////////
    	
    	public static List<TreningsOkt> getNSisteTreningsOkter(Connection connection, int n) throws SQLException{
    		
    		List<TreningsOkt> treningsOkter = new ArrayList<TreningsOkt>();
    		
    		//First find a list over the N last workouts
    		String stmt = "select * from treningsokt order by dato desc limit ?";
    		PreparedStatement prepStat = connection.prepareStatement(stmt);
    		prepStat.setInt(1, n);
    		ResultSet rs = prepStat.executeQuery();
    		
    		
    		
    		//Legger inn de n siste treningsøkter inn i en liste med treningsøkter.
    		while(rs.next()) {
    			TreningsOkt t = new TreningsOkt(rs.getDate("dato"), rs.getTime("tidspunkt"), 
    			rs.getInt("varighet"), rs.getInt("personligForm"), rs.getInt("prestasjon"), rs.getString("notat"));
    			treningsOkter.add(t);
    		}
    		
    		return treningsOkter;
    	}
    	
    	

    	//KRAV 3///////////////Hente ut øvelse x mellom to datoer y og z/////////////////
  	
    	@SuppressWarnings("resource")
		public static List<Ovelse> getInfoAboutOvelseInTimeInterval(Connection connection, String ovelsesnavn, Date startsDato, Date sluttDato) throws SQLException{
  		
    		List<Ovelse> ovelser = new ArrayList<>();
  		
    		//Sjekker først om det finnes noen ovelsesnavn i apparatøvelse. Hvis det gjør det så execute kode
    		String queryStatement = "select count(*) from apparatovelse where ovelsesnavn = ?";
    		PreparedStatement preStat = connection.prepareStatement(queryStatement);
    		preStat.setString(1, ovelsesnavn);
    		ResultSet rs = preStat.executeQuery();

    		rs.next();
    		System.out.println(rs);
    		if (rs.getInt(1) > 0){
    			/////SJUUUUUK SPØRRING//////
    			queryStatement = "select dato, tidspunkt, antallKilo, antallSett, apparatNavn from ovelseITreningsokt JOIN "
    					+ "apparatovelse on (apparatovelse.ovelsesnavn = ovelseITreningsokt.ovelsesnavn) "
    					+ "and dato >= ? and dato < ? and apparatovelse.ovelsesnavn = ?";
    			preStat = connection.prepareStatement(queryStatement);
    			preStat.setDate(1, startsDato);
    			preStat.setDate(2, sluttDato);
    			preStat.setString(3, ovelsesnavn);
    			
    			ResultSet resultat = preStat.executeQuery();
    			while (resultat.next()){
 
    				ApparatOvelse apparatovelse = new ApparatOvelse(ovelsesnavn, 
    				resultat.getInt("antallKilo"), resultat.getInt("antallSett"), new Apparat(resultat.getString("apparatNavn")));
    		  		ovelser.add(apparatovelse);
    			}
    			return ovelser;
    		}

    		queryStatement = "select count(*) from friovelse where ovelsesnavn = ?";
    		preStat = connection.prepareStatement(queryStatement);
    		preStat.setString(1, ovelsesnavn);
    		
    		rs = preStat.executeQuery();
    		rs.next();

    		if (rs.getInt(1) > 0){
    			queryStatement = "select dato, tidspunkt, beskrivelse from ovelseITreningsokt JOIN "
    					+ "friovelse on (friovelse.ovelsesnavn = ovelseITreningsokt.ovelsesnavn) "
    					+ "and dato >= ? and dato < ? and friovelse.ovelsesnavn = ?";
    			preStat = connection.prepareStatement(queryStatement);
    			preStat.setDate(1, startsDato);
    			preStat.setDate(2, sluttDato);
    			preStat.setString(3, ovelsesnavn);
    			
    			ResultSet resultat = preStat.executeQuery();
    			while (resultat.next()){
    				System.out.println("Da var vi inne her");
    				FriOvelse friOvelse= new FriOvelse(ovelsesnavn, resultat.getString("beskrivelse"));
    		  		ovelser.add(friOvelse);
    			}
    			return ovelser;
    		}
			return null;
  		
    	}
    	
    	
    	
    	
    	//KRAV 4////////////LAGE ØVELSESGRUPPER OG FINNE ØVELSER SOM ER I SAMME GRUPPE////////
    	
    	public static List<Ovelse> getOvelserIOvelsesgrupper(Connection connection, String ovelsesgruppe) throws SQLException{
    		
    		List<Ovelse> ovelser = new ArrayList<>();
    		
    		String queryStatement = "select ovelsesnavn from ovelseIOvelsesgruppe where ovelsesgruppe = ?";
    		PreparedStatement preStat = connection.prepareStatement(queryStatement);
    		preStat.setString(1, ovelsesgruppe);
    		ResultSet resultat = preStat.executeQuery();
    		
    		while (resultat.next()){
    			Ovelse ovelse = new Ovelse(resultat.getString("ovelsesnavn"));
    			if (ovelser.contains(ovelse)){
    				ovelse.getOvelsesnavn();
    			}
    			else{
    				ovelser.add(ovelse);
    			}
    		}
			return ovelser;
    		
    	}
    	
    	
    	//KRAV 5///////////HVOR MANGE TRENINGSØKTER TOTALT/////////
    	
    	public static int getTotalTreningsøkter(Connection conn) throws SQLException {
    		String stmt  = "select count(dato) as total from treningsokt";
    		PreparedStatement pr = conn.prepareStatement(stmt);
    		ResultSet rs = pr.executeQuery();
    		return rs.next() ? rs.getInt("total") : 0;
    	}
    	
    	
    	///////////ANDRE GET METODER/////////////
    	
    	//Henter ut apparater////FERDIG
    	public static List<Apparat> getApparater(Connection connection) throws SQLException{
    		
    		//Oppretter list med apparater
    		List<Apparat> apparater = new ArrayList<>();
    		
    		//Henter ut apparater
    		String queryStatement = "select * from apparat";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		ResultSet rs = prepStat.executeQuery();
    		
    		//Resultset starter på index 0, vi må finne neste
    		while(rs.next()){
    			Apparat apparat = new Apparat(rs.getString(1), rs.getString(2));
    			apparater.add(apparat);
    		}
    		
			return apparater;
    		
    	}
    	

    	//Henter ut treningsøkter////FERDIG
    	public static List<TreningsOkt> getTreningsøkter(Connection connection) throws SQLException{
    		
    		List<TreningsOkt> treningsOkter = new ArrayList<TreningsOkt>();
    		
    		String stmt = "select * from treningsokt";
    		PreparedStatement prepStat = connection.prepareStatement(stmt);
    		ResultSet rs = prepStat.executeQuery();
    		
    		while(rs.next()) {
    			TreningsOkt t = new TreningsOkt(rs.getDate("dato"), rs.getTime("tidspunkt"), 
    			rs.getInt("varighet"), rs.getInt("personligForm"), rs.getInt("prestasjon"), rs.getString("notat"));
    			treningsOkter.add(t);
    		}
    		
    		return treningsOkter;
    	}
    	
    
    	//HENTER UT ØVELSESGRUPPER/////FERDIG
    	public static List<Ovelsesgruppe> getOvelsesgrupper(Connection connection) throws SQLException{
    		
    		//Oppretter list med OvelsesGrupper
    		List<Ovelsesgruppe> ovelsesgrupper = new ArrayList<>();
    		
    		//Henter ut OvelsesGrupper fra ovelsesgruppe-tabell
    		String queryStatement = "select * from ovelsesgruppe";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		ResultSet rs = prepStat.executeQuery();
    		
    		while (rs.next()){
    			Ovelsesgruppe ovelsesgruppe1 = new Ovelsesgruppe(rs.getString("ovelsesgruppenavn"));
    			ovelsesgrupper.add(ovelsesgruppe1);
    		}
    		
    		//Henter ut ovelsesgrupper fra ovelseiovelsesgruppe
    		queryStatement = "select ovelsesgruppe from ovelseIOvelsesgruppe";
    		prepStat = connection.prepareStatement(queryStatement);
    		rs = prepStat.executeQuery();
    		
    		while (rs.next()){
    			Ovelsesgruppe ovelsesgruppe2 = new Ovelsesgruppe(rs.getString("ovelsesgruppe"));
    			ovelsesgrupper.add(ovelsesgruppe2);
    		}
    		
			return ovelsesgrupper;
    		
    	}
    	
    	//HENTER UT OVELSER//////FERDIG
    	
    	public static List<Ovelse> getOvelserSomHarBlittGjort(Connection connection) throws SQLException{
    		
    		List<Ovelse> ovelser = new ArrayList<>();
    		
    		//Henter ut ovelse fra apparatovelse
    		String queryStatement = "select * from apparatovelse";
    		PreparedStatement prepStat = connection.prepareStatement(queryStatement);
    		ResultSet rs = prepStat.executeQuery();
    		
    		while (rs.next()){
    			ApparatOvelse apparatovelse = new ApparatOvelse(rs.getString("ovelsesnavn"), rs.getInt("antallKilo"), 
    					rs.getInt("antallSett"), new Apparat(rs.getString("apparatNavn")));
    			ovelser.add(apparatovelse);
    		}
    		
    		//Henter ut ovelser fra friovelse
    		queryStatement = "select * from friovelse";
    		prepStat = connection.prepareStatement(queryStatement);
    		rs = prepStat.executeQuery();
    		
    		while (rs.next()){
    			FriOvelse friOvelse = new FriOvelse(rs.getString("ovelsesnavn"), rs.getString("beskrivelse"));
    			ovelser.add(friOvelse);
    		}
    	
			return ovelser;
    		
    	}
    	
    	
    	///////////////////MAIN ////////////////
    	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			/*
			System.out.println(getNSisteTreningsøkter(DBConnection.createDBConnection(), 3));
    		DatabaseOperations.addApparatØvelse(DBConnection.createDBConnection(), "hei", "90", "5", "hola");
    		
    		Statement prepStatement = DBConnection.createDBConnection().createStatement();
    		ResultSet rs = prepStatement.executeQuery("select count(*) from apparatovelse where ovelsesnavn like 'hei'");
    		System.out.println(rs);
    		rs.next();
    		System.out.println(rs.getInt(1));
    		
    		DatabaseOperations.addApparat(DBConnection.createDBConnection(), "Hei", "Dette er en test");
    		System.out.println(DatabaseOperations.getApparater(DBConnection.createDBConnection()));
    		System.out.println(getInfoAboutOvelseInTimeInterval(DBConnection.createDBConnection(), "heioghopp", new Date(1990-1900, 10, 20), new Date(2000-1900, 10, 20)));
    		
    		DatabaseOperations.addOvelseIOvelsesgruppe(DBConnection.createDBConnection(), "Test", "JADA");
    		System.out.println(DatabaseOperations.getOvelserIOvelsesgrupper(DBConnection.createDBConnection(), "JADA"));
    		
    		System.out.println(DatabaseOperations.getTotalTreningsøkter(DBConnection.createDBConnection()));
    		
    		DatabaseOperations.addOvelsesgruppe(DBConnection.createDBConnection(), "Test testesen");
    		System.out.println(DatabaseOperations.getOvelsesgrupper(DBConnection.createDBConnection()));
    		
    		System.out.println(DatabaseOperations.getOvelser(DBConnection.createDBConnection()));
    		*/
    		DatabaseOperations.addOvelse(DBConnection.createDBConnection(), "testøvelse");
		}	
    	
    }
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	


    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    