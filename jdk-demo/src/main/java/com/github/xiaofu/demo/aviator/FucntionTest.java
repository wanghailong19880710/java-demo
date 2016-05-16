package com.github.xiaofu.demo.aviator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class FucntionTest {
	static class SplitContains extends AbstractFunction {

		@Override
		public AviatorObject call(Map<String, Object> env, AviatorObject args1,
				AviatorObject args2, AviatorObject args3, AviatorObject args4) {

			String fieldName = (String) args1.getValue(env);
			String fieldSeparator = (String) args2.getValue(env);
			String testValue = (String) args3.getValue(env);
			String valueSeparator = (String) args4.getValue(env);
			Map<String, String> xxxobj = (Map<String, String>) env
					.get("xxxobj");

			if (StringUtils.isEmpty(xxxobj.get(fieldName)))
				return AviatorBoolean.FALSE;
			if (StringUtils.isEmpty(valueSeparator)) {
				for (String item : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					if(testValue.equalsIgnoreCase(item))
						return AviatorBoolean.TRUE;
				}
			} else {
				for (String orginal : StringUtils.split(xxxobj.get(fieldName),
						fieldSeparator)) {
					for (String test : StringUtils.split(testValue,
							valueSeparator)) {
						 if(orginal.equalsIgnoreCase(test))
							 return AviatorBoolean.TRUE;
					}
				}
			}
			return AviatorBoolean.FALSE;
		}

		public String getName() {
			return "splitContains";
		}

	}

	public static void main(String[] args) {
		String aa = " splitContains('classids_s',';','123;567',';') ";
		AviatorEvaluator.addFunction(new SplitContains());
		Expression express = AviatorEvaluator.compile(aa, true);
		Map<String, Object> env = new HashMap<String, Object>();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("classids_s", "123;234");
		env.put("xxxobj", obj);
	   System.out.println(express.execute(env));
	}
}
