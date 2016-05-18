/**
 * 
 */
package com.github.xiaofu.demo.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author home
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		context.getBean(IService.class).test();
		 
	}

}
