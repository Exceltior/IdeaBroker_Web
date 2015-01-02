<%@page import="java.util.ArrayList"%>
<%@page import="IdeaBroker.IdeaBrokerBean"%>
<%@page import="IdeaBroker.Topico"%>
<%@page import="java.util.Vector"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! ArrayList<Topico> topicos; %>

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
    <script>
        function valida_dados(form)
        {
            if (form.topico.value=="") {
                alert("Tema do Tópico não está preenchido!");
               form.topico.focus();
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
            connect('ws://' + window.location.host + '/IdeiaBroker_Web/NotificacaoWebSocketServlet');
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
            document.getElementById('notificacao').onkeydown = function(key) {
                if (key.keyCode == 13)
                    doSend(); // call doSend() on enter key
            };
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
            var message = document.getElementById('chat').value;
            document.getElementById('chat').value = '';
            if (message != '')
                websocket.send(message); // send the message
        }

        function writeToHistory(text) {
            var history = document.getElementById('notificacao_div');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = text + "<br><-------------------------><br>";
            history.appendChild(p);
            while (history.childNodes.length > 25)
                history.removeChild(console.firstChild);
            history.scrollTop = history.scrollHeight;
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
                    <a href="logout" >  [Logout]</a>
                    </font>
                    </li>
                    </ul>
            </div> 
           </div><!--/.nav-collapse -->
          </div>
      </div>
    </div>

  <div class="container">

      <h4 align="center">Listagem de Tópicos:</h4>
      <hr>
      <!-- Example row of columns -->
      <div class="container marketing">  
	<fieldset align="center">
    <!--<fieldset align="center" >-->
    <table width='100%' height='100%' align="center" border="2" bordercolor="#000000" style="background-color:#FFFFFF" >
     <tr>
        <td width="100">
         	<table id="newspaper-a" align="center" summary="Lista de Utilizadores">
                <thead>
                    <tr>
                    	<th scope="col">ID Tópico</th>
                        <th scope="col">Tema</th>
                        <th scope="col">Autor</th>
                        <th scope="col">Nº Ideias</th>
                        <th scope="col">Ultima Ideia Por:</th>
                        <th scope="col">Entrar</th>
                        <!-- <th scope="col">Apagar</th> -->
                    </tr>
                </thead>
                <tbody>
                    <%
                        String username = (String) session.getAttribute("username");
                        String password = (String) session.getAttribute("password");
                        IdeaBrokerBean ideabrokerbean = (IdeaBrokerBean) session.getAttribute("ideabrokerbean");
                        ideabrokerbean.print_service();
                        //out.println("Token 1 == "+ideabrokerbean.getOAuth()); 
                        topicos = ideabrokerbean.buscatopicos(); 
                       // out.println("Token 2 == "+ideabrokerbean.getOAuth());
                        if(topicos != null)
                        {
                            for(int i = topicos.size() -1; i >= 0; i--)
                            {
                                Topico topico = topicos.get(i);
                                
                                if(topico.getIdeia().size() > 0)
                                    out.println("<tr><td>"+topico.getidtopico()+"</td><td>"+topico.gettema()+"</td><td>"+topico.getautor()+"</td><td>"+topico.getIdeia().size()+"</td><td>"+topico.getIdeia().get(topico.getIdeia().size()-1).getautor()+"</td><td> <a href='listarideia.jsp?id="+topico.getidtopico()+" '>[Entrar]</a></td></tr>");
                                else
                                    out.println("<tr><td>"+topico.getidtopico()+"</td><td>"+topico.gettema()+"</td><td>"+topico.getautor()+"</td><td>Sem Ideias</td><td>--</td><td><a href='listarideia.jsp?id="+topico.getidtopico()+" '>[Entrar]</a></td></tr>");
                            } 
                        }
                    %>
                   <!-- <td>0</td>
                    <td>O jogo do Benfica x Sporting</td>
                    <td>5</td>
                    <td>Bruno Rolo</td>
                    <td><a href="apagarideia.html">[Apagar]</a></td>-->
                
                  
                </tbody>
                <tbody>
                    <td colspan="7">
                        <form method="post" action="InserirTopicoServlet" name="" onsubmit="return valida_dados(this);" class="navbar-form pull-center">   <br> 
                            <input  type="text" placeholder="Tema Topico" name="topico" id="topico"> 
                          <button type="submit" class="btn btn-small">Criar</button>
                        </form>
                    </td>
                </tbody>
            </table>
        </td>
        <td  width="300">
            
            <div id="notificacao_div">
                
            </div>
        </td>
    </tr>
    </table>
    <!--<p align="center"> Listagem de Utilizadores </p>-->
   <!-- <table>
        <tr>
            <td>
                <h5>Utilizadores a ver esta página:</h5>
            </td>
        </tr>
        <tr>
            <td>
                <div id="history"></div>
            </td>
        </tr>
    </table>-->
    
</fieldset>
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