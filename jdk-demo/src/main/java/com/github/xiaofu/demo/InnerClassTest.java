package com.github.xiaofu.demo;

public class InnerClassTest {

	public ITest start(final int interval) {
		
		ITest test = new ITest() {
			@Override
			public void doIt() {
				System.out.println(interval);
			}
		};
		return test;

	}

	public static void main(String[] args) {
		InnerClassTest test = new InnerClassTest();
		ITest[] tests=new ITest[10];
		for (int i = 0; i < 10; i++) {
			tests[i]=test.start(i);
		}
		for (ITest iTest : tests) {
			iTest.doIt();	 
		}
	}

	interface ITest {
		void doIt();
	}
}
