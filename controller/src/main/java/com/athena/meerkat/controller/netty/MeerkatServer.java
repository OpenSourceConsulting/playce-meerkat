/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.meerkat.controller.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.athena.meerkat.common.provider.AppContext;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("meerkatServer")
public class MeerkatServer implements InitializingBean, ApplicationContextAware {
	
    @Value("${listen.port}")
    private int port;

    @Inject
    @Named("bossGroup")
    private EventLoopGroup bossGroup;

    @Inject
    @Named("workerGroup")
    private EventLoopGroup workerGroup;

    @Inject
    @Named("meerkatServerInitializer")
    private MeerkatServerInitializer initializer;

    
    private Channel channel;
	
	//@PostConstruct
	public void start() throws Exception {
		
		new Thread() {
			@Override
			public void run() {
		        try {
					ServerBootstrap b = new ServerBootstrap();
			        b.group(bossGroup, workerGroup)
			         .channel(NioServerSocketChannel.class)
			         .handler(new LoggingHandler(LogLevel.WARN))
			         .childHandler(initializer);

			        // Bind and start to accept incoming connections.
					channel = b.bind(port).sync().channel().closeFuture().sync().channel();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@PreDestroy
	public void stop() {
		if (channel != null) {
			channel.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread() {
			@Override
			public void run() {
		        try {
					ServerBootstrap b = new ServerBootstrap();
			        b.group(bossGroup, workerGroup)
			         .channel(NioServerSocketChannel.class)
			         .handler(new LoggingHandler(LogLevel.WARN))
			         .childHandler(initializer);

			        // Bind and start to accept incoming connections.
					channel = b.bind(port).sync().channel().closeFuture().sync().channel();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		if (AppContext.getApplicationContext() == null) {
			AppContext.setApplicationContext(ctx);
		}
	}
}
//end of MeerkatServer.java