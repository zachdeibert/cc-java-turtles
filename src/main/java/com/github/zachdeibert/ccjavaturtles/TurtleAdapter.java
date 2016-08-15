package com.github.zachdeibert.ccjavaturtles;

/**
 * An adapter for the {@link ITurtleListener} interface
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public abstract class TurtleAdapter implements ITurtleListener {
	@Override
	public void turtleConnected(Turtle turtle) {
	}

	@Override
	public void turtleCrashed(Turtle turtle) {
	}

	@Override
	public void turtleDisconnected(Turtle turtle) {
	}
}
