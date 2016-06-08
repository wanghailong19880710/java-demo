package com.github.xiaofu.demo;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.web.context.ContextLoaderListener;

//import org.springframework.web.context.ContextLoaderListener;

public class Main {
	private static Connector createConnector() {
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost("localhost");
		connector.setPort(8080);
		connector.setAcceptors(2);
		connector.setMaxIdleTime(30000);
		connector.setLowResourcesMaxIdleTime(5000);
		connector.setLowResourcesConnections(5000);
		connector.setAcceptQueueSize(128);
		connector.setResolveNames(false);
		connector.setUseDirectBuffers(false);
		return connector;
	}

	private static QueuedThreadPool createThreadPool() {
		QueuedThreadPool threadPool = new QueuedThreadPool();

		threadPool.setMaxThreads(200);
		threadPool.setMinThreads(10);
		return threadPool;
	}

	public static void CxfWithSpringCodeFirst() throws Exception {

		Server server = new Server(8080);
		server.setConnectors(new Connector[] { createConnector() });
		server.setThreadPool(createThreadPool());
		ServletHolder servletHolder = new ServletHolder(new CXFServlet());
		final ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(servletHolder, "/webService/*"); // 使用它来加载SPRING
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

	/**
	 * 默认加载SpringBusFacotry类，如果类加载失败或缺少相应的Spring配置文件 ，那么退回到使用CXFBusFactory
	 * 
	 * @throws Exception
	 */
	public static void CxfWithNonSpringConfiguration() throws Exception {

		Resource fileserver_xml = Resource
				.newSystemResource("jetty-nospring.xml");
		XmlConfiguration configuration = new XmlConfiguration(
				fileserver_xml.getInputStream());
		Server server = (Server) configuration.configure();
		server.start();

		// 发布必须放在后面是因为在CXFNonSpringServlet中重新创建了一个BUS
		// JETTY和CXF是通过共用一个BUS来连接的。CXFNonSpringServlet我觉得有改进的必要！
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress("/Test");
		factoryBean.setServiceClass(IService.class);
		factoryBean.setServiceBean(new Service());
		factoryBean.create();

	}

	public static void main(String[] args) throws Exception {
		//CxfWithSpringCodeFirst();
		CxfWithSpringConfiguration();
		//CxfWithNonSpringConfiguration();
	}
}
