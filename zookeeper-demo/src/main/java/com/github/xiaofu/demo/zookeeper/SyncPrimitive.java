package com.github.xiaofu.demo.zookeeper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class SyncPrimitive implements Watcher {
	protected ZooKeeper zk;
	protected Integer mutex;
	protected static AtomicInteger counts = new AtomicInteger(0);
	protected String root;

	SyncPrimitive(String address) {
		if (zk == null) {
			try {
				System.out.println("Starting ZK:");
				zk = new ZooKeeper(address, 3000, this);
				mutex = new Integer(-1);
				System.out.println("Finished starting ZK: " + zk);
			} catch (IOException e) {
				System.out.println(e.toString());
				zk = null;
			}
		}
	}

	public void process(WatchedEvent event) {
		synchronized (mutex) {
			mutex.notify();
		}
	}
}
