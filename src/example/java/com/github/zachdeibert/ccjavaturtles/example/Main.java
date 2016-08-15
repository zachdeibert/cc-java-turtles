package com.github.zachdeibert.ccjavaturtles.example;

import com.github.zachdeibert.ccjavaturtles.Turtle;
import com.github.zachdeibert.ccjavaturtles.TurtleAdapter;
import com.github.zachdeibert.ccjavaturtles.TurtleServer;

public class Main {
	public static void main(String[] args) throws Throwable {
		TurtleServer server = new TurtleServer();
		server.setPort(8080);
		server.addListener(new TurtleAdapter() {
			@Override
			public void turtleConnected(Turtle turtle) {
				turtle.getLogger().info("Hello, world!");
			}
		});
		server.run();
	}
}
