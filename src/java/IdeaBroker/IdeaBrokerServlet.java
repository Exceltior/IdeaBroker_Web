/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

@WebServlet(name = "IdeaBrokerServlet", urlPatterns = {"/IdeaBrokerServlet"})
public class IdeaBrokerServlet extends HttpServlet {

    private static final Token EMPTY_TOKEN = null;
    IdeaBrokerBean ideiabrokerBean;
    
    @Override
    public void init() throws ServletException
    {
        try{
            ideiabrokerBean = new IdeaBrokerBean();
        }catch(Exception e){
        }
        try {
            ideiabrokerBean.conectar_RMI();
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(IdeaBrokerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
/**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {       
        RequestDispatcher dispatcher = null;
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //password = new Integer(password.hashCode()).toString();
        
        int result = ideiabrokerBean.login(username,password);
        if(result == 1)
        {
            //Efectuar Login
            HttpSession session = request.getSession(true);
          /*session.setAttribute("username",username);
            session.setAttribute("password",password);
            session.setAttribute("ideabrokerbean", ideiabrokerBean);*/
            
            getServletContext().setAttribute("ideabrokerbean", ideiabrokerBean);
            getServletContext().setAttribute("username", username);
            getServletContext().setAttribute("password", password);
            
            String apiKey = "181330185393791";
            
            String apiSecret = "76bc212c4544ce557d6eb2222512a9cf";
            OAuthService service = new ServiceBuilder()
                                        .provider(FacebookApi.class)
                                        .apiKey(apiKey)
                                        .apiSecret(apiSecret)
                                        .scope("publish_stream,publish_actions,read_stream,create_event,manage_pages")
                                        .callback("http://localhost:8080/IdeiaBroker_Web/FacebookServlet")
                                        .build();
                                
            System.out.println("Vou enviar um novo service.... ");
            
            ideiabrokerBean.setService(service);
            
            System.out.println("----------------------------");
            
            String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
                        
            response.sendRedirect(authorizationUrl);
           // response.sendRedirect("inicio.jsp");
            
        }
        else
        {
            dispatcher = request.getRequestDispatcher("/login_errado.html");
            dispatcher.forward(request, response);
        }
                
        //dispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Controllet da aplicação IdeaBroker...";
    }// </editor-fold>
}
