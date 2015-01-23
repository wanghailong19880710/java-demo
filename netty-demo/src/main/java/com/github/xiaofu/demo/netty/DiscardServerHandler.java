package com.github.xiaofu.demo.netty;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class DiscardServerHandler extends SimpleChannelUpstreamHandler  {    
    @Override    
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {    
       System.out.println("服务器接收1："+e.getMessage());    
    }    
     

   @Override    
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {    
        e.getChannel().write("Reply");    
    }  


    @Override   
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {   
    e.getCause().printStackTrace();   
    Channel ch = e.getChannel();   
    ch.close();   
    }   
}  