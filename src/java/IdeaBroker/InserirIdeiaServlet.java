/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IdeaBroker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
/*
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

/**
 *
 * @author joao
 */
@WebServlet(name = "InserirIdeiaServlet", urlPatterns = {"/InserirIdeiaServlet"})
@MultipartConfig
public class InserirIdeiaServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;
    
    
    public void init()
    {
        filePath = "/home/joao/Área\\ de\\ Trabalho/";
    }
    
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
        throw new ServletException("Metodo Get usado com  "+getClass().getName()+" sendo o metodo Post o requirido.");
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
       
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(true);
        
        RequestDispatcher dispatcher = null;
        
        String textData = null;
        String fileName = null;
        int investimento = -1,
            id_topico = -1;
  
        try{ // Para apagar!!!!
            
      /*  isMultipart = ServletFileUpload.isMultipartContent(request);
        java.io.PrintWriter out = response.getWriter( );
        if( !isMultipart ){
          System.out.println("Multipart error ");
          return;
        }
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(maxMemSize);
        factory.setRepository(new File("/home/joao/Área\\ de\\ Trabalho/"));
        
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        upload.setSizeMax(maxFileSize);
        
        try{
            
            List fileItems = upload.parseRequest(request);
            
            Iterator i = fileItems.iterator();
            
            while( i.hasNext())
            {
                FileItem fi = (FileItem)i.next();
                
                if( !fi.isFormField() )
                {
                    fileName = fi.getName();
                    if(!fileName.isEmpty())
                    {
                         if( fileName.lastIndexOf("\\") >= 0 ){
                            file = new File( filePath + 
                            fileName.substring( fileName.lastIndexOf("\\"))) ;
                         }else{
                            file = new File( filePath + 
                            fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                         }
                         fi.write(file);
                    }

                }
                else
                {
                    String inputName = (String) fi.getFieldName();
                    if (inputName.equals("descricao"))
                        textData = (String) fi.getString();
                    else if(inputName.equals("investimento"))
                        investimento = Integer.parseInt(fi.getString());
                    else if(inputName.equals("id_topico"))
                        id_topico = Integer.parseInt(fi.getString());
   
                }
            }*/
        
             /** Criar Ideia **/
            IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
            String username = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            //System.out.println("[Servlet] Sou username =="+username+ "\n[Servlet] Token == "+ideabrokerbean.getOAuth());
            textData = request.getParameter("descricao");
            System.out.println("Descricao == "+textData);
            
            if((request.getParameter("investimento") != null) && (!request.getParameter("investimento").isEmpty()) && (ideabrokerbean.ContarLetras(request.getParameter("investimento"))))
            {
                System.out.println("Entra no if e investimento == "+request.getParameter("investimento"));
                investimento = Integer.parseInt(request.getParameter("investimento"));
            }

            filePath = null;
            if((request.getParameter("id_topico") != null) && (!request.getParameter("id_topico").isEmpty()))
                id_topico = Integer.parseInt(request.getParameter("id_topico"));

            if((textData != null && !textData.isEmpty()))
            {
                if(investimento != -1)
                {
                    
                    if((fileName != null) && (!fileName.isEmpty()))
                    {                        
                        //chama funcao que cria a ideia sem anexo
                        if(ideabrokerbean.criaIdeia(username,textData,null,id_topico,investimento))
                            dispatcher = request.getRequestDispatcher("ideia_aceite.html");
                        else
                            dispatcher = request.getRequestDispatcher("erro_ideia.html");
                        
                        ideabrokerbean.print_service();
                    }
                    else
                    {
                        //chama funcao que cria a ideia com anexo
                        if(ideabrokerbean.criaIdeia(username,textData,null,id_topico,investimento))
                            dispatcher = request.getRequestDispatcher("ideia_aceite.html");
                        else
                            dispatcher = request.getRequestDispatcher("erro_ideia.html");
                    }
                }   
            }
            
            //dispatcher = request.getRequestDispatcher("ideia_aceite.html");
            dispatcher.forward(request,response);
            
        }catch(Exception ex) {
               System.out.println(ex);
        }
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Tratamento do request do Criar Ideia.";
    }



}
