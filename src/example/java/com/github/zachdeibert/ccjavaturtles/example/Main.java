package com.github.zachdeibert.ccjavaturtles.example;

import com.github.zachdeibert.ccjavaturtles.TurtleServer;

public class Main {
	public static void main(String[] args) throws Throwable {
		TurtleServer server = new TurtleServer();
		server.setPort(8080);
		server.run();
	}
}
