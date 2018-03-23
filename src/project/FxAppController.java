package project;

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
			txtOvelsesGruppeFri, txtSokGruppe;
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
		DatabaseOperations.addTreningsØkt(DBConnection.createDBConnection(), stringToDate(txtDato.getText()), 
				stringToTime(txtTidspunkt.getText()), Integer.parseInt(txtVarighet.getText()), 
				Integer.parseInt(txtPersonligForm.getText()), Integer.parseInt(txtPrestasjon.getText()), txtNotat.getText());
		System.out.println("Økt laget");
	}
	
	public void leggTilOvelserIOkt() {
		for (Ovelse ovelse : ovelser) {
			if (txtOvelseNavn.getText().equals(ovelse.getOvelsesnavn())) {
				if (ovelse instanceof ApparatOvelse) {
					System.out.println("Den skj�nte at det var et Apparat, digg");
					ApparatOvelse ovelseIOkt = new ApparatOvelse(ovelse.getOvelsesnavn());
					ovelseIOkt.setAntallKilo(Integer.parseInt(txtKilo.getText()));
					ovelseIOkt.setAntallSett(Integer.parseInt(txtSett.getText()));
					for (Apparat apparat : apparater) {
						if (apparat.getNavn().equals(txtApparatValg.getText())) {
							ovelseIOkt.setApparat(apparat);
						}
					}
					treningsOkter.get(treningsOkter.size() - 1).getOvelser().add(ovelseIOkt);
				}
				else {
					FriOvelse ovelseIOkt = new FriOvelse(ovelse.getOvelsesnavn(), ((FriOvelse) ovelse).getBeskrivelse());
					treningsOkter.get(treningsOkter.size() - 1).getOvelser().add(ovelseIOkt);
				}
			}
		}
		
		listeMedOvelser += treningsOkter.get(treningsOkter.size() - 1).getOvelser().get(treningsOkt.getOvelser().size() - 1).getOvelsesnavn() + " \n";
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
	
	
	public void addApparatOvelse() {
		ApparatOvelse apparatOvelse = new ApparatOvelse(txtApparatOvelse.getText());
		for (Apparat apparat : apparater) {
			if (apparat.getNavn().equals(txtApparatTilOvelse.getText())) {
				apparatOvelse.setApparat(apparat);
			}
		}
		for (Ovelsesgruppe gruppe : ovelsesGrupper) {
			if (gruppe.getNavn().equals(txtOvelsesGruppeApparat.getText())) {
				gruppe.addToList(apparatOvelse);
			}
		}
		ovelser.add(apparatOvelse);
		txtApparatOvelse.clear();
		txtOvelsesGruppeApparat.clear();
		txtApparatTilOvelse.clear();
		System.out.println("Apparatovelse lagt til");
	}
	
	public void addFriOvelse() {
		FriOvelse friOvelse = new FriOvelse(txtFriOvelse.getText(), txtFriBeskrivelse.getText());
		for (Ovelsesgruppe gruppe : ovelsesGrupper) {
			if (gruppe.getNavn().equals(txtFriOvelse.getText())) {
				gruppe.addToList(friOvelse);
			}
		}
		for (Ovelsesgruppe gruppe : ovelsesGrupper) {
			if (gruppe.getNavn().equals(txtOvelsesGruppeFri.getText())) {
				gruppe.addToList(friOvelse);
			}
		}
		ovelser.add(friOvelse);
		txtOvelsesGruppeFri.clear();
		txtFriOvelse.clear();
		txtFriBeskrivelse.clear();
		System.out.println("Friovelse lagt til");
	}
	
	// ----------------------------------------------------------
	// Legg til Apparat
	// ----------------------------------------------------------
	
	public void addApparat() {
		Apparat apparat = new Apparat(txtApparat.getText(), txtBeskrivelse.getText());
		apparater.add(apparat);
		System.out.println(apparat.getNavn());
		txtApparat.clear();
		txtBeskrivelse.clear();
	}
	
	// ----------------------------------------------------------
	// �velsesgruppe
	// ----------------------------------------------------------
	
	// N�r fanen velges kj�res denne, legger inn i choicebox
	
	public void opprettOvelsesGruppe() {
		Ovelsesgruppe oGruppe = new Ovelsesgruppe(txtNavnGruppe.getText());
		ovelsesGrupper.add(oGruppe);
		txtNavnGruppe.clear();
		System.out.println("Gruppe laget");
		
	}
	
	public void sokIGruppe() {
		txtOvelseGruppe.clear();
		String listeOvelser = "";
		for (Ovelsesgruppe gruppe : ovelsesGrupper) {
			if (txtSokGruppe.getText().equals(gruppe.getNavn())) {
				for (Ovelse ovelse : gruppe.getOvelser()) {
					listeOvelser += (ovelse.getOvelsesnavn() + " \n");
				}
			}
			
		}
		txtOvelseGruppe.setText(listeOvelser);
		
	}
	
	// -----------------------------------------------------------
	// Oversikt okter
	// -----------------------------------------------------------
	
	public void initTable() {
		int antall = Integer.parseInt(txtNSisteOkter.getText());
		tblcDato.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("dato"));
		tblcTidspunkt.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("startTidspunkt"));
		tblcNotat.setCellValueFactory(new PropertyValueFactory<TreningsOkt, String>("notat"));
		tblcVarighet.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("varighet"));
		tblcForm.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("personligForm"));
		tblcPrestasjon.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Integer>("prestasjon"));
		
		tblvOkter.setItems(getOkter(antall));
	}
	
	private ObservableList<TreningsOkt> getOkter(int antall) {
		if (!treningsOkterObs.isEmpty()) {
			treningsOkterObs.clear();
		}
		for (int i = 0; i < antall; i++) {
			treningsOkterObs.add(treningsOkter.get(i));
		}
		return treningsOkterObs;
	}
	
	// -----------------------------------------------------------
	// �velselogg
	// -----------------------------------------------------------
	
	public void sokOvelseLogg() throws ParseException {
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
		
		for (TreningsOkt okt : treningsOkter) {
			dato = format.parse(okt.getDato());
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
