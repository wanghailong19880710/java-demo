package com.github.xiaofu.demo.jetty;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;

/**
 * 
 */

/**
 * @author xiaofu
 *
 */
public class CxfWithJety6 {
	private static int port = 9100;// default port

	private Server server;
	private Connector listener;
	private Context appContext;
	private BehavioralAnalysisService analysisService;
	/**
	 * 开始
	 * 
	 * @author fulaihua 2012-9-7 下午12:30:46
	 * @throws Exception
	 */
	public void startServer() throws Exception
	{
		 
		 
		 
		initServer();
		// listener.open();
		publishService();
	 

	}
	/**
	 * 停止
	 * 
	 * @author fulaihua 2012-9-7 下午12:30:56
	 * @throws Exception
	 */
	public void stop() throws Exception
	{
		if (server != null)
		{
			 
			server.stop();
			 
		}
	}
	/**
	 * 初始化server
	 * 
	 * @author fulaihua 2012-9-7 下午12:31:06
	 * @throws Exception
	 */
	public void initServer() throws Exception
	{
		server = new Server();
		// add connector
		listener = createConnector();
		server.addConnector(listener);
		// set threadpool
		server.setThreadPool(createThreadPool());
	}

	/**
	 * 添加Listener
	 * 
	 * @author fulaihua 2012-9-7 下午12:31:22
	 * @return
	 * @throws Exception
	 */
	private Connector createConnector()
	{
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost("localhost");
		connector.setPort(port);
		connector.setAcceptors(2);
		connector.setMaxIdleTime(30000);
		connector.setLowResourceMaxIdleTime(5000);
		connector.setLowResourcesConnections(5000);
		connector.setAcceptQueueSize(128);
		connector.setResolveNames(false);
		connector.setUseDirectBuffers(false);
		return connector;
	}

	private QueuedThreadPool createThreadPool()
	{
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setLowThreads(20);
		threadPool.setMaxThreads(200);
		threadPool.setMinThreads(10);
		return threadPool;
	}

	private void publishService() throws Exception
	{
		String busFactory = System
				.getProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME);
		System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME,
				"org.apache.cxf.bus.CXFBusFactory");
		try
		{

			// add servlet
			 
			 
			appContext = new Context(null, "/");
			server.setHandler(appContext);
			CXFNonSpringServlet cxfServlet = new CXFNonSpringServlet();
		 
			ServletHolder cxfServletHolder = new ServletHolder(cxfServlet);
			 
			// servlet.setName("VipCloud");
			// servlet.setForcedPath("VipCloud");
			appContext.addServlet(cxfServletHolder, "/*");
			 
			server.start();
			Bus bus = cxfServlet.getBus();
			BusFactory.setDefaultBus(bus);
			// publish
			JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
			factoryBean.setAddress("/VipCloud/Service/BehaviorAlanalysis");
			factoryBean.setServiceClass(BehavioralAnalysisService.class);
			analysisService = new BehavioralAnalysisServiceImpl();
			factoryBean.setServiceBean(analysisService);

			factoryBean.create();
		}
		catch (Exception e)
		{
 
			throw e;
		}
		finally
		{
			// clean up the system properties
			if (busFactory != null)
			{
				System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME,
						busFactory);
			}
			else
			{
				System.clearProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME);
			}
		}
	}

	/**
	 * @author fulaihua 2014-5-15 上午10:27:55
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		new CxfWithJety6().startServer();
	}

}
