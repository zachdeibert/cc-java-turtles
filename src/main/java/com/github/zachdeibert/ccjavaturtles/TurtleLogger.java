package com.github.zachdeibert.ccjavaturtles;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "Turtle", category = "Core", elementType = "appender", printObject = true)
class TurtleLogger extends AbstractAppender {
	private static final long serialVersionUID = -4921453580982803077L;

	@Override
	public void append(LogEvent event) {
		// Logger FQCN should be
		// 'com.github.zachdeibert.ccjavaturtles.Turtle{id}p{port}'
		String msg = new String(getLayout().toByteArray(event));
		String fqcn = event.getLoggerName();
		String id = fqcn.replaceAll("com.github.zachdeibert.ccjavaturtles.turtles.Turtle(.+)p([0-9]+)", "$1");
		int port = Integer.parseInt(fqcn.replaceAll("com.github.zachdeibert.ccjavaturtles.turtles.Turtle(.+)p([0-9]+)", "$2"));
		TurtleServer server = InstanceManager.serverRunningOnPort(port);
		Turtle turtle = server.getTurtle(id);
		turtle.sendCommand(new TurtleCommand("test", "print", msg));
	}

	@PluginFactory
	public static TurtleLogger createAppender(@PluginAttribute("name") String name,
			@PluginElement("Filters") Filter filter, @PluginElement("Layout") Layout<? extends Serializable> layout) {
		return new TurtleLogger(name, filter, layout);
	}

	private TurtleLogger(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}
}
