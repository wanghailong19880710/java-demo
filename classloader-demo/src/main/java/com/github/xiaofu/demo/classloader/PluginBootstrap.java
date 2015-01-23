
package com.github.xiaofu.demo.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import com.github.xiaofu.demo.plugin.service.QueryService;

/**
 * <p></p>
 * @author fulaihua 2015年1月23日 下午1:32:44
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015年1月23日
 * @modify by reason:{方法名}:{原因}
 */
public class PluginBootstrap
{

	/**
	 * @author fulaihua 2015年1月23日 下午1:32:44
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
	{
		    File file=new File("E:\\open-source-projects\\github\\java-demo\\target\\debug\\plugin\\service-impl-0.0.1-SNAPSHOT.jar");
		    URL url=file.toURI().toURL();
		 	URLClassLoader loader=new URLClassLoader(new URL[]{url});
		 	Class<?> serviceImplClass=loader.loadClass("com.github.xiaofu.demo.plugin.service.impl.QueryServiceImpl");
		 	QueryService service=(QueryService) serviceImplClass.newInstance();
		 	System.out.println(service.query());
		 	 
	}

}
