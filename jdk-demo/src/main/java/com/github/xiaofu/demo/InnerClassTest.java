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
		for (int i = 0; i < 10; i++) {
			tests[i].doIt();
			tests[i]=null;//会被垃圾回收？它的回收机制又是什么？
		}
		 
		Thread.currentThread().join();
	}

	interface ITest {
		void doIt();
	}
}
