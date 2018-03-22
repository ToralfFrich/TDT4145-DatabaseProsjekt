package prosjekt_del2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sporringer {
	private String db = "tcfrich_treningsapp";
	
	public void insertIntoApparatovelse (Connection conn, String ovelsesnavn, String antallKilo, String antallSett, String apparatNavn) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".Apparatovelse(Ovelsesnavn, AntallKilo, AntallSett, ApparatNavn)"
    				+ "values('" + ovelsesnavn + "', '" + antallKilo + "', '" + antallSett + "','" + apparatNavn + "');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Apparatovelse lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of apparatovelse= "+e);
            return;
        }
    }
	
	public void insertIntoFriOvelse (Connection conn, String ovelsesnavn, String beskrivelse) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".FriOvelse(Ovelsesnavn, Beskrivelse)"
    				+ "values('" + ovelsesnavn + "', '" + beskrivelse + "');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Fri ovelse lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of friOvelse= "+e);
            return;
        }
    }
	
	public void insertIntoApparat (Connection conn, String navn, String beskrivelse) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".Apparat(Navn, Beskrivelse)"
    				+ "values('" + navn + "', '" + beskrivelse + "');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Apparat lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of apparat= "+e);
            return;
        }
    }
	
	public void insertIntoInngårI(Connection conn, String ovelsesnavn, String ovelsesgruppenavn) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".InngårI(Ovelsesnavn, Ovelsesgruppenavn)"
    				+ "values('" + ovelsesnavn + "', '" + ovelsesgruppenavn + "');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Inngår i lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of inngårI= "+e);
            return;
        }
    }
	
	public void insertIntoOvelseITreningsokt(Connection conn, String dato, String tidspunkt, String ovelsesnavn) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".OvelseITreningsokt(Dato, Tidspunkt, Ovelsesnavn)"
    				+ "values('" + dato + "', '" + tidspunkt + "', '"+ ovelsesnavn + "');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Ovelse i treningsokt lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of ovelseITreningsokt= "+e);
            return;
        }
    }
	
	public void insertIntoTreningsokt(Connection conn, String dato, String tidspunkt, String varighet, String personligForm, String prestasjon, String notat) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".Treningsokt(Dato, Tidspunkt, Varighet, PersonligForm, Prestasjon, Notat)"
    				+ "values('" + dato + "', '" + tidspunkt + "', '" + varighet + "', '" + personligForm + "', '" + prestasjon + "', '" + notat +"');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Treningsokt lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of treningsokt= "+e);
            return;
        }
    }
	
	public void insertIntoOvelsesgruppe(Connection conn, String ovelsesgruppenavn) {
        try {
            Statement stmt = conn.createStatement();
    			String sql = "INSERT INTO "+db+".Treningsokt(Ovelsesgruppenavn)"
    				+ "values('" + ovelsesgruppenavn +"');";
    			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    			System.out.println("Ovelsesgruppe lagt til!");
        }catch (Exception e) {
            System.out.println("db error during insert of ovelsesgruppe= "+e);
            return;
        }
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
				System.out.println("Dato " + dato +  "\n"
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
	
    
/*
    public void refresh (Connection conn) {
        initialize(conn);
    }
    
    
    public void save (Connection conn) {
        try {    
            Statement stmt = conn.createStatement(); 
            stmt.executeUpdate("insert into Avtale values (NULL,"+startTid+","+timer+","+type+")");
        } catch (Exception e) {
            System.out.println("db error during insert of Avtale="+e);
            return;
        }
        for (int i=0;i<brukere.size();i++) {
            try {    
                Statement stmt = conn.createStatement(); 
                stmt.executeUpdate("insert into HarAvtale values ("+brukere.get(i).getBid()+",LAST_INSERT_ID())");
            } catch (Exception e) {
                System.out.println("db error during insert of HarAvtale="+e);
                return;
            }
        }
    }
    */
}
