package com.github.xiaofu.demo.cxf;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class CxfClient {
	public static void main(String[] args)
	{
		
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(HelloWorld.class);
		factory.setAddress("http://localhost:9100/helloWorld");
		HelloWorld client = (HelloWorld) factory.create();
		 
		String reply = client.sayHi("HI");
		System.out.println("Server said: " + reply);
		System.exit(0); 
	}
}
