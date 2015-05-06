/**
 * 
 */
package com.github.xiaofu.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * @author fulaihua
 *
 */
public class RegexDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	   Splitter splitter=	Splitter.onPattern(",|\\.").trimResults().omitEmptyStrings();
	  System.out.println(Iterables.size(splitter.split("32,43"))) ;
	   
		String b= CharMatcher.anyOf(",").trimFrom("中国,dd,fasf,");
		System.out.println(b);
		
		Pattern reg = Pattern.compile(".*?,(.*?)\\((.*)\\).*");
		Matcher m1 = reg.matcher("2015,32(04)");
		System.out.println(m1);

		if (m1.matches()) {
			System.out.println(m1.group(1));
			
			 System.out.println(m1.group(2));
			 
		}

	}

}
