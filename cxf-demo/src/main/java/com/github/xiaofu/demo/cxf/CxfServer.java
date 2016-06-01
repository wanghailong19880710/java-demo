/**
 * 
 */
package com.github.xiaofu.demo.cxf;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Message;

/**
 * @author xiaofu
 * 
 */
public class CxfServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * System.out.println("Starting Server"); HelloWorldImpl implementor =
		 * new HelloWorldImpl(); String address =
		 * "http://localhost:9100/helloWorld"; Endpoint.publish(address,
		 * implementor);
		 */

		
		  HelloWorldImpl implementor = new HelloWorldImpl();
		  JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		  svrFactory.setServiceClass(HelloWorld.class);
		  svrFactory.setAddress("http://localhost:9000/helloWorld");
		  svrFactory.setServiceBean(implementor);
		  svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		  svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		  svrFactory.create();
		 
		 
		
	}
}
