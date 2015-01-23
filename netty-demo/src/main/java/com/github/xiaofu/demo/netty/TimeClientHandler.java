package com.github.xiaofu.demo.netty;

import java.nio.ByteOrder;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TimeClientHandler extends SimpleChannelUpstreamHandler
{
	private final ChannelBuffer buf = new DynamicChannelBuffer(
			ByteOrder.BIG_ENDIAN, 4);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		ChannelBuffer m = (ChannelBuffer) e.getMessage();
		buf.writeBytes(m);

		if (buf.readableBytes() >= 4)
		{
			long currentTimeMillis = buf.readInt() * 1000L;
			System.out.println(new Date(currentTimeMillis));
//			e.getChannel().close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}