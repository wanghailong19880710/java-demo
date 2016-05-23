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
		Entity t=new Entity();
		t.setT1("abc");
		context.getBean(IService.class).test(t);
		 
	}

}
