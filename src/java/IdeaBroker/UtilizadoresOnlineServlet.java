/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.websocket.MessageInbound;

/**
 *
 * @author joao
 */
@WebServlet(name = "UtilizadoresOnlineServlet", urlPatterns = {"/UtilizadoresOnlineServlet"})
public class UtilizadoresOnlineServlet extends HttpServlet {
    
    private final AtomicInteger sequence = new AtomicInteger(0);
    static Set<UtilizadoresOnlineServlet.MensagemChat> connections =
            new CopyOnWriteArraySet<UtilizadoresOnlineServlet.MensagemChat>();
    IdeaBrokerBean ideiabrokerbean;
    HttpSession session;
    
    private final class MensagemChat extends MessageInbound
    {

        @Override
        protected void onBinaryMessage(ByteBuffer bb) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected void onTextMessage(CharBuffer cb) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
