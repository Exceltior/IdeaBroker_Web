<%@page import="java.util.UUID"%>
<!--
Autores:
 Bruno Miguel Oliveira Rolo nº 2010131200 brolo@student.dei.uc.pt
 Joao Artur Ventura Valerio Nobre nº 2010131516 janobre@student.dei.uc.pt
-->
<%@page import="java.util.ArrayList"%>
<%@page import="IdeaBroker.IdeaBrokerBean"%>
<%@page import="IdeaBroker.Topico"%>
<%@page import="IdeaBroker.Ideia"%>
<jsp:useBean id="ideabrokerbean" class="IdeaBroker.IdeaBrokerBean" scope="session" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! Topico topico; %>
<%! Ideia ideia; %>
<%! int id_topico; %>
<%! int id_ideia; %>

<!DOCTYPE html>
<jsp:include page="verifica.jsp"></jsp:include>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title>.:IdeaBroker:. - Listar Ideia</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="assets/css/tabela.css" rel="stylesheet">
    <script type="text/javascript" charset="utf-8">
        function apagarideia()
        {
            
        }
        
        function rejeita()
        {
            window.location = "../listarideia.jsp";
        }
    
    </script>
  </head>

  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#"></a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="inicio.jsp">Inicio</a></li>
              <li><a href="Pesquisar.jsp">Pesquisar</a></li>
              <li><a href="halloffame.jsp">Hall of Fame</a></li>
              <li class="dropdown">
                
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Gerir <b class="caret"></b></a>
                <ul class="dropdown-menu">
                         
                  <li class="nav-header">Perfil</li>
                  <li> <a href="profile.jsp">Minha Conta</a></li>
                  <li> <a href="portofolio.jsp">Portofolio</a> </li>
                 search
                </ul>
              </li>
            </ul> 
            <div class="navbar-form pull-right"> 
                    <ul class="nav">
                    <li style="line-height:15px"> <br>
                    <font color="white" > 
                        Bem Vindo,  <%= session.getAttribute("username") %>
                    <a href="./logout" >  [Logout]</a>
                    </font>
                    </li>
                    </ul>
            </div> 
               </div><!--/.nav-collapse -->
          </div>
      </div>
    </div>

  <div class="container">
      <%
            String username = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            UUID id_face = null;
            id_topico = Integer.parseInt(request.getParameter("id_t"));   
            id_ideia = Integer.parseInt(request.getParameter("id_i"));
            if(request.getParameter("id_ui") != null)
                id_face = UUID.fromString(request.getParameter("id_ui"));
            
            System.out.println("Voltemos ao inicio........."+ideabrokerbean.verifica_topico(id_topico) +" ...  "+ideabrokerbean.verifica_ideia(id_topico, id_ideia));
            if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
            {
                topico = ideabrokerbean.GetTopico(id_topico);
                ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
                
                System.out.println("Entrou no  1º if == "+topico.gettema());
                if(topico != null && ideia != null)
                {                    
                    if(ideabrokerbean.PermissaoApagar(ideia,username))
                    {     
      %>
      <h3 align="center">IdeaBroker</h3>
      <hr>        
      <!-- Example row of columns -->
    <div class="container marketing">  
    	<fieldset align="center">
       
         <table border="0" align="center" summary="Lista de Utilizadores">
                <tr>
                    <td colspan="2"><h4>Pretende Apagar Ideia [<%= ideia.getidideia() %>] do T&oacute;pico [<%= topico.gettema() %>] ? </h4>(Este processo &eacute; irreversivel)<br><br></td> 
                </tr>
                <tr>
                    <td>
                       <form action="ApagarIdeiaServlet" method="get" name="login" class="navbar-form pull-center"> 
                           <input type="hidden" value="<%= topico.getidtopico() %>" name="id_topico" id="id_topico">
                           <input type="hidden" value="<%= ideia.getidideia() %>" name="id_ideia" id="id_ideia">
                           <input type="hidden" value="<%= id_face %>" name="id_face" id="id_face">
                           <input type="submit" value="Sim" onclick="apagarideia" class="btn btn-primary btn-medium btn-danger" />
                       </form>
                    </td>
                    <td>
                        <a href="listarideia.jsp?id=<%= id_topico %>"><input type="button" name="nao" id="nao" value="Não" onclick="rejeita" class="btn btn-primary btn-medium btn-danger"/></a>
                    </td>
                </tr>
        </table>
      
       
        </fieldset>
		<hr>
        <%
        
                    }
                    else
                    {
                        out.println("<div align='center'><h4> N&atilde;o cont&eacute;m as ac&ccedil;&otilde;s necess&aacute;rias para efectuar esta opera&ccedil;&atilde;o </h4></p>");
                    }
                }
                else
                {
                    out.println("<div align='center'><h4> T&oacute;pico n&atilde;o existe!! </h4></p>");
                }
            }
            else
            {
                out.println("<div align='center'><h4> T&oacute;pico n&atilde;o existe!! </h4></p>");
            }
            
        %>
    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- No final do documento para acelarar carregamento da pagina-->
    
    <script src="assets/js/jquery.js"></script>
    <script src="assets/js/bootstrap-transition.js"></script>
    <script src="assets/js/bootstrap-alert.js"></script>
    <script src="assets/js/bootstrap-modal.js"></script>
    <script src="assets/js/bootstrap-dropdown.js"></script>
    <script src="assets/js/bootstrap-scrollspy.js"></script>
    <script src="assets/js/bootstrap-tab.js"></script>
    <script src="assets/js/bootstrap-tooltip.js"></script>
    <script src="assets/js/bootstrap-popover.js"></script>
    <script src="assets/js/bootstrap-button.js"></script>
    <script src="assets/js/bootstrap-collapse.js"></script>
    <script src="assets/js/bootstrap-carousel.js"></script>
    <script src="assets/js/bootstrap-typeahead.js"></script>
  </body>
 
</html>
