<%--
Autores:
  Bruno Miguel Oliveira Rolo nº 2010131200 brolo@student.dei.uc.pt
  Joao Artur Ventura Valerio Nobre nº 2010131516 janobre@student.dei.uc.pt
--%>
<%@page import="IdeaBroker.Accao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="IdeaBroker.IdeaBrokerBean"%>
<%@page import="IdeaBroker.Topico"%>
<%@page import="IdeaBroker.Ideia"%>
<jsp:useBean id="ideabrokerbean" class="IdeaBroker.IdeaBrokerBean" scope="session" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%! Topico topico; %>
<%! Ideia ideia; %>
<%! int id_topico = -1; %>
<%! int id_ideia = -1; %>

<!DOCTYPE html>
<jsp:include page="verifica.jsp"></jsp:include>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title>.:IdeaBroker:. - Histórico</title>
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
              <li ><a href="inicio.jsp">Inicio</a></li>
              <li><a href="Pesquisar.jsp">Pesquisar</a></li>
              <li><a href="halloffame.jsp">Hall of Fame</a></li>
              <li class="dropdown">
                
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Gerir <b class="caret"></b></a>
                <ul class="dropdown-menu">
                         
                  <li class="nav-header">Perfil</li>
                  <li> <a href="profile.jsp">Minha Conta</a></li>
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
            if((request.getParameter("id_t")!=null) && (request.getParameter("id_i")!=null))
            {
                id_topico = Integer.parseInt(request.getParameter("id_t"));   
                id_ideia = Integer.parseInt(request.getParameter("id_i"));
            }
                      
      %>
      <h3 align="center"> IdeaBroker </h3>
      <br>
      <h4 align="center"> Histórico de Transações </h4>
      <h4 align="center">Topico:[<%= id_topico %>,Ideia:<%= id_ideia %>]:</h4>
      <hr>
      <!-- Example row of columns -->
          <div class="container marketing">  
	<fieldset align="center">
        <table id="newspaper-a" align="center" summary="Lista de Utilizadores">
            <thead>
                <tr>
                   <!-- <th scope="col">ID Ideia</th> -->
                    <th scope="col">ID Transação:</th>
                    <th scope="col">Descrição da Transação:</th>
                </tr>
            </thead>
            <tbody>
                <!-- <td>0</td> -->

                <%
                    String resposta;
                    resposta= ideabrokerbean.getHistorico(id_topico,id_ideia);
                    out.println(resposta);
                %>
            </tbody>
        </table>
        <br><br>
    
       
    </fieldset>
    

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
