package com.github.xiaofu.demo.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository("mapData")
public class MapData  implements IMapData{

	@Override
	public String get() {
		return "...............";
	}

}
