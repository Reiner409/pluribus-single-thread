package it.ringmaster.pluribus.main;

import it.ringmaster.pluribus.classes.History;

public class PluribusMain {

	public static void main(String[] args) {
		History c = new History();
		c.forward("CB");
		System.out.println(c.getH());
		c.backward();
		System.out.println(c.getH());
	}
}
