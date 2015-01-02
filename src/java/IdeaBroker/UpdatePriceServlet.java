/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

import javax.servlet.http.HttpServletRequest;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
/**
 *
 * @author joao
 */
@WebServlet(name = "UpdatePriceServlet", urlPatterns = {"/UpdatePriceServlet"})
public class UpdatePriceServlet extends WebSocketServlet {

    private final AtomicInteger sequence = new AtomicInteger(0);
    
    IdeaBrokerBean ideabrokerbean;
    static Set<UpdatePriceServlet.ChatMessageInbound> connections =
            new CopyOnWriteArraySet<UpdatePriceServlet.ChatMessageInbound>();
    HttpSession session;
   
    @Override
    protected StreamInbound createWebSocketInbound(String string, HttpServletRequest request) {
        session = request.getSession(true);
        return new UpdatePriceServlet.ChatMessageInbound(sequence.incrementAndGet());
  
   }
    
    private final class ChatMessageInbound extends MessageInbound {

        private final String nickname;

        private ChatMessageInbound(int id) {
            this.nickname = "Client" + id;
        }

        protected void onOpen(WsOutbound outbound) {
            connections.add(this);
           // ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
           // ideabrokerbean.showAccoes();
        }

        protected void onClose(int status) {
            connections.remove(this);
        }

        protected void onTextMessage(CharBuffer message) throws IOException {
            // never trust the client
            String filteredMessage = filter(message.toString());
            broadcast("&lt;" + nickname + "&gt; " + filteredMessage);
        }

        private void broadcast(String message) { // send message to all
            for (UpdatePriceServlet.ChatMessageInbound connection : connections) {
                try {
                    CharBuffer buffer = CharBuffer.wrap(message);
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException ignore) {
                }
            }
        }

        public String filter(String message) {
            if (message == null) {
                return (null);
            }
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

        protected void onBinaryMessage(ByteBuffer message) throws IOException {
            throw new UnsupportedOperationException("Binary messages not supported.");
        }
    }
    
   
    
}
