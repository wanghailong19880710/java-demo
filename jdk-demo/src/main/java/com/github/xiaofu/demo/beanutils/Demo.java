package com.github.xiaofu.demo.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;

public class Demo {

	public static void standard() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Employee employee = new Employee();
		// simple property
		PropertyUtils.setSimpleProperty(employee, "firstName", "ok");
		System.out.println(PropertyUtils.getSimpleProperty(employee,
				"firstName"));
		// indexed property
		int index = 0;
		Employee subordinate = new Employee();
		subordinate.setAddress("home", new Employee().new Address());
		subordinate.setFirstName("sec");
		String name = "subordinate[" + index + "]";
		PropertyUtils.setIndexedProperty(employee, name, subordinate);
		PropertyUtils.setIndexedProperty(employee, "subordinate", 1,
				subordinate);
		subordinate = (Employee) PropertyUtils.getIndexedProperty(employee,
				name);
		subordinate = (Employee) PropertyUtils.getIndexedProperty(employee,
				"subordinate", 1);
		System.out.println(subordinate.getFirstName());
		// mapped property
		Employee.Address address = new Employee().new Address();
		PropertyUtils.setMappedProperty(employee, "address(home)", address);
		// Nested Property
		PropertyUtils.getNestedProperty(employee, "address(home).city");
		// 任何类型的属性操作
		PropertyUtils
				.getProperty(employee, "subordinate[0].address(home).city");
	}

	public static void dynamic() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		DynaProperty[] props = new DynaProperty[] {
				new DynaProperty("address", java.util.Map.class),
				new DynaProperty("subordinate", Employee[].class),
				new DynaProperty("firstName", String.class),
				new DynaProperty("lastName", String.class) };
		BasicDynaClass dynaClass = new BasicDynaClass("employee", null, props);
		DynaBean employee= dynaClass.newInstance();
		 employee.set("address", new HashMap());
	     employee.set("subordinate", new  Employee[0]);
	     employee.set("firstName", "Fred");
	     employee.set("lastName", "Flintstone");
	     PropertyUtils.setProperty(employee, "firstName", "ok");
	     System.out.println(PropertyUtils.getProperty(employee, "firstName"));
	}

	public static void main(String[] args) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, InstantiationException {
		dynamic();
	}
}
