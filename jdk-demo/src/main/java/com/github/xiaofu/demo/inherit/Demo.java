/**
 * 
 */
package com.github.xiaofu.demo.inherit;

/**
 * @author xiaofu
 *
 */
class A {
	void test1() {
		System.out.println("test1 A");

		this.private_test2();
		this.non_private_test2();
	}
	/**
	 * here is private method,so will be called
	 */
	private void private_test2() {
		System.out.println("private_test2 A");
	}

	void non_private_test2() {
		System.out.println("non_private_test2 A");
	}
}

class B extends A {
	void test1() {
		System.out.println("test1 B");
		super.test1();

	}

	void private_test2() {
		System.out.println("private_test2 B");
	}

	void non_private_test2() {
		System.out.println("non_private_test2 B");
	}
}

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new B().test1();

	}

}
