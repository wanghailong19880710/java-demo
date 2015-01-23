package com.github.xiaofu.demo.reflect;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntityDaoTest
{

	@Test
	public void test()
	{
		 EntityDao testDao = new EntityDao();  
	        Entity e = testDao.get(null);  
	        assertNotNull(e);  
	        System.out.println(e.getClass().getName());
	}

}
