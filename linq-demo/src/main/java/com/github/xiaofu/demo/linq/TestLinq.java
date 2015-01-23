package com.github.xiaofu.demo.linq;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import com.github.xiaofu.demo.utils.Tools;

import net.hydromatic.linq4j.Enumerator;
import net.hydromatic.linq4j.Grouping;
import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.*;

public class TestLinq
{
	/** Employee. */
	static class Employee
	{
		public final int empno;
		public final String name;
		public final int deptno;

		public Employee(int empno, String name, int deptno)
		{
			this.empno = empno;
			this.name = name;
			this.deptno = deptno;
		}

		public String toString()
		{
			return "Employee(name: " + name + ", deptno:" + deptno + ")";
		}
	}

	public static final Employee[] EMPS = {
			new Employee(100, "Fred", 10), new Employee(110, "Bill", 30),
			new Employee(120, "Eric", 10), new Employee(130, "Janet", 10),
	};
	public static final Function1<Employee, Integer> EMP_DEPTNO_SELECTOR = new Function1<Employee, Integer>()
	{
		public Integer apply(Employee employee)
		{
			return employee.deptno;
		}
	};

	public static void main(String[] args) throws ParseException
	{
		final List<User> users = new ArrayList<User>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Tools.parseDate(
				Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"),
				"yyyy-MM-dd"));
		calendar = (Calendar) calendar.clone();
		calendar.set(Calendar.DATE, 3);
		users.add(new User(2, calendar.getTime(), 50));
		calendar = (Calendar) calendar.clone();
		calendar.set(Calendar.DATE, 3);
		users.add(new User(3, calendar.getTime(), 200));
		calendar.set(Calendar.DATE, 1);
		users.add(new User(1, calendar.getTime(), 200));
		calendar = (Calendar) calendar.clone();
		calendar.set(Calendar.DATE, 2);
		users.add(new User(1, calendar.getTime(), 100));

		List<Grouping<Date, User>> lists = Linq4j.asEnumerable(users)
				.groupBy(new Function1<User, Date>()
				{

					@Override
					public Date apply(User a0)
					{

						return a0.getInject();
					}
				}).orderBy(new Function1<Grouping<Date, User>, Date>()
				{

					@Override
					public Date apply(Grouping<Date, User> a0)
					{

						return a0.getKey();
					}

				}).toList();
		for (Grouping<Date, User> grouping : lists)
		{
			System.out.println(grouping.getKey());
			Enumerator<User> iterator = grouping.enumerator();
			while (iterator.moveNext())
			{
				System.out.println(iterator.current().getId());
			}
		}
		String s = Linq4j.asEnumerable(EMPS)
				.groupBy(EMP_DEPTNO_SELECTOR, new Function0<String>()
				{
					public String apply()
					{
						return null;
					}
				}, new Function2<String, Employee, String>()
				{
					public String apply(String v1, Employee e0)
					{
						return v1 == null ? e0.name : (v1 + "+" + e0.name);
					}
				}, new Function2<Integer, String, String>()
				{
					public String apply(Integer v1, String v2)
					{
						return v1 + ": " + v2;
					}
				}).orderBy(Functions.<String> identitySelector()).toList()
				.toString();
		System.out.println(s.equals("[10: Fred+Eric+Janet, 30: Bill]"));

	}
}
