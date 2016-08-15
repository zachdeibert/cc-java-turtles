package com.github.zachdeibert.ccjavaturtles;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

class WebServer extends NanoHTTPD {
	private static final Logger log = LogManager.getLogger();
	private TurtleServer server;

	@Override
	public Response serve(IHTTPSession session) {
		log.debug("{} {}", session.getMethod(), session.getUri());
		try {
			if ( session.getUri().equals("/") ) {
				try ( InputStream in = WebServer.class.getResourceAsStream("help.html") ) {
					return newFixedLengthResponse(Status.OK, "text/html", IOUtils.toString(in, Charset.defaultCharset())
							.replace("{{ port }}", String.valueOf(getListeningPort())));
				}
			} else if ( session.getUri().equals("/lua") ) {
				try ( InputStream in = WebServer.class.getResourceAsStream("turtlescript.lua") ) {
					return newFixedLengthResponse(Status.OK, "text/x-lua",
							IOUtils.toString(in, Charset.defaultCharset()));
				}
			} else if ( session.getUri().equals("/register") ) {
				try ( InputStream in = session.getInputStream() ) {
					try ( Scanner scan = new Scanner(in) ) {
						String id = scan.nextLine();
						String version = scan.nextLine();
						server.registerTurtle(id, version);
						return newFixedLengthResponse(Status.OK, "text/plain", "success");
					}
				}
			} else if ( session.getUri().equals("/pull") ) {
				try ( InputStream in = session.getInputStream() ) {
					String id = IOUtils.toString(in, Charset.defaultCharset());
					StringBuilder resp = new StringBuilder();
					for ( TurtleCommand cmd : server.pullCommands(id) ) {
						resp.append(cmd.getId());
						resp.append(':');
						resp.append(cmd.getApi());
						resp.append('.');
						resp.append(cmd.getMethod());
						for ( Object arg : cmd.getArguments() ) {
							resp.append(',');
							resp.append(arg);
						}
						resp.append('\n');
					}
					return newFixedLengthResponse(Status.OK, "text/plain", resp.toString());
				}
			} else if ( session.getUri().equals("/post") ) {
				try ( InputStream in = session.getInputStream() ) {
					try ( Scanner scan = new Scanner(in) ) {
						String turtleId = scan.nextLine();
						String commandId = scan.nextLine();
						String result = scan.nextLine();
						server.postResult(turtleId, commandId, result);
						return newFixedLengthResponse(Status.OK, "text/plain", "OK");
					}
				}
			} else {
				return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "404 Not Found");
			}
		} catch ( Throwable t ) {
			log.catching(t);
			return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", t.toString());
		}
	}

	WebServer(int port, TurtleServer server) {
		super(port);
		this.server = server;
	}
}
