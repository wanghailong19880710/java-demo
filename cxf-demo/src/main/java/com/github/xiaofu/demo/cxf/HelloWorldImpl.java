package com.github.xiaofu.demo.cxf;

import javax.jws.WebService;

 
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHi(String text) {
		System.out.println("sayHi called");
		return "Hello " + text;
	}

}
