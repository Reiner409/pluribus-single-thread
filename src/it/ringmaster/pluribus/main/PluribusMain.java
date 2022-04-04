package it.ringmaster.pluribus.main;

import it.ringmaster.pluribus.classes.History;
import it.ringmaster.pluribus.global.Global;

public class PluribusMain {

	public static void main(String[] args) {
		
		//Initializes the Global Variables
		new Global();
		
		History c = new History();
		c.forward("CB");
		System.out.println(c.getH());
		c.backward();
		System.out.println(c.getH());
	}
}
