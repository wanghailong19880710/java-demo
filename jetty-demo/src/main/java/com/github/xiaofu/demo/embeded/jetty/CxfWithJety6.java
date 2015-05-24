package com.github.xiaofu.demo.embeded.jetty;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.ContextDeployer;
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
	private static int port = 9200;// default port

	private Server server;
	private Connector listener;
	/**
	 * 开始
	 * 
	 * @author fulaihua 2012-9-7 下午12:30:46
	 * @throws Exception
	 */
	public void startServer() throws Exception {

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
	public void stop() throws Exception {
		if (server != null) {

			server.stop();

		}
	}

	/**
	 * 初始化server
	 * 
	 * @author fulaihua 2012-9-7 下午12:31:06
	 * @throws Exception
	 */
	public void initServer() throws Exception {
		server = new Server();
		// add connector
		listener = createConnector();
		server.addConnector(listener);
		server.setStopAtShutdown(true);
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
	private Connector createConnector() {
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

	private QueuedThreadPool createThreadPool() {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setLowThreads(20);
		threadPool.setMaxThreads(200);
		threadPool.setMinThreads(10);
		return threadPool;
	}
	/**
	 * 使用的热布署方式，但是这里还是会有一个BUG，那就是扫描线程正在卸载，把webapphandler移出集合，新的集合还没有添加进来，而外面的请求已经
	 * 到来，发现找不到handler，就会发生404
	 * @throws Exception
	 */
	private void publishService() throws Exception {
		 
		// add servlet
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		ContextDeployer deployer = new ContextDeployer();
		//配置目录只会查找XML文件，这里放context（每一个站点配置的地方），WAR包或WEB目录由XML指定，这里测试是放在一起的
		deployer.setConfigurationDir("E:\\open-source-projects\\github\\java-demo\\jetty-demo\\webapp");
		deployer.setContexts(contexts);
	 
		server.addLifeCycle(deployer);
		server.setHandler(contexts);
		server.start();
		server.join();

	}

	/**
	 * @author fulaihua 2014-5-15 上午10:27:55
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new CxfWithJety6().startServer();
	}

}
