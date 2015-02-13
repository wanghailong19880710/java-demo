package com.github.xiaofu.demo.cxf.programmatically;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import com.github.xiaofu.demo.cxf.web.PersonServiceImpl;
 
/**
 * 需要引入cxf-rt-transports-http-jetty
 * @author xiaofu
 *
 */
public class WithoutSpringService {
	public static void main(String[] args) {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(PersonServiceImpl.class);
		sf.setResourceProvider(PersonServiceImpl.class, new SingletonResourceProvider(new PersonServiceImpl()));
		sf.setAddress("http://localhost:9001/");
		//官网上说加下面代码可以不用spring,但是我不加一样可以不用spring
		/*BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
		JAXRSBindingFactory factory = new JAXRSBindingFactory();
		factory.setBus(sf.getBus());
		manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);*/
		sf.create();
	}
}
