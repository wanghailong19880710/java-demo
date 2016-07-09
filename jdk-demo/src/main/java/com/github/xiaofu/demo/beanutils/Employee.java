package com.github.xiaofu.demo.beanutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

public class Employee{
	
	public class Address {
		 private String city;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}
		 
	}
	private Map<String,Address> addresses=new HashMap<String,Address>();
	private List<Employee> lists=new ArrayList<Employee>();
	private String firstName;
	private String lastName;
	
	public Address getAddress(String type) {
		return addresses.get(type);
	}

	public void setAddress(String type, Address address) {
		addresses.put(type, address);
	}

	public Employee getSubordinate(int index) {
		return lists.get(index);
	}

	public void setSubordinate(int index, Employee subordinate) {
		lists.add(index, subordinate);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName=lastName;
	}
}
