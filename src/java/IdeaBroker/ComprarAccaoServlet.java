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
@WebServlet(name = "ComprarAccaoServlet", urlPatterns = {"/ComprarAccaoServlet"})
public class ComprarAccaoServlet extends HttpServlet {


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

    
   public boolean containsOnlyNumbers(String str) {  
       System.out.println("Vou verifica se ["+str+"] e so numero");
       if (str == null || str.length() == 0)  
            return false;  

        for (int i = 0; i < str.length(); i++) {  

            if (!Character.isDigit(str.charAt(i)))  
                return false;  
        }  

        return true;  
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
        
        /** Comprar accoes**/
        int id_ideia = -1,
            id_topico = -1;
        Topico topico;
        Ideia ideia;
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        RequestDispatcher dispatcher = null;
        int numero_accoes = -1,
            preco_compra = -1,
            preco_venda = -1,
            valida = 1;
        
        if((request.getParameter("id_topico") != null) && (!request.getParameter("id_topico").isEmpty()) && containsOnlyNumbers(request.getParameter("id_topico")))
        {
           System.out.println("Entra no if e ID_topico == "+request.getParameter("id_topico"));
           id_topico = Integer.parseInt(request.getParameter("id_topico"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            valida = 0;
        }
        
        if((request.getParameter("id_ideia") != null) && (!request.getParameter("id_ideia").isEmpty()) && containsOnlyNumbers(request.getParameter("id_ideia")))
        {
            id_ideia = Integer.parseInt(request.getParameter("id_ideia"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("erro_interno.html");
            valida = 0;
        }

        if((request.getParameter("numero_accoes") != null) && (!request.getParameter("numero_accoes").isEmpty()) && containsOnlyNumbers(request.getParameter("numero_accoes")))
        {
            numero_accoes = Integer.parseInt(request.getParameter("numero_accoes"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp");
            valida = 0;
        }
        
        if((request.getParameter("preco_compra") != null) && (!request.getParameter("preco_compra").isEmpty()) && containsOnlyNumbers(request.getParameter("preco_compra")))
        {
            preco_compra = Integer.parseInt(request.getParameter("preco_compra"));
        }
        else
        {   
            dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp");
            valida = 0;
        }
        
        if((request.getParameter("preco_venda") != null) && (!request.getParameter("preco_venda").isEmpty()) && containsOnlyNumbers(request.getParameter("preco_venda")))
        {
            preco_venda = Integer.parseInt(request.getParameter("preco_venda"));
        }
        else
        {
            dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp");
            valida = 0;
        }
        System.out.println("Entra1");
       /** Apagar Ideia **/
       IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
       String username = (String) session.getAttribute("username");
       String password = (String) session.getAttribute("password");
       
       //System.out.println("[Servlet] Sou username =="+username+ "\n[Servlet] Token == "+ideabrokerbean.getOAuth());
      
       if(valida == 1) //se os parametros estao de acordo com o esperado..
       {
            topico = ideabrokerbean.GetTopico(id_topico);
            ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
            if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
            {         
                 if(topico != null && ideia != null)
                 {
                     if(ideabrokerbean.ComprarAccoes(id_topico,id_ideia,username,numero_accoes,preco_venda,preco_compra))
                         dispatcher = request.getRequestDispatcher("Compra_efectuada.jsp");
                     else
                         dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp");
                 }
                 else
                    dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp"); 
            }
            else
                dispatcher = request.getRequestDispatcher("Compra_Nefectuada.jsp");
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
        return "Comprar Acções...";
    }// </editor-fold>

}
