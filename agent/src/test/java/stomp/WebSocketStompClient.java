package stomp;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

//@Component
public class WebSocketStompClient implements InitializingBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStompClient.class);

	//@Value("${meerkat.agent.server.topic}")
	private String topic;
	
	//@Value("${meerkat.agent.server.endpoint}")
	private String endpoint;
	
	//@Value("${meerkat.agent.server.app.dest:/app/monitor/hello}")
	private String appDestination;
	
	//@Autowired
	private StompMessageHandler stompMessageHandler;
	
	private final URI uri;

	private final WebSocketHttpHeaders headers;

	private final WebSocketClient webSocketClient = new StandardWebSocketClient();

	private MessageConverter messageConverter;

	public WebSocketStompClient(URI uri, WebSocketHttpHeaders headers) {
		this.uri = uri;
		this.headers = headers;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public void connect() {
		try {
			StompWebSocketHandler webSocketHandler = new StompWebSocketHandler(stompMessageHandler, this.messageConverter);
			this.webSocketClient.doHandshake(webSocketHandler, this.headers, this.uri).get();
			LOGGER.info("connecting to {}", endpoint);
			
			//this.webSocketClient.doHandshake(new StompWebSocketHandler(topic), endpoint).get();
			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static class StompWebSocketHandler extends AbstractWebSocketHandler {

		private static final Charset UTF_8 = Charset.forName("UTF-8");

		private final StompMessageHandler stompMessageHandler;

		private final MessageConverter messageConverter;

		private final StompEncoder encoder = new StompEncoder();

		private final StompDecoder decoder = new StompDecoder();

		private StompWebSocketHandler(StompMessageHandler delegate) {
			this(delegate, new MappingJackson2MessageConverter());
		}

		private StompWebSocketHandler(StompMessageHandler delegate,
				MessageConverter messageConverter) {
			this.stompMessageHandler = delegate;
			this.messageConverter = messageConverter;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session)
				throws Exception {

			StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
			headers.setAcceptVersion("1.1,1.2");
			headers.setHeartbeat(0, 0);
			Message<byte[]> message = MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();

			TextMessage textMessage = new TextMessage(new String(this.encoder.encode(message), UTF_8));
			session.sendMessage(textMessage);
		}

		@Override
		protected void handleTextMessage(WebSocketSession session,
				TextMessage textMessage) throws Exception {

			ByteBuffer payload = ByteBuffer.wrap(textMessage.getPayload().getBytes(UTF_8));
			List<Message<byte[]>> messages = this.decoder.decode(payload);

			for (Message message : messages) {
				StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
				
				if (StompCommand.CONNECTED.equals(headers.getCommand())) {

					this.stompMessageHandler.afterConnected(session, headers);
				} else if (StompCommand.MESSAGE.equals(headers.getCommand())) {
					this.stompMessageHandler.handleMessage(message);
				} else if (StompCommand.RECEIPT.equals(headers.getCommand())) {
					this.stompMessageHandler.handleReceipt(headers
							.getReceiptId());
				} else if (StompCommand.ERROR.equals(headers.getCommand())) {
					this.stompMessageHandler.handleError(message);
				} else if (StompCommand.ERROR.equals(headers.getCommand())) {
					this.stompMessageHandler.afterDisconnected();
				} else {
					LOGGER.debug("Unhandled message " + message);
				}
			}
		}

		@Override
		public void handleTransportError(WebSocketSession session,
				Throwable exception) throws Exception {
			LOGGER.error("WebSocket transport error", exception);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session,
				CloseStatus status) throws Exception {
			this.stompMessageHandler.afterDisconnected();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}