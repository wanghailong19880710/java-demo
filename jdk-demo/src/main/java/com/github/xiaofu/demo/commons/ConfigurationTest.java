/**
 * 
 */
package com.github.xiaofu.demo.commons;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author home
 *
 */
public class ConfigurationTest {

	/**
	 * @param args
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws ConfigurationException {
		 PropertiesConfiguration conf=new PropertiesConfiguration("test.properties");
		 System.out.println(conf.getString("test1"));
	}

}
