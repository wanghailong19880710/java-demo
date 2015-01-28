package com.github.xiaofu.demo.zookeeper;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.I0Itec.zkclient.ZkClient;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-7-11 下午2:10:55
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-7-11
 * @modify by reason:{方法名}:{原因}
 */
public class ZKMgr {
	private static Lock zkLock = new ReentrantLock();
	private static ZkClient instance = null;

	public static ZkClient getNewInstance() {
		String zkServerHost = "localhost:2181";
		int sessionTimeout = 1000 * 5;
		int connTimeout = 1000 * 5;
		return new ZkClient(zkServerHost, sessionTimeout, connTimeout);
	}

	public static ZkClient getInstance() {
		if (instance != null) {
			return instance;
		}
		zkLock.lock();
		try {
			if (instance == null) {

				String zkServerHost = "localhost:2181";
				int sessionTimeout = 1000 * 5;
				int connTimeout = 1000 * 5;
				instance = new ZkClient(zkServerHost, sessionTimeout,
						connTimeout);

			}
		} finally {
			zkLock.unlock();
		}
		return instance;
	}
}
