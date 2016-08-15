package com.github.zachdeibert.ccjavaturtles;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

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
			} else {
				session.parseBody(new HashMap<String, String>());
				String queryString = session.getQueryParameterString();
				String[] body = queryString == null ? new String[0] : queryString.split("\n");
				if ( session.getUri().equals("/register") ) {
					if ( body.length != 2 ) {
						log.warn("Invalid message body");
					} else {
						server.registerTurtle(body[0], body[1]);
						return newFixedLengthResponse(Status.OK, "text/plain", "success");
					}
				} else if ( session.getUri().equals("/pull") ) {
					if ( body.length != 1 ) {
						log.warn("invalid message body");
						return newFixedLengthResponse(Status.OK, "text/plain", "");
					} else {
						StringBuilder resp = new StringBuilder();
						for ( TurtleCommand cmd : server.pullCommands(body[0]) ) {
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
					if ( body.length != 3 ) {
						log.warn("Invalid message body");
					} else {
						server.postResult(body[0], body[1], body[2]);
						return newFixedLengthResponse(Status.OK, "text/plain", "OK");
					}
				}
			}
			return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "404 Not Found");
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
