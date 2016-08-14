package com.github.zachdeibert.ccjavaturtles;

import fi.iki.elonen.NanoHTTPD;

class WebServer extends NanoHTTPD {
	@Override
	public Response serve(IHTTPSession session) {
		return newFixedLengthResponse("Hello, world!");
	}

	WebServer(int port) {
		super(port);
	}
}
