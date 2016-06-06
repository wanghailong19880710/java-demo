package com.github.xiaofu.demo;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.web.context.ContextLoaderListener;

public class Main {
	public static void CxfWithSpringCodeFirst() throws Exception {
		Server server = new Server(8080);
		// Register and map the dispatcher servlet
		final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
		final ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(servletHolder, "/Test/*");
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter("contextConfigLocation",
				"classpath*:cxf-beans.xml");
		server.setHandler(context);
		server.start();
		server.join();
	}

	public static void CxfWithSpringConfiguration() throws Exception {
		Resource fileserver_xml = Resource.newSystemResource("jetty.xml");
		XmlConfiguration configuration = new XmlConfiguration(
				fileserver_xml.getInputStream());
		Server server = (Server) configuration.configure();
		server.start();
		server.join();
	}

	public static void main(String[] args) throws Exception {

		CxfWithSpringConfiguration();
	}
}
