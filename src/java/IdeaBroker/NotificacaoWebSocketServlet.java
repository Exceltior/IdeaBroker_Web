/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

/**
 *
 * @author joao
 */
@WebServlet(name = "NotificacaoWebSocketServlet", urlPatterns = {"/NotificacaoWebSocketServlet"})
public class NotificacaoWebSocketServlet extends WebSocketServlet {

    
    static Set<ChatMessageInbound> connections = new CopyOnWriteArraySet<ChatMessageInbound>();
    static Hashtable outBounds=new Hashtable();
    
    @Override
    protected StreamInbound createWebSocketInbound(String string, HttpServletRequest request) {
                return new ChatMessageInbound(request);  
    }
    
    private final class ChatMessageInbound extends MessageInbound {

		private final String nickname;

		private ChatMessageInbound(HttpServletRequest request) {
                    HttpSession session=request.getSession(true);
                    String username=(String)session.getAttribute("username");
		    this.nickname = username;
		}

                @Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
                        NotificacaoWebSocketServlet.outBounds.put(nickname, outbound);
                        System.out.println("outIn: "+NotificacaoWebSocketServlet.outBounds+" sizeIn:"+NotificacaoWebSocketServlet.outBounds.size());
                        //broadcast("[" + nickname + "]");
		}

                @Override
		protected void onClose(int status) {
			connections.remove(this);
			//broadcast("[" + nickname + "]");
		}

                @Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			// never trust the client
			String filteredMessage = filter(message.toString());
			broadcast("&lt;" + nickname + "&gt; " + filteredMessage);
		}

		private void broadcast(String message) { // send message to all
			for (ChatMessageInbound connection : connections) {
				try {
					CharBuffer buffer = CharBuffer.wrap(message);
					connection.getWsOutbound().writeTextMessage(buffer);
				} catch (IOException ignore) {}
			}
		}

		public String filter(String message) {
			if (message == null)
				return (null);
			// filter characters that are sensitive in HTML
			char content[] = new char[message.length()];
			message.getChars(0, message.length(), content, 0);
			StringBuilder result = new StringBuilder(content.length + 50);
			for (int i = 0; i < content.length; i++) {
				switch (content[i]) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '"':
					result.append("&quot;");
					break;
				default:
					result.append(content[i]);
				}
			}
			return (result.toString());
		}

                @Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			throw new UnsupportedOperationException("Binary messages not supported.");
		}
	}
}
