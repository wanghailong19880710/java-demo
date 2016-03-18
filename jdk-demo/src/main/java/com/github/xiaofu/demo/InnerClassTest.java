package com.github.xiaofu.demo;

public class InnerClassTest {

	
	public ITest start(final int interval) {
		
		ITest test = new ITest() {
			private int[] tt;
			@Override
			public void doIt() {
				tt=new int[1024*1024*10];
				System.out.println(interval);
			}
		};
		return test;

	}

	public static void main(String[] args) throws InterruptedException {
		InnerClassTest test = new InnerClassTest();
		ITest[] tests=new ITest[10];
		for (int i = 0; i < 10; i++) {
			tests[i]=test.start(i);
		}
		for (ITest iTest : tests) {
			iTest.doIt();	 
		
		}
		Thread.currentThread().join();
	}

	interface ITest {
		void doIt();
	}
}
