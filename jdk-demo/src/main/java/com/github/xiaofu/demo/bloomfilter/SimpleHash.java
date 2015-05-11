package com.github.xiaofu.demo.bloomfilter;

public class SimpleHash {
	private int size;
	private int seed;

	SimpleHash(int size, int seed) {
		this.size = size;
		this.seed = seed;
	}

	public int getHashCode(String value) {
		int result = 0;
		int length = value.length();
		for (int i = 0; i < length; i++) {
			result = seed * result + value.charAt(i);
		}
		return (size - 1) & result;
	}
}