package com.github.xiaofu.demo.concurrency;

import java.util.ArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
 

public class TestDelayQueue
{
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		list.add("string 1");
		list.add("string 2");
		list.add("string 3");
		list.add("string 4");
		list.add("string 5");
		list.add("string 6");
		list.add("string 7");
		list.add("string 8");
		list.add("string 9");
		list.add("string 10");

		DelayQueue<TestString> queue = new DelayQueue<TestString>();

		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++)
		{
			queue.put(new TestString(list.get(i), TimeUnit.NANOSECONDS.convert(
					1, TimeUnit.SECONDS)));
			try
			{
				queue.take().print();
				System.out.println("After "
						+ (System.currentTimeMillis() - start)
						+ " MilliSeconds");
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static class TestString implements Delayed
	{
		private String str;
		private long timeout;

		TestString(String str, long timeout)
		{
			this.str = str;
			this.timeout = timeout + System.nanoTime();
		}

		// 返回距离你自定义的超时时间还有多少
		public long getDelay(TimeUnit unit)
		{
			return unit.convert(timeout - System.nanoTime(),
					TimeUnit.NANOSECONDS);
		}

		// 比较getDelay()函数的返回值
		public int compareTo(Delayed other)
		{
			if (other == this) // compare zero ONLY if same object
				return 0;

			TestString t = (TestString) other;
			long d = (getDelay(TimeUnit.NANOSECONDS) - t
					.getDelay(TimeUnit.NANOSECONDS));
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}

		void print()
		{
			System.out.println(str);
		}
	}
}
