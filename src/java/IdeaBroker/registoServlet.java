/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author joao
 */
@WebServlet(name = "registoServlet", urlPatterns = {"/registoServlet"})
public class registoServlet extends HttpServlet {

    IdeaBrokerBean ideabrokerbean;
    
    @Override
    public void init() throws ServletException {
        try {
            ideabrokerbean = new IdeaBrokerBean();
        } catch (Exception e)
        { }
        
        try {
            ideabrokerbean.conectar_RMI();
            System.out.println("----> IdeaBrokerBean: "+ideabrokerbean);
        } catch (RemoteException ex) {
            Logger.getLogger(registoServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(registoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Processar Pedido
        
        HttpSession session = request.getSession(true);
        /*Verifica se ja´se encontra logado*/
        String username_aux = (String) session.getAttribute("username");
        String password_aux = (String) session.getAttribute("password");
        IdeaBrokerBean bean_aux = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
        if((username_aux != null) || (password_aux != null) || (bean_aux != null))
        {
            response.getWriter().println("Já se encontrado logado!");
            return;
        }
        /* ------ */
        
        RequestDispatcher dispatcher;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nome = request.getParameter("nome");
        
        /*Verificar.. */
        if(username.isEmpty()) 
        {
            response.getWriter().println("Campo username é de preenchimento Obrigatório");
            return;
        }
        
        if(password.isEmpty())
        {
            response.getWriter().println("Campo Password é de preenchimento Obrigatório");
            return;
        }
        
        if(nome.isEmpty())
        {
            response.getWriter().println("Campo Password é de preenchimento Obrigatório");
            return;
        }
        
        int resultado = ideabrokerbean.registo(username,password,nome);
        if (resultado == 1)
        {   
            dispatcher = request.getRequestDispatcher("/RegistoAceite.html");
            
        } else {
            dispatcher = request.getRequestDispatcher("/erro_registo.html");
        }
        dispatcher.forward(request, response);       
        
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para tratamento do um pedido para registo";
    }// </editor-fold>
}
