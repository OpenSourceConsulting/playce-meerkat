package stomp;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.WebSocketSession;

public interface StompMessageHandler { 
 
 void afterConnected(WebSocketSession session, StompHeaderAccessor headers); 
 
 void handleMessage(Message<byte[]> message); 
 
 void handleReceipt(String receiptId); 
 
 void handleError(Message<byte[]> message); 
 
 void afterDisconnected(); 
 
}