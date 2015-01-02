/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author joao
 */
@WebServlet(name = "FacebookServlet", urlPatterns = {"/FacebookServlet"})
public class FacebookServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        String code = request.getParameter("code");
        HttpSession session = request.getSession(true);
        
        IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) getServletContext().getAttribute("ideabrokerbean");
        String username = (String) getServletContext().getAttribute("username");
        String password = (String) getServletContext().getAttribute("password");
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("ideabrokerbean", ideabrokerbean);
        
        /* Faz o restore da session da camada application para  a session */
        if(code != null)
        {
            OAuthService service = ideabrokerbean.getService();
            System.out.println("Service == "+service);
            Verifier verifier = new Verifier(code);
            Token accessToken = service.getAccessToken(null, verifier);
            System.out.println("Username == "+username+" token == "+accessToken);
           
            String accessTokenv2 = accessToken.getToken();
            System.out.println("Token em string  == "+accessTokenv2);
            
            ideabrokerbean.setOAuth(username,accessTokenv2);
            //ideabrokerbean.setID(1);
            
            System.out.println("Username == "+username);
            System.out.println("E o meu token == "+accessToken);
           // response.sendRedirect("inicio.jsp");
       
            RequestDispatcher dispatcher;
            
            dispatcher = request.getRequestDispatcher("/inicio.jsp");
            dispatcher.forward(request,response); 
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
