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

/**
 *
 * @author joao
 */
@WebServlet(name = "InserirTopicoServlet", urlPatterns = {"/InserirTopicoServlet"})
public class InserirTopicoServlet extends HttpServlet {

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
       
       int resultado = 0;
       request.setCharacterEncoding("UTF-8");
       HttpSession session = request.getSession(true);
       RequestDispatcher dispatcher;
        
       /** Criar Topico **/
       IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
       String username = (String) session.getAttribute("username");
       String password = (String) session.getAttribute("password");

       if (request.getParameter("topico") != null) {
           
           System.out.println("Tem algo....");
           String novo_topico = request.getParameter("topico");
           resultado = ideabrokerbean.criarTopico(username,novo_topico);
           if(resultado == 1)
           {
                dispatcher = request.getRequestDispatcher("/inicio.jsp");
                dispatcher.forward(request, response);
           }
           else
           {
                dispatcher = request.getRequestDispatcher("/erro_criarTopico.html");
                dispatcher.forward(request, response);
           }
        }
        else
        {
            dispatcher = request.getRequestDispatcher("/erro_criarTopico.html");
            dispatcher.forward(request, response);           
        }

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
        doGet(request, response);
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
