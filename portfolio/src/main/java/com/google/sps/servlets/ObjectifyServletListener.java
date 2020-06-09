package com.google.sps.servlets;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import com.googlecode.objectify.ObjectifyService;

// listener executed by web.xml, required for objectify
public class ObjectifyServletListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.init();
        ObjectifyService.register(Message.class);
	}
}