package com.github.xiaofu.demo.zookeeper;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;

/**
 * This refers to the different ways a node can change. It helps to think of
 * ZooKeeper as maintaining two lists of watches: data watches and child
 * watches. getData() and exists() set data watches. getChildren() sets child
 * watches. Alternatively, it may help to think of watches being set according
 * to the kind of data returned. getData() and exists() return information about
 * the data of the node, whereas getChildren() returns a list of children. Thus,
 * setData() will trigger data watches for the znode being set (assuming the set
 * is successful). A successful create() will trigger a data watch for the znode
 * being created and a child watch for the parent znode. A successful delete()
 * will trigger both a data watch and a child watch (since there can be no more
 * children) for a znode being deleted as well as a child watch for the parent
 * znode.
 * 
 * @author xiaofu
 *
 */
public class ZkMain {

	/**
	 * @author fulaihua 2014-7-12 下午2:26:35
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		// createChildNode();
		// deleteChildNode();
		// setChildNodeData();
//		barrierTest();
		producerAndConsumerQueueTest();
 
	}

	/**
	 * 在已经存在父节点上，创建子节点会得到一个EventType枚举的NodeChildrenChanged
	 * 
	 * @throws InterruptedException
	 */
	private static void createChildNode() throws InterruptedException {
		ListenerParentNode listener1 = new ListenerParentNode();
		ListenerChildNode listener2 = new ListenerChildNode();
		listener1.startWatcher();
		Thread.currentThread().sleep(1000 * 2);
		listener2.startWatcher();
		Thread.currentThread().join();
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	private static void deleteChildNode() throws InterruptedException {
		ListenerParentNode listener1 = new ListenerParentNode();
		ListenerChildNode listener2 = new ListenerChildNode();
		listener1.startWatcher();
		Thread.currentThread().sleep(1000 * 2);
		listener2.startWatcher();
		Thread.currentThread().sleep(1000 * 2);
		listener2.getZkClient().delete(ZKConstant.CHILD_PATH);
		Thread.currentThread().join();
	}

	private static void setChildNodeData() throws InterruptedException {
		ListenerParentNode listener1 = new ListenerParentNode();
		ListenerChildNode listener2 = new ListenerChildNode();
		listener1.startWatcher();
		Thread.currentThread().sleep(1000 * 2);
		listener2.startWatcher();
		Thread.currentThread().sleep(1000 * 2);
		listener2.getZkClient().writeData(ZKConstant.CHILD_PATH, 2);
		Thread.currentThread().join();
	}

	private static void barrierTest() {
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i <5; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					ZkDoubleBarrier b1 = new ZkDoubleBarrier("localhost:2181", "/b1", 5);
					try {
						b1.enter();
						Thread.currentThread().sleep(1000*2);
						b1.leave();
					} catch (KeeperException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				}
			});
		}
		
	service.shutdown();

	}
	private static void producerAndConsumerQueueTest()
	{
		final Random ran=new Random(10);
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i <2; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					ZkQueue b1 = new ZkQueue("localhost:2181", "/queue");
					try {
						int radValue=ran.nextInt(10);
						System.out.println("produce value:"+radValue);
						b1.produce(5);
					} catch (KeeperException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				}
			});
		}
		ExecutorService service2 = Executors.newCachedThreadPool();
		for (int i = 0; i <5; i++) {
			service2.execute(new Runnable() {
				@Override
				public void run() {
					ZkQueue b1 = new ZkQueue("localhost:2181", "/queue");
					try {
						
						System.out.println("consume value:"+b1.consume());
					} catch (KeeperException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				}
			});
		}
	service.shutdown();
	service2.shutdown();
	}

}
