<!--
Autores:
 Bruno Miguel Oliveira Rolo nº 2010131200 brolo@student.dei.uc.pt
 Joao Artur Ventura Valerio Nobre nº 2010131516 janobre@student.dei.uc.pt


-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="IdeaBroker.IdeaBrokerBean" %>
<%
    //Verifica se esta logado
    String username = (String) session.getAttribute("username");
    String password = (String) session.getAttribute("password");
    
    if((username != null) || (password != null))
    {
        /*RequestDispatcher dispatcher;
        dispatcher  = request.getRequestDispatcher("/");
        
        dispatcher.forward(request, response);*/

    }
    
        
    if (username == null)
    {
    %>
        <jsp:forward page="index.jsp"></jsp:forward>
    <%
    } 
    %>