package com.github.zachdeibert.ccjavaturtles;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

class WebServer extends NanoHTTPD {
	@Override
	public Response serve(IHTTPSession session) {
		System.out.println(session.getUri());
		if ( session.getUri().equals("/") ) {
			try ( InputStream in = WebServer.class.getResourceAsStream("help.html") ) {
				return newFixedLengthResponse(Status.OK, "text/html", IOUtils.toString(in, Charset.defaultCharset())
						.replace("{{ port }}", String.valueOf(getListeningPort())));
			} catch ( IOException ex ) {
				ex.printStackTrace();
				return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", ex.toString());
			}
		}
		return newFixedLengthResponse("Hello, world!");
	}

	WebServer(int port) {
		super(port);
	}
}
