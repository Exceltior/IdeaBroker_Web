
<%@page import="IdeaBroker.IdeaBrokerBean"%>
<jsp:useBean id="ideabrokerbean" class="IdeaBroker.IdeaBrokerBean" scope="session" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<jsp:include page="verifica.jsp"></jsp:include>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title>.:IdeaBroker:. - Listar Tópicos</title>
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
    
    	h3 {
    		margin: 0px;
    		padding: 0px;	
    	}
    
    	.suggestionsBox {
    		position: relative;
    		left: 0px;
    		margin: 0px 0px 0px 0px;
    		width: 200px;
    		background-color: #212427;
    		-moz-border-radius: 7px;
    		-webkit-border-radius: 7px;
    		border: 2px solid #000;	
    		color: #fff;
    	}
    	
    	.suggestionList {
    		margin: 0px;
    		padding: 0px;
    	}
    	
    	.suggestionList li {
    		font-family: Helvetica;
    	    font-size: 11px;
    		margin: 0px 0px 3px 0px;
    		padding: 3px;
    		cursor: pointer;
    	}
    	
    	.suggestionList li:hover {
    		background-color: #659CD8;
    	}
        
    </style>
    <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="assets/css/tabela.css" rel="stylesheet">
    <script src="ajax/jquery-1.9.1.js"></script>
    <script src="ajax/jquery-ui.js"></script>
    <script type="text/javascript" src="ajax/jquery.form.js"></script> 
     <script>

    
    // Quando carregado a página 
    $(function($) {   
        // Quando enviado o formulário 
        $('#pesquisar_form').submit(function() {   
            // Limpando mensagem de erro 
             $("#resposta").html("<img src='ajax/image-load.gif' alt='Load' height='100' width='100'><br><h4>Carregando...</h4>");    
            
             // Enviando informações do formulário via AJAX 
             $(this).ajaxSubmit(function(resposta) {   
                 // Se não retornado nenhum erro 
                 $("#resposta").empty();
                 if(resposta == null)
                 {
                    $("#resposta").append("Sem Ideias..");
                    return;
                 }
                 var table = $('<table id="box-table-a" name="tableid" border="2" width="100%" height="100%" align="center"><thead><tr><th scope="col" align="center">ID</th><th scope="col" align="center" >Descri&ccedil;&atilde;o</th><th scope="col" align="center">Autor</th></tr></thead><tbody align="center"> ').appendTo($('#resposta'));
                 
                 $.each(resposta, function(index, data) {
                        $('<tr>').appendTo(table)
                            .append($('<td>').text(data.idideia))
                            .append($('<td>').text(data.descricao))
                            .append($('<td>').text(data.autor));
                    });
                    table.append("</tbody>");
                    
                 $('html, body').animate({scrollTop: $("#resposta").offset().top}, 2000);
                 /*if (!resposta) 
                     // Redirecionando para o painel
                     window.location.href = 'painel.php'; 
                 else { // Escondendo loader 
                     $('div.loader').hide();   
                     // Exibimos a mensagem de erro 
                     $('div.mensagem-erro').html(resposta); 
                 } */  
                 
             });
            
            // Retornando false para que o formulário não envie as informações da forma convencional return false;  
            return false;
            
        });

    }); 


    
    window.onload = function(){
        //Nada..
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
              <li><a href="inicio.jsp">Inicio</a></li>
              <li class="active"><a href="Pesquisar.jsp">Pesquisar</a></li>
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
      <br>
    <form method="post" action="PesquisarServlet" name="pesquisar_form" id="pesquisar_form" > 
    <!---------------------- TABELA PRINCIPAL ---------------------->
    <fieldset align="center" >
        <table width='100%' height='100%' align="center" border="0" bordercolor="#000000" style="background-color:#FFFFFF" >
                <tr>
                    <td colspan = "4">
                        <label>
                            <hr style="border-color:#f00; background-color: #f00;">
                                <h4>Pesquisar</h4>
                            <hr style="border-color:#f00; background-color: #f00;">
                        </label>
                        <br>
                    </td> 
                  
                </tr> 
                <tr class="hero-unit">
                <!-- Pesquisar por: !-->
                <!-- Possibilidade 1 por nome topico !-->
                    <!--<td NOWRAP='NOWRAP'>
                        <label>Nome do Tópico:</label> 
                    </td>
                    <td>
                        <input class='span3' type="text" placeholder="Nome" name="nome_topico" id="nome_topico">
                    </td>-->
                    <!-- Possibilidade 2 por autor!-->
                    <td>
                        <label>Autor de Ideia:</label> 
                    </td>
                    <td>
                        <input class='span2' type="text" placeholder="Username" name="autor_ideia" id="autor_ideia">
                    </td>
                    <td>
                        <label>Ideias com valor igual a:</label> 
                    </td>
                    <td>
                        <input class='span3' type="text" placeholder="Valor total" name="valor_ideia" id="valor_ideia">
                    </td>
                 </tr>
                 <tr>
                     <td align="right" colspan="4">
                        <input type='submit' value='Procurar' id="Procurar" name="Procurar" class="btn btn-danger" />
                     </td>
                 </tr>
                <!-- <tr class="hero-unit">
                    <td>
                        <label>Ideias com valor<br> superior a:</label> 
                    </td>
                    <td>
                        <input class='span3' type="text" placeholder="Valor Total" name="valorsharesup" id="valorsharesup">
                    </td>
                    <td>
                        <label>Data In&iacute;cio:</label> 
                    </td>
                    <td>
                        <input class="span2" type="date" name="data_inicio" id="data_inicio"> 
                    </td>
                    <td>
                        
                    </td>
                    <td>
                        
                    </td>
                </tr> -->
              
                   
    </table>
    </fieldset>
    </form>
    <div id="resposta" align="center">  
            
    </div>
    <table id="box-table-a" name="tableid" border="2" width='100%' height='100%' align="center" >
        
    </table>  
    <br><br><br>
    <hr>

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
