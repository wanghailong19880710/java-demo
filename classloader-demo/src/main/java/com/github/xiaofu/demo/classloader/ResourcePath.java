package com.github.xiaofu.demo.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ResourcePath
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("==============bootstrap===========");
		URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
		for (int i = 0; i < urls.length; i++)
		{
			System.out.println(urls[i]);
		}
		System.out.println("==============ext==============");
		// System.out.println(System.getProperty("java.ext.dirs"));
		ClassLoader extensionClassloader = ClassLoader.getSystemClassLoader()
				.getParent();
		System.out.println("the parent of extension classloader : "
				+ extensionClassloader.getParent());
		
		System.out.println(ClassLoader
				.getSystemResource("com/github/xiaofu/demo/classloader/ResourcePath.class"));

		System.out.println(ClassLoader.getSystemClassLoader().getResource("."));
	}
}
