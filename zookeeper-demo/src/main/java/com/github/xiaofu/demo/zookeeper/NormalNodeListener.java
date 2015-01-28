/**
 * 
 */
package com.github.xiaofu.demo.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建一个临时ZNODE节点，当ZOOKEEPER服务端挂了后，此临时节点是不会被删除的，它会持久化到磁盘上，只有会话过期才会被删除
 * @author fulaihua
 *
 */
public class NormalNodeListener implements IZkStateListener, IZkDataListener {
	private boolean isHolder;
	private ZkClient zkClient;
	protected static final Logger LOG = LoggerFactory
			.getLogger(NormalNodeListener.class);

	public NormalNodeListener() {
		zkClient = ZKMgr.getNewInstance();
	}

	protected void createZNode() {

		String resultSatus = ZkUtils.createZNodeIfNotExists(zkClient,
				ZKConstant.NORMAL_PATH, 0, CreateMode.EPHEMERAL);
		if (resultSatus != null) {
			System.out.println("I'm  leader");
			isHolder = true;
		} else {
			System.out.println("I'm  follower");

		}
	}

	public void startWatcher() {
		createZNode();
		zkClient.unsubscribeAll();

		zkClient.subscribeStateChanges(this);
		zkClient.subscribeDataChanges(ZKConstant.NORMAL_PATH, this);
		System.out.println(this.getClass().getSimpleName()
				+ "  started watcher to zookeeper for path "
				+ ZKConstant.NORMAL_PATH);

	}

	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		System.out.println("dataPath:" + dataPath);
		System.out.println("data:" + data);
	}

	@Override
	public void handleDataDeleted(String arg0) throws Exception {
		System.out.println("normal node be deleted!");
		createZNode();
	}

	@Override
	public void handleNewSession() throws Exception {
		System.out.println("begin  a new session");
		startWatcher();
	}

	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		System.out.println("I'm changed state:" + state.toString());
	}

}
