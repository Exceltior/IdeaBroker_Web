/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
@WebServlet(name = "PesquisaBolsa", urlPatterns = {"/PesquisaBolsa"})
public class PesquisaBolsa extends HttpServlet {

 
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, RemoteException {
           
            ArrayList<Accao> accoes; 
            Topico topico; 
            Ideia ideia; 
            int id_topico = -1; 
            int id_ideia = -1; 
            HttpSession session = request.getSession(true);
            
            String username = (String) session.getAttribute("username");
            IdeaBrokerBean ideabrokerbean = new IdeaBrokerBean();
            
            try {
                 ideabrokerbean.conectar_RMI();
            } catch (NotBoundException ex) {
                Logger.getLogger(PesquisaBolsa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(ideabrokerbean.ContarLetras(request.getParameter("id_topico")))
                id_topico = Integer.parseInt(request.getParameter("id_topico"));   
            if(ideabrokerbean.ContarLetras(request.getParameter("id_ideia")))
                id_ideia = Integer.parseInt(request.getParameter("id_ideia"));
            
            System.out.println("Voltemos ao inicio........."+ideabrokerbean.verifica_topico(id_topico) +" ...  "+ideabrokerbean.verifica_ideia(id_topico, id_ideia));
            if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
            {
                topico = ideabrokerbean.GetTopico(id_topico);
                ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
                
                System.out.println("Entrou no  1º if == "+topico.gettema());
                if(topico != null && ideia != null)
                {         
                    accoes = ideabrokerbean.GetAccao(id_topico,id_ideia);
                    if(accoes != null)
                    {
                        String json = new Gson().toJson(accoes);
                        response.getWriter().write(json);
                    }
                    else
                    {
                        String json = new Gson().toJson("Sem Acções");
                        response.getWriter().write(json);
                    }
                }
                else
                {
                    String json = new Gson().toJson("Sem Acções");
                    response.getWriter().write(json);
                }
            }
            else
            {
               String json = new Gson().toJson("Sem Acções");
               response.getWriter().write(json);
            }
        
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
