package com.github.xiaofu.demo.generic;

public class GenericTypeDemo {
  public  static void main(String[] args)
  {
	  Manager ceo = new Manager("Gus Greedy", 800000, 2003, 12, 15);
      Manager cfo = new Manager("Sid Sneaky", 600000, 2003, 12, 15);
      Pair<Manager> buddies = new Pair<Manager>(ceo, cfo);      
      printBuddies(buddies);

      ceo.setBonus(1000000);
      cfo.setBonus(500000);
      Manager[] managers = { ceo, cfo };

      Pair<Employee> result = new Pair<Employee>();
      minMaxBonus(managers, result);
      System.out.println("first: " + result.getFirst().getName() 
         + ", second: " + result.getSecond().getName());
      maxMinBonus(managers, result);
      System.out.println("first: " + result.getFirst().getName() 
         + ", second: " + result.getSecond().getName());
   
  }
   
  public static void minMaxBonus(Manager[] a, Pair<? super Manager> result)
  {
     if (a == null || a.length == 0) return;
     Manager min = a[0];
     Manager max = a[0];
     for (int i = 1; i < a.length; i++)
     {
        if (min.getBonus() > a[i].getBonus()) min = a[i];
        if (max.getBonus() < a[i].getBonus()) max = a[i];
     }
     result.setFirst(min);
     result.setSecond(max);
  }
  public static void printBuddies(Pair<? extends Employee> p)
  {
     Employee first = p.getFirst();
     Employee second = p.getSecond();
     System.out.println(first.getName() + " and " + second.getName() + " are buddies.");
  }
  public static void maxMinBonus(Manager[] a, Pair<? super Manager> result)
  {
     minMaxBonus(a, result);
     PairAlg.swapHelper(result); // OK--swapHelper captures wildcard type
  }
 
 static  class PairAlg
  {
     public static boolean hasNulls(Pair<?> p)
     {
        return p.getFirst() == null || p.getSecond() == null;
     }

     public static void swap(Pair<?> p) { swapHelper(p); }

     public static <T> void swapHelper(Pair<T> p)
     {
        T t = p.getFirst();
        p.setFirst(p.getSecond());
        p.setSecond(t);
     }
  }
}
