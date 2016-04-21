package com.github.xiaofu.demo.commons;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;

public class CodecTest {
	public static void main(String[] args)
	{
		System.out.println(BinaryCodec.toAsciiString(new byte[]{-128}));
	    char[] chars=	Hex.encodeHex(new byte[]{23,44,22,45});
	    System.out.println(chars);
	    
	    
	}
}
