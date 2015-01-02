/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
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
@WebServlet(name = "ApagarIdeiaServlet", urlPatterns = {"/ApagarIdeiaServlet"})
public class ApagarIdeiaServlet extends HttpServlet {    

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
        
        int id_ideia = -1,
            id_topico = -1;
        Topico topico;
        Ideia ideia;
        UUID id_face = null;
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        RequestDispatcher dispatcher = null;
        
        if(request.getParameter("id_face") != null)
                id_face = UUID.fromString(request.getParameter("id_face"));
        
        if((request.getParameter("id_topico") != null) && (!request.getParameter("id_topico").isEmpty()))
        {
           System.out.println("Entra no if e ID_topico == "+request.getParameter("id_topico"));
           id_topico = Integer.parseInt(request.getParameter("id_topico"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            //dispatcher = request.getRequestDispatcher("ideia_aceite.html");
            dispatcher.forward(request,response);          
        }
        
        if((request.getParameter("id_ideia") != null) && (!request.getParameter("id_ideia").isEmpty()))
        {
            System.out.println("Entra no if e ID_ideia == "+request.getParameter("id_ideia"));
            id_ideia = Integer.parseInt(request.getParameter("id_ideia"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            //dispatcher = request.getRequestDispatcher("ideia_aceite.html");
            dispatcher.forward(request,response);
        }

       /** Apagar Ideia **/
       IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
       String username = (String) session.getAttribute("username");
       String password = (String) session.getAttribute("password");
            
       topico = ideabrokerbean.GetTopico(id_topico);
       ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
       
       if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
       {         
            if(topico != null && ideia != null)
            {
                if(id_face == null)
                {
                    if(ideabrokerbean.apagarideia(id_topico,id_ideia,username))
                        dispatcher = request.getRequestDispatcher("ApagarIdeiaSucesso.jsp");
                    else
                        dispatcher = request.getRequestDispatcher("ApagarIdeiaInsucesso.jsp");
                 
                }
                else
                {
                     if(ideabrokerbean.apagarideia(id_topico,id_ideia,username,id_face))
                        dispatcher = request.getRequestDispatcher("ApagarIdeiaSucesso.jsp");
                    else
                        dispatcher = request.getRequestDispatcher("ApagarIdeiaInsucesso.jsp");                   
                }
            }
            else
                dispatcher = request.getRequestDispatcher("ApagarIdeiaInsucesso.jsp");
       }
       else
           dispatcher = request.getRequestDispatcher("ApagarIdeiaInsucesso.jsp");
       
       dispatcher.forward(request,response);
       
        
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
        
        doGet(request,response);
    
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
