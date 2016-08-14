package com.github.zachdeibert.ccjavaturtles;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * The server that all of the turtles connect to
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class TurtleServer {
	private int port;
	private boolean running;
	private WebServer server;

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
		running = true;
		server = new WebServer(getPort());
		server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}
}
