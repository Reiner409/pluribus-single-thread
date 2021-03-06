package it.ringmaster.pluribus.main;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import it.ringmaster.pluribus.action.Action;
import it.ringmaster.pluribus.classes.PluribusTrainer;

public class PluribusMain {

	public static void main(String[] args) {

		//Initializes the Global Variables
		new Action();

		//number of players = 2
		int[] Players = new int[] {0,1};

		int iterations = 500_000;
		int strategy_Interval = 10;
		int prune_Threshold = 5_000;
		int LCFR_Threshold = 5_000;
		int discount_Interval = 10;
		double discount = 10;
		int regret_min = -300_000;

		PluribusTrainer mcCFR_trainer = new PluribusTrainer(regret_min);
		double[] utility = mcCFR_trainer.train(iterations, Players, strategy_Interval, prune_Threshold, LCFR_Threshold, discount_Interval, discount);

		
		System.out.println("Players utility -> " + printArrayDouble(utility));

		
		List<String> sorted_keys = new LinkedList<>(); 
		sorted_keys.addAll(mcCFR_trainer.getInfoset_map().keySet());
		Collections.sort(sorted_keys,new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.length() - o2.length();
			}
		});
		
		System.out.println("Regret Bet Pass");
		for(String key : sorted_keys)
		{
			System.out.println(key+":    "+ printArrayDouble(mcCFR_trainer.getInfoset_map().get(key).getRegret_sum()));
		}

		System.out.println("Strategy Bet Pass");
		for(String key : sorted_keys)
		{
			System.out.println(key+":    "+printArrayDouble(mcCFR_trainer.getInfoset_map().get(key).getStrategy_sum()));
		}
		System.out.println("AVG Strategy Bet Pass");
		for(String key : sorted_keys)
		{
			System.out.println(key+":    "+printArrayDouble(mcCFR_trainer.getInfoset_map().get(key).get_average_strategy()));
		}		
	}

	private static String printArrayDouble(double[] array)
	{
		StringBuilder s = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.##");
		for (double d : array) {
			s.append(df.format(d) + " ");
		}
		return s.toString();
	}
	
}
