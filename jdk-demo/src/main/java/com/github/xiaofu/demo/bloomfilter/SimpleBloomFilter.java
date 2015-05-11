package com.github.xiaofu.demo.bloomfilter;

import java.util.BitSet;

public class SimpleBloomFilter {
	private BitSet bits;
	private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
	private SimpleHash[] hashFunctions = new SimpleHash[seeds.length];

	public SimpleBloomFilter() {
		this(2 << 24);
	}

	public SimpleBloomFilter(int size) {
		bits = new BitSet(size);
		for (int i = 0; i < hashFunctions.length; i++) {
			hashFunctions[i] = new SimpleHash(size, seeds[i]);
		}
	}

	public void add(String value) {
		for (SimpleHash hashFunction : hashFunctions) {
			bits.set(hashFunction.getHashCode(value), true);
		}
	}

	public boolean contains(String value) {
		if (null == value) {
			return false;
		}
		boolean result = true;
		for (SimpleHash hashFunction : hashFunctions) {
			result = result && bits.get(hashFunction.getHashCode(value));
		}
		return result;
	}

	public static void main(String[] args) {
		SimpleBloomFilter bloomFilter = new SimpleBloomFilter();
		String value = "iAm333";
		System.out.println(bloomFilter.contains(value));
		bloomFilter.add(value);
		System.out.println(bloomFilter.contains(value));
	}
}