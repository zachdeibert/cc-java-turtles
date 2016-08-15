package com.github.zachdeibert.ccjavaturtles;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that can be sent to a turtle
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class TurtleCommand {
	private static int lastId = 0;
	private final List<ITurtleCommandListener> listeners;
	private boolean sent;
	private String api;
	private String method;
	private Object[] arguments;
	private String result;
	private int id;

	/**
	 * Adds a listener to this command
	 * 
	 * @param listener
	 *            The listener
	 * @since 1.0
	 */
	public void addListener(ITurtleCommandListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener from this command
	 * 
	 * @param listener
	 *            The listener
	 * @since 1.0
	 */
	public void removeListener(ITurtleCommandListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Gets an array of all of the listeners to this command
	 * 
	 * @return The listeners to this command
	 * @since 1.0
	 */
	public ITurtleCommandListener[] getListeners() {
		return listeners.toArray(new ITurtleCommandListener[0]);
	}

	/**
	 * Determines whether or not the command has been sent
	 * 
	 * @return <code>true</code> if the command has been sent,
	 *         <code>false</code> otherwise
	 * @since 1.0
	 */
	public boolean isSent() {
		return sent;
	}

	void onSend() {
		for ( ITurtleCommandListener listener : getListeners() ) {
			listener.commandSent();
		}
		sent = true;
	}

	/**
	 * Gets the name of the API to use
	 * 
	 * @return The name of the API to use
	 * @since 1.0
	 */
	public String getApi() {
		return api;
	}

	/**
	 * Sets the name of the API to use
	 * 
	 * @param api
	 *            The new name of the API to use
	 * @since 1.0
	 * @throws IllegalStateException
	 *             If the command has already been sent to a turtle
	 */
	public void setApi(String api) throws IllegalStateException {
		if ( isSent() ) {
			throw new IllegalStateException("Unable to modify command after it has been sent");
		}
		this.api = api;
	}

	/**
	 * Gets the name of the method to call on the API
	 * 
	 * @return The name of the method to call on the API
	 * @since 1.0
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the name of the method to call on the API
	 * 
	 * @param method
	 *            The new name of the method to call on the API
	 * @since 1.0
	 * @throws IllegalStateException
	 *             If the command has already been sent to a turtle
	 */
	public void setMethod(String method) throws IllegalStateException {
		if ( isSent() ) {
			throw new IllegalStateException("Unable to modify command after it has been sent");
		}
		this.method = method;
	}

	/**
	 * Gets the arguments to the method
	 * 
	 * @return The arguments to the method
	 * @since 1.0
	 */
	public Object[] getArguments() {
		Object[] args = new Object[arguments.length];
		System.arraycopy(arguments, 0, args, 0, arguments.length);
		return args;
	}

	/**
	 * Sets the arguments to the method
	 * 
	 * @param arguments
	 *            The new arguments to the method
	 * @throws IllegalStateException
	 *             If the command has already been sent to a turtle
	 */
	public void setArguments(Object[] arguments) throws IllegalStateException {
		if ( isSent() ) {
			throw new IllegalStateException("Unable to modify command after it has been sent");
		}
		this.arguments = new Object[arguments.length];
		System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
	}

	/**
	 * Determines whether or not the command has received a result yet
	 * 
	 * @return <code>true</code> if it has received a result, <code>false</code>
	 *         otherwise
	 * @since 1.0
	 */
	public boolean hasResult() {
		return result != null;
	}

	/**
	 * Gets the result of the command
	 * 
	 * @return The result of the command
	 * @since 1.0
	 * @throws IllegalStateException
	 *             If the command has not been run yet
	 */
	public String getResult() throws IllegalStateException {
		if ( !hasResult() ) {
			if ( isSent() ) {
				throw new IllegalStateException("The command has not been sent yet");
			} else {
				throw new IllegalStateException("The command is currently running");
			}
		}
		return result;
	}

	void setResult(String result) throws IllegalStateException {
		if ( !isSent() ) {
			throw new IllegalStateException("The command has not been sent yet");
		}
		this.result = result;
		for ( ITurtleCommandListener listener : getListeners() ) {
			listener.commandFinished(result);
		}
	}

	/**
	 * Gets the id of the command
	 * 
	 * @return The id of the command
	 * @since 1.0
	 */
	public int getId() {
		return id;
	}

	/**
	 * Nullary constructor
	 * 
	 * @since 1.0
	 */
	public TurtleCommand() {
		this(null, null);
	}

	/**
	 * Default constructor
	 * 
	 * @param api
	 *            The name of the API to use
	 * @param method
	 *            The name of the method to call on the API
	 * @param arguments
	 *            The arguments to the method
	 * @since 1.0
	 */
	public TurtleCommand(String api, String method, Object... arguments) {
		listeners = new ArrayList<ITurtleCommandListener>();
		sent = false;
		this.api = api;
		this.method = method;
		this.arguments = arguments;
		result = null;
		id = ++lastId;
	}
}
