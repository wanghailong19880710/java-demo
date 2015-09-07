package com.github.xiaofu.demo.javacup;

import java_cup.runtime.*;

class Main {
	public static void main(String[] argv) throws Exception {
		System.out.println("Please type your arithmethic expression:");
		MyCalcParser p = new MyCalcParser(new CalcScanner());
		p.parse();
	}
}
