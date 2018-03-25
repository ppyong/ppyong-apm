package com.devluff.server.network;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devluff.server.ApplicationConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class NetworkServerThread extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkServerThread.class);
	
	private CountDownLatch oLatch;
	private EventLoopGroup oParentGroup; 
	private EventLoopGroup oChildGroup; 
	
	@Autowired private ApplicationConfig oApplicationConfig;
	
	public NetworkServerThread() {
		oParentGroup = new NioEventLoopGroup(1); // 접속에 대한 처리
		oChildGroup = new NioEventLoopGroup();   // 로직에 대한 처리
	}
	
	public boolean initialize() {
		return true;
	}
	
	public void setCountDownLatch(CountDownLatch oLatch) {
		this.oLatch = oLatch;
	}
	
	public void run() {
		try {
			ServerBootstrap oServer = new ServerBootstrap();
			oServer.group(oParentGroup, oChildGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					ChannelPipeline cp = sc.pipeline();
					cp.addLast(new NetworkServerHandler());
				}
			});
			ChannelFuture cf = oServer.bind(oApplicationConfig.getServerPORT()).sync();
			cf.channel().closeFuture().sync();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			oParentGroup.shutdownGracefully();
			oChildGroup.shutdownGracefully();
		}
		// Thread 를 모두 종료하기 위함.
		oLatch.countDown();
	}
	
	public void terminate() {
		oParentGroup.shutdownGracefully();
		oChildGroup.shutdownGracefully();
	}
}
