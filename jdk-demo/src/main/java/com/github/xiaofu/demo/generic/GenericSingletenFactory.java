package com.github.xiaofu.demo.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

 

public final class GenericSingletenFactory
{
	private static final Map<Class<?>, Object> CACHES = new HashMap<Class<?>, Object>();

	private GenericSingletenFactory()
	{

	}

	@SuppressWarnings("unchecked")
	public static <T> T getSingletenInstance(Class<T> classType)
	{
		Object instance = CACHES.get(classType);

		if (instance == null)
		{
			synchronized (CACHES)
			{

				if (instance == null)
				{

					Constructor<T> constructor;
					try
					{
						constructor = classType.getConstructor(null);
						try
						{
							instance = constructor.newInstance(null);
							CACHES.put(classType, instance);
						}
						catch (InstantiationException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (IllegalArgumentException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					catch (NoSuchMethodException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (SecurityException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		return (T) instance;
	}

	public static void main(String[] args)
	{
	 
	}
}
