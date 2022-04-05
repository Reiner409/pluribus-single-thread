package it.ringmaster.pluribus.classes;

import it.ringmaster.pluribus.global.Action;

public class InformationSet {
	
	private int[] regret_sum;
	private int[] strategy_sum;
	private int num_actions;
	
	public InformationSet()
	{
		this.regret_sum = new int[Action.Actions.length];
		this.strategy_sum = new int[Action.Actions.length];
		this.num_actions = Action.Actions.length;
		
		for(int i = 0;i<Action.Actions.length;i++)
		{
			this.regret_sum[i] = 0;
			this.strategy_sum[i] = 0;
		}
	}
	/**
	 * Normalizes the actual strategy
	 * @param strategy
	 * @return The normalized strategy
	 */
	public int[] normalize(int[] strategy)
	{
		//Normalization
		int sum_strategy = 0;
		for (int value : strategy) {
			sum_strategy += value;
		}
		
		//Maybe instead of making this for cycle i could've checked the first element
		// in the array? -> strategy[0] > 0. If i recall correctly every element in the array
		//should be (el >= 0).
		if(sum_strategy > 0)
		{
			for (int i=0;i<this.num_actions;i++) 
			{
				strategy[i] /= sum_strategy;
			}
		}
		else
		{
			// num_actions dimension array (e.g.: [0.33, 0.33, 0.33])
			for(int i=0;i<this.num_actions;i++)
			{
				strategy[i] = 1/this.num_actions;
			}
		}
		return strategy;
	}
	/**
	 * Checks the regret sum and compares it with 0, choosing the highest among those 2.
	 * This method returns an array full of numbers -> 0<=x<=1, which the sum must be equal to 1.
	 * @return
	 */
	public int[] get_strategy()
	{
		int[] strategy = new int[this.num_actions];
		for(int i=0;i<this.num_actions;i++) {
			strategy[i] = (this.regret_sum[i] > 0) ? regret_sum[i] : 0;
		}
		strategy = normalize(strategy);
		return strategy;
	}
	
	/**
	 * Gets a clone of the normalized array for the strategy.
	 * @return The strategy array normalized.
	 */
	public int[] get_average_strategy()
	{
		//Check the clone method since it could lead to errors while modifying
		//the actual array.
		return this.normalize(strategy_sum.clone());
	}
	
	public int[] getRegret_sum() {
		return regret_sum;
	}

	public int[] getStrategy_sum() {
		return strategy_sum;
	}

	public int getNum_actions() {
		return num_actions;
	}
	
}
