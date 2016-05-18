package com.github.xiaofu.demo.spring;

import javax.annotation.Resource;

public class Service implements IService{
	
	@Resource(name="mapData")
	IMapData mapData;
	@Override
	public void test() {
		 
		System.out.println(mapData.get());
	}

}
