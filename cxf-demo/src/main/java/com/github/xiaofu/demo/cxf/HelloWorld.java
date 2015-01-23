package com.github.xiaofu.demo.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name="HelloWorld",serviceName="HelloWorld")
public interface HelloWorld {
	@WebMethod
    String sayHi(String text);
}