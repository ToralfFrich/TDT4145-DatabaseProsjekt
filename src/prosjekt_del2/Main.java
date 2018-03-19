package Databaser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	//public static Treningsdagbok treningsdagbok;
	
	public static void main(String[] args) {
		
		Main m = new Main();
		
		boolean exit = false;
		while (!exit) {
			m.startmeny();
			Scanner scanner = new Scanner(System.in);
			String s = scanner.next();
			if (m.checkInt(s)){
				int i = Integer.parseInt(s);
				switch(i) {
				case 1: m.addWorkout(); break;
				case 2: m.getInformation(); break;
				case 3: m.seeResults(); break;
				case 4: m.makeWorkoutGroup(); break;
				case 5: m.seeWorkouts(); break;
				case 6: exit = true;	break;
				}
			} 
		} 
		System.out.println("Du har avsluttet programmet.");
	}
	
	public void addWorkout() { //case 1 notat
		Scanner in = new Scanner(System.in);
		
		Date dato = null;
		while (dato == null) {
			System.out.println("Legg til dato på formen yyyy:mm:dd ");
			dato = createDate(in.nextLine());
		}
		String tid = null;
		while (tid == null) {
			System.out.println("Legg til tid på formen hh:mm ");
			tid = createTime(in.nextLine());
		}
		int varighet = 0;
		while (varighet == 0) {
			System.out.println("Legg til varighet i minutter ");
			varighet = createDuration(in.nextLine());
		}
		int personligForm = 0;
		while (personligForm == 0) {
			System.out.println("Legg til personlig form fra 1 til 10 ");
			personligForm = createInt(in.nextLine(),10);
		}
		int prestasjon = 0;
		while (prestasjon == 0) {
			System.out.println("Legg til prestasjon fra 1 til 10 ");
			prestasjon = createInt(in.nextLine(),10);
		}
		System.out.println("Legg til et notat fra treningsøkten ");
		String notat = in.nextLine();
		
		// opprett en treningsøkt
	}
	
	public void getInformation() { // case 2
		Scanner in = new Scanner(System.in);
		
		int antall = 0;
		while (antall == 0) {
			System.out.println("Hvor mange treningsøkter vil du se?");
			antall = createInt(in.nextLine(), 0);
		}
		//hent informasjon fra treningsdagbok
	}
	
	public void seeResults() { // case 3
		Scanner in = new Scanner(System.in);
		
		int antall = 0;
		while (antall == 0) {
			System.out.println("Hvor mange resultater vil du se?");
			antall = createInt(in.nextLine(), 0);
		}
		//hent informasjon fra treningsdagbok
	}
	
	public void makeWorkoutGroup() { // case 4
		Scanner in = new Scanner(System.in);
		System.out.println("Hva skal øvelsesgruppen hete?");
		String navn = in.nextLine();
		System.out.println("Her kommer en liste over øvelser: ");
		List<String> liste = Arrays.asList("øvelse2", "øvelse2", "øvelse3");
		printListe(liste);
		boolean exit = false;
		while (!exit) {
			int øvelse = 0;
			while (øvelse == 0) {
				System.out.println("Hvilken øvelse vil du legge til? (skriv exit for å slutte å legge til øvelser) ");
				String s = in.nextLine();
				if (s.equals("exit")) {
					exit = true;
					break;
				}
				øvelse = createInt(s, liste.size());
			}
			//legg til øvelsen i øvelsesgruppa
		}
		System.out.println("Øvelsesgruppen er opprettet.");
	}
	
	public void seeWorkouts() { // case 5
		Scanner in = new Scanner(System.in);
		List<String> liste = Arrays.asList("Øvelsesgruppe 1", "Øvelsesgruppe 2", "Øvelsesgruppe 3");
		System.out.println("Her kommer en liste over øvelsesgrupper: ");
		printListe(liste);
		int øvelsesgruppe = 0;
		while (øvelsesgruppe == 0) {
			System.out.println("Hvilken øvelsesgruppe vil du vise? ");
			String s = in.nextLine();
			øvelsesgruppe = createInt(s, liste.size());
		}
		List<String> øvelser = Arrays.asList("øvelse 1", "øvelse 2");
		printListe(øvelser);
	}
	
	private void printListe(List<String> liste) { // må endres
		String s = "";
		for (int i=0 ; i < liste.size(); i++) {
			s += i+1 + ": ";
			s += liste.get(i);
			s += "\n";
		}
		System.out.println(s);
	}
	
	private int createInt(String s, int limit) { //sjekker at strengen er et heltall fra 1 til 10, og gjør den om til en int
		try {
			int i = Integer.parseInt(s);
			if ((limit == 0 && i > 0) || (i > 0 && i < limit+1)) {
				return i;
			} throw new Exception();
		} catch (Exception e) {
			System.out.println("Du må oppgi et gyldig heltall");
			return 0;
		}
	}
	
	private int createDuration(String s) { //sjekker at strengen er på riktig format, og gjør den om til en int
		try {
			int duration = Integer.parseInt(s);
			if (duration <= 0) {
				throw new Exception();
			} return duration;
		} catch (Exception e) {
			System.out.println("Du må oppgi varighet i minutter");
			return 0;
		}
	}
	
	private String createTime(String s) { //sjekker at strengen er på gyldig format
		try {
			String[] timeList = s.split(":");
			Integer.parseInt(timeList[0]);
			Integer.parseInt(timeList[1]);
			if (timeList.length != 2) {
				throw new Exception();
			} return s;
		} catch (Exception e) {
			System.out.println("Du må oppgi tid på formen hh:mm");
			return null;
		}
	}
	
	private Date createDate(String s) { //sjekker at strengen er på riktig format, og gjør den om til en Date
		try {
			String[] dateList = s.split(":");
			int year = Integer.parseInt(dateList[0]);
			int month = Integer.parseInt(dateList[1]);
			int day = Integer.parseInt(dateList[2]);
			Date date = new Date(year, month, day);
			return date;
		} catch (Exception e) {
			System.out.println("Du må oppgi dato på formen yyyy:mm:dd");
			return null;
		}
	}
	
	private boolean checkInt(String s) {
		try {
			int i = Integer.parseInt(s);
			if (i < 1 || i > 6) {
				throw new Exception();
			}
			return true;
		} catch (Exception e) {
			System.out.println("Du må skrive inn et heltall fra 1-6");
			return false;
		}
	}
	
	public void startmeny() {
		System.out.println("\nHer fÂr du seks valg:\n"
				+ "1. Legg til trening\n"
				+ "2. Få informasjon om dine siste treningsøkter\n"
				+ "3. Se dine resultater for en øvelse\n"
				+ "4. Lag en ny øvelsesgruppe\n"
				+ "5. Se øvelser i en øvelsesgruppe\n"
				+ "6. Avslutt program\n\n"
				
				+ "Velg 1, 2, 3, 4, 5 eller 6");
	}

}
