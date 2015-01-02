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
<%! Ideia ideia; %>
<%! Topico topico; %>


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
    <script>
        function valida_dados(form)
        {
            if (form.descricao.value=="") {
                alert("Descrição não preenchida!");
               form.descricao.focus();
               return false;
            }
            
            if (form.investimento.value=="") {
                alert("Têm que especificar o seu investimento na ideia!");
               form.investimento.focus();
               return false;
            }
            
            return true;
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
                  <li> <a href="favoritos.jsp">Favoritos</a> </li>
                  <li> <a href="portofolio.jsp">Portofolio</a> </li>
                 
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
            
            int id_topico = Integer.parseInt(request.getParameter("id_topico"));   
                //out.print(ideabrokerbean.getOAuth());
                //  out.print(username);
            if(ideabrokerbean.verifica_topico(id_topico))
            {
                topico = ideabrokerbean.GetTopico(id_topico);
                //System.out.println("Estou no topico == "+topico.gettema());
                if(topico != null)
                {
                    
                
      %>
      <h4 align="center">Criar Nova Ideia [<%= topico.gettema() %>]:</h4>
      <hr>
      <!-- Example row of columns -->
          <div class="container marketing">  
	<fieldset align="center">
        <table width='100%' height='100%' align="center" border="0" bordercolor="#000000" style="background-color:#FFFFFF" >
            <form method="post" action="InserirIdeiaServlet" name=""  onsubmit="return valida_dados(this);" class="navbar-form pull-center" enctype="multipart/form-data"> 
                <input type="hidden" value="<%= topico.getidtopico() %>" name="id_topico" id="id_topico"/>
            <tr>
                <td>
                   <h5>Descri&ccedil;&atilde;o:</h5>
                </td>
                <td align="left">
                    <textarea class="span7" rows="5" cols="200" name="descricao" id="descricao"></textarea>
                </td>
                
            </tr>
            <tr>
                <td>
                    <h5>Anexo:</h5>
                </td>
                <td align="left">
                    <input type="file" name="ficheiro" id="ficheiro" onchange="" />
                </td>
            </tr>
            <tr>
                <td>
                    <h5>Quanto quer investir? (DEIcoins)</h5>
                </td>
                <td align="left">
                     <input  type="text1" placeholder="Investimento da Ideia (DEIcoins)" name="investimento" id="investimento"> 
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <button type="submit" class="btn btn-primary btn-large btn-danger">Enviar</button>
                </td>
            </tr>
           </form>
        </table>
    </fieldset>
    <hr>
    <% 
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