package com.github.xiaofu.demo.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZkDoubleBarrier extends SyncPrimitive {
	private int size;
	private String name;

	/**
	 * Barrier constructor
	 *
	 * @param address
	 * @param root
	 * @param size
	 */
	ZkDoubleBarrier(String address, String root, int size) {
		super(address);
		this.root = root;
		this.size = size;
		this.name = "child" + counts.incrementAndGet();
		// Create barrier node
		if (zk != null) {
			try {
				Stat s = zk.exists(root, false);
				if (s == null) {
					zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				}
			} catch (KeeperException e) {
				System.out
						.println("Keeper exception when instantiating queue: "
								+ e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception");
			}
		}

	}

	boolean enter() throws KeeperException, InterruptedException {
		zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);

				if (list.size() < size) {
					mutex.wait();
				} else {
					System.out.println("I enter barrier:"+name);
					return true;
				}
			}
		}
	}

	boolean leave() throws KeeperException, InterruptedException {
		zk.delete(root + "/" + name, 0);
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				if (list.size() > 0) {
					mutex.wait();
				} else {
					System.out.println("I leave barrier:"+name);
					return true;
				}
			}
		}
	}
}
