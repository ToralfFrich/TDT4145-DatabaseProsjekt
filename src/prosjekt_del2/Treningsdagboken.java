package prosjekt_del2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Treningsdagboken {

	private static String host = "mysql.stud.ntnu.no";
	private static String user = "magnukun_tdt4145";
	private static String password = "mordi";
	private static String database = "magnukun_dagbok";
	private static Connection myConn;
	private Statement treningStmt;
	private Statement ovelseStmt;
	private Statement pulsStmt;
	private String sql;
	private int T_ID;
	private int O_ID;
	private boolean treningAdded = false;

	//Trening variable
	private String treningsNavn;
	private String startTid;
	private String sluttTid;
	private boolean plass; //Inne = true, ute = false
	private int form;
	private int prestasjon;
	private String notat;
	private int luft;
	private int tilskuere;
	private String vÊrType;
	private int temperatur;

	//ÿvelse variable
	private String ovelseNavn;
	private String beskrivelse;
	private String katNavn;

	//Puls variable
	private String pgTid;
	private int puls;
	private double lengdeGrad;
	private double breddegrad;
	private double moh;

	//KategoriStryke
	private double belastning;
	private int repetisjoner;
	private int sett;

	//KategoriKondisjon
	private int lengde;
	private String kondisVarighet;
	
	//Kategori variable
	


	public Treningsdagbok() {
		createConnection();
	}

	private void createConnection() {
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://" + host + "/"
					+ database + "?" + "user=" + user + "&password=" + password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertTrening() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Hva heter treningen din?");
		this.treningsNavn = scan.nextLine();

		System.out.println("NÂr starter du din trening? Format: <YYYY-MM-DD HH:MM:SS>");
		this.startTid = scan.nextLine();

		System.out.println("NÂr sluttet du din trening?");
		this.sluttTid = scan.nextLine();

		System.out.println("Trente du inne eller ute?");

		if (scan.nextLine().equals("inne".toLowerCase())) {
			this.plass = true;
		} else {
			this.plass = false;
		}

		System.out.println("PÂ en skala fra 1-10, hvordan var din form under treningen?");
		this.form = Integer.parseInt(scan.nextLine());

		System.out.println("PÂ en skala fra 1-10, hvordan var din prestasjon under treningen?");
		this.prestasjon = Integer.parseInt(scan.nextLine());

		System.out.println("Skriv en kort notat for treningen");
		this.notat = scan.nextLine();

		try {
			treningStmt = myConn.createStatement();
			sql = "INSERT INTO magnukun_dagbok.Trening (Navn, Start_tid, Slutt_tid)"
					+ "values('" + treningsNavn + "', '" + startTid + "', '" + sluttTid + "');";
			treningStmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println("Trening lagt til!");

			try (ResultSet generatedKeys = treningStmt.getGeneratedKeys()){
				if (generatedKeys.next()){
					this.T_ID = generatedKeys.getInt(1);
				}
			}

		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		if (plass) {

			System.out.println("Hvordan var luften pÂ en skala fra 1-10?");
			this.luft = Integer.parseInt(scan.nextLine());

			System.out.println("Hvor mange tilskuere hadde du?");
			this.tilskuere = Integer.parseInt(scan.nextLine());			

			sql = "INSERT INTO magnukun_dagbok.Trening_Inne (Luft, Tilskuere, Form, Prestasjon, Notat, T_ID)"
					+ "values('" + luft + "', '" + tilskuere + "', '" + form + "', '" + prestasjon + "', '" + notat +"', '" + T_ID + "  ');";
			try {
				treningStmt.executeUpdate(sql);
				System.out.println("Trening_inne lagt til!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {

			System.out.println("Hvordan var vÊret?");
			this.vÊrType = scan.nextLine();

			System.out.println("Hva var temperaturen?");
			this.temperatur = Integer.parseInt(scan.nextLine());			

			sql = "INSERT INTO magnukun_dagbok.Trening_Ute (Temp, Vaertype, Form, Prestasjon, Notat, T_ID)"
					+ "values('" + temperatur + "', '" + vÊrType + "', '" + form + "', '" + prestasjon +
					"', '" + notat +"', '" + T_ID + "  ');";
			try {
				treningStmt.executeUpdate(sql);
				System.out.println("Trening_ute lagt til!");
			} catch (SQLException e) {
				e.printStackTrace();
			}


		}
		treningAdded = true;

		System.out.println("Vil du legge til øvelse? ja/nei");

		String janei = scan.nextLine();

		while(janei.equals("ja")){
			insertOvelse();
			System.out.println("\nVil du legge til øvelse? ja/nei\n");
			janei = scan.nextLine();

		}


	}

	public void insertOvelse() {
		
		if (treningAdded) {
			Scanner scan = new Scanner(System.in);
			String neste;
			try {
				System.out.println("Hva er navnet pÂ ¯velsen?");
				this.ovelseNavn = scan.nextLine();

				System.out.println("Beskriv ¯velsen:");
				this.beskrivelse = scan.nextLine();

				System.out.println("Hvilken kategori tilh¯rer ¯velsen?");
				this.katNavn = scan.nextLine();

				try {
					ovelseStmt = myConn.createStatement();
					sql = "Insert INTO magnukun_dagbok.Ovelse (Navn, Beskrivelse, katNavn ) "
							+ "values ('" + this.ovelseNavn + "', '" + this.beskrivelse + "', '" + this.katNavn + "');";
					ovelseStmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
					System.out.println("ÿvelse lagt til!");
					String quary = "SELECT Navn from Kategori;";
					Statement stmt = myConn.createStatement();
					ResultSet rst = null;
					if (stmt.execute(quary)) {
						rst = stmt.getResultSet();
					}
					System.out.println("Tilhører kategorien din noen av disse kategoriene?/n Skriv inn navn på kategori i liste. Skriv 'nei' hvis ikke.");
					while (rst.next()) {
						String kat = rst.getString(1);
						System.out.println(kat + "\n");
					}
					neste = scan.next();
					if (neste.equals("nei")) {
						insertKategori(this.katNavn, null);
					} else {
						insertKategori(this.katNavn, neste);
					}
					insertStyrkeKondisjon();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				try (ResultSet generatedKeys = ovelseStmt.getGeneratedKeys()){
					if (generatedKeys.next()){
						this.O_ID = generatedKeys.getInt(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Du må legge til treningsøkt før du legger til øvelse!\n");
		}
	}

	public void insertStyrkeKondisjon() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Var øvelsen din styrke eller kondisjon?");
		if (scan.nextLine().equals("styrke")) {

			System.out.println("Hva var belastningen?");
			this.belastning =  Integer.parseInt(scan.next());

			System.out.println("Hvor mange repetisjoner?");
			this.repetisjoner = Integer.parseInt(scan.next());

			System.out.println("Hvor mange sett?");
			this.sett = Integer.parseInt(scan.next());

			try {
				stmt = myConn.createStatement();

				sql = "Insert INTO magnukun_dagbok.Styrke (Navn, Belastning, Repetisjoner, Sett, O_ID) "
						+ "values ('"+ this.ovelseNavn + "', '" + this.belastning + "', '" + 
						this.repetisjoner + "', '" + this.sett + "', '" + this.O_ID +"');";  
				stmt.executeUpdate(sql);
				System.out.println("Styrketrening lagt til!");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Hva var belastningen?");
			this.belastning =  Integer.parseInt(scan.next());

			System.out.println("Hvor mange repetisjoner?");
			this.repetisjoner = Integer.parseInt(scan.next());

			System.out.println("Hvor mange sett?");
			this.sett = Integer.parseInt(scan.next());
			
			System.out.println("Hvor langt varte øvelsen (i antall hele kilometer)?");
			this.lengde = Integer.parseInt(scan.next());
			
			System.out.println("Hvor lenge varte øvelsen(<HH:MM:SS>)?");
			this.kondisVarighet = scan.next();
			

			try {
				stmt = myConn.createStatement();

				sql = "Insert INTO magnukun_dagbok.Kondisjon (Navn, Belastning, Repetisjoner, Sett, Lengde,"
						+ "Kondis_varighet, O_ID) "
						+ "values ('"+ this.ovelseNavn + "', '" + this.belastning + "', '" + 
						this.repetisjoner + "', '" + this.sett + "', '" + this.lengde + "', '" +
						this.kondisVarighet + "', '" + this.O_ID +"');";
				stmt.executeUpdate(sql);
				System.out.println("Kondisjonstrening lagt til!");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public void insertPuls() {
		if (this.treningAdded) {
			Scanner scan = new Scanner(System.in);

			System.out.println("Skriv inn tiden for pulsdataen:");
			this.pgTid = scan.nextLine();

			System.out.println("Skriv inn pulsen din:");
			this.puls = Integer.parseInt(scan.nextLine());

			System.out.println("Skriv inn lengdegraden:");
			this.lengdeGrad = Double.parseDouble(scan.nextLine());

			System.out.println("Skriv inn breddegraden:");
			this.breddegrad = Double.parseDouble(scan.nextLine());

			System.out.println("Skriv inn h¯yden over havet:");
			this.moh = Double.parseDouble(scan.nextLine());

			try {
				pulsStmt = myConn.createStatement();
				sql = "Insert INTO magnukun_dagbok.Puls_GPS (PG_tid, Puls, Lengdegrad"
						+ ", Breddegrad, Moh, T_id)"
						+ "values ('" + this.pgTid + "', '" + this.puls + "', '" + 
						this.lengdeGrad + "', '" + this.breddegrad + "', '" + this.moh +
						"', '" + this.T_ID + "')";
				pulsStmt.executeUpdate(sql);
				System.out.println("Pulsdata lagt til!");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Du må legge til en treningsøkt før du kan legge til pulsdata!\n");
		}
	}

	public void insertKategori(String katnavn, String member) {

		if(!member.equals("null")){
			sql = "Insert INTO magnukun_dagbok.Kategori (Navn, memberOf)"
					+ " values ('" + katnavn + "', '" + member + "');";
		}else{
			sql = "Insert INTO magnukun_dagbok.Kategori (Navn)"
					+ " values('" + katnavn + "');";
			
		}

	System.out.println(sql);
		try {
			Statement stmt = myConn.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Kategori lagt til!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Sporing:
	Statement stmt = null;
	ResultSet rs = null;
	String inOrout;

	public void sporringBesteTrening(){

		System.out.println("Skrive ut beste trening:\n\nVil du se på bste trening inne eller ute?");
		
		Scanner scan = new Scanner(System.in);

		//System.out.println("Hvilken uke vil du hente ut for? Format: <YYYY-MM-DD HH:MM:SS> , <YYYY-MM-DD HH:MM:SS>");

		inOrout = scan.next();
		
		if( inOrout.equals("inne")){

			try{
				stmt = myConn.createStatement();
				String quary = "SELECT * FROM magnukun_dagbok.Trening "+
						"where T_ID "+
						"in( select magnukun_dagbok.Trening.T_ID from magnukun_dagbok.Trening join magnukun_dagbok.Trening_Inne on magnukun_dagbok.Trening.T_ID = magnukun_dagbok.Trening_Inne.T_ID " +
						"where (date_sub(now(), interval 7 day) <= Start_tid and Start_tid < now() "+
						"and ( select magnukun_dagbok.Trening_Inne.T_ID from magnukun_dagbok.Trening_Inne "+
						"where magnukun_dagbok.Trening_Inne.Prestasjon =( select max(magnukun_dagbok.Trening_Inne.Prestasjon) from magnukun_dagbok.Trening_Inne))));";

				if(stmt.execute(quary)){
					rs = stmt.getResultSet();
				}
				while (rs.next()) {
					String kol1 = rs.getString(1);
					String kol2 = rs.getString(2);
					String kol3 = rs.getString(3);
					String kol4 = rs.getString(4);
					System.out.println(kol2 + " - " + kol3);
				}
			} catch (SQLException ex) {
				System.out.println("SQLExcetion:" + ex.getMessage());
				ex.printStackTrace();
			}

		}else if(inOrout.equals("ute")){
			try{
				stmt = myConn.createStatement();
				String quary = "SELECT * FROM magnukun_dagbok.Trening "+
						"where T_ID "+
						"in( select magnukun_dagbok.Trening.T_ID from magnukun_dagbok.Trening join magnukun_dagbok.Trening_Ute on magnukun_dagbok.Trening.T_ID = magnukun_dagbok.Trening_Ute.T_ID " +
						"where (date_sub(now(), interval 7 day) <= Start_tid and Start_tid < now() "+
						"and ( select magnukun_dagbok.Trening_Ute.T_ID from magnukun_dagbok.Trening_Ute "+
						"where magnukun_dagbok.Trening_Ute.Prestasjon =( select max(magnukun_dagbok.Trening_Ute.Prestasjon) from magnukun_dagbok.Trening_Ute ))));";

				if(stmt.execute(quary)){
					rs = stmt.getResultSet();
				}
				while (rs.next()) {
					String kol1 = rs.getString(1);
					String kol2 = rs.getString(2);
					String kol3 = rs.getString(3);
					String kol4 = rs.getString(4);
					System.out.println(kol2 + " - " + kol3);
				}
			} catch (SQLException ex) {
				System.out.println("SQLExcetion:" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	public void Statsporring(){

		Scanner scanner = new Scanner(System.in);
		System.out.println("Statistikk:\n");

		ResultSet res= null;
		ResultSet res2 = null;
		ResultSet res3 = null;
		String quary = "select magnukun_dagbok.Trening.Start_tid, magnukun_dagbok.Trening.Navn, timestampdiff(minute, Trening.Start_tid, magnukun_dagbok.Trening.Slutt_tid) from magnukun_dagbok.Trening "+
				"where (date_sub(now(), interval 30 day) <= Start_tid and Start_tid < now());";

		String quary2 = "select sum(timestampdiff(minute, Trening.Start_tid, magnukun_dagbok.Trening.Slutt_tid)) from magnukun_dagbok.Trening "+
				"where (date_sub(now(), interval 30 day) <= Start_tid and Start_tid < now());";

		String quary3 ="select count(magnukun_dagbok.Trening.T_ID) from magnukun_dagbok.Trening "+
				"where (date_sub(now(), interval 30 day) <= Start_tid and Start_tid < now());";

		try{
			Statement statstmt = myConn.createStatement();
			Statement statstmt2 = myConn.createStatement();
			Statement statstmt3 = myConn.createStatement();

			if(statstmt.execute(quary)){
				res = statstmt.getResultSet();
			}
			if(statstmt2.execute(quary2)){
				res2 = statstmt2.getResultSet();
			}
			if(statstmt3.execute(quary3)){
				res3 = statstmt3.getResultSet();
			}

			while(res.next()){
				String kol1 = res.getString(1);
				String kol2 = res.getString(2);
				String kol3 = res.getString(3);

				System.out.println(kol1+ " - "+ kol2 + " - " + kol3 + "min");

			}

			res2.next();
			System.out.println("Totalt antall minutter: " + res2.getString(1));

			res3.next();
			System.out.println("Totalt antall økter: " + res3.getString(1));

		} catch (SQLException ex) {
			System.out.println("SQLExcetion:" + ex.getMessage());
			ex.printStackTrace();
		}

	}



	public void getNotat(){


		ResultSet res = null;
		ResultSet res2 = null;

		String quary = "select magnukun_dagbok.Trening.Navn, magnukun_dagbok.Trening_Inne.Notat, magnukun_dagbok.Trening.Start_tid "+
				"FROM magnukun_dagbok.Trening JOIN magnukun_dagbok.Trening_Inne ON magnukun_dagbok.Trening.T_ID = magnukun_dagbok.Trening_Inne.T_ID;";

		String quary2 = "select magnukun_dagbok.Trening.Navn, magnukun_dagbok.Trening_Ute.Notat, magnukun_dagbok.Trening.Start_tid "+
				"FROM magnukun_dagbok.Trening JOIN magnukun_dagbok.Trening_Ute ON magnukun_dagbok.Trening.T_ID = magnukun_dagbok.Trening_Ute.T_ID;";

		try{
			Statement stmt = myConn.createStatement();
			Statement stmt2 = myConn.createStatement();


			if(stmt.execute(quary)){
				res = stmt.getResultSet();
			}
			if(stmt2.execute(quary2)){
				res2 = stmt2.getResultSet();
			}

			System.out.println("Notater inne:\n");
			while(res.next()){
				String kol1 = res.getString(1);
				String kol2 = res.getString(2);
				String kol3 = res.getString(3);

				System.out.println(kol1 + " - " + kol2 + " - " + kol3);
			}

			System.out.println("\nNotater ute:\n");
			while(res2.next()){
				String kol1 = res2.getString(1);
				String kol2 = res2.getString(2);
				String kol3 = res2.getString(3);

				System.out.println(kol1 + " - " + kol2 + " - " + kol3);
			}

		}catch(SQLException ex){
			ex.printStackTrace();
		}



	}

	public void startMeny() {
		System.out.println("\nHer får du fire valg:\n"
				+ "1. Legg til trening\n"
				+ "2. Sjekk beste trening for denne uken\n"
				+ "3. Statistikk for forrige måned\n"
				+ "4. Notater fra siste måned\n"
				+ "5. Legg til kategori\n"
				+ "6. Avslutt program\n\n"
				
				+ "Velg 1, 2, 3, 4, 5 eller 6");
	}




	public static void main(String[] args) {

		Treningsdagbok bok = new Treningsdagbok();

		bok.startMeny();

		Scanner scanner = new Scanner(System.in);

		int tall;
 
		tall = Integer.parseInt(scanner.next());

		while (tall != 6) {

			if (tall == 1) {
				System.out.println("1. Legg til treningsøkt\n"
						+ "2. Legg til øvelse\n"
						+ "3. Legg til puls");

				int choice = Integer.parseInt(scanner.next());

				if (choice == 1) {
					bok.insertTrening();
				} else if (choice == 2) {
					bok.insertOvelse();
				} else if (choice == 3) {
					bok.insertPuls();
				}
			} else if (tall == 2) {
				bok.sporringBesteTrening();
			} else if (tall == 3) {
				bok.Statsporring();
			} else if (tall == 4) {
				bok.getNotat();
			}else if (tall==5){
				System.out.println("Hvilken kategori vil du legge til:");
				Scanner scan = new Scanner(System.in);
				String s = scan.nextLine();
				bok.insertKategori(s, "null");
			}

			bok.startMeny();
			tall = Integer.parseInt(scanner.next());
		}

		System.out.println("Programmet er ferdig!");
		return;
	}

}
