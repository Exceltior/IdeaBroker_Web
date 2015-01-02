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
<%! ArrayList<Accao> accoes; %>
<%! Topico topico; %>
<%! Ideia ideia; %>
<%! int id_topico = -1; %>
<%! int id_ideia = -1; %>

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
    </style>
    <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="assets/css/tabela.css" rel="stylesheet">
    <script src="ajax/jquery-1.9.1.js"></script>
    <script src="ajax/jquery-ui.js"></script>
    <script type="text/javascript" src="ajax/jquery.form.js"></script> 
    <script type="text/javascript" charset="utf-8">
        
        $(document).ready(function()
        {        
            
                // Recuperar ndados necessarios
                var idtopico = $("#inputid_topico").val(); 
                var idideia = $("#inputid_ideia").val(); 
                var username = $("#username").val();
                console.log(idtopico);
                console.log(idideia);
                console.log(username);
                // Mensagem de carregando 
                $("#update_price").val("Carregando..."); 
                     // Faz requisição ajax e envia o nome do selcionado via método POST 
                      $.post("PesquisaBolsa", {id_topico: idtopico, id_ideia: idideia}, function(resposta1) { 
                          console.log("Resposta == "+resposta1);
                          
                          var resposta = $.parseJSON(resposta1);
                          $.each(resposta, function(index, data) {
                                if(data.utilizador === username)
                                {
                                   $("table[name=table_shared]").last().append("<tr><td>"+data.utilizador+"</td><td>"+data.percentagem+"</td><td>"+data.precoaccao+"</td><td><a href='EditarAccao.jsp?id_t="+ idtopico +"&id_i="+ idideia +"'>[Editar]</a></td>/tr>");
                                }
                                else
                                {
                                    $("table[name=table_shared]").last().append("<tr><td>"+data.utilizador+"</td><td>"+data.percentagem+"</td><td>"+data.precoaccao+"</td><td></td></tr>");
                                }
                               
                            });
                            
                          $('html, body').animate({scrollTop: $("table[name=table_shared]").offset().top}, 2000);
            
                      }); 

        });
    
        function valida_dados(form)
        {
            if (form.numero_accoes.value=="") {
                alert("Número de acções que pretende comprar não especificado!");
               form.numero_accoes.focus();
               return false;
            }
            
            if (form.preco_compra.value=="") {
                alert("Preço que pretende comprar acções não está preenchido!");
               form.preco_compra.focus();
               return false;
            }
            
            if (form.preco_venda.value=="") {
               alert("Preço de venda das acções não está preenchido!");
               form.preco_venda.focus();
               return false;
            }
            
            return true;
        }   
        
        var websocket;

        window.onload = function() { // ecexuta quando a pagina e carregada 
            initialize();
            //document.getElementById("history").focus();
        }

        function initialize() { // URI = ws://10.16.0.165:8080/chat/chat URL
           // connect('ws://' + window.location.host + '/SocialWeb/ChatWebSocketServlet');
            connect('ws://' + window.location.host + '/IdeiaBroker_Web/UpdatePriceServlet');
        }

        function connect(host) { // connect to the host websocket servlet connectar a servlet atraves do websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            //writeToHistory('Connected to ' + window.location.host + '.');
           //writeToHistory('Bem vindo IdeaBroker..');
          /*  document.getElementById('notificacao').onkeydown = function(key) {
                if (key.keyCode == 13)
                    doSend(); // call doSend() on enter key
            };*/
        }
        
        function onClose(event) {
            //writeToHistory('WebSocket closed.');
           // document.getElementById('notificacao').onkeydown = null;
            
        }
        
        function onMessage(message) { // print the received message
            writeToHistory(message.data);
        }
        
        function onError(event) {
            writeToHistory('WebSocket error (' + event.data + ').');
            //document.getElementById('notificacao').onkeydown = null;
        }
        
        function doSend() {
           /* var message = document.getElementById('chat').value;
            document.getElementById('chat').value = '';
            if (message != '')
                websocket.send(message); // send the message*/
        }

        function writeToHistory(text) {
              //var table_price = document.getElementsByName('table_shared');
              var table_price = document.getElementById('table_shared');
              if (text.toString() === "clear")
              {
                    //alert("Quantas vezes e queeu faço clear?!?!");
                    //update_price.innerHTML = "";
                    var rows = table_price.rows; 
                    var aux = rows.length;
                    if(aux > 2)
                    {
                        for(i=1; i < aux ; i++) {
                            console.log("I == "+i+" < "+aux);
                            table_price.deleteRow(1);
                        }
                    }
                    else
                    {
                        table_price.deleteRow(1);
                    }
              }
              else 
              {
                  
                    var tmp = document.createElement('tr'); 
                    tmp.innerHTML = text;
                    var new_w = tmp.firstChild;
                   // var parent = insertion_point.parentNode;
                  //  parent.insertBefore(new_w, insertion_point.nextSibling);
                    table_price.appendChild(tmp);
                   
                    //alert(text);
                    //while (history.childNodes.length > 25)
                    //history.removeChild(console.firstChild);
                    table_shared.scrollTop = table_shared.scrollHeight;
              }            
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
            //String password = (String) session.getAttribute("password");
            
            if(ideabrokerbean.ContarLetras(request.getParameter("id_t")))
                id_topico = Integer.parseInt(request.getParameter("id_t"));   
            if(ideabrokerbean.ContarLetras(request.getParameter("id_i")))
                id_ideia = Integer.parseInt(request.getParameter("id_i"));
            
            //System.out.println("Voltemos ao inicio........."+ideabrokerbean.verifica_topico(id_topico) +" ...  "+ideabrokerbean.verifica_ideia(id_topico, id_ideia));
            if((ideabrokerbean.verifica_topico(id_topico) == true) && (ideabrokerbean.verifica_ideia(id_topico, id_ideia) == true))
            {
                topico = ideabrokerbean.GetTopico(id_topico);
                ideia = ideabrokerbean.GetIdeia(id_topico,id_ideia);
                
                System.out.println("Entrou no  1º if == "+topico.gettema());
                if(topico != null && ideia != null)
                {                    
                      
      %>
      <h3 align="center"> IdeaBroker </h3>
      <br>
      <h4 align="center"> [<%= topico.gettema() %>] </h4>
      <h4 align="center">Bolsa de Ac&ccedil;&otilde;es [<%= ideia.getidideia() %>]:</h4>
      <hr>
      <!-- Example row of columns -->
          <div class="container marketing">  
	<fieldset align="center">
        <input type="hidden" name="inputid_topico" id="inputid_topico" value="<%= id_topico %>">
        <input type="hidden" name="inputid_ideia" id="inputid_ideia" value="<%= id_ideia %>">
        <input type="hidden" value="<%= session.getAttribute("username") %>" name="username" id="username">
      <!-- newspaper-a -->
        <table id="table_shared" border="2" align="center" summary="Lista de Utilizadores" name="table_shared">
            <thead>
                <tr>
                   <!-- <th scope="col">ID Ideia</th> -->
                    <th scope="col">Acionista:</th>
                    <th scope="col">Percentagem (%):</th>
                    <th scope="col">Pre&ccedil;o (DEIcoins):</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
            <div id="update_price"> 
                <%
                   /* accoes = ideabrokerbean.GetAccoes(id_topico,id_ideia,username);
                    if(accoes != null)
                    {
                        for(int i = 0; i < accoes.size(); i++)
                        {
                            if(accoes.get(i).getutilizador().compareTo(username) == 0)
                            {
                                out.println("<tr><td>"+accoes.get(i).getutilizador()+"</td><td>"+accoes.get(i).getpercentagem()+"</td><td>"+accoes.get(i).getpreco()+"</td><td><a href='EditarAccao.jsp?id_t="+ id_topico +"&id_i="+ id_ideia +"'>[Editar]</a></td></tr>");
                            }
                            else
                            {
                                out.println("<tr><td>"+accoes.get(i).getutilizador()+"</td><td>"+accoes.get(i).getpercentagem()+"</td><td>"+accoes.get(i).getpreco()+"</td><td></td></tr>");
                            }
                        }*/
                %>
                
             </div> 
            </tbody>
        </table>
        <br><br>
    
        <div align="center">
        <form method="post" action="ComprarAccaoServlet" onsubmit="return valida_dados(this);" name="comprar_accoes">
            <table align="center">
                <input type="hidden" value="<%= id_topico %>" name="id_topico" id="id_topico">
                <input type="hidden" value="<%= id_ideia %>" name="id_ideia" id="id_ideia">
                 
                <tr>
                    <td>
                        <h5>Quantas acções pretende comprar? </h5>
                    </td>
                    <td> &nbsp; &nbsp;</td>
                    <td>
                        <input  type="text1" placeholder="Investimento da Ideia (DEIcoins)" id="numero_accoes" name="numero_accoes">
                    </td>
                    
                </tr>
                <tr>
                    <td>
                        <h5>A que pre&ccedil;o?</h5>
                    </td>
                    <td> &nbsp; &nbsp;</td>
                    <td>
                        <input  type="text1" placeholder="DEIcoins" id="preco_compra" name="preco_compra">
                    </td>
                </tr>
                <tr>
                    <td>
                        <h5>Preço de Venda?</h5>
                    </td>
                    <td> &nbsp; &nbsp;</td>
                    <td>
                        <input  type="text1" placeholder="DEIcoins" id="preco_venda" name="preco_venda">
                    </td>
                </tr>
                <tr align="center">
                    <td colspan="3"><button type="submit" class="btn btn-primary btn-medium btn-danger">Comprar</button></td>
                </tr>
            </table>
        </form>
        </div>
    </fieldset>
       <%
                        /*}
                        else
                        {
                            out.println("<div align='center'><h4></h4></div>");
                        }*/

                }
                else
                {
                    out.println("<div align='center'><h4> Ideia/T&oacute;pico n&atilde;o existe!! </h4></p>");
                }
            }
            else
            {
                out.println("<div align='center'><h4> Ideia/T&oacute;pico n&atilde;o existe!! </h4></p>");
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
