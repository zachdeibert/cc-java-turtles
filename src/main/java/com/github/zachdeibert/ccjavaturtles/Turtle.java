package com.github.zachdeibert.ccjavaturtles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the connection to a turtle in the world
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class Turtle {
	private String id;
	private String version;
	private volatile List<TurtleCommand> commands;

	/**
	 * Gets the id of the turtle
	 * 
	 * @return The id of the turtle
	 * @since 1.0
	 */
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the version of the turtle
	 * 
	 * @return The version of the turtle
	 * @since 1.0
	 */
	public String getVersion() {
		return version;
	}

	void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sends a command to the turtle
	 * 
	 * @param cmd
	 *            The command
	 * @since 1.0
	 */
	public void sendCommand(TurtleCommand cmd) {
		commands.add(cmd);
	}

	/**
	 * Gets an array of all of the commands being sent to the turtle
	 * 
	 * @return An array of all of the commands being sent to the turtle
	 * @since 1.0
	 */
	public TurtleCommand[] getPendingCommands() {
		return commands.toArray(new TurtleCommand[0]);
	}

	/**
	 * Attempts to remove a pending command from the turtle's send queue
	 * 
	 * @param cmd
	 *            The command to attempt to cancel
	 * @return <code>true</code> if the command was canceled, <code>false</code>
	 *         otherwise
	 * @since 1.0
	 */
	public boolean cancelCommand(TurtleCommand cmd) {
		return commands.remove(cmd);
	}

	private void initCommandList() {
		commands = Collections.synchronizedList(new ArrayList<TurtleCommand>());
	}

	List<TurtleCommand> onCommandSend() {
		List<TurtleCommand> commands = this.commands;
		initCommandList();
		return commands;
	}

	Turtle() {
		initCommandList();
	}
}
