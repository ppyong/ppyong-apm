package com.devluff.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.devluff.server.network.handler.NetworkClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

@Service
public class NetworkClient{
    
	private static final Logger logger = LoggerFactory.getLogger(NetworkClient.class);
	
	private EventLoopGroup oGroup;
	
	public NetworkClient() {
	    oGroup = new NioEventLoopGroup();
	}
	
	public void sendMessageToClient(String strIP, int nPort) {
	    try{
            Bootstrap b = new Bootstrap();
            b.group(oGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    ChannelPipeline cp = sc.pipeline();
                    cp.addLast(new NetworkClientHandler());
                }
            });

            ChannelFuture cf = b.connect(strIP, nPort).sync();
            cf.channel().closeFuture().sync();
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally{
            oGroup.shutdownGracefully();
        }
	}
}
