package project;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FxAppController extends Application {
	
	@FXML
	TextField txtDato, txtFra, txtTil, txtTidspunkt, txtVarighet, txtPersonligForm, txtPrestasjon,  
			txtNotat, txtKilo, txtSett, txtListeOvelser, txtApparatOvelse, txtFriOvelse, txtApparat, 
			txtBeskrivelse, txtOvelseGruppe, txtNSisteOkter, txtOvelserGruppe, txtNavnGruppe, 
			txtFriBeskrivelse, txtApparatValg, txtVelgOvelse, txtOvelseNavn, txtOvelsesGruppeApparat, txtApparatTilOvelse,
			txtOvelsesGruppeFri, txtSokGruppe,txtTotAntallOkter;
	@FXML
	ChoiceBox<Ovelse> cboxOvelser, cboxOvelseVelger;
	@FXML
	ChoiceBox<Ovelsesgruppe> cboxOvelsesGruppe1, cboxOvelsesGruppe2, cboxOvelsesgrupper;
	@FXML
	ChoiceBox<Apparat> cboxApparat;
	@FXML
	Button btVelg, btAdd, btOpprettOkt, btAddApparatOvelse, btAddFriOvelse, btAddApparat, btSokGrupper, btSokNSiste, btOpprettOvelsesGruppe, btLagreOkt, btSokOvelser;
	@FXML
	TableView<TreningsOkt> tblvOkter;
	@FXML
	TableView<OvelseITreningsokt> tblvOvelseOversikt;
	@FXML
	TableColumn<TreningsOkt, String> tblcDato, tblcTidspunkt, tblcNotat;
	@FXML
	TableColumn<OvelseITreningsokt, Date> tblcDatoLogg;
	@FXML
	TableColumn<OvelseITreningsokt, String> tblcNavnLogg, tblcBeskrivelseLogg;
	@FXML
	TableColumn<TreningsOkt, Integer> tblcVarighet, tblcForm, tblcPrestasjon;
	@FXML
	TableColumn<OvelseITreningsokt, Integer> tblcKiloLogg, tblcSettLogg;
	@FXML
	Tab tabOvelsesGruppe, tabApparat, tabOvelse, tabOkt, tabOversiktOkter, tabLogg;
	TreningsOkt treningsOkt;
	String listeMedOvelser = "";
	Date fraDato, tilDato;
	ObservableList<TreningsOkt> treningsOkterObs = FXCollections.observableArrayList();
	ObservableList<OvelseITreningsokt> loggObs = FXCollections.observableArrayList();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	
	// i stedet for i database
	List<Ovelse> ovelser = new ArrayList<Ovelse>();
	List<Apparat> apparater = new ArrayList<Apparat>();
	List<Ovelsesgruppe> ovelsesGrupper = new ArrayList<Ovelsesgruppe>();
	List<TreningsOkt> treningsOkter = new ArrayList<TreningsOkt>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
		Scene scene = new Scene(root, 900, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// ----------------------------------------------------------
	// TreningsOkt
	// ----------------------------------------------------------
	
	public static java.sql.Date stringToDate(String date){
		String[] deler = date.split("-");
		Integer år = Integer.parseInt(deler[2]) - 1990;
		Integer måned = Integer.parseInt(deler[1]) - 1;
		Integer dag = Integer.parseInt(deler[0]);
		@SuppressWarnings("deprecation")
		java.sql.Date dato = new java.sql.Date(år, måned, dag);
		return dato;
	}

	public static Time stringToTime(String tid){
		String[] deler = tid.split(":");
		Integer time = Integer.parseInt(deler[0]);
		Integer minutt = Integer.parseInt(deler[1]);
		Integer sekund = Integer.parseInt(deler[2]);
		@SuppressWarnings("deprecation")
		Time tiden = new java.sql.Time(time, minutt, sekund);
		return tiden;
	}
	
	public void lagOkt() throws NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DatabaseOperations.addTreningsOkt(DBConnection.createDBConnection(), 
				stringToDate(txtDato.getText()), 
				stringToTime(txtTidspunkt.getText()), 
				Integer.parseInt(txtVarighet.getText()), 
				Integer.parseInt(txtPersonligForm.getText()), 
				Integer.parseInt(txtPrestasjon.getText()), 
				txtNotat.getText());
		System.out.println("TreningsOkt lagt til i database");
	}
	
	//Går for en litt pragmatisk løsning her ettersom at vi aldri trenger å hente ut ovelsesinfo som er knyttet opp mot treningsokt
	public void leggTilOvelserITreningsokt() throws SQLException, NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		String ovelsesnavn = txtOvelseNavn.getText();
		
		
		//Henter ut alle ovelser fra getOvelser.
		for (Ovelse ovelse : DatabaseOperations.getOvelser(DBConnection.createDBConnection())){
			if (ovelse.getOvelsesnavn().equals(ovelsesnavn)){
				
			
				//Hvis ApparatOvelse, Ovelse.type er lagt til som et attributt.
				if (ovelse.getType().equals("ApparatOvelse")){
					
					//Oppretter ApparatOvelse objekt
					ApparatOvelse ovelseIOkt = new ApparatOvelse(ovelse.getOvelsesnavn());
					ovelseIOkt.setAntallKilo(txtKilo.getText());
					ovelseIOkt.setAntallSett(txtSett.getText());
					for (Apparat apparat : DatabaseOperations.getApparater(DBConnection.createDBConnection())) {
						if (apparat.getNavn().equals(txtApparatValg.getText())) {
							ovelseIOkt.setApparat(apparat);
						}
					}
					//ApparatOvelse lagt til
					DatabaseOperations.addApparatOvelse(DBConnection.createDBConnection(), ovelseIOkt.getOvelsesnavn(), 
							ovelseIOkt.getAntallKilo(), ovelseIOkt.getAntallSett(), ovelseIOkt.getApparat().getNavn());
					
					//Legger til Ovelse i OvelseITreningOkt
					DatabaseOperations.addOvelseITreningsOkt(DBConnection.createDBConnection(), 
							(DatabaseOperations.getNSisteTreningsOkter(DBConnection.createDBConnection(), 1)).get(0).getDato(),
							(DatabaseOperations.getNSisteTreningsOkter(DBConnection.createDBConnection(), 1)).get(0).getStartTidspunkt(),
							txtOvelseNavn.getText());
				}
				
				
				//Hvis friovelse. Vet ikke hvor greit det er å caste her da, men virker fair
				else {
					FriOvelse ovelseIOkt = new FriOvelse(ovelse.getOvelsesnavn(), 
							((FriOvelse) ovelse).getBeskrivelse());
					
					//FriOvelse lag til
					DatabaseOperations.addFriOvelse(DBConnection.createDBConnection(), ovelseIOkt.getOvelsesnavn(),
							ovelseIOkt.getBeskrivelse());
					
					//Legger til Ovelse i OvelseITreningOkt
					DatabaseOperations.addOvelseITreningsOkt(DBConnection.createDBConnection(), 
							(DatabaseOperations.getNSisteTreningsOkter(DBConnection.createDBConnection(), 1)).get(0).getDato(),
							(DatabaseOperations.getNSisteTreningsOkter(DBConnection.createDBConnection(), 1)).get(0).getStartTidspunkt(),
							txtOvelseNavn.getText());
				}
			}
			
			//Hvis ovelsen ikke eksisterer i ovelse-database-tabell
			else {
				System.out.println("Du må definere ovelsen forst.");
			}
		}
		
		System.out.println("Ovelse lagret til okt");
		txtKilo.clear();
		txtSett.clear();
		txtOvelseNavn.clear();
		txtApparatValg.clear();
		
	}
		
	
	// ----------------------------------------------------------
	// Ovelse
	// ----------------------------------------------------------
	
	
	public void addApparatOvelse() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		boolean check = false;
		
		for (Apparat apparat : DatabaseOperations.getApparater(DBConnection.createDBConnection())){
			if (txtApparatTilOvelse.getText().equals(apparat.getNavn())){
				check = true;
			}
		}
		
		if (check == false){
			System.out.println("Du må legge til en ovelse forst");
			return;
		}
		
		//Legger til ApparatOvelse i ovelsesGrupper
		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(DBConnection.createDBConnection())) {
			if (gruppe.getNavn().equals(txtOvelsesGruppeApparat.getText())) {
				
				DatabaseOperations.addOvelseIOvelsesgruppe(
						DBConnection.createDBConnection(), 
						txtApparatOvelse.getText(), 
						txtOvelsesGruppeApparat.getText());
			}
		}
		
		//Add ApparatOvelse i ovelse
		DatabaseOperations.addOvelse(DBConnection.createDBConnection(), txtApparatOvelse.getText(), "ApparatOvelse");
		txtApparatOvelse.clear();
		txtOvelsesGruppeApparat.clear();
		txtApparatTilOvelse.clear();
		System.out.println("Apparatovelse lagt til");
	}
	
	//Legger til en FriOvelse i ovelseiovelsesgruppe og i ovelse
	public void addFriOvelse() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		FriOvelse friOvelse = new FriOvelse(txtFriOvelse.getText(), txtFriBeskrivelse.getText());

		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(DBConnection.createDBConnection())) {
			if (gruppe.getNavn().equals(txtOvelsesGruppeFri.getText())) {
				
				DatabaseOperations.addOvelseIOvelsesgruppe(
						DBConnection.createDBConnection(), 
						friOvelse.getOvelsesnavn(), 
						gruppe.getNavn());
			}
		}
		
		//Add friOvelse i ovelse
		DatabaseOperations.addOvelse(DBConnection.createDBConnection(), txtFriOvelse.getText(), "FriOvelse");
		txtOvelsesGruppeFri.clear();
		txtFriOvelse.clear();
		txtFriBeskrivelse.clear();
		System.out.println("Friovelse lagt til");
	}
	
	// ----------------------------------------------------------
	// Legg til Apparat
	// ----------------------------------------------------------
	
	public void addApparat() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		DatabaseOperations.addApparat(DBConnection.createDBConnection(), txtApparat.getText(), txtBeskrivelse.getText());
		txtApparat.clear();
		txtBeskrivelse.clear();
	}
	
	// ----------------------------------------------------------
	// �velsesgruppe
	// ----------------------------------------------------------
	
	// N�r fanen velges kj�res denne, legger inn i choicebox
	
	// Siden vi har valgt � ikke bruke en table i database for OvelsesGruppe saa maa opprettOvelsesGruppe utgaa
	// Dette er egentlig et krav, men da gjoeres det slik at hver gang du lager et apparat saa legger man til en ovelsesgruppe,
	// som String da som lagres i databasen. 
	public void opprettOvelsesGruppe() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Ovelsesgruppe oGruppe = new Ovelsesgruppe(txtNavnGruppe.getText());
		DatabaseOperations.addOvelsesgruppe(DBConnection.createDBConnection(), 
				oGruppe.getNavn());
		txtNavnGruppe.clear();
		System.out.println("Gruppe laget");
		
	}
	
	public void sokIOvelsesGruppe() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		txtOvelseGruppe.clear();
		String listeOvelser = "";
		// Her m� man legge inn ovelserIOvelsesgrupper, og sjekke feltet for ovelsesgruppe om det stemmer med det som er skrevet inn
		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(DBConnection.createDBConnection())) {
			if (txtSokGruppe.getText().equals(gruppe.getNavn())) {
				for (Ovelse ovelse : gruppe.getOvelser()) {
					listeOvelser += (ovelse.getOvelsesnavn() + ", ");
				}
			}
			
		}
		txtOvelseGruppe.setText(listeOvelser);
		
	}
	
	// -----------------------------------------------------------
	// Oversikt okter
	// -----------------------------------------------------------
	
	public void initTable() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		int antall = Integer.parseInt(txtNSisteOkter.getText());
		tblcDato.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("dato"));
		tblcTidspunkt.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("startTidspunkt"));
		tblcNotat.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("notat"));
		tblcVarighet.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("varighet"));
		tblcForm.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("personligForm"));
		tblcPrestasjon.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("prestasjon"));
		
		txtTotAntallOkter.setText(String.valueOf(DatabaseOperations.getTotalTreningsokter(DBConnection.createDBConnection())));
		tblvOkter.setItems(getOkter(antall));
	}
	
	private ObservableList<TreningsOkt> getOkter(int antall) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (!treningsOkterObs.isEmpty()) {
			treningsOkterObs.clear();
		}
		for (int i = 0; i < antall; i++) {
			treningsOkterObs.add(DatabaseOperations.getNSisteTreningsOkter(DBConnection.createDBConnection(), antall).get(i));
		}
		return treningsOkterObs;
	}
	
	// -----------------------------------------------------------
	// �velselogg
	// -----------------------------------------------------------
	
	public void sokOvelseLogg() throws ParseException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (!loggObs.isEmpty()) {
			loggObs.clear();
		}
		String startsDato1 = txtFra.getText();
		String sluttDato1 = txtTil.getText();
		String ovelsesnavn = txtVelgOvelse.getText();
		
		System.out.println(startsDato1);
		System.out.println(sluttDato1);
		System.out.println(ovelsesnavn);
	
		List<OvelseITreningsokt> ovelseit = new ArrayList<>();
		//getInfoAboutOvelseInTimeInterval(Connection connection, String ovelsesnavn, Date startsDato, Date sluttDato
		
		
		ovelseit.addAll(DatabaseOperations.getInfoAboutOvelseInTimeInterval(DBConnection.createDBConnection(), ovelsesnavn, 
				FxAppController.stringToDate(startsDato1), FxAppController.stringToDate(sluttDato1)));
		for (OvelseITreningsokt o : ovelseit){
			System.out.println(o.getOvelsesnavn());
		}
		
		tblcDatoLogg.setCellValueFactory(new PropertyValueFactory<OvelseITreningsokt, Date>("Dato"));
		tblcNavnLogg.setCellValueFactory(new PropertyValueFactory<OvelseITreningsokt, String>("Ovelsesnavn"));
		tblcKiloLogg.setCellValueFactory(new PropertyValueFactory<OvelseITreningsokt, Integer>("AntallKilo"));
		tblcSettLogg.setCellValueFactory(new PropertyValueFactory<OvelseITreningsokt, Integer>("AntallSett"));
		tblcBeskrivelseLogg.setCellValueFactory(new PropertyValueFactory<OvelseITreningsokt, String>("Beskrivelse"));
		
		tblvOvelseOversikt.setItems(loggObs);
		tblvOvelseOversikt.setVisible(true);
	}
	
	
	// -----------------------------------------------------------
	// Main
	// -----------------------------------------------------------
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		launch(args);
	}
	
		/*while (rs.next() && i < n) {
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
	
*/
}
