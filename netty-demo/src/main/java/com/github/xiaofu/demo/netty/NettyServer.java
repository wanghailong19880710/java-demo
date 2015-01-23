/**
 * @ProjectName: java-demo
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014年12月24日 上午10:43:16
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014年12月24日 上午10:43:16
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014年12月24日
 * @modify by reason:{方法名}:{原因}
 */
public class NettyServer
{ 
	/**
	 * @author fulaihua 2014年12月24日 上午10:43:17
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		 
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory()
		{
			public ChannelPipeline getPipeline()
			{
				ChannelPipeline pipeline = Channels.pipeline();
			 
				pipeline.addLast("handler", new TimeServerHandler());
				return pipeline;
			}
		});
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(8888));
	}
}
