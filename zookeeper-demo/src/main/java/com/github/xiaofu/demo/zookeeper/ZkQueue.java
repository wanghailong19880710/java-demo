package com.github.xiaofu.demo.zookeeper;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZkQueue extends SyncPrimitive {
	ZkQueue(String address, String name) {
		super(address);
		this.root = name;
		// Create ZK node name
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

	public boolean produce(int i) throws KeeperException, InterruptedException {
		ByteBuffer b = ByteBuffer.allocate(4);
		byte[] value;

		// Add child with value i
		b.putInt(i);
		value = b.array();
		zk.create(root + "/element", value, Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT_SEQUENTIAL);

		return true;
	}

	public int consume() throws KeeperException, InterruptedException {
		int retvalue = -1;
		Stat stat = null;

		// Get the first element available
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				if (list.size() == 0) {
					System.out.println("Going to wait");
					mutex.wait();
				} else {
					Integer min = new Integer(list.get(0).substring(7));
					for (String s : list) {
						Integer tempValue = new Integer(s.substring(7));

						if (tempValue < min)
							min = tempValue;
					}
					System.out.println("Temporary value: " + root + "/element"
							+ min);

					try {
						//competetion of multiple thread 
						byte[] b = zk.getData(root + "/element" + String.format("%010d", min), false,
								stat);
						zk.delete(root + "/element" + String.format("%010d", min), 0);
						System.out.println("delete node:" + root + "/element"
								+ min);
						ByteBuffer buffer = ByteBuffer.wrap(b);
						retvalue = buffer.getInt();

						return retvalue;
					} catch (Exception e) {

						continue;
					}

				}
			}
		}
	}

}
