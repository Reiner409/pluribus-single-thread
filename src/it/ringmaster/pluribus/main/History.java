package it.ringmaster.pluribus.main;

public class History {
	
	private String h;
	
	/**
	 * Constructor class to keep tracking of the history
	 */
	public History()
	{
		this.h = new String("");
	}
	
	/**
	 * Gets the history
	 * @return a string containing the history
	 */
	public String getH()
	{
		return this.h;
	}
	
	/**
	 * Adds a piece of history to the current history
	 * @param action A string containing a new piece of history
	 */
	public void forward (String action)
	{
		this.h += action;
	}
	
	/**
	 * Deletes the last piece of history added
	 */
	public void backward()
	{
		this.h = this.h.substring(0,this.h.length()-1);
	}
}
