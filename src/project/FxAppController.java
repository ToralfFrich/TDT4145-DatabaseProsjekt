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
	Button btVelg, btAdd, btOpprettOkt, btAddApparatOvelse, btAddFriOvelse, btAddApparat, btSokGrupper, btSokNSiste, btOpprettOvelsesGruppe, btLagreOkt;
	@FXML
	TableView<TreningsOkt> tblvOkter;
	@FXML
	TableView<Ovelse> tblvOvelseOversikt;
	@FXML
	TableColumn<TreningsOkt, String> tblcDato, tblcTidspunkt, tblcNotat, tblcDatoLogg;
	@FXML
	TableColumn<Ovelse, String> tblcNavnLogg, tblcBeskrivelseLogg;
	@FXML
	TableColumn<TreningsOkt, Integer> tblcVarighet, tblcForm, tblcPrestasjon;
	@FXML
	TableColumn<Ovelse, Integer> tblcKiloLogg, tblcSettLogg;
	@FXML
	Tab tabOvelsesGruppe, tabApparat, tabOvelse, tabOkt, tabOversiktOkter, tabLogg;
	TreningsOkt treningsOkt;
	String listeMedOvelser = "";
	Date fraDato, tilDato;
	ObservableList<TreningsOkt> treningsOkterObs = FXCollections.observableArrayList();
	ObservableList<Ovelse> loggObs = FXCollections.observableArrayList();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	Connection conn;
	
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
		Integer år = Integer.parseInt(deler[0]) - 1900;
		Integer måned = Integer.parseInt(deler[1]) - 1;
		Integer dag = Integer.parseInt(deler[2]);
		java.sql.Date dato = new java.sql.Date(år, måned, dag);
		return dato;
	}

	public static Time stringToTime(String tid){
		String[] deler = tid.split(":");
		Integer time = Integer.parseInt(deler[0]);
		Integer minutt = Integer.parseInt(deler[1]);
		Integer sekund = Integer.parseInt(deler[2]);
		Time tiden = new Time(time, minutt, sekund);
		return tiden;
	}
	
	public void lagOkt() throws NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DatabaseOperations.addTreningsOkt(DBConnection.createDBConnection(), stringToDate(txtDato.getText()), 
				stringToTime(txtTidspunkt.getText()), Integer.parseInt(txtVarighet.getText()), 
				Integer.parseInt(txtPersonligForm.getText()), Integer.parseInt(txtPrestasjon.getText()), txtNotat.getText());
		System.out.println("Okt laget");
	}
	
	public void leggTilOvelserITreningsokt() throws SQLException, NumberFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Bytt ut lista her
		for (Ovelse ovelse : DatabaseOperations.getOvelser(DBConnection.createDBConnection())) {
			if (txtOvelseNavn.getText().equals(ovelse.getOvelsesnavn())) {
				if (ovelse instanceof ApparatOvelse) {
					System.out.println("Den skjonte at det var et Apparat, digg");
					ApparatOvelse ovelseIOkt = new ApparatOvelse(ovelse.getOvelsesnavn());
					ovelseIOkt.setAntallKilo(Integer.parseInt(txtKilo.getText()));
					ovelseIOkt.setAntallSett(Integer.parseInt(txtSett.getText()));
					// Bytt ut lista her
					for (Apparat apparat : DatabaseOperations.getApparater(conn)) {
						if (apparat.getNavn().equals(txtApparatValg.getText())) {
							ovelseIOkt.setApparat(apparat);
						}
					}
					DatabaseOperations.addOvelseITreningsOkt(conn, DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getDato(), 
							DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getStartTidspunkt(), ovelseIOkt.getOvelsesnavn());
					// Vi har ikke brukt objektet her, bare navnet, burde sende inn objektet i databasen, som har kilo osv. 
					// Burde endre hva add-metoden gjor
					// treningsOkter.get(treningsOkter.size() - 1).getOvelser().add(ovelseIOkt);
				}
				else {
					FriOvelse ovelseIOkt = new FriOvelse(ovelse.getOvelsesnavn(), ((FriOvelse) ovelse).getBeskrivelse());
					DatabaseOperations.addOvelseITreningsOkt(conn, DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getDato(), 
							DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getStartTidspunkt(), ovelseIOkt.getOvelsesnavn());
					// treningsOkter.get(treningsOkter.size() - 1).getOvelser().add(ovelseIOkt);
				}
			}
		}
		int storrelse = DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getOvelser().size();
		listeMedOvelser += DatabaseOperations.getNSisteTreningsOkter(conn, 1).get(0).getOvelser().get(storrelse - 1).getOvelsesnavn() + ", ";
		txtListeOvelser.setText(listeMedOvelser);
		System.out.println("Ovelser lagret til okt");
		System.out.println(treningsOkter.size());
		txtKilo.clear();
		txtSett.clear();
		txtOvelseNavn.clear();
		txtApparatValg.clear();
		
	}
		
	
	// ----------------------------------------------------------
	// Ovelse
	// ----------------------------------------------------------
	
	
	public void addApparatOvelse() throws SQLException {
		// Maa endre "apparater" til database.getApparater
		for (Apparat apparat : DatabaseOperations.getApparater(conn)) {
			//maa ha en metode for aa hente ut alle apparater
			if (apparat.getNavn().equals(txtApparatTilOvelse.getText())) {
				DatabaseOperations.addApparatOvelse(conn, txtApparatOvelse.getText(), 
						txtKilo.getText(), txtSett.getText(), txtApparatTilOvelse.getText());
			}
		}
		// Maa endre "ovelsesGrupper" til database.getOvelsesGrupper

		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(conn)) {
			//samme som over
			if (gruppe.getNavn().equals(txtOvelsesGruppeApparat.getText())) {
				DatabaseOperations.addOvelseIOvelsesgruppe(conn, txtApparatOvelse.getText(), gruppe.getNavn());
			}
		}
		// add i db
		DatabaseOperations.addOvelse(conn, txtApparatTilOvelse.getText());
		txtApparatOvelse.clear();
		txtOvelsesGruppeApparat.clear();
		txtApparatTilOvelse.clear();
		System.out.println("Apparatovelse lagt til");
	}
	
	public void addFriOvelse() throws SQLException {
		FriOvelse friOvelse = new FriOvelse(txtFriOvelse.getText(), txtFriBeskrivelse.getText());
		// Maa hente fra DB en liste med ovelser i ovelsesgruppe som stemmer mtp navnet skrevet inn

		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(conn)) {
			if (gruppe.getNavn().equals(txtOvelsesGruppeFri.getText())) {
				DatabaseOperations.addOvelseIOvelsesgruppe(conn, friOvelse.getOvelsesnavn(), gruppe.getNavn());
			}
		}
		// add i db
		DatabaseOperations.addOvelse(conn, txtFriOvelse.getText());
		txtOvelsesGruppeFri.clear();
		txtFriOvelse.clear();
		txtFriBeskrivelse.clear();
		System.out.println("Friovelse lagt til");
	}
	
	// ----------------------------------------------------------
	// Legg til Apparat
	// ----------------------------------------------------------
	
	public void addApparat() throws SQLException {
		DatabaseOperations.addApparat(conn, txtApparat.getText(), txtBeskrivelse.getText());
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
	public void opprettOvelsesGruppe() throws SQLException {
		Ovelsesgruppe oGruppe = new Ovelsesgruppe(txtNavnGruppe.getText());
		DatabaseOperations.addOvelsesgruppe(conn, oGruppe.getNavn());
		txtNavnGruppe.clear();
		System.out.println("Gruppe laget");
		
	}
	
	public void sokIOvelsesGruppe() throws SQLException {
		txtOvelseGruppe.clear();
		String listeOvelser = "";
		// Her m� man legge inn ovelserIOvelsesgrupper, og sjekke feltet for ovelsesgruppe om det stemmer med det som er skrevet inn
		for (Ovelsesgruppe gruppe : DatabaseOperations.getOvelsesgrupper(conn)) {
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
	
	public void initTable() throws SQLException {
		int antall = Integer.parseInt(txtNSisteOkter.getText());
		tblcDato.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("dato"));
		tblcTidspunkt.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("startTidspunkt"));
		tblcNotat.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("notat"));
		tblcVarighet.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("varighet"));
		tblcForm.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("personligForm"));
		tblcPrestasjon.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("prestasjon"));
		
		txtTotAntallOkter.setText(String.valueOf(DatabaseOperations.getTotalTreningsOkter(conn)));
		tblvOkter.setItems(getOkter(antall));
	}
	
	private ObservableList<TreningsOkt> getOkter(int antall) throws SQLException {
		if (!treningsOkterObs.isEmpty()) {
			treningsOkterObs.clear();
		}
		for (int i = 0; i < antall; i++) {
			treningsOkterObs.add(DatabaseOperations.getNSisteTreningsOkter(conn, antall).get(i));
		}
		return treningsOkterObs;
	}
	
	// -----------------------------------------------------------
	// �velselogg
	// -----------------------------------------------------------
	
	public void sokOvelseLogg() throws ParseException, SQLException {
		if (!loggObs.isEmpty()) {
			loggObs.clear();
		}
		fraDato = format.parse(txtFra.getText());
		tilDato = format.parse(txtTil.getText());
		Date dato;
		
		tblcDatoLogg.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("dato"));
		tblcNavnLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, String>("ovelsesnavn"));
		tblcKiloLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, Integer>("antallKilo"));
		tblcSettLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, Integer>("antallSett"));
		tblcBeskrivelseLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, String>("beskrivelse"));
		
		for (TreningsOkt okt : DatabaseOperations.getTreningsOkter(conn)) {
			dato = okt.getDato();
			if ((dato.compareTo(fraDato) > 0 && dato.compareTo(tilDato) < 0) 
					|| dato.compareTo(fraDato) == 0 || dato.compareTo(tilDato) == 0) {
				for (Ovelse ovelse : okt.getOvelser()) {
					if (ovelse.getOvelsesnavn().equals(txtVelgOvelse.getText())) {
						loggObs.add(ovelse);
					}
				}
			}
		}
		tblvOvelseOversikt.setItems(loggObs);
	}
	
	
	// -----------------------------------------------------------
	// Main
	// -----------------------------------------------------------
	
	public static void main(String[] args) {
		// DatabaseOperations data = new DatabaseOperations();
        // data.connect();
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
