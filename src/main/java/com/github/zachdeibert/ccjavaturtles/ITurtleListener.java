package com.github.zachdeibert.ccjavaturtles;

/**
 * Interface for a listener to turtle events
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public interface ITurtleListener {
	/**
	 * Called when a new turtle connects to the server
	 * 
	 * @param turtle
	 *            The turtle
	 * @since 1.0
	 */
	void turtleConnected(Turtle turtle);

	/**
	 * Called when a turtle crashes
	 * 
	 * @param turtle
	 *            The turtle
	 * @since 1.0
	 */
	void turtleCrashed(Turtle turtle);

	/**
	 * Called when a turtle gets disconnected from the server
	 * 
	 * @param turtle
	 *            The turtle
	 * @since 1.0
	 */
	void turtleDisconnected(Turtle turtle);
}
