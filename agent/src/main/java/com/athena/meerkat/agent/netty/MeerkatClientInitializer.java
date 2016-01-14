/*
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.meerkat.agent.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("meerkatClientInitializer")
public class MeerkatClientInitializer extends ChannelInitializer<SocketChannel>
		implements InitializingBean {

	@Inject
	@Named("meerkatClientHandler")
	private MeerkatClientHandler handler;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MeerkatClientHandler.class);

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		// Create a default pipeline implementation.
		LOGGER.debug("**************************Tran - Init *****************************");
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE,
				ClassResolvers.cacheDisabled(null)));
		pipeline.addLast("encoder", new ObjectEncoder());
		pipeline.addLast("handler", handler);
	}

	public void afterPropertiesSet() throws Exception {

		LOGGER.debug("!!!!!!!!!!meerkatClientInitializer inited !!!!!!!!!!!!!\n\n\n");
		System.out
				.println("!!!!!!!!!!meerkatClientInitializer inited !!!!!!!!!!!!!\n\n\n");

	}

}
// end of MeerkatClientInitializer.java