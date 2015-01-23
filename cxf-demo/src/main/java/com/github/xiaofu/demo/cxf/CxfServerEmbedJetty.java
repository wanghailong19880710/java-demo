/**
 * 
 */
package com.github.xiaofu.demo.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.github.xiaofu.demo.utils.Tools;

/**
 * @author xiaofu
 * 
 */
public class CxfServerEmbedJetty {
	private static Connector createConnector() {
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost(Tools.getHostName());
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

	public static void main(String[] args) {
		String busFactory = System
				.getProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME);
		System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME,
				"org.apache.cxf.bus.CXFBusFactory");
		try {

			Server httpServer = new Server();
			httpServer.addConnector(createConnector());
			httpServer.setThreadPool(createThreadPool());

			ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			CXFNonSpringServlet cxfServlet = new CXFNonSpringServlet();

			context.addServlet(new ServletHolder(cxfServlet),
					"/*");
			httpServer.setHandler(context);

			httpServer.start();

			Bus bus = cxfServlet.getBus();
			BusFactory.setDefaultBus(bus);
			JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
			factoryBean.setAddress("/Cxf/Demo");
			factoryBean.setServiceClass(HelloWorld.class);
			HelloWorld helloWorld = new HelloWorldImpl();
			factoryBean.setServiceBean(helloWorld);

			factoryBean.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// clean up the system properties
			if (busFactory != null) {
				System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME,
						busFactory);
			} else {
				System.clearProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME);
			}
		}
	}
}
