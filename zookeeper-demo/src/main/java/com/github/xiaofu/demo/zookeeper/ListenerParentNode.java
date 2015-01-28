package com.github.xiaofu.demo.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-7-11 下午2:55:38
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-7-11
 * @modify by reason:{方法名}:{原因}
 */
public class ListenerParentNode implements IZkStateListener, IZkDataListener,
		IZkChildListener {

	private boolean isHolder;
	private ZkClient zkClient;
	protected static final Logger LOG = LoggerFactory
			.getLogger(ListenerParentNode.class);

	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setHolder(boolean isHolder) {
		this.isHolder = isHolder;
	}

	public boolean isHolder() {
		return isHolder;
	}

	public ListenerParentNode() {
		zkClient = ZKMgr.getNewInstance();
	}

	protected void createZNode() {
		 
		String resultSatus = ZkUtils.createZNodeIfNotExists(zkClient,
				ZKConstant.PARENT_PATH, 0, CreateMode.PERSISTENT);
		if (resultSatus != null) {
			LOG.info("I'm parent leader");
			isHolder = true;
		} else {
			LOG.info("I'm parent follower");
		}
	}

	/**
	 * 监控程序状态，如果持掉，临时结点也会被删除
	 * 
	 * @author fulaihua 2014-7-11 下午3:05:29
	 */
	public void startWatcher() {
		createZNode();
		zkClient.unsubscribeAll();
		zkClient.subscribeChildChanges(
				ZKConstant.PARENT_PATH, this);
		zkClient.subscribeStateChanges(this);
		zkClient.subscribeDataChanges(ZKConstant.PARENT_PATH, this);

		LOG.info(this.getClass().getSimpleName()
				+ "  started watcher to zookeeper for path "
				+ ZKConstant.PARENT_PATH);
	}

	@Override
	public void handleNewSession() throws Exception {
		LOG.info("begin  a new session");
		startWatcher();
	}

	/**
	 * 发生状态改变，follower重新竞争持有锁
	 */
	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		System.out.println("I'm changed state:"+state.toString());
	}

	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		System.out.println("dataPath:" + dataPath);
		System.out.println("data:" + data);
	}

	/**
	 * 判断是否有其它程序退出了
	 */
	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		
		LOG.info("parent node be deleted!");
		createZNode();
		
	}

	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds)
			throws Exception {
		System.out.println("parentPath:" + parentPath);
		for (String string : currentChilds) {
			System.out.println(string);
		}

	}

}
