package com.github.zachdeibert.ccjavaturtles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fi.iki.elonen.NanoHTTPD;

/**
 * The server that all of the turtles connect to
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class TurtleServer {
	private static final Logger log = LogManager.getLogger();
	private final Map<String, Turtle> turtles;
	private final Map<Integer, TurtleCommand> commands;
	private final List<ITurtleListener> listeners;
	private int port;
	private boolean running;
	private WebServer server;

	/**
	 * Adds a listener to this command
	 * 
	 * @param listener
	 *            The listener
	 * @since 1.0
	 */
	public void addListener(ITurtleListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener from this command
	 * 
	 * @param listener
	 *            The listener
	 * @since 1.0
	 */
	public void removeListener(ITurtleListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Gets an array of all of the listeners to this command
	 * 
	 * @return The listeners to this command
	 * @since 1.0
	 */
	public ITurtleListener[] getListeners() {
		return listeners.toArray(new ITurtleListener[0]);
	}

	/**
	 * Gets an array of all of the turtles
	 * 
	 * @return The turtles
	 * @since 1.0
	 */
	public Turtle[] getTurtles() {
		return turtles.values().toArray(new Turtle[0]);
	}

	void registerTurtle(String turtleId, String version) {
		Turtle turtle = new Turtle();
		turtle.setId(turtleId);
		turtle.setVersion(version);
		turtles.put(turtleId, turtle);
		for ( ITurtleListener listener : getListeners() ) {
			listener.turtleConnected(turtle);
		}
	}

	TurtleCommand[] pullCommands(String turtleId) {
		Turtle turtle = turtles.get(turtleId);
		List<TurtleCommand> pending = turtle.onCommandSend();
		for ( TurtleCommand cmd : pending ) {
			commands.put(cmd.getId(), cmd);
		}
		return pending.toArray(new TurtleCommand[0]);
	}

	void postResult(String turtleId, String commandId, String result) {
		TurtleCommand cmd = commands.get(commandId);
		cmd.setResult(result);
	}

	/**
	 * Gets the port number the server is running on
	 * 
	 * @return The port number
	 * @since 1.0
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port number the server is running on
	 * 
	 * @param port
	 *            The new port number to run on
	 * @since 1.0
	 * @throws IllegalStateException
	 *             If the server is already running
	 * @throws IllegalArgumentException
	 *             If the port is not a valid port number
	 */
	public void setPort(int port) throws IllegalStateException, IllegalArgumentException {
		if ( running ) {
			throw new IllegalStateException("Cannot change ports while the server is running");
		}
		if ( port <= 0 || port > 65535 ) {
			throw new IllegalArgumentException("Port is not a valid port number");
		}
		this.port = port;
	}

	/**
	 * Determines if the server is currently running
	 * 
	 * @return <code>true</code> if it is running, <code>false</code> otherwise
	 * @since 1.0
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Runs the server for the turtles to connect to
	 * 
	 * @since 1.0
	 * @throws IllegalStateException
	 *             If the server is already running
	 * @throws IOException
	 *             If the server cannot be created
	 */
	public void run() throws IllegalStateException, IOException {
		if ( isRunning() ) {
			throw new IllegalStateException("Server is already running");
		}
		log.info("Starting server on http://localhost:{}/", getPort());
		running = true;
		server = new WebServer(getPort(), this);
		server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	/**
	 * Default constructor
	 * 
	 * @since 1.0
	 */
	public TurtleServer() {
		turtles = new HashMap<String, Turtle>();
		commands = new HashMap<Integer, TurtleCommand>();
		listeners = new ArrayList<ITurtleListener>();
		DebugLogger.register(this);
	}
}
