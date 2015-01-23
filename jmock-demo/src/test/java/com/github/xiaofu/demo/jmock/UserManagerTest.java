/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xiaofu.demo.jmock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.core.StringContains;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.xiaofu.demo.jmock.Address;
import com.github.xiaofu.demo.jmock.AddressService;
import com.github.xiaofu.demo.jmock.Para;
import com.github.xiaofu.demo.jmock.UserManager;

import static org.junit.Assert.*;

/**
 * 
 * @author xiaofu
 */
public class UserManagerTest
{

	private Mockery context;
	AddressService service;
	UserManager manager;

	public UserManagerTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		context = new Mockery();
		service = context.mock(AddressService.class);
		manager = new UserManager();
		// 设置mock对象
		manager.addressService = service;
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testFindAddress()
	{

		context.checking(new Expectations()
		{
			{
				oneOf(service).findAddress("allen");
				will(returnValue(Para.Xian));
			}
		});

		// 调用方法
		Address result = manager.findAddress("allen");

		// 验证结果
		assertEquals(Para.Xian, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException()
	{
		context.checking(new Expectations()
		{
			{
				// 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress("allen");
				will(returnValue(Para.BeiJing));

				// 当参数为null的时候，抛出IllegalArgumentException异常。
				allowing(service).findAddress(null);
				will(throwException(new IllegalArgumentException()));
			}
		});

		// 调用方法
		Address result = manager.findAddress("allen");
		manager.findAddress(null);
		// 验证结果
		assertEquals(Para.BeiJing, result);

	}

	@Test
	public void testIterator()
	{
		// 生成地址列表
		final List<Address> addresses = new ArrayList<Address>();
		addresses.add(Para.Xian);
		addresses.add(Para.HangZhou);

		final Iterator<Address> iterator = addresses.iterator();

		// 设置期望。
		context.checking(new Expectations()
		{
			{
				// 当参数为"allen"的时候，addressServcie对象的findAddresses方法用returnvalue返回一个Iterator<Address>对象。
				allowing(service).findAddresses("allen");
				will(returnValue(iterator));

				// 当参数为"dandan"的时候，addressServcie对象的findAddresses方法用returnIterator返回一个Iterator<Address>对象。
				allowing(service).findAddresses("dandan");
				will(returnIterator(addresses));
			}
		});
		Iterator<Address> resultIterator = null;

		// 第1次以"allen"调用方法
		resultIterator = manager.findAddresses("allen");
		// 断言返回的对象。
		assertIterator(resultIterator);

		// 第2次以"allen"调用方法,返回的与第一次一样的iterator结果对象，所以这里没有next了。
		resultIterator = manager.findAddresses("allen");
		Assert.assertFalse(resultIterator.hasNext());

		// 第1次以"dandan"调用方法
		resultIterator = manager.findAddresses("dandan");
		// 断言返回的对象。
		assertIterator(resultIterator);

		// 第2次以"dandan"调用方法,返回的是一个全新的iterator。
		resultIterator = manager.findAddresses("dandan");
		// 断言返回的对象。
		assertIterator(resultIterator);
	}

	/** 断言resultIterator中有两个期望的Address */
	private void assertIterator(Iterator<Address> resultIterator)
	{
		Address address = null;
		// 断言返回的对象。
		address = resultIterator.next();
		Assert.assertEquals(Para.Xian, address);
		address = resultIterator.next();
		Assert.assertEquals(Para.HangZhou, address);
		// 没有Address了。
		Assert.assertFalse(resultIterator.hasNext());
	}

	@Test
	public void testAction()
	{
		// 设置期望。
		context.checking(new Expectations()
		{
			{
				// 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress("allen");
				will(new Action()
				{

					@Override
					public Object invoke(Invocation invocation)
							throws Throwable
					{
						return Para.Xian;
					}

					@Override
					public void describeTo(Description description)
					{
					}

				});
			}
		});
		assertEquals(manager.findAddress("allen"), Para.Xian);
	}

	@Test
	public void testArgument()
	{
		// 设置期望。
		context.checking(new Expectations()
		{
			{
				// 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress("allen");
				will(returnValue(Para.Xian));

				// 当参数为"dandan"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress(with(equal("dandan")));
				will(returnValue(Para.HangZhou));

				// 当参数包含"zhi"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress(with(new BaseMatcher<String>()
				{

					@Override
					public boolean matches(Object item)
					{
						String value = (String) item;
						if (value == null)
							return false;
						return value.contains("zhi");
					}

					@Override
					public void describeTo(Description description)
					{
					}

				}));

				will(returnValue(Para.BeiJing));

				// 当参数为其他任何值的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				allowing(service).findAddress(with(any(String.class)));

				will(returnValue(Para.ShangHai));
			}
		});
		// 以"allen"调用方法
		assertEquals(manager.findAddress("allen"), Para.Xian);
		// 以"dandan"调用方法
		assertEquals(manager.findAddress("dandan"), Para.HangZhou);
		// 以包含"zhi"的参数调用方法
		assertEquals(manager.findAddress("abczhidef"), Para.BeiJing);
		// 以任意一个字符串"abcdefg"调用方法
		assertEquals(manager.findAddress("abcdefg"), Para.ShangHai);
	}

	@Test
	public void testInSequence()
	{
		final Sequence sequence = context.sequence("mySeq_01");

		// 设置期望。
		context.checking(new Expectations()
		{
			{
				// 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				oneOf(service).findAddress("allen");
				inSequence(sequence);
				will(returnValue(Para.Xian));

				// 当参数为"dandan"的时候，addressServcie对象的findAddress方法返回一个Adress对象。
				oneOf(service).findAddress("dandan");
				inSequence(sequence);
				will(returnValue(Para.HangZhou));

			}
		});
		assertEquals(manager.findAddress("allen"), Para.Xian);
		assertEquals(manager.findAddress("dandan"), Para.HangZhou);

	}

	@Test
	public void testStates()
	{
		final States states = context.states("sm").startsAs("s1");
		// 设置期望。
		context.checking(new Expectations()
		{
			{
				// 状态为s1参数包含allen的时候返回西安
				allowing(service).findAddress(
						with(StringContains.containsString("allen")));
				when(states.is("s1"));
				will(returnValue(Para.Xian));

				// 状态为s1参数包含dandan的时候返回杭州，跳转到s2。
				allowing(service).findAddress(
						with(StringContains.containsString("dandan")));
				when(states.is("s1"));
				will(returnValue(Para.HangZhou));
				then(states.is("s2"));

				// 状态为s2参数包含allen的时候返回上海
				allowing(service).findAddress(
						with(StringContains.containsString("allen")));
				when(states.is("s2"));
				will(returnValue(Para.ShangHai));
			}
		});
		// s1状态
		assertEquals(manager.findAddress("allen"), Para.Xian);
		assertEquals(manager.findAddress("allen0"), Para.Xian);

		// 状态跳转到 s2
		assertEquals(manager.findAddress("dandan"), Para.HangZhou);

		// s2状态
		assertEquals(manager.findAddress("allen"), Para.ShangHai);
	}
}
