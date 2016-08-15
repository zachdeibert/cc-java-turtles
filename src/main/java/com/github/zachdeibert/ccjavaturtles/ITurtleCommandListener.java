package com.github.zachdeibert.ccjavaturtles;

/**
 * Interface for a listener to turtle command events
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public interface ITurtleCommandListener {
	/**
	 * Called after the command is sent to a turtle
	 * 
	 * @since 1.0
	 */
	void commandSent();

	/**
	 * Called after the command finishes running on a turtle
	 * 
	 * @param result
	 *            The result of the command
	 * @since 1.0
	 */
	void commandFinished(String result);
}
