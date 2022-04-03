package it.ringmaster.pluribus.classes;

import it.ringmaster.pluribus.main.History;

public class PluribusMain {

	public static void main(String[] args) {
		History c = new History();
		c.forward("CB");
		System.out.println(c.getH());
		c.backward();
		System.out.println(c.getH());
	}
}
