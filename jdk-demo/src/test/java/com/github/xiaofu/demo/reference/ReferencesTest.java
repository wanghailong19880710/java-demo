/**
 * 
 */
package com.github.xiaofu.demo.reference;

import static org.junit.Assert.*;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

/**
 * @author xiaofu
 *
 */
public class ReferencesTest {
	/**
	 * Strong Reference<br>
	 * 是 Java 的默认引用实现, 它会尽可能长时间的存活于 JVM 内， 当没有任何强引用指向对象时， GC 执行后将会回收对象
	 */
	@Test
	public void strongReferenceNotGC() {
		System.out.println("start test strongReferenceNotGC");
		Object referent = new VeryBig("strongReferenceNotGC");

		/**
		 * 通过赋值创建 StrongReference
		 */
		Object strongReference = referent;

		assertSame(referent, strongReference);

		referent = null;
		System.gc();

		/**
		 * 由于对象还存在引用，因此gc后，对象未被回收
		 */
		assertNotNull(strongReference);
		System.out.println("end test strongReferenceNotGC");
	}

	@Test
	public void strongReferenceGC() throws InterruptedException {
		System.out.println("start test strongReferenceGC");
		Object referent = new VeryBig("strongReferenceGC");
		referent = null;
		System.gc();
		Thread.sleep(500);
		System.out.println("end test strongReferenceGC");
	}

	/**
	 * WeakReference<br>
	 * 顾名思义, 是一个弱引用, 当所引用的对象在 JVM 内不再有强引用时, GC后，弱引用被置成null，并回收所指的对象
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void weakReferenceGC() throws InterruptedException {
		System.out.println("start test weakReference");
		WeakReference<Object> weakRerference = new WeakReference<Object>(new VeryBig(
				"weakReference"));
		System.gc();
		Thread.sleep(500);
		/**
		 * weak reference 在 GC 后会被置成null，对象就可以被 回收了。。。
		 */
		assertNull(weakRerference.get());
		System.out.println("end test weakReference");
	}
	/**
	 * 由于弱引用对象指向的对象在外部在强引用 ，所以不会被GC掉
	 * @throws InterruptedException
	 */
	@Test
	public void weakReferenceNotGC() throws InterruptedException {
		System.out.println("start test weakReference");
		VeryBig outerStrong= new VeryBig(
				"weakReference");
		WeakReference<Object> weakRerference = new WeakReference<Object>(outerStrong);
		System.gc();
		Thread.sleep(500);
		/**
		 * 外部有强引用，不能被GC掉
		 */
		assertEquals(outerStrong, weakRerference.get());
		System.out.println("end test weakReference");
	}
	/**
	 * WeakHashMap<br>
	 * 使用 WeakReference 作为 key， 一旦没有指向 key 的强引用, WeakHashMap 在 GC 后将自动删除相关的
	 * entry
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void weakHashMap() throws InterruptedException {
		Map<Object, Object> weakHashMap = new WeakHashMap<Object, Object>();
		Object key = new VeryBig("weakHashMap key");
		Object value = new Object();
		weakHashMap.put(key, value);

		assertTrue(weakHashMap.containsValue(value));

		key = null;
		System.gc();
		/**
		 * 等待无效 entries 进入 ReferenceQueue 以便下一次调用 getTable 时被清理
		 */
		Thread.sleep(1000);

		/**
		 * 一旦没有指向 key 的强引用, WeakHashMap 在 GC 后将自动删除相关的 entry
		 */
		assertFalse(weakHashMap.containsValue(value));
	}

	/**
	 * SoftReference<br>
	 * 与 WeakReference 的特性基本一致， 最大的区别在于
	 * SoftReference会尽可能长的保留引用，不会在GC时就回收对象，而是直到 JVM 内存不足时才会被回收(虚拟机保证),
	 * 这一特性使得 SoftReference 非常适合缓存应用
	 */
	@Test
	public void softReference() {
		SoftReference<Object> softRerference = new SoftReference<Object>(new VeryBig(
				"softReference"));

		assertNotNull(softRerference.get());

		System.gc();

		/**
		 * soft references 只有在 jvm OutOfMemory 之前才会被回收, 所以它非常适合缓存应用
		 */
		assertNotNull(softRerference.get());

		// make oom....
		int i = 0;
		while (true) {
			try {
				++i;
				new VeryBig("oom ", 10000000);
			} catch (Throwable e) {
				System.out.println("OOM after " + i + " times");
				e.printStackTrace();
				break;
			}
		}

		assertNull(softRerference.get());
	}

	/**
	 * PhantomReference<br>
	 * Phantom Reference(幽灵引用) 与 WeakReference 和 SoftReference 有很大的不同, 因为它的
	 * get() 方法永远返回 null, 这也正是它名字的由来
	 */
	@Test
	public void phantomReferenceAlwaysNull() {
		ReferenceQueue<Object> q = new ReferenceQueue<Object>();
		PhantomReference<Object> phantomReference = new PhantomReference<Object>(
				new VeryBig("phantomReferenceAlwaysNull"), q);

		/**
		 * phantom reference 的 get 方法永远返回 null
		 */
		assertNull(phantomReference.get());

		assertNull(q.poll());
		System.gc();
		assertNull(q.poll());
	}

	/**
	 * RererenceQueue<br>
	 * 当一个 WeakReference 开始返回 null 时， 它所指向的对象已经准备被回收， 这时可以做一些合适的清理工作. 将一个
	 * ReferenceQueue 传给一个 Reference 的构造函数， 当对象被回收时， 虚拟机会自动将这个weak ref插入到
	 * ReferenceQueue 中， WeakHashMap 就是利用 ReferenceQueue 来清除 key 已经没有强引用的
	 * entries
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void referenceQueueWithWeakReference() throws InterruptedException {
		Object referent = new VeryBig("referenceQueueWithWeakReference");
		ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
		Reference<Object> ref = new WeakReference<Object>(referent, referenceQueue);

		assertFalse(ref.isEnqueued());
		Reference<? extends Object> polled = referenceQueue.poll();
		assertNull(polled);

		referent = null;
		System.gc();

		assertTrue(ref.isEnqueued());
		Reference<? extends Object> removed = referenceQueue.remove();
		assertNotNull(removed);
		assertSame(ref, removed);
		assertNull(removed.get());
	}

	@Test
	public void referenceQueueWithSoftReference() throws InterruptedException {
		Object referent = new VeryBig("referenceQueueWithWeakReference");
		ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
		Reference<Object> ref = new SoftReference<Object>(referent, referenceQueue);

		assertFalse(ref.isEnqueued());
		Reference<? extends Object> polled = referenceQueue.poll();
		assertNull(polled);

		referent = null;
		// make oom....
		try {
			new VeryBig("oom ", 100000000);
		} catch (Throwable e) {

		}
		assertTrue(ref.isEnqueued());
		Reference<? extends Object> removed = referenceQueue.remove();
		assertNotNull(removed);
		assertSame(ref, removed);
		assertNull(removed.get());
	}
}
