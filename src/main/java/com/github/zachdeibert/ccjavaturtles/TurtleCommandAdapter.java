package com.github.zachdeibert.ccjavaturtles;

/**
 * An adapter for the {@link ITurtleCommandListener} interface
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public abstract class TurtleCommandAdapter implements ITurtleCommandListener {
	@Override
	public void commandSent() {
	}

	@Override
	public void commandFinished(String result) {
	}
}
