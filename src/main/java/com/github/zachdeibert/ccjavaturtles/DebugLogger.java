package com.github.zachdeibert.ccjavaturtles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logs information about turtle connections to log4j2
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
public class DebugLogger implements ITurtleListener {
	private static final Logger log = LogManager.getLogger();
	private static final DebugLogger instance = new DebugLogger();

	/**
	 * Registers the debug logger to a server
	 * 
	 * @param server
	 *            The server to register the listener on
	 * @since 1.0
	 */
	public static void register(TurtleServer server) {
		server.addListener(instance);
	}

	/**
	 * Unregisters the debug logger from a server
	 * 
	 * @param server
	 *            The server to unregister from
	 * @since 1.0
	 */
	public static void unregister(TurtleServer server) {
		server.removeListener(instance);
	}

	@Override
	public void turtleConnected(Turtle turtle) {
		log.info("Turtle {} connected with version '{}'", turtle.getId(), turtle.getVersion());
	}

	@Override
	public void turtleCrashed(Turtle turtle) {
		log.info("Turtle {} crashed", turtle.getId());
	}

	@Override
	public void turtleDisconnected(Turtle turtle) {
		log.info("Turtle {} disconnected", turtle.getId());
	}

	private DebugLogger() {
	}
}
