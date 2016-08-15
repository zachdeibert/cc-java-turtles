package com.github.zachdeibert.ccjavaturtles;

import java.util.HashMap;
import java.util.Map;

class InstanceManager {
	private static final Map<Integer, TurtleServer> servers = new HashMap<Integer, TurtleServer>();

	static TurtleServer serverRunningOnPort(int port) {
		return servers.get(port);
	}

	static void registerServer(TurtleServer server) {
		servers.put(server.getPort(), server);
	}
}
