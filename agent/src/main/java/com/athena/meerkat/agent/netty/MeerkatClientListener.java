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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MeerkatClientListener implements ChannelFutureListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MeerkatClientListener.class);

	private final MeerkatClient client;
	private final String host;

	public MeerkatClientListener(MeerkatClient client, String host) {
		this.client = client;
		this.host = host;
	}

	public void operationComplete(ChannelFuture future) throws Exception {
		if (future.isSuccess()) {
			future.sync();
		} else {
			// 서버와의 연결을 위해 5초 단위로 재접속을 수행한다.
			final EventLoop loop = future.channel().eventLoop();

			loop.schedule(new Runnable() {
				public void run() {
					LOGGER.debug("Attempt to reconnect within 5 seconds.");
					client.createBootstrap(new Bootstrap(), loop, host);
				}
			}, 5L, TimeUnit.SECONDS);
		}
	}
}
// end of MeerkatClientListener.java