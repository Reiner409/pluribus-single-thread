package it.ringmaster.pluribus.main;

import java.util.Arrays;
import java.util.List;

public class KuhnPoker {

	private static List<String> terminalValues = Arrays.asList("BC","BB","CC","CBB","CBC");

	/**
	 * Verifies if the actual node is a terminal node
	 * @param history
	 * @return true if terminal. False otherwise
	 */
	public static boolean is_terminal(String history) {
		return terminalValues.contains(history);
	}

	/**
	 * Gettings payoffs
	 * @param history
	 * @param cards
	 * @return The players payoffs into an array of int.
	 */
	public static int[] get_payoffs(String history, List<String> cards) 
	{
		int[] payoffs = new int[2];
		
		if (history.equals("BC")) 
		{
			payoffs[0] = 1;
			payoffs[1] = -1;
		}
		else if (history.equals("CBC"))
		{
			payoffs[0] = 1;
			payoffs[1] = -1;
		}
		else //CC or BB or CBB
		{
			int payoff = 2 - (history.contains("B") ? 1 : 0);
			String p1_card = cards.get(0);
			String p2_card = cards.get(1);
			if (p1_card.equals("K") || p2_card.equals("J"))
			{
				payoffs[0] = payoff;
				payoffs[1] = -payoff;
			}
			else
			{
				payoffs[0] = -payoff;
				payoffs[1] = payoff;
			}
		}
		
		return payoffs;
	}

}

