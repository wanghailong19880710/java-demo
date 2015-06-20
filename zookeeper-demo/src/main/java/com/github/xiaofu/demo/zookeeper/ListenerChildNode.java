package com.github.xiaofu.demo.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ListenerChildNode extends ListenerParentNode  {

	@Override
	protected void createZNode()
	{
		// 创建一个临时节点，创建成功设置一个状态
				String resultSatus = ZkUtils.createZNodeIfNotExists(getZkClient(),
						ZKConstant.CHILD_PATH, null, CreateMode.EPHEMERAL);
				if (resultSatus != null) {
					LOG.info("I'm child  leader");
					setHolder(true);
				} else {
					LOG.info("I'm child follower");
				}
	}
	/**
	 * 
	 */
	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		System.out.println("I'm changed state:"+state.toString());
	}
	/**
	 * 在数据改变或节点被创建都会触发此事件
	 */
	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		System.out.println("dataPath:" + dataPath);
		System.out.println("data:" + data);
	}

	/**
	 * 节点删除时会触发此事件 
	 */
	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		
		LOG.info("child node be deleted!");
		createZNode();
	}
	@Override
	public void startWatcher() {

		createZNode();
		getZkClient().unsubscribeAll();
	
		getZkClient().subscribeStateChanges(this);
		getZkClient().subscribeDataChanges(ZKConstant.CHILD_PATH, this);

		LOG.info(this.getClass().getSimpleName()
				+ "  started watcher to zookeeper for path "
				+ ZKConstant.CHILD_PATH);
	}

}
