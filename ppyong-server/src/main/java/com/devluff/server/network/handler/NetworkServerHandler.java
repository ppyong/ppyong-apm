package com.devluff.server.network.handler;

import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.devluff.server.ApplicationManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class NetworkServerHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(NetworkServerHandler.class);

    @Autowired
    private ApplicationManager oApplicationManager; 
    
    // Client가 접하고 발생하는 이벤트 
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception{
        String strIp = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        logger.info("Client({}) is connected", strIp);
        
        if(oApplicationManager.oMapCurrentConnectedAgent.containsKey(strIp)) {
            logger.debug("Client({}) is removed to connected agent map...", strIp);
            oApplicationManager.oMapCurrentConnectedAgent.remove(strIp);
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) 
            throws Exception{
        String strIp = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        logger.info("Client({}) is disconnected", strIp);

        if(!oApplicationManager.oMapCurrentConnectedAgent.containsKey(strIp)) {
            logger.debug("Client({}) is added to connected agent map...", strIp);
            oApplicationManager.oMapCurrentConnectedAgent.put(strIp, ctx);
        }
    }
    
    // Client로부터의 메시지를 받는 이벤트 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf oReadMessage = (ByteBuf)msg;
    }
    
    //  Client로부터 메시지 전송이 끝났을 때 발생하는 이벤트 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        
    }
    
    // Client와의 통신중 Exception이 발생했을 때 이벤
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        //ctx.close();
    }
}
