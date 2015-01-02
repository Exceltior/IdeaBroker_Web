<!--
Autores:
 Bruno Miguel Oliveira Rolo nº 2010131200 brolo@student.dei.uc.pt
 Joao Artur Ventura Valerio Nobre nº 2010131516 janobre@student.dei.uc.pt
<<!-- jsp:useBean id="ideabrokerbean" class="IdeaBroker.IdeaBrokerBean" scope="session" />
-->
<%@page import="java.util.ArrayList"%>
<%@page import="IdeaBroker.IdeaBrokerBean"%>
<%@page import="IdeaBroker.Topico"%>
<%@page import="IdeaBroker.Ideia"%>
<jsp:useBean id="ideabrokerbean" class="IdeaBroker.IdeaBrokerBean" scope="session" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            //IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
            
            //out.print(ideabrokerbean.getService());
            //out.print(ideabrokerbean.getOAuth());
            //out.print(username);
            //out.print("<br> ID == "+ideabrokerbean.getID());      
              ideabrokerbean.print_service();
            int id_topico = -1,
                valida = 1;
            
            if(ideabrokerbean.ContarLetras(request.getParameter("id")))
            {
                id_topico = Integer.parseInt(request.getParameter("id"));
            }
            else
                valida = 0;
            
            
            if((ideabrokerbean.verifica_topico(id_topico)) && (valida == 1))
            {
                topico = ideabrokerbean.GetTopico(id_topico);
                //System.out.println("Estou no topico == "+topico.gettema());
                if(topico != null)
                {
                    
                
      %>
      
      <h4 align="center">Listagem de Ideias no Tópico [<%= topico.gettema() %>]:</h4>
      <hr>
      <!-- Example row of columns -->
    <div class="container marketing">  
    	<fieldset align="center">
<table id="newspaper-a" align="center" summary="Lista de Utilizadores">
            <thead>
                <tr>
                    <th scope="col">ID Ideia</th>
                    <th scope="col">Descrição da Ideia</th>
                    <th scope="col">Anexo</th>
                    <th scope="col">Autor</th>
                    <th scope="col">Ac&ccedil;&otilde;es</th>
                    <th scope="col">Apagar</th>
                    <th score="col">Histórico de Acções</th>
                    <%
                    if(username.compareTo("root")==0)
                    {
                        out.println("<th scope='col'> TakeOver </th>");
                    }
                      %>
                      
                </tr>
            </thead>
            <tbody>
                <%
                    String aux_anexo = null;
                    //Se contiver ideias..
                    if(!topico.getIdeia().isEmpty())
                    {
                        for(int i = 0 ; i < topico.getIdeia().size() ; i++)
                        {                            
                            if(topico.getIdeia().get(i).getAccoes().get(0).getutilizador().compareTo("root")!=0)
                            {
                                if(topico.getIdeia().get(i).getficheiro() != null)
                                {
                                     aux_anexo = topico.getIdeia().get(i).getficheiro().getAnexo();
                                }
                                else
                                {
                                    aux_anexo = "--";
                                }
                                if(username.compareTo("root")==0)
                                {
                                    out.println("<tr><td>"+topico.getIdeia().get(i).getidideia()+"</td><td>"+topico.getIdeia().get(i).getdescricao()+"</td><td>"+aux_anexo+"</td><td>"+topico.getIdeia().get(i).getautor()+"</td><td><a href='bolsa.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"' target='_blank'>Op&ccedil;&otilde;es</a></td><td><a href='ApagarIdeia.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"&id_ui="+topico.getIdeia().get(i).getId()+"'>[Apagar]</a></td><td><a href='historico.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"'>[Hist&oacute;rico]</a></td><td><a href='takeover.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"'>[TakeOver]</a></td></tr>");
                                }
                                else
                                {
                                    out.println("<tr><td>"+topico.getIdeia().get(i).getidideia()+"</td><td>"+topico.getIdeia().get(i).getdescricao()+"</td><td>"+aux_anexo+"</td><td>"+topico.getIdeia().get(i).getautor()+"</td><td><a href='bolsa.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"' target='_blank'>Op&ccedil;&otilde;es</a></td><td><a href='ApagarIdeia.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"&id_ui="+topico.getIdeia().get(i).getId()+"'>[Apagar]</a></td><td><a href='historico.jsp?id_t="+topico.getidtopico()+"&id_i="+topico.getIdeia().get(i).getidideia()+"'>[Hist&oacute;rico]</a></td></tr>");
                                }
                            }
                        }
                    }
                    else
                    {
                        out.println("<tr> <td colspan='7' align='center'><h4>Sem ideias.. <h4></td></tr>");
                    }
                %>
            </tbody>
        </table>
    
        <p align="center"><a href="criarIdeia.jsp?id_topico=<%= topico.getidtopico() %>" target="_blank">Criar Ideia</a></p>
        
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
