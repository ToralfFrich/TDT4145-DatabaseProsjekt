package prosjekt_del2;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.TreningsOkt;


public class Treningsdagbok {
	
	public Map<Integer, TreningsOkt> okter = new HashMap<Integer, TreningsOkt>();
	
	public Treningsdagbok(HashMap<Integer, TreningsOkt> okter) {
		this.okter = okter;
		
	}

}
