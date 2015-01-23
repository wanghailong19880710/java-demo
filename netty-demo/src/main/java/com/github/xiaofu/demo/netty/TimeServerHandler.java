package com.github.xiaofu.demo.netty;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TimeServerHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		System.out.println("totalCounts:"+buf.readableBytes());
		System.out.println("buffer:"+buf.toString());
		 
		while (buf.readable()) {
			System.out.println(buf.readInt());
			System.out.println(buf.readLong());
			System.out.println(buf.readInt());
			System.out.println(buf.readInt());
			System.out.println(buf.readInt());
			ByteBuffer name = ByteBuffer.allocate(2048);
			while (buf.readable()) {
				byte split =  buf.readByte();
				if ((char)split == '\r')
				{
					buf.readByte();
					break;
				}
				else
					name.put(split);
			}
			name.flip();
			System.out.println(Charset.forName("UTF-8").decode(name));
			ByteBuffer extension = ByteBuffer.allocate(1024);
			while(buf.readable())
			{
				byte split =  buf.readByte();
				if (split == '\r')
				{
					buf.readByte();
					break;
				}
				else
					extension.put(split);
			}
			extension.flip();
			System.out.println(Charset.forName("UTF-8").decode(extension));
			System.out.println("contents:"+buf.readableBytes());
			ByteBuffer contents=ByteBuffer.allocate(1024);
			while(buf.readable())
			{
				contents.put(buf.readByte());
			}
			contents.flip();
			System.out.println(Charset.forName("UTF-8").decode(contents));
		 
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Channel ch = e.getChannel();
		ChannelBuffer time = ChannelBuffers.buffer(4);
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

		ChannelFuture f = ch.write(time);

		f.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) {
				// Channel ch = future.getChannel();
				// ch.close();
				System.out.println("write complete");
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		Channel ch = e.getChannel();
		ch.close();
	}
}
