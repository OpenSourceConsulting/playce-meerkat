/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * BongJin Kwon		2016. 4. 5.		First Draft.
 */
package stomp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.athena.meerkat.agent.monitoring.websocket.StompTextMessageBuilder;

public class StompTest {

	public static void main(String[] args) throws Exception {

		// URI uri = new URI(
		// "stomp+ws://localhost:8080/controller/monitor/hello" );
		// System.out.println(uri.getScheme());

		// return;

		test();

	}

	public static void test() throws Exception {

		TextMessage message1 = StompTextMessageBuilder.create(StompCommand.SUBSCRIBE).headers("id:subs1", "destination:/topic/greetings").build();
		TextMessage message = StompTextMessageBuilder.create(StompCommand.SEND).headers("destination:/app/monitor/hello").body("{\"name\":\"ddd\"}").build();


		WebSocketHandler wsHandler = new TestClientWebSocketHandler(0, message1, message);

		StandardWebSocketClient wsClient = new StandardWebSocketClient();

		WebSocketSession session = wsClient.doHandshake(wsHandler, "ws://localhost:8080/controller/monitor/endpoint").get();

		// session.s

		try {
			Thread.sleep(3000);
		} finally {
			disconnect(session);
		}
	}

	public static void disconnect(WebSocketSession session) {
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		Message<byte[]> message = MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();
		sendInternal(session, message);
		try {
			session.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void sendInternal(WebSocketSession session, Message<byte[]> message) {
		StompEncoder encoder = new StompEncoder();
		byte[] bytes = encoder.encode(message);
		try {
			session.sendMessage(new TextMessage(new String(bytes, Charset
					.forName("UTF-8"))));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static class TestClientWebSocketHandler extends
			TextWebSocketHandler {

		private final TextMessage[] messagesToSend;

		private final int expected;

		private final List<TextMessage> actual = new CopyOnWriteArrayList<>();

		private final CountDownLatch latch;

		public TestClientWebSocketHandler(int expectedNumberOfMessages,
				TextMessage... messagesToSend) {
			this.messagesToSend = messagesToSend;
			this.expected = expectedNumberOfMessages;
			this.latch = new CountDownLatch(this.expected);
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session)
				throws Exception {

			System.out.println("Connected!!");

			for (TextMessage message : this.messagesToSend) {
				session.sendMessage(message);
				System.out.println(">>>>> " + message.getPayload());
			}
		}

		@Override
		protected void handleTextMessage(WebSocketSession session,
				TextMessage message) throws Exception {

			System.out.println("<<<< : " + message.getPayload());

			this.actual.add(message);
			this.latch.countDown();
		}

	}

}
// end of StompClient.java