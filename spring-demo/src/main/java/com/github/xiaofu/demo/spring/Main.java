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
	 
		String[] aliases= context.getAliases("test2");
		for (String string : aliases) {
			System.out.println(string);//test1,test3
		}
	}

}
