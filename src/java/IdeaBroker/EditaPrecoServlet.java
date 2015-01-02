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
@WebServlet(name = "EditaPrecoServlet", urlPatterns = {"/EditaPrecoServlet"})
public class EditaPrecoServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         /** Comprar accoes**/
        int id_ideia = -1,
            id_topico = -1;
        Topico topico;
        Ideia ideia;
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        RequestDispatcher dispatcher = null;
        int preco_venda = -1,
            valida = 1;
        
        IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
       
        if((request.getParameter("id_topico") != null) && (!request.getParameter("id_topico").isEmpty()) && ideabrokerbean.ContarLetras(request.getParameter("id_topico")))
        {
           id_topico = Integer.parseInt(request.getParameter("id_topico"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            valida = 0;
        }
        
        if((request.getParameter("id_ideia") != null) && (!request.getParameter("id_ideia").isEmpty()) && ideabrokerbean.ContarLetras(request.getParameter("id_ideia")))
        {
            id_ideia = Integer.parseInt(request.getParameter("id_ideia"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            valida = 0;
        }
        
        if((request.getParameter("preco_venda") != null) && (!request.getParameter("preco_venda").isEmpty()) && ideabrokerbean.ContarLetras(request.getParameter("preco_venda")))
        {
            preco_venda = Integer.parseInt(request.getParameter("preco_venda"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("Accao_Neditada.jsp");
            valida = 0;
        }

       if(valida == 1) //se os parametros estao de acordo com o esperado..
       {
            if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
            {    
                 topico = ideabrokerbean.GetTopico(id_topico);
                 ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
           
                 if(topico != null && ideia != null)
                 {
                     //if(ideabrokerbean.ComprarAccoes(id_topico,id_ideia,username))
                     if(ideabrokerbean.EditarAccao(id_topico,id_ideia,username,preco_venda))
                         dispatcher = request.getRequestDispatcher("Accao_Editada.jsp");
                     else
                         dispatcher = request.getRequestDispatcher("Accao_Neditada.jsp");

                 }
                 else
                    dispatcher = request.getRequestDispatcher("Accao_Neditada.jsp"); 
            }
            else
                dispatcher = request.getRequestDispatcher("Accao_Neditada.jsp");
       }
       
       dispatcher.forward(request,response);
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
