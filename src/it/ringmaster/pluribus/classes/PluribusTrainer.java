package it.ringmaster.pluribus.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import it.ringmaster.pluribus.action.Action;

public class PluribusTrainer {

	private Map<String, InformationSet> infoset_map;
	private int regret_min;

	//Uses the default regret_min = 300_000
	public PluribusTrainer() 
	{
		this(-300000);
	}

	public PluribusTrainer(int regret_min) 
	{
		this.infoset_map = new HashMap<String, InformationSet>();
		this.regret_min = regret_min;
	}

	public InformationSet cache_and_get_information_set(String card_and_history)
	{
		if (!this.infoset_map.containsKey(card_and_history))
			this.infoset_map.put(card_and_history, new InformationSet());
		return infoset_map.get(card_and_history);
	}

	public void update_strategy(List<String> cards, History history, int traverser)
	{
		if (KuhnPoker.is_terminal(history.getH()))
			return;

		int turn_player = history.getH().length() % 2;
		String turn_card = cards.get(turn_player);
		String info_key = turn_card + history.getH();
		InformationSet info_set = this.cache_and_get_information_set(info_key);

		double[] strategy = info_set.get_strategy();

		if(turn_player == traverser)
		{
			
			int[] numToGenerate = new int[]{0,1};
            EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(
            		numToGenerate, strategy);
            
            // Sample a number according to the distribuiton and pick the corresponding action
            int random_action = distribution.sample();
			String action = Action.Actions[random_action];
			info_set.getStrategy_sum()[random_action] += 1;
			history.forward(action);
			this.update_strategy(cards, history, traverser);
			history.backward();
		}
		else
		{
			for (String action : Action.Actions) 
			{
				history.forward(action);
				this.update_strategy(cards, history, traverser);
				history.backward();
			}
		}
	}

	public double traverse_mc_cfr(List<String> cards, History history, int traverser, boolean prune)
	{
		if (KuhnPoker.is_terminal(history.getH()))
			return KuhnPoker.get_payoffs(history.getH(), cards)[traverser];

		int turn_player = (history.getH().length()) % 2;
		String turn_card = cards.get(turn_player);
		String info_key = turn_card + history.getH();
		InformationSet info_set = this.cache_and_get_information_set(info_key);

		double[] strategy = info_set.get_strategy();

		if (turn_player == traverser)
		{
			List<String> explored = new LinkedList<String>();
			explored.addAll(Arrays.asList(Action.Actions));
			//v_h_a -> payoff received by a certain action
			double[] v_h_a = new double[Action.Actions.length];
			for(int i = 0;i<v_h_a.length;i++)
			{
				v_h_a[i] = 0;
			}
			double v_h = 0;

			for(int ix = 0;ix<Action.Actions.length;ix++)
			{
				String action = Action.Actions[ix];

				if (prune && info_set.getRegret_sum()[ix] <= this.regret_min)
				{
					explored.remove(action);
				}
				else
				{
					history.forward(action);
					v_h_a[ix] = this.traverse_mc_cfr(cards, history, traverser, prune);
					history.backward();
					v_h += strategy[ix] * v_h_a[ix];
				}
			}
			for (String action : explored) {
				int ix;
				for (int i=0; i<Action.Actions.length;i++) {
					if(Action.Actions[i].equals(action))
					{
						ix = i;
						info_set.getRegret_sum()[ix] += (v_h_a[ix] - v_h);
						break;
					}
				}
			}
			return v_h;
		}
		else
		{
			//Calculating a random int based on the strategy probability
			int[] numToGenerate = new int[]{0,1};
            EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(numToGenerate, strategy);

            // Sample a number according to the distribuiton and pick the corresponding action
            int random_action = distribution.sample();
			String action = Action.Actions[random_action];
			history.forward(action);
			double v_h = this.traverse_mc_cfr(cards, history, traverser, prune);
			history.backward();
			return v_h;
		}
	}

	public double[] train(int iterations, int[] PLAYERS, int strategy_interval, int prune_Threshold, int LCFR_Threshold, int discount_Interval, double discount)
	{
		double[] util = new double[] {0, 0};
		List<String> kuhn_cards= new ArrayList<String>();
		kuhn_cards.add("J");
		kuhn_cards.add("Q");
		kuhn_cards.add("K");

		for (int t=1; t<iterations; t++)
		{
			
			List<String> cards = new ArrayList<String>();
			cards.addAll(kuhn_cards);
			//Shuffling the list
			Collections.shuffle(cards);
			//Removing the first card from the list
			cards.remove(0);
			
			for (int p_i : PLAYERS) {
				if (t % strategy_interval == 0)
					this.update_strategy(cards, new History(), p_i);
				if (t > prune_Threshold)
				{
					Random rand = new Random();
					float chance = rand.nextFloat();
					if(chance < 0.05)
						util[p_i] += this.traverse_mc_cfr(cards, new History(), p_i, false);
					else
						util[p_i] += this.traverse_mc_cfr(cards, new History(), p_i, true);
				}
				else
					util[p_i] += this.traverse_mc_cfr(cards, new History(), p_i, false);
			}
			
			if (t < LCFR_Threshold && t % discount_Interval == 0)
			{
				double discounted = (t/discount) / (t/discount + 1);
				for(String i_set_key : infoset_map.keySet())
				{
					InformationSet i_set_val = infoset_map.get(i_set_key);
					for (int i_r=0; i_r<i_set_val.getRegret_sum().length;i_r++) {
						i_set_val.getRegret_sum()[i_r]*= discounted;
					}
					for (int i_s=0; i_s<i_set_val.getStrategy_sum().length;i_s++) {
						i_set_val.getStrategy_sum()[i_s] *= discounted;					}
				}
			}
		}
		return util;
	}

	public Map<String, InformationSet> getInfoset_map() {
		return infoset_map;
	}
	

}
