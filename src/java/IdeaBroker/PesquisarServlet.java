/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "PesquisarServlet", urlPatterns = {"/PesquisarServlet"})
public class PesquisarServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PesquisarServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PesquisarServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
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

        ArrayList<Ideia> Ideias = new ArrayList<Ideia>();
        
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        
        RequestDispatcher dispatcher = null;
        String nome_topico = null,
               autor_ideia = null;
        int valor_ideia = -1;
        
        IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
        String username = (String) session.getAttribute("username");
        
      /*  if((request.getParameter("nome_topico") != null) && (!request.getParameter("nome_topico").isEmpty()))
        {
            System.out.println("Entra no if e Nome topico == "+request.getParameter("nome_topico"));
            nome_topico = request.getParameter("nome_topico");
        }*/
        if((request.getParameter("autor_ideia") != null) && (!request.getParameter("autor_ideia").isEmpty()))
        {
            System.out.println("Entra no if e Nome topico == "+request.getParameter("autor_ideia"));
            autor_ideia = request.getParameter("autor_ideia");
        }
        
        if((request.getParameter("valor_ideia") != null) && (!request.getParameter("valor_ideia").isEmpty()) && (ideabrokerbean.ContarLetras(request.getParameter("valor_ideia"))))
        {
            System.out.println("Entra no if e Nome topico == "+request.getParameter("valor_ideia"));
            valor_ideia = Integer.parseInt(request.getParameter("valor_ideia"));
        }
        //System.out.println("Autor Ideia == "+autor_ideia);
        
        Ideias = ideabrokerbean.GetPesquisa(autor_ideia,valor_ideia);
        if((Ideias != null) && (!Ideias.isEmpty()))
        {
            String json = new Gson().toJson(Ideias);
            response.getWriter().write(json);
        }
        else
        {
            String json = new Gson().toJson("Sem ideias");
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
        return "Pesquisar Servlet";
    }// </editor-fold>

}
