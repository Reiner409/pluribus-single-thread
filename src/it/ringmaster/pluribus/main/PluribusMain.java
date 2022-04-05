package it.ringmaster.pluribus.main;

import it.ringmaster.pluribus.classes.PluribusTrainer;
import it.ringmaster.pluribus.global.Action;

public class PluribusMain {

	public static void main(String[] args) {
		
		//Initializes the Global Variables
		new Action();
		
		//number of players = 2
		int[] Players = new int[2];
		
		int iterations = 500;
		int strategy_Interval = 10;
		int prune_Threshold = 5000;
		int LCFR_Threshold = 5000;
		int discount_Interval = 10;
		int discount = 10;
		int regret_min = -300000;
		
		PluribusTrainer mcCFR_trainer = new PluribusTrainer(regret_min);
		int[] utility = mcCFR_trainer.train(iterations, Players, strategy_Interval, prune_Threshold, LCFR_Threshold, discount_Interval, discount);
		
		System.out.println("Players utility -> " + utility);
		
		System.out.println("Regret Bet Pass");
		for(String key : mcCFR_trainer.getInfoset_map().keySet())
		{
			System.out.println(key+":    "+mcCFR_trainer.getInfoset_map().get(key).getRegret_sum());
		}
		
		System.out.println("Strategy Bet Pass");
		for(String key : mcCFR_trainer.getInfoset_map().keySet())
		{
			System.out.println(key+":    "+mcCFR_trainer.getInfoset_map().get(key).getStrategy_sum());
		}
		System.out.println("AVG Strategy Bet Pass");
		for(String key : mcCFR_trainer.getInfoset_map().keySet())
		{
			System.out.println(key+":    "+mcCFR_trainer.getInfoset_map().get(key).get_average_strategy());
		}
			
			
			
	}
}
