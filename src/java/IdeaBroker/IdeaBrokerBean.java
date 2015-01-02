/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IdeaBroker;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//import org.apache.catalina.websocket.MessageInbound;
//import org.eclipse.jdt.internal.compiler.parser.diagnose.LexStream.Token;

/**
 *
 * @author joao
 */

public class IdeaBrokerBean extends UnicastRemoteObject implements WebClienteInterface,Serializable{
    
    private static final long serialVersionUID = 1L;
    private Registry registry;
    private IdeiaMethods serverRMI;
    private Vector<String> users_online = new Vector<String>();
    private IdeaBrokerBean cliente;
    private Token accessToken;
    
    private transient OAuthService service = null;
    
   //private String service;
    private int id;
   //private Vector<String> onlineUsers = new Vector<String>();
    
    public IdeaBrokerBean() throws RemoteException
    {
        super();
    }    
    
    public void setOAuth(String username,String code)
    {
        System.out.println("<------------------------------------------------------>");
        System.out.println("BEAN VAI MUDAR DE TOKEN... user == ["+username+"] e token == ["+code+"]");
        /*try{
            serverRMI.setOAuth(username,code);
        } catch (RemoteException e) {
            System.out.println("BUG!?!??!!??!");
        }*/
        
        System.out.println("<------------------------------------------------------>");
        System.out.println("Sou o Token == "+code);
        //accessToken = code;
        try{
            serverRMI.adicionaToken(username,code);
         } catch (RemoteException e) {
            System.out.println("Remote...");
        }
    }
    
    public int getID()
    {
        return id;
    }
    
    public void setID(int id)
    {
        this.id = id;
    }
    
    public String getOAuth(String username)
    {        
        try{
            return serverRMI.getOAuth(username);
         } catch (RemoteException e) {
            System.out.println("Remote...");
        }
        
        return null;
        
       // try{
            //return accessToken;
        /*} catch (RemoteException e) {
            System.out.println("BUG!?!??!!??!");
        }*/
        //return null;
    }
    
    public void setService(OAuthService _service)
    {
        System.out.println("_serivce..... BEAN === "+_service);

     /*   try{
             serverRMI.setService(_service);
         } catch (RemoteException e) {
            System.out.println("Remote..."+e);
        }*/
        this.service = _service;
    }
    
    public OAuthService getService()
    {
        /*try{
            return serverRMI.getService();
         } catch (RemoteException e) {
            System.out.println("Remote...");
        }
        return null;*/
        
        return service;
    }
    
    @Override
    public void updateprice(ArrayList<Accao> _Accao, int id_topico, int id_ideia) throws RemoteException {
        
      System.out.println("[Bean] Vou actualizar listagem de accoes....");
      for (MessageInbound connection : UpdatePriceServlet.connections) {
            try {
                connection.getWsOutbound().writeTextMessage(CharBuffer.wrap("clear"));
                for(Accao accoes : _Accao) {
                    System.out.println("Esou a percorrer as accoes == "+accoes.getutilizador());
                    String bolsa = "";
                    bolsa ="<td>"+accoes.getutilizador()+"</td><td>"+accoes.getpercentagem()+"</td><td>"+accoes.getpreco()+"</td><td><a href='EditarAccao.jsp?id_t="+ id_topico +"&id_i="+ id_ideia +"'>[Editar]</a></td>";
                    System.out.println("bolsa == "+bolsa);
                    CharBuffer buffer = CharBuffer.wrap(bolsa);
                    connection.getWsOutbound().writeTextMessage(buffer);
                }
            } catch (IOException ignore) {
                
            }
      }
    
    }
    
    public void print_service()
    {
        System.out.println("[NOVA PAGINA] ----> "+service +" // "+id);
    }
    
    @Override
    public void addNote(String user, String notificacao) throws RemoteException
    {        
        WsOutbound out;
        Hashtable outs;
        
        System.out.println("Sou a Bean e vou enviar uma notificação para a servelet...");
        System.out.println("Username == ["+user+"] Notificacao == "+notificacao);
        
        CharBuffer buffer = CharBuffer.wrap(notificacao);
        
        
        if (user.equals("")) {                          //envia para todos os users na versao web
            
            outs = (Hashtable) NotificacaoWebSocketServlet.outBounds;
            Enumeration usernames = outs.keys();
            while (usernames.hasMoreElements()) {
                try {
                    String username = (String) usernames.nextElement();
                    out = (WsOutbound) NotificacaoWebSocketServlet.outBounds.get(username);
                    out.writeTextMessage(buffer);
                    buffer = CharBuffer.wrap(notificacao);
                } catch (IOException ex) {
                    Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {                            //envia para um user especifico
           
            if(!NotificacaoWebSocketServlet.connections.isEmpty())
            {
                out = (WsOutbound) NotificacaoWebSocketServlet.outBounds.get(user);
            
                try {
                    out.writeTextMessage(buffer);
                } catch (IOException ignore){
                }
            }
              
        }
    }
     
    public void adicionar_Notas(String user, String notificacao) throws RemoteException {
        
    }
     
    public void conectar_RMI() throws RemoteException, NotBoundException
    {
     /*    System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        try {
            cliente = new IdeaBrokerBean();
            registry = LocateRegistry.getRegistry(1099);
            serverRMI = (IdeiaMethods) registry.lookup("//192.168.1.157/Ideia");
            
            serverRMI.Webcliente(cliente);
        } catch (AccessException e) {
            System.out.println("Acesso Negado...");
        } catch (RemoteException e) {
            System.out.println("Remote...");
        } catch (NotBoundException e) {
           System.out.println("Endereço nao encontrado..");
        }*/
        
        //System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        //System.out.println("Chega QUI............");     
        try {
                //ESTE H tem todos os métodos do servidor
               //h = (IdeiaMethods) Naming.lookup("Ideia");
               try{
                    cliente = new IdeaBrokerBean();
                    System.out.println("//192.168.1.157:1099/Ideia");
                    serverRMI = (IdeiaMethods) Naming.lookup("//192.168.1.157:1099/Ideia");
                    serverRMI.Webcliente(cliente);
                } catch(RemoteException e) {

                     serverRMI = (IdeiaMethods) Naming.lookup("//192.168.1.157:6000/Ideia");
                
                }
               
               
          
        } catch (Exception e) {

                System.out.println("Exception in main: RMI\n");
                return;

        }
        
    }
    
    public int login(String username, String password) throws RemoteException
    {
        try{
            Utilizador  resultado = serverRMI.login(username,password);
            if( resultado != null)
            {
               serverRMI.adicionaOnlineWeb(username);
               return 1;
            }
            else
            {
               return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return -2;
    }

    public int registo(String username,String password,String nome)
    {
        try {
            if (serverRMI.verificaUtilizador(username, "", true)) {
                return -1; // Nome ja existe
            }
            System.out.println(password);
            
            if(serverRMI.registar(username,password) != null)
            {
               return 1; 
            }
            else
            {
                return -1;
            }
            
        } catch (Exception e) {
            
        }
        return 1;
    }
   
    public void retiraOnlineWeb(String username) {
        try {
            serverRMI.retiraOnlineWeb(username);
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Topico> buscatopicos() 
    {
        ArrayList<Topico> Topicos = new ArrayList<Topico>();
        System.out.println("Entra no buscatopicos...");
        try{
            Topicos = serverRMI.gettopicos();
            return Topicos;
            
        } catch (RemoteException e) {
            System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. "
                    + "Pelo que não pode efectuar este comando.");
            return null;
        }

    }
    
    public int criarTopico(String username, String tema)
    {
        int result = 0;
        
        /** Cria Topico **/
        //ArrayList<Topico> Topicos;
        System.out.println("Vou criar o topico com o tema == "+tema);

        //Lock
        /*try
        {

            Topicos = serverRMI.gettopicos();  

        } catch (Exception e) {

            System.out.println("Problema no gettopicos ao RMI");
           return -1;
        
        }
        */
       /* for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).gettema().compareTo(tema)==0)
            {
                System.out.println("Tópico já existente.");
                return -1;
            }
        }*/

        try {
            
           result = serverRMI.adicionartopico(username,tema);
         //h.adicionarNotasAgendadas("Criado Novo Topico ["+tema+"]\n",eu.getusername());
           
        } catch (RemoteException e) {
       
            System.out.println("Erro a criar topico devido a ligação RMI");
            return -1;
       
        }
        
        return result;
        
    }
    
    public Topico GetTopico(int id_topico)
    {
        Topico topico = null;
        System.out.println("Gettopico");
        try{
            return serverRMI.GetTopico(id_topico);
        } catch (RemoteException e) {
            return null;
        }
        
    }
    
    
    public int geraridtopico(ArrayList<Topico> Topicos)
    {
        int max=-1;
        for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).getidtopico()>max)
            {
                max=Topicos.get(i).getidtopico();
            }
        }
        return max;
    }
    
    public boolean verifica_topico(int id_topico) 
    {
        System.out.println("[RMI] Estou a verificar topico == "+id_topico);
        try{
            if(serverRMI.verifica_topico(id_topico))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (RemoteException e) {
            return false;
        }

    }
    
    public boolean verifica_ideia(int id_topico, int id_ideia) 
    {
        try{
            if(serverRMI.verifica_ideia(id_topico,id_ideia))
            {
                return true;
            }
        } catch (RemoteException e) {
            return false;
        }
        
        return false;
    }
    
    
    public boolean criaIdeia(String username, String textData, String anexo, int idtopico, int investimento) throws SAXException
    {
        int resultado = -1;
        try
        {
            
            /** FACEBOOK ***/
            OAuthRequest request2 = new OAuthRequest(Verb.POST, "https://api.facebook.com/method/stream.publish");
            request2.addHeader("Content-Type", "text/html");
            request2.addBodyParameter("message", textData);
            String acesso = serverRMI.getOAuth(username);
            
            System.out.println("Token em String == "+acesso);
            Token token = new Token(acesso,"");
        
            //OAuthService service;
           // OAuthService service = serverRMI.getService();
            
            service.signRequest(token, request2);
            System.out.println("CRIAR IDEIA BEAN");
            System.out.println("[Bean] User == "+username);
            System.out.println("Token == "+token);
            
            System.out.println("<---------------->");
            Response response2 = request2.send();
            
            String execType = null;
            try {
                
                DOMParser parser = new DOMParser();
                parser.parse(new InputSource(new StringReader(response2.getBody())));
                Document doc = parser.getDocument();
                NodeList root = doc.getChildNodes();
                Node comp = getNode("stream_publish_response", root);
                execType = getNodeValue(comp);
                System.out.println("Nova Ideia com face == "+execType);
                
             }
             catch ( Exception e ) 
             {
                 
                    System.out.println("BUG");
                    e.printStackTrace();
             
             }
            /** ----------------- **/
            
            if((anexo != null) && (!anexo.isEmpty()))
            {
                System.out.println("Tem anexo!!");
                resultado = serverRMI.criarideia(idtopico, textData, username, new Anexo(anexo),investimento,execType);   
            }
            else
            {
                System.out.println("[Criar] ANTES service == "+service);
                resultado = serverRMI.criarideia(idtopico, textData, username, null,investimento,execType);
                System.out.println("[Criar] DEPOIS service == "+service);
            }
                
            System.out.println("Resultado do RMI == "+resultado);
            if(resultado == 1)
               return true;
            else
               return false;
            
        }
        catch( RemoteException e)
        {
            System.out.println("Problema RMI: Criar Ideia");
        }
        
        return false;
    }
    
    public Ideia GetIdeia(int id_topico,int id_ideia)
    {
        Ideia ideia = null;
        System.out.println("GetIdeia");
        try{
            return serverRMI.GetIdeia(id_topico,id_ideia);
        } catch (RemoteException e) {
            return null;
        }
    }
    
    public boolean apagarideia(int id_topico, int id_ideia, String username)
    {
        Topico topico = null;
        Utilizador user = null;
        int escolha = 0;
        
        try {
            topico = serverRMI.GetTopico(id_topico);
        } catch (RemoteException ex) {
            System.out.println("aki8");
        }
        
        if(topico == null)
            return false;
        
        String faceAPICode = null;
        for (int i = 0; i < topico.getIdeia().size(); i++) {
            if (topico.getIdeia().get(i).getidideia() == id_ideia) {
                System.out.println("[Bean] Encontrei a ideia ["+id_topico+"]["+id_ideia+"] == "+topico.getIdeia().get(i).getFaceID());
                escolha = i;
                faceAPICode = topico.getIdeia().get(i).getFaceID();
                break;
            }
        }
       
        
        try{

            if(serverRMI.ApagarIdeia(id_topico,escolha,username))
            {
               System.out.println("Apagou a ideia no RMI agor é no facebook.....");
               if(faceAPICode != null)
               {
                   
                    /*** FACEBOOK ***/
                    OAuthRequest request_apagar = new OAuthRequest(Verb.POST, "https://api.facebook.com/method/stream.remove");
                    request_apagar.addHeader("Content-Type", "text/html");
                    request_apagar.addBodyParameter("post_id", faceAPICode);

                    System.out.println("APAGA face == "+faceAPICode);
                    String acesso = serverRMI.getOAuth(username);
            
                    System.out.println("Token em String == "+acesso);
                    Token token = new Token(acesso,"");
                   // OAuthService service = serverRMI.getService();    
                    service.signRequest(token, request_apagar);
                    Response response_apagar = request_apagar.send();

                    System.out.println(response_apagar.getBody());
                    System.out.println("Supostamente mensagem FACEBOOK apagada!!!! ");
                    /*** ------- ***/   
               }
              
               return true;
            }  
            else
                return false;
            
        } catch(RemoteException e) {
            
            return false;
        
        }
    }
    
    public boolean apagarideia(int id_topico, int id_ideia, String username, UUID id)
    {
        Topico topico = null;
        Utilizador user = null;
        int escolha = 0;
        
        try {
            topico = serverRMI.GetTopico(id_topico);
        } catch (RemoteException ex) {
            System.out.println("aki8");
        }
        
        if(topico == null)
            return false;
        
        String faceAPICode = null;
        for (int i = 0; i < topico.getIdeia().size(); i++) {
            System.out.println("[Bean] "+topico.getIdeia().get(i).getId()+" == "+id);
            if(topico.getIdeia().get(i).getId() != null)
                if (topico.getIdeia().get(i).getId().compareTo(id) == 0) {
                    System.out.println("[Bean] Encontrei a ideia ["+id_topico+"]["+id_ideia+"] == "+topico.getIdeia().get(i).getFaceID());
                    escolha = i;
                    faceAPICode = topico.getIdeia().get(i).getFaceID();
                    break;
                }
        }
       
        
        try{
            
                System.out.println("Apagou a ideia no RMI agor é no facebook.....");
                if(serverRMI.ApagarIdeia(id_topico,escolha,username))
                {
                    if(faceAPICode != null)
                    {
                        /*** FACEBOOK ***/
                        OAuthRequest request_apagar = new OAuthRequest(Verb.POST, "https://api.facebook.com/method/stream.remove");
                        request_apagar.addHeader("Content-Type", "text/html");
                        request_apagar.addBodyParameter("post_id", faceAPICode);

                        System.out.println("APAGA face == "+faceAPICode);
                        String acesso = serverRMI.getOAuth(username);
            
                        System.out.println("Token em String == "+acesso);
                        Token token = new Token(acesso,"");
                        System.out.println("Service == "+service);
                        //OAuthService service = serverRMI.getService();
                        service.signRequest(token, request_apagar);
                        Response response_apagar = request_apagar.send();
                        System.out.println(response_apagar.getBody());
                        
                        /*** ------- ***/
                        System.out.println("Supostamente mensagem FACEBOOK apagada!!!! ");
                        
                    }
                    return true;
                }  
                else
                    return false;
            
        } catch(RemoteException e) {
            
            return false;
        
        }
    }
    
    public boolean PermissaoApagar(Ideia ideia, String username)
    {
        try{
            if(serverRMI.PermissaoApagar(ideia,username))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (RemoteException e) {
            return false;
        }

    }
    
    public ArrayList<Accao> GetAccao(int id_topico, int id_ideia)
    {
        Topico topico = null;
        
       try {
        topico = serverRMI.GetTopico(id_topico);
       } catch (RemoteException ex) {
        System.out.println("aki8");
       }
       
       for(Ideia _ideia: topico.getIdeia())
       {
           if(_ideia.getidideia() == id_ideia)
           {
               return _ideia.getAccoes();
           }
       }
       
       return null;
    }
    
    public Accao GetAccao(int id_topico, int id_ideia, String username)
    {
        try
        {
            return serverRMI.getAccao(id_topico,id_ideia,username);
        } catch (RemoteException e){
            return null;
        }
    }
    
    public ArrayList<Accao> GetAccoes(int id_topico, int id_ideia, String username)
    {
        ArrayList<Accao> accoes = new ArrayList<Accao>();
        
        try
        {
           return serverRMI.getAccoes(id_topico,id_ideia);
        } catch (RemoteException e){
            return null;
        }
  
    }
    
    public boolean ComprarAccoes(int id_topico, int id_ideia, String username, int numero_accoes, int precovenda,int precocompra)
    {          
        Topico topico = null;
        String faceAPICode = null;
        
        try
        {
            if(serverRMI.efectuarcompra(username,id_topico,id_ideia,numero_accoes,precovenda,precocompra) == 1)
            {                   
                try {
                    topico = serverRMI.GetTopico(id_topico);
                } catch (RemoteException ex) {
                    System.out.println("aki8");
                }
                System.out.println("Vou percorrer os topicos..");
                
                for(int i = 0; i<topico.getIdeia().size(); i++)
                {
                    if(topico.getIdeia().get(i).getidideia() == id_ideia)
                    {
                        System.out.println("Encontrei a ideia e o seu FaceID == "+topico.getIdeia().get(i).getFaceID());
                        faceAPICode  = topico.getIdeia().get(i).getFaceID();
                        break;
                    }
                }
                
                System.out.println("faceAPICode == "+faceAPICode);
               // System.out.println("Acesso TOken == "+accessToken);
                System.out.println("Sou o user == "+username);
                
                if(faceAPICode != null)
                {
                    /*** FACEBOOK ***/
                    OAuthRequest request2 = new OAuthRequest(Verb.POST, "https://api.facebook.com/method/stream.addComment");
                    request2.addHeader("Content-Type", "text/html");
                    request2.addBodyParameter("post_id", faceAPICode);
                    request2.addBodyParameter("comment", "Foi efectuada uma transação");
                    System.out.println("Username que quer comprar == "+username);
                    String acesso = serverRMI.getOAuth(username);
                    System.out.println("[Responder a Ideia] Token em String == "+acesso);
                    Token token = new Token(acesso,"");
                  //  System.out.println("[Bean] Vou inserir um comentario com Token == \n "+accessToken);
                    
                   // OAuthService service = serverRMI.getService();
                    service.signRequest(token, request2);
                    Response response2 = request2.send();
                    System.out.println(response2.getBody());
                }
                
                return true;
            
            }
            else
                return false;
            
            
        } catch (RemoteException e){
            return false;
        }
    }
    
    public boolean EditarAccao(int id_topico, int id_ideia, String username,double preco_venda)
    {
        try
        {
            if(serverRMI.EditarAccao(id_topico,id_ideia,username,preco_venda))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch(RemoteException e)
        {
            return false;
        }

    }
    
    public ArrayList<Ideia> GetPesquisa(String autor_ideia, int valor_ideia)
    {
        ArrayList<Ideia> Ideias = new ArrayList<Ideia>();
        
        try{
            
            return serverRMI.GetPesquisa(autor_ideia,valor_ideia);
            
        } catch(RemoteException e)
        {
            return null;
        }
    }
    
    public boolean ContarLetras(String str) {  
       
        if (str == null || str.length() == 0)  
            return false;  

        for (int i = 0; i < str.length(); i++) {  

            if (!Character.isDigit(str.charAt(i)))  
                return false;  
        }  

        return true;  
    }
    

        /********** XML PARSING FUNCS ******************/
        private Node getNode(String tagName, NodeList nodes) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    return node;
                }
            }

            return null;
        }

        private String getNodeValue( Node node ) {
            NodeList childNodes = node.getChildNodes();
            for (int x = 0; x < childNodes.getLength(); x++ ) {
                Node data = childNodes.item(x);
                if ( data.getNodeType() == Node.TEXT_NODE )
                    return data.getNodeValue();
            }
            return "";
        }

        private String getNodeValue(String tagName, NodeList nodes ) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    NodeList childNodes = node.getChildNodes();
                    for (int y = 0; y < childNodes.getLength(); y++ ) {
                        Node data = childNodes.item(y);
                        if ( data.getNodeType() == Node.TEXT_NODE )
                            return data.getNodeValue();
                    }
                }
            }
            return "";
        }

        private String getNodeAttr(String attrName, Node node ) {
            NamedNodeMap attrs = node.getAttributes();
            for (int y = 0; y < attrs.getLength(); y++ ) {
                Node attr = attrs.item(y);
                if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                    return attr.getNodeValue();
                }
            }
            return "";
        }

        private String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    NodeList childNodes = node.getChildNodes();
                    for (int y = 0; y < childNodes.getLength(); y++ ) {
                        Node data = childNodes.item(y);
                        if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                            if ( data.getNodeName().equalsIgnoreCase(attrName) )
                                return data.getNodeValue();
                        }
                    }
                }
            }

            return "";
        }
   
   /** ------------------------------------- **/
    public String getHistorico(int idtopico,int idideia) throws RemoteException
    {
        String resposta;
        System.out.println("HISTORICO ");
          try{
             resposta=serverRMI.historico(idtopico,idideia);
             return resposta;
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public int takeover(int idtopico,int idideia) throws RemoteException
    {
        int resposta=0;
        System.out.println("TAKE OVER BEAN");
          try{
             resposta=serverRMI.takeover(idtopico,idideia);
             return resposta;
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        
        
    }
    public String profile(String Username) throws RemoteException
    {
        System.out.println("PERFIL");
        String resposta;
        try{
             resposta=serverRMI.profile(Username);
             return resposta;
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public String portifolio(String Username) throws RemoteException
    {
        System.out.println("ALGO PORTIFOLIO??????????????????");
        String resposta;
        try{
             resposta=serverRMI.portifolio(Username,1);
             return resposta;
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    public String halloffame() throws RemoteException
    {
        System.out.println("ALGO??????????????????");
        String resposta;
        try{
             resposta=serverRMI.halloffame(1);
             return resposta;
        } catch (RemoteException ex) {
            Logger.getLogger(IdeaBrokerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    
        
        
  /** ------------------------------------------ **/
}
