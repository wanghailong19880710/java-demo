 
package com.github.xiaofu.demo.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

 

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;



/**
 * <p></p>
 * @author fulaihua 2014-7-11 下午2:09:01
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-7-11
 * @modify by reason:{方法名}:{原因}
 */
public class ZkUtils
{
	protected static final Logger LOG =LoggerFactory.getLogger(ZkUtils.class);
	private static final char ZNODE_PATH_SEPARATOR = '/';

	static public String getZNode(String parentZNode, String znodeName)
	{
		return znodeName.charAt(0) == ZNODE_PATH_SEPARATOR ? znodeName
				: joinPath(parentZNode, znodeName);
	}

	static private String joinPath(String parent, String child)
	{
		return parent + ZNODE_PATH_SEPARATOR + child;
	}

	/**
	 * Make sure this znode exists by creating it if it's missing
	 * 
	 * @param znode
	 *            full path to znode
	 * @return true if it works
	 */
	static public boolean ensureExists(ZkClient zkClient, final String znode)
	{
		try
		{
			if (zkClient.exists(znode))
			{
				return true;
			}
			zkClient.create(znode, null, CreateMode.PERSISTENT);
			LOG.debug("Created ZNode " + znode);
			return true;
		}
		catch (ZkNodeExistsException e)
		{
			return true; // ok, move on.
		}
		catch (ZkNoNodeException e)
		{
			return ensureParentExists(zkClient, znode)
					&& ensureExists(zkClient, znode);
		}
		catch (ZkInterruptedException e)
		{
			LOG.warn("Failed to create " + znode
					+ " -- check quorum servers, currently=", e);
		}
		catch (ZkException e)
		{
			LOG.warn("Failed to create " + znode
					+ " -- check quorum servers, currently=", e);
		}
		return false;
	}

	static public boolean ensureParentExists(ZkClient zkClient,
			final String znode)
	{
		int index = znode.lastIndexOf(ZNODE_PATH_SEPARATOR);
		if (index <= 0)
		{ // Parent is root, which always exists.
			return true;
		}
		return ensureExists(zkClient, znode.substring(0, index));
	}

	/**
	 * Unrecursive deletion of specified znode
	 * 
	 * @param znode
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	static public void deleteZNode(ZkClient zkClient, String znode)
	{
		deleteZNode(zkClient, znode, false);
	}

	/**
	 * Optionnally recursive deletion of specified znode
	 * 
	 * @param znode
	 * @param recursive
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	static public void deleteZNode(ZkClient zkClient, String znode,
			boolean recursive)
	{
		deleteZNode(zkClient, znode, recursive, -1);
	}

	/**
	 * Atomically delete a ZNode if the ZNode's version matches the expected
	 * version.
	 * 
	 * @param znode
	 *            Fully qualified path to the ZNode
	 * @param recursive
	 *            If true, will recursively delete ZNode's children
	 * @param version
	 *            Expected version, as obtained from a Stat object
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	static public void deleteZNode(ZkClient zkClient, String znode,
			boolean recursive, int version)
	{
		if (recursive)
		{
			LOG.info("deleteZNode get children for " + znode);
			List<String> znodes = zkClient.getChildren(znode);
			if (znodes != null && znodes.size() > 0)
			{
				for (String child : znodes)
				{
					String childFullPath = getZNode(znode, child);
					LOG.info("deleteZNode recursive call " + childFullPath);
					deleteZNode(zkClient, childFullPath, true, version);
				}
			}
		}
		zkClient.delete(znode);
		LOG.debug("Deleted ZNode " + znode);
	}

	/**
	 * Check if the specified node exists. Sets no watches.
	 * 
	 * Returns true if node exists, false if not. Returns an exception if there
	 * is an unexpected zookeeper exception.
	 * 
	 * @param znode
	 *            path of node to watch
	 * @return version of the node if it exists, -1 if does not exist
	 * @throws KeeperException
	 *             if unexpected zookeeper exception
	 */
	static public boolean checkExists(ZkClient zkClient, String znode)
	{
		try
		{
			return zkClient.exists(znode);
		}
		catch (ZkInterruptedException e)
		{
			LOG.warn(" Unable to set watcher on znode (" + znode + ")", e);
			return false;
		}
		catch (ZkException e)
		{
			LOG.warn(" Unable to set watcher on znode (" + znode + ")", e);
			return false;
		}
	}

	static public String createZNodeIfNotExists(ZkClient zkClient,
			String fullyQualifiedZNodeName, byte[] data, CreateMode createMode)
	{
		if (!ensureParentExists(zkClient, fullyQualifiedZNodeName))
		{
			return null;
		}

		try
		{
			// create the znode
			zkClient.create(fullyQualifiedZNodeName, data, createMode);
			LOG.debug("Created ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper");
		}
		catch (ZkNodeExistsException nee)
		{
			LOG.debug("ZNode " + fullyQualifiedZNodeName);
			return null;
		}
		catch (ZkInterruptedException e)
		{
			LOG.warn("Failed to create ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper", e);
			return null;
		}
		catch (ZkException e)
		{
			LOG.error("Failed to create ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper", e);
			return null;
		}
		return fullyQualifiedZNodeName;
	}
	static public String createZNodeIfNotExists(ZkClient zkClient,
			String fullyQualifiedZNodeName, int data, CreateMode createMode)
	{
		if (!ensureParentExists(zkClient, fullyQualifiedZNodeName))
		{
			return null;
		}

		try
		{
			// create the znode
			zkClient.create(fullyQualifiedZNodeName, data, createMode);
			LOG.debug("Created ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper");
		}
		catch (ZkNodeExistsException nee)
		{
			LOG.debug("ZNode " + fullyQualifiedZNodeName);
			return null;
		}
		catch (ZkInterruptedException e)
		{
			LOG.warn("Failed to create ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper", e);
			return null;
		}
		catch (ZkException e)
		{
			LOG.error("Failed to create ZNode " + fullyQualifiedZNodeName
					+ " in ZooKeeper", e);
			return null;
		}
		return fullyQualifiedZNodeName;
	}
	static public boolean writeZNode(ZkClient zkClient,
			String fullyQualifiedZNodeName, byte[] data, int version)
			throws IOException
	{
		try
		{
			Stat stat = zkClient.writeData(fullyQualifiedZNodeName, data,
					version);
			return stat != null;
		}
		catch (ZkInterruptedException e)
		{
			LOG.warn("Failed to write data to ZooKeeper", e);
			throw new IOException(e);
		}
		catch (ZkException e)
		{
			LOG.warn("Failed to write data to ZooKeeper", e);
			throw new IOException(e);
		}
	}
	 
	/**
	 * 通过建立一个短连接， 来获取sequential
	 * 
	 * @author Xukaiqiang 2013-5-3 下午03:10:48
	 * @return
	 */
	public static String getZkSequential(String parent)
	{
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String znode = parent + "/" + uuid;
		ZkClient zk = ZKMgr.getInstance();
		ensureParentExists(zk, parent);
	    ensureExists(zk, znode);
		
		String sequential = zk.createEphemeralSequential(znode, null);
		sequential = sequential.replaceAll(znode, "");
		zk.delete(znode);
		return sequential;
	}

	 
}
