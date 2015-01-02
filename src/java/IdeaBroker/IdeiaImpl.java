package IdeaBroker;

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.net.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

@SuppressWarnings("unchecked, deprecation")
public class IdeiaImpl extends UnicastRemoteObject implements IdeiaMethods {

	/**
	 * 
	 **/
	private static final long serialVersionUID = 1L;
	private ArrayList<Utilizador> Utilizadores= new ArrayList<Utilizador>();
	private ArrayList<Topico> Topicos = new ArrayList<Topico>();
	private ArrayList<Topico> TopicoBackup = new ArrayList<Topico>();
	/**<-------------->**/

	//private  ArrayList<ClienteTCP> = new ArrayList<ClienteTCP>();
        private  ArrayList<String> OnlineUsers = new ArrayList<String>();
        private  ArrayList<Utilizador> ListaUtilizadores = new ArrayList<Utilizador>();
	private  ArrayList<NotificacaoRMI> NotasAgendadas= new ArrayList<NotificacaoRMI>();
      
        public static Vector<String> onlineUserWeb = new Vector<String>();
        public static ClienteRMIInterface cliente;
       // private ArrayList<Topico> TopicoBackup = new ArrayList<Topico>();
        private ArrayList<Utilizador> UtilizadorBackup = new ArrayList<Utilizador>();
        public static Vector<ClienteRMIInterface> ligacoes = new Vector<ClienteRMIInterface>();
        public static WebClienteInterface WebCliente = null;
        public static Token accessToken;
        public static OAuthService service;
                
        //public static
        
     //   public static Hashtable<String, Token> tokens;
        
        /**<----------------->**/
	
	public IdeiaImpl() throws RemoteException {
		super();
		loaddados();
	}

	/** <-------------------- META 2 --------------------> **/
        
        /** <------------------> **/
        public void setService(OAuthService _service)
        {
            System.out.println("[RMI] ----> SETService == "+_service);
            this.service = _service;
        }

        public OAuthService getService()
        {
            System.out.println("getService == "+service);
            return service;
        }
        /** <-------------------> **/
        
        
        public void adicionaToken(String username, String acesso)
        {
            System.out.println("[RMI] Username == "+username+" acesso =="+acesso);
            if(!Utilizadores.isEmpty())
                for(int i = 0; i < Utilizadores.size(); i++)
                {
                    if(Utilizadores.get(i).getusername().compareTo(username) == 0)
                    {
                        
                        System.out.println("Vai adicioanr acesoo....");
                        Utilizadores.get(i).setAcesso(acesso);
                        System.out.println("Adicionou");
                      /*  try {
                            savedados();
                        } catch (IOException ex) {
                            System.out.println("Erro RMI!!!!");
                            Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                    }
                }
        }
        
        public void setOAuth(String username,Token code)
        {
            System.out.println("<------------------------------------------------------>");
            System.out.println("RMI VAI MUDAR DE TOKEN...");
            System.out.println("Sou o Token == "+code);
            System.out.println("<------------------------------------------------------>");
            
            accessToken = code;
        }
    
        public String getOAuth(String username)
        {
            if(!Utilizadores.isEmpty())
                for(int i = 0; i < Utilizadores.size(); i++)
                {
                    if(Utilizadores.get(i).getusername().compareTo(username) == 0)
                    {
                        return Utilizadores.get(i).getAcesso();
                    }
                }
            return null;
        }
        
        @Override
        public void Webcliente(WebClienteInterface _WebCliente) throws RemoteException
        {
            System.out.println("[RMI] Novo Cliente no RMI");
            this.WebCliente = _WebCliente;
        }
        
        @Override
        public void adicionaOnlineWeb(String username) throws RemoteException
        {
            if(!onlineUserWeb.contains(username))
            {
                onlineUserWeb.add(username);
                //WebCliente.mostraOnline(this.listaOnline());
            }
        }
        
        public Vector<String> listaOnline() throws RemoteException 
        {
            Vector<String> lista = new Vector<String>();
            
          /*for(ClienteRMIInterface c: ligacoes)
            {
                if(!lista.contains(c.getUsername())) {
                    lista.add(c.getUsername());
                }
                
            }*/
            
            //Adiciona tambem aqueles que pertencem ao TCP
            for( String s : OnlineUsers)
            {
                if(!lista.contains(s)) {
                    lista.add(s);
                }
            }
            
            for( String s : onlineUserWeb)
            {
                if(!lista.contains(s)) {
                    lista.add(s);
                }
            }   
            return lista;
        }
        
        @Override
        public void retiraOnlineWeb(String username) throws RemoteException {
            onlineUserWeb.remove(username);
            //WebCliente.mostraOnline(this.listaOnline());
        }
        
        public Accao getAccao(int id_topico, int id_ideia, String username)
        {   
            ArrayList<Accao> accao = new ArrayList<Accao>();
            
            if(!Topicos.isEmpty())
            for(int i = 0 ; i < Topicos.size(); i++)
            {
                if(Topicos.get(i).getidtopico() == id_topico)
                {
                    if(!Topicos.get(i).getIdeia().isEmpty())
                    for(int j = 0; j < Topicos.get(i).getIdeia().size(); j++)
                    {
                        if(Topicos.get(i).getIdeia().get(j).getidideia() == id_ideia)
                        {
                            for(Accao accao_aux : Topicos.get(i).getIdeia().get(j).getAccoes())
                            {
                                if(accao_aux.getutilizador().compareTo(username) == 0)
                                {
                                    return accao_aux;
                                }
                            }
                            //return Topicos.get(i).getIdeia().get(j).getAccoes();
                        }
                    }
                }
            }
            
            return null;
        }
        
        public ArrayList<Accao> getAccoes(int id_topico, int id_ideia)
        {   
            if(!Topicos.isEmpty())
            for(int i = 0 ; i < Topicos.size(); i++)
            {
                if(Topicos.get(i).getidtopico() == id_topico)
                {
                    if(!Topicos.get(i).getIdeia().isEmpty())
                    for(int j = 0; j < Topicos.get(i).getIdeia().size(); j++)
                    {
                        if(Topicos.get(i).getIdeia().get(j).getidideia() == id_ideia)
                        {
                            return Topicos.get(i).getIdeia().get(j).getAccoes();
                        }
                    }
                }
            }
            
            return null;
        }
        
        public synchronized boolean EditarAccao(int id_topico, int id_ideia, String username, double novo_preco)
        {            
            if(!Topicos.isEmpty())
            {
                for(int i = 0; i < Topicos.size() ; i++)
                {
                    if((Topicos.get(i).getidtopico() == id_topico) && (!Topicos.get(i).getIdeia().isEmpty()))
                    {
                        for(int j = 0; j < Topicos.get(i).getIdeia().size(); j++)
                        {
                            if(Topicos.get(i).getIdeia().get(j).getidideia() == id_ideia)
                            {
                                for(int k = 0; k < Topicos.get(i).getIdeia().get(j).getAccoes().size(); k++)
                                {
                                    if((Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getutilizador().compareTo(username)) == 0)
                                    {
                                        Topicos.get(i).getIdeia().get(j).getAccoes().get(k).setprecoaccao(novo_preco);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                 return false;
            }
            else
            {
                return false;
            }

        }
        
        public ArrayList<Ideia> GetPesquisa(String autor_ideia, int valor_ideia)
        {
            ArrayList<Ideia> ideia_aux = new ArrayList<Ideia>();

            if(autor_ideia != null)
            {                
                if(!Topicos.isEmpty())
                    for(int i=0; i < Topicos.size(); i++)
                    {
                        if(!Topicos.get(i).getIdeia().isEmpty())
                            for(Ideia ideia: Topicos.get(i).getIdeia())
                            {
                                if(ideia.getautor().compareTo(autor_ideia) == 0)
                                {
                                    ideia_aux.add(ideia);
                                }
                            }
                        
                    }
                if(!ideia_aux.isEmpty())
                    return ideia_aux;
            }
            
            /*if(valor_ideia != -1)
            {
                
                if(!Topicos.isEmpty())
                    for(int i=0; i < Topicos.size(); i++)
                    {
                        if(!Topicos.get(i).getIdeia().isEmpty())
                            for(Ideia ideia: Topicos.get(i).getIdeia())
                            {
                                if(ideia.getautor().compareTo(autor_ideia) == 0)
                                {
                                    topico_aux.add(ideia);
                                    
                                }
                            }
                        
                    }
            }*/
            
            return null;
        }
        /** <-------------------------------------------------> **/
        
	public void adicionarUsersOnline(String _onlineuser) throws java.rmi.RemoteException, FileNotFoundException, IOException
	{
		//System.out.println("Vou adicionar um novo utilziador == "+_onlineuser+"\n");
		//utilizadoresOnline.add(_onlineuser);
		
                //savedados();
	}


	public ArrayList<String> getOnlineUsers() throws RemoteException 
        {
		return OnlineUsers;
	}

        @Override
	public String verficarNotificacao(String _user) throws java.rmi.RemoteException
	{
		String NotificacaoLocal = null;

		/** Construi o String de notificacoes do utilizador especifico **/
		if(!NotasAgendadas.isEmpty()) {
                for (NotificacaoRMI notas : NotasAgendadas) {
                    if (notas.getUser().compareTo(_user) == 0) {
                            if(notas.getExecutado() == 0) {
                            NotificacaoLocal = notas.getNotificao() + "\n";
                        }
                    }
                }
            }

	    //System.out.println("Vou enviar a notificacao == "+NotificacaoLocal+"\n");        
            return NotificacaoLocal;
        
	}

        @Override
	public void adicionarNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException
	{
            NotasAgendadas.add(new NotificacaoRMI(notificacao,user,0));
            /*try {
                savedados();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            }*/
	}

        @Override
	public void removerNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException
	{
            /** Tem que procurar NOtificacao **/
            NotasAgendadas.remove(new NotificacaoRMI(notificacao,user,0));
            /*try {
                savedados();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            }*/
	}

        @Override
	public void Notificacao_vista(int indice) throws java.rmi.RemoteException
	{
            NotasAgendadas.get(indice).setExecutado(1);
           /* try {
                savedados();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
            }*/
	}
        
        @Override
        public boolean verifica_ideia(int id_topico, int id_ideia)
        {           
            System.out.println("Verifica Ideia");
            if((!Topicos.isEmpty()) && (Topicos.size() > id_topico))
            {
                for(int i = 0; i < Topicos.get(id_topico).getIdeia().size() ; i++)
                {
                    System.out.println("ID ideia "+id_ideia+" // Ideideia == "+Topicos.get(id_topico).getIdeia().get(i).getidideia());
                    if(Topicos.get(id_topico).getIdeia().get(i).getidideia() == id_ideia)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return false;
            }
            
            return false;
        }
        
        @Override
        public boolean PermissaoApagar(Ideia ideia, String username)
        {
            System.out.println("PermissaoApagar");
            for(Accao accao : ideia.getAccoes())
            {
                System.out.println(username+" => "+accao.getutilizador()+" == "+(accao.getutilizador().compareTo(username)));
                if((accao.getutilizador().compareTo(username)) != 0)
                {
                    System.out.println("Entrou no if "+username+" => "+accao.getutilizador()+" comparacao == "+(accao.getutilizador().compareTo(username)));
                    return false;
                }
            }
            
            
            return true;
        }
        
        
        @Override
        public boolean verifica_topico(int id_topico)
        {
            if(!Topicos.isEmpty())
                for(int i = 0; i < Topicos.size(); i++)
                {
                    if(Topicos.get(i).getidtopico() == id_topico)
                    {
                        return true;
                    }
                }
                
            else
                return false;
            return false;
        } 
        
        @Override
        public boolean verifica_tema_topico(String tema)
        {
            for(int i=0;i<Topicos.size();i++)
            {
                if(Topicos.get(i).gettema().compareTo(tema) == 0)
                {
                    System.out.println("Tópico já existente.");
                    return true;
                }
            }
            
            return false;
        }
        
        @Override
        public Topico GetTopico(int id_topico)
        {
            if(!Topicos.isEmpty())
            {
                System.out.println("ID == "+id_topico+" == "+Topicos.get(id_topico).gettema());
                for(int i = 0; i < Topicos.size(); i++)
                {
                    if(Topicos.get(i).getidtopico() == id_topico)
                    {
                        return Topicos.get(i);
                    }
                }
                
            }
            else
            {
                return null;
            }
            
            return null;
        }
        
        @Override
        public Utilizador getAutorIdeia(Ideia ideia)
        {
            if(!Utilizadores.isEmpty())
            {
                for(int i = 0; i < Utilizadores.size(); i++)
                {
                    if(ideia.getautor().compareTo(Utilizadores.get(i).getusername()) == 0)
                    {
                        return Utilizadores.get(i);
                    }
                }
            }
            return null;
            
        }
	/*public void enviaNotificacao(String notificacao ) throws RemoteException
        {
   		for(ClientInterface userOnline:utilizadoresOnline)
    	{
    		userOnline.addNotification(notificacao);
    	}
    	
    }*/
        @Override
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
        
	/**<------------------>**/
	

	public void say(String saywhat)
	{
		System.out.println("[Server]:" + saywhat);
	}
	
       
        
        @Override
	public int adicionartopico(String username, String tema) throws java.rmi.RemoteException
	{
               System.out.println("Vou criar o Topico == "+tema+" \n");
		// Verifica se existe tema igual
                if(!verifica_tema_topico(tema))
                {
                    //Adiciona o topico dado ao arraylist topico
                    Topicos.add(new Topico(geraridtopico(Topicos)+1,tema,username,null));
                    try {
                        savedados();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                        return -1;
                    } catch (IOException ex) {
                        Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                        return -1;
                    }
                }
                else
                    return -1;
                
		return 1;
	}

	
	public void savedados() throws FileNotFoundException, IOException
	{
		//Gravar dados para ficheiro
		say("Gravando Utilizadores.ser");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("utilizadores.ser");
                 ObjectOutputStream oout = new ObjectOutputStream(out);
                    for(int i=0;i<Utilizadores.size();i++)
                    {
                            oout.writeObject(Utilizadores.get(i));
                    }
                    out.close();
                    oout.close();
                }catch (Exception ex) {
                }
	     
                
                
		//Gravar dados para ficheiro
		say("Gravando Tópicos.");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("topicos.ser");
                 ObjectOutputStream oout = new ObjectOutputStream(out);
                    for(int i=0;i<Topicos.size();i++)
                    {
                            oout.writeObject(Topicos.get(i));
                    }
                out.close();
                oout.close();

	      } catch (Exception ex) {
	      }

	    //Gravar dados para ficheiro
		say("Gravando Notificação.");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("notificacao.ser");
                 ObjectOutputStream oout = new ObjectOutputStream(out);
                    for(int i=0;i<NotasAgendadas.size();i++)
                    {
                            oout.writeObject(NotasAgendadas.get(i));
                    }
                out.close();
                oout.close();

	      } catch (Exception ex) {
	      }

	}
	
        @Override
	public String historicoutilizador(String username) throws java.rmi.RemoteException
	{
		String texto="\nErro Interno :(";
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				if(!Utilizadores.get(i).getNotificacao().isEmpty())
				{
					texto="O seu historico de acções:\n";
					for(int j=Utilizadores.get(i).getNotificacao().size()-1;j>=0;j--)
					{	
						texto+="\n\t";
						texto+=Utilizadores.get(i).getNotificacao().get(j).getNotificao();
							
					}
				}
				else
				{
					texto="Não tem histórico de acções :( , Faça alguma coisa ! :)";
				}

				return texto;
			}
		}
		return texto;
	}

        @Override
	public String dadosutilizador(String username) throws java.rmi.RemoteException
	{
		String texto="\nErro Interno :(";
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				//Get data
				texto="O seu perfil:\n";
				texto+="\tID:" + Utilizadores.get(i).getiduser();
				texto+="\n\tUtilizador:" + Utilizadores.get(i).getusername();
				texto+="\n\tSaldo:" + Utilizadores.get(i).getsaldo();
				texto+="\n--------------------------------------";
				return texto;
			}
		}
		return texto;
	}
        
        public void enviarNotificacao(String username, String notificacao) throws RemoteException
        {
           /* for(ClienteRMIInterface user: utilizadoresOnline)
            {*/
                System.out.println("Vou enviar uma notificacao ["+ligacoes.size()+"]");
                if(!ligacoes.isEmpty())
                for(ClienteRMIInterface _cliente : ligacoes)
                {
                    System.out.println("Estou a percorrer Enviar notificacao ao == "+username);
                    System.out.println("Estou a percorrer ENTRA IF");
                    _cliente.addNotification(username,notificacao);
                }
                
           // }
        }
        
        public void enviarNotificacaoWeb(String user_destino, String notificacao)
        {
              try {
                   System.out.println("Vou Enviar uma notificaca ovia WEB ... ");
                   if(WebCliente!=null)
                    WebCliente.addNote(user_destino,notificacao);

                } catch (RemoteException ex) {
                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
              
           
           
           
        @Override
        public synchronized int efectuarcompra(String comprador, int idtopico, int idideia, int percentagem, int precorevenda, int investimento) throws java.rmi.RemoteException {
            int idcomprador = -1,tem_accoes=0,idaccaocomprador=-1;
        
        System.out.println("ENTRA1");
        TopicoBackup = Topicos;
        UtilizadorBackup=Utilizadores;
        String[][] notificacoes = new String[100][100];
        int cont=0;
        
        System.out.println("STRING COMPRADOR=" + comprador);
        System.out.println("INT IDTOPICO=" + idtopico);
        System.out.println("INT IDIDEIA=" + idideia);
        System.out.println("INT PERCENTAGEM=" + percentagem);
        System.out.println("INT PRECOREVENDA=" + precorevenda);
        System.out.println("INT INVESTIMENTO=" + investimento);

                //Passo -1 Encontrar o ou os vendedores..
        //Passo 1 Trabalhar sobre variavel auxiliar para simular uma transação A a ponto B apenas
        //Passo 2 - Encontrar no ArrayList o Utilizador  vendedor e o Utilizador Comprador
        idcomprador=encontraruser(comprador);
        if(percentagem<1 || percentagem>100)
        {
            System.out.println("Percentagem Invalida");
            return 0;
        }
        //Ver se foi encontrado o utilizador
        if (idcomprador == -1) {
            //Um dos utilizadores nao foi encontrado
            return 0;
        }
            //Passo 3- Encontrar Transações Necessárias para satisfazer a percentagem pedida , encontrado sempre o preço mais barato por cada 1% de share
            int percentagemactual = 0;
            int naccoes = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size();
            ArrayList<Integer> transacoes = new ArrayList<Integer>();
            double min;
            int indice;
            double a_pagar = 0;
            while (percentagemactual < percentagem) 
            {
                min = 999999999;
                indice = -1;
                for (int i = 0; i < naccoes; i++) 
                {    
                    if (transacoes.contains(i)) {} 
                    else {
                        //Passo 3.1 Verificar se preco e menor q o min encontrado ate agora
                        double precoapagar = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpreco() / Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpercentagem();
                        if ((precoapagar < min) && (TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getutilizador().compareTo(comprador))!=0)
                        {
                            min = precoapagar;
                            indice = i;
                        }
                    }

                }
                if (percentagemactual + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpercentagem() <= percentagem) 
                {
                    percentagemactual = percentagemactual + Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpercentagem();
                    a_pagar = a_pagar + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpreco();
                    transacoes.add(indice);
                } else {
                    int percentagemnecessaria = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpercentagem() - (percentagemactual + Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpercentagem() - percentagem);
                    a_pagar = a_pagar + (percentagemnecessaria * TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpreco() / TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(indice).getpercentagem());
                    transacoes.add(indice);
                    percentagemactual = percentagemactual + percentagemnecessaria;
                }
                System.out.println("PERCENTAGEM NA FINDER:" + percentagemactual);
            }
            System.out.println("A PAGAR = " + a_pagar);
            //Passo 4 : Ver se Comprador tem saldo suficiente.
            if (investimento >= a_pagar) 
            {
                System.out.println("ENTRA2");
                int encontrado;
                int idaccao;
                String vendedor;
                int idvendedor;
                int percentagemaux;
                double precosharei;
                percentagemactual=0;
                //Tem saldo suficiente logo partir par ao passo 5
                //Passo 5 - Retirar saldo do comprador para os vendedores
                for (int i = 0; i < transacoes.size() - 1; i++) 
                {
                    System.out.println("ENTRA3");
                    //Passo 5.1 - Buscar nome vendedor
                    encontrado = 0;
                    idaccao = transacoes.get(i);
                    vendedor = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getutilizador();
                    precosharei = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco();
                    percentagemaux = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem();
                    idvendedor = 0;
                    //Passo 5.2  Encontrar Utilizador no arraylist utilizadores
                    idvendedor=encontraruser(vendedor);
                    if (idvendedor == -1) {
                        //Erro 
                        return 0;

                    }
                    //Passo 5.3 Efetuar a transação nº i
                    System.out.println("-----------------Transação nº" + i + "-----------------");
                    System.out.println("Retirando Saldo ao utilizador:" + UtilizadorBackup.get(idcomprador).getusername());
                    System.out.println("Adicionando Saldo ao utilizador:" + UtilizadorBackup.get(idvendedor).getusername());
                    //Passo 5.4 Retirar Percentagem ao Vendedor e adicionala ao comprador
                    System.out.println("PRECOSHARE I="+ precosharei);
                    UtilizadorBackup.get(idcomprador).retirarsaldo(precosharei);
                    UtilizadorBackup.get(idvendedor).adicionarsaldo(precosharei);
                    
                       //Passo 5.5 Verificar se o Comprador tem já acções desta ideia, Se tiver então adicionar percentagem , se não tiver, então vamos criar um novo
                    //"registo" no arraylist Accoes da Ideia em questão e adicionar a percentagem comprada na transação i;
                    if(tem_accoes==0)
                    {
                        idaccaocomprador=temaccoes(comprador,idtopico,idideia,precorevenda);
                        tem_accoes=1;
                    }     
                    //Retirar percentagem
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagemaux);
                    //Adicionar nova accção e atribui percentagem;
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccaocomprador).adicionaraccao(percentagemaux);
                    
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getHistorico().add(new Historico(geraridhistorico(idtopico,idideia)+1,"Foram comprados ao utilizador ["+ vendedor +"]"+percentagemaux+ "% de Shares a "+ precosharei));
                    percentagemactual=percentagemactual+percentagemaux;
                    notificacoes[cont][0]=vendedor;
                    notificacoes[cont][1]="Compraram-lhe "+ percentagemaux+ " % accoes da ideia nº " + idideia + " pertencente ao topico "+ idtopico;
                    cont++;
                    
                    UtilizadorBackup.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou " + percentagemaux + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
                    

                }
                
                //Fazer ultima transação
                tem_accoes=0;
                idaccao = transacoes.get(transacoes.size() - 1);
                System.out.println("PER=" +percentagemactual);
                if (percentagemactual + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem() == percentagem) 
                {     //Passo 5.1 - Buscar nome vendedor
                    System.out.println("ENTRA4");
                    vendedor = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getutilizador();
                    precosharei = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco();
                    percentagemaux = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem();
                    idvendedor = 0;
                    //Passo 5.2  Encontrar Utilizador no arraylist utilizadores
                    idvendedor=encontraruser(vendedor);
                    
                    if (idvendedor == -1) {
                        //Erro 
                        return 0;

                    }
                    //Passo 5.3 Efetuar a transação nº i
                    System.out.println("-----------------Transação final -----------------");
                    System.out.println("Retirando Saldo ao utilizador:" + UtilizadorBackup.get(idcomprador).getusername());
                    System.out.println("Adicionando Saldo ao utilizador:" + UtilizadorBackup.get(idvendedor).getusername());
                    //Passo 5.4 Retirar Percentagem ao Vendedor e adicionala ao comprador
                    System.out.println("PRECOSHARE I="+ precosharei);
                    UtilizadorBackup.get(idcomprador).retirarsaldo(precosharei);
                    UtilizadorBackup.get(idvendedor).adicionarsaldo(precosharei);

                       //Passo 5.5 Verificar se o Comprador tem já acções desta ideia, Se tiver então adicionar percentagem , se não tiver, então vamos criar um novo
                    //"registo" no arraylist Accoes da Ideia em questão e adicionar a percentagem comprada na transação i;
                    if(tem_accoes==0)
                    {
                        idaccaocomprador=temaccoes(comprador,idtopico,idideia,precorevenda);
                        tem_accoes=1;
                    }     
                    //Retirar percentagem
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagemaux);
                    //Adicionar nova accção e atribui percentagem;
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccaocomprador).adicionaraccao(percentagemaux);
                    UtilizadorBackup.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou " + percentagemaux + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getHistorico().add(new Historico(geraridhistorico(idtopico,idideia)+1,"Foram comprados ao utilizador ["+ vendedor +"]"+percentagemaux+ "% de Shares a "+ precosharei));
                    notificacoes[cont][0]=vendedor;
                    notificacoes[cont][1]="Compraram-lhe "+ percentagemaux+ " % accoes da ideia nº " + idideia + " pertencente ao topico "+ idtopico;
                    cont++;
                    if(terminarcompra(idtopico,idideia,notificacoes,cont)==1)
                    {
                        printransacoesbackup(idtopico,idideia);
                        return 1;
                        
                    }
                    else
                    {
                        return 0;
                    }   
                } 
                else    
                {
                    System.out.println("ENTRA5");
                    int percentagemnecessaria = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem() - (percentagemactual + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem() - percentagem);
                    System.out.println("PERCENTAGEM NECSSARIA=" + percentagemnecessaria);
                    a_pagar = (percentagemnecessaria * TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco() / TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem());
                    System.out.println("A_PAGAR=" + a_pagar);
                    vendedor = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getutilizador();
                    precosharei = TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco();
                    idvendedor = 0;
                    encontrado=0;
                    //Passo 5.2  Encontrar Utilizador no arraylist utilizadores
                    idvendedor=encontraruser(vendedor);
                    if(idvendedor==-1)
                    {
                        return 0;
                    }
                    //Passo 5.3 Efetuar a transação nº i
                    System.out.println("-----------------Transação final:-----------------");
                    System.out.println("Retirando Saldo ao utilizador:" + UtilizadorBackup.get(idcomprador).getusername());
                    System.out.println("Adicionando Saldo ao utilizador:" + UtilizadorBackup.get(idvendedor).getusername());
                    //Passo 5.4 Retirar Percentagem ao Vendedor e adicionala ao comprador
                    UtilizadorBackup.get(idcomprador).retirarsaldo(a_pagar);
                    UtilizadorBackup.get(idvendedor).adicionarsaldo(a_pagar);
                        //Passo 5.5 Verificar se o Comprador tem já acções desta ideia, Se tiver então adicionar percentagem , se não tiver, então vamos criar um novo
                    //"registo" no arraylist Accoes da Ideia em questão e adicionar a percentagem comprada na transação i;
                     if(tem_accoes==0)
                    {
                        idaccaocomprador=temaccoes(comprador,idtopico,idideia,precorevenda);
                        tem_accoes=1;
                    }     
              
                       //5.6 Se utilizador nao tiver acções Criar novo Indice
                    //Retirar percentagem
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagemnecessaria);
                    //Adicionar nova accção e atribui percentagem;
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccaocomprador).adicionaraccao(percentagemnecessaria);
                    Utilizadores.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou " + percentagemnecessaria + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
                    //Set NEW SELLER PRICE
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).setprecoaccao(TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco()-a_pagar);
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getHistorico().add(new Historico(geraridhistorico(idtopico,idideia)+1,"Foram comprados ao utilizador ["+ vendedor +"]"+percentagemnecessaria+ "% de Shares a "+ a_pagar));
                    notificacoes[cont][0]=vendedor;
                    notificacoes[cont][1]="Compraram-lhe "+ percentagemnecessaria+ " % accoes da ideia nº " + idideia + " pertencente ao topico "+ idtopico;
                    cont++;
                    if(terminarcompra(idtopico,idideia,notificacoes,cont)==1)
                    {
                        printransacoesbackup(idtopico,idideia);
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
                
            } else 
                {
                    //Erro
                    System.out.println("Erro , Comprador não tem saldo suficiente");
                    return 0;
                }  
        
        }
             public int eliminaraccoesinvalidas2(int idtopico, int idideia) throws java.rmi.RemoteException
        {
            if(TopicoBackup.isEmpty())
            {
                System.out.println("TOPICO EMPTY WTFFF!");
            }
            else if(TopicoBackup.get(idtopico).getIdeia().isEmpty())
            {
                System.out.println("IDEIA EMPTY WTFFF!");
            }
            else if(TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().isEmpty())
            {
                System.out.println("ACCOES EMPTY WTFFF!");
            }
            System.out.println("IDTOPICO=" + idtopico);
            System.out.println("IDIDEIA=" + idideia);
            System.out.println("TAMANHO="+TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size());
            for (int i = 0; i < TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size(); i++) 
            {
                if (TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpercentagem() == 0) 
                {
                    TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().remove(i);
                    return 0;
                } 
                else if (TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpercentagem() < 0) 
                {
                    return -1;
                }
            }
            return 1;
        }

    public int eliminaraccoesinvalidas(int idtopico, int idideia) throws java.rmi.RemoteException 
    {    
        int notdone=0;
        while(notdone==0)
        {
            notdone=eliminaraccoesinvalidas2(idtopico,idideia);
        }
        if(notdone==1)
        return 1;
        else
        return -1;

    }
        
    public int encontraruser(String Username)
    {
       for (int i = 0; i < Utilizadores.size(); i++) {
            if (UtilizadorBackup.get(i).getusername().compareTo(Username) == 0) 
            {
                    return i;
                    //Erro interno terminar
                    
            }
       }
       return -1;
    }
    
        
 
        
    public int temaccoes(String quem,int idtopico,int idideia,int precorevenda)
    {
        for (int j = 0; j < TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size(); j++) 
        {
          if (TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(j).getutilizador().compareTo(quem) == 0) 
          {
              return j;
          }
        }
        TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().add(new Accao(precorevenda,0,quem));
        return TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size()-1;
    }
    public void enviarnotificacoes(String [][] aenviar,int cont, int idtopico, int idideia) throws java.rmi.RemoteException
    {
        for(int i=0;i<cont;i++)
        {
            enviarNotificacao(aenviar[i][0],aenviar[i][1]);
            enviarNotificacaoWeb(aenviar[i][0],aenviar[i][1]);
            if(WebCliente!=null)
                WebCliente.updateprice(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes(),idtopico,idideia);
         
        }
    }
    public int terminarcompra(int idtopico,int idideia,String[][] aenviar,int cont) throws java.rmi.RemoteException
    {
        if (eliminaraccoesinvalidas(idtopico, idideia) == 1) 
        {
                    //Completar transação copiando alterações para topicos original;
                    Topicos=TopicoBackup;
                    Utilizadores=UtilizadorBackup;
                       try {
                        savedados();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     enviarnotificacoes(aenviar,cont,idtopico,idideia);
                    return 1;
         } 
         else 
         {
            return 0;
         }
    }
    public void printransacoesbackup(int idtopico,int idideia)
    {
        for(int i=0;i<TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().size();i++)
        {
            System.out.println("Transação nº" + i);
            System.out.println("De" + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getutilizador());
            System.out.println("Percentagem:" + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpercentagem());
            System.out.println("Preco:" + TopicoBackup.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpreco());
            System.out.println("--------------------------------------------");
            
        }
    }
        
    
        
        // Nao sabemos o vendedor, nos e q temos que decidir...
	/*public synchronized int efectuarcompra(String comprador,String vendedor,int idtopico,int idideia,int idaccao,int percentagem,int precorevenda) throws java.rmi.RemoteException
	{
		int idvendedor=-1,idcomprador=-1;
                
                //Passo -1 Encontrar o ou os vendedores..
                
		//PassoWebCliente.updateprice(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes(),idtopico,idideia); 1 Trabalhar sobre variavel auxiliar para simular uma transação A a ponto B apenas 
		//Passo 2 - Encontrar no ArrayList o Utilizador  vendedor e o Utilizador Comprador
		for(int i=0;i<Utilizadores.size();i++)
		{
			if((Utilizadores.get(i).getusername().compareTo(comprador)==0) || (Utilizadores.get(i).getusername().compareTo(vendedor)==0))
			{
				//Ver qual e
				if(Utilizadores.get(i).getusername().compareTo(comprador)==0)
				{
					idcomprador=i;
				}
				else if(Utilizadores.get(i).getusername().compareTo(vendedor)==0)
				{
					idvendedor=i;
				}
				else
				{
					//Erro interno terminar
					return 0;
				}
			}
		}
                
		//Ver se foi encontrado os dois utilizadores
		if((idcomprador!=-1) && (idvendedor!=-1))
		{
			//Passo 3- Reverificar se acção é valida de ser comprada
			if((Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getutilizador().compareTo(vendedor)==0) && (Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()>=percentagem))
			{
				//Passo 4 : Ver se Comprador tem saldo suficiente.
				double a_pagar=(percentagem*Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco())/Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem();
				if(Utilizadores.get(idcomprador).getsaldo()>a_pagar)
				{ 
					//Tem saldo suficiente logo partir par ao passo 5
					//Passo 5 - Retirar saldo do comprador para o vendedor
					System.out.println("Retirando Saldo ao utilizador:" + Utilizadores.get(idcomprador).getusername());
					System.out.println("Adicionando Saldo ao utilizador:" + Utilizadores.get(idvendedor).getusername());
					Utilizadores.get(idcomprador).retirarsaldo(a_pagar);
					Utilizadores.get(idvendedor).adicionarsaldo(a_pagar);
					//Passo 6- Retirar percentagem ao Vendedor e adicionala ao comprador
						//Passo 6.1 Ver se este utilizador ja tem algumas acções 
						for(int j=0;j<Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().size();j++)
						{
							if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(j).getutilizador().compareTo(comprador)==0)
							{
								//Adicionar percentagem
								Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(j).adicionaraccao(percentagem);
								//Retirar percentagem
								Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagem);
								if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()==0)
								{
									Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().remove(idaccao);
									
								}
								Utilizadores.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou "+ percentagem + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
                                                                try {
                                                                    savedados();
                                                                } catch (FileNotFoundException ex) {
                                                                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                                                } catch (IOException ex) {
                                                                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                                                }
								return 1;
							}
								
						}
						
						
						//6.2 Se utilizador nao tiver acções Criar novo Indice
						//Retirar percentagem
						Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagem);
						if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()==0)
						{
							Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().remove(idaccao);
							
						}
						//Adicionar nova accção e atribui percentagem;
						Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().add(new Accao(precorevenda,percentagem,comprador));
						Utilizadores.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou "+ percentagem + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
                                                try {
                                                    savedados();
                                                } catch (FileNotFoundException ex) {
                                                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                                }
						return 1;
				}
				else
				{
					//Erro 
					System.out.println("Erro , Comprador não tem saldo suficiente");
					return 0;
				}
			}
			else
			{
				System.out.println("Erro Interno: Ver se accção e valida , terminar");
				return 0;
				
			}

		}
		else
		{
			System.out.println("Um dos utilizadores nao foram encontrados.");
			return 0;
		}
		//Passo 3 Ver saldo do comprador.

	}*/


	public void loaddados() throws RemoteException{
		
		//Carregar dados para ficheiro
		say("Carregando Utilizadores");
		 try{
		   FileInputStream fin = new FileInputStream("utilizadores.ser");
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   Object object;
		   while(fin.available()>0)
		   {
			   //System.out.println("ENTRA");
			   object = ois.readObject();
			   Utilizadores.add((Utilizador) object);
			  
		   }
		   ois.close();
	   }catch(Exception ex){
		   
		   say("Erro a ler ficheiro relativo aos Utilizadores.");
		   
	   } 
		 
		 say("Carregando ficheiro topicos.ser ");
		 try{
			   FileInputStream fin = new FileInputStream("topicos.ser");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   Object object;
			   while(fin.available()>0)
			   {
		
				   object = ois.readObject();
				   Topicos.add((Topico) object);
				  
			   }
			   ois.close();
		   }catch(Exception ex){
			   
			   say("Erro a ler ficheiro relativo aos topicos.");
			   
		   } 
		 say("Carregando ficheiro notificacoes.ser ");
		 try{
			   FileInputStream fin = new FileInputStream("notificacao.ser");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   Object object;
			   while(fin.available()>0)
			   {
		
				   object = ois.readObject();
				   NotasAgendadas.add((NotificacaoRMI) object);
				  
			   }
			   ois.close();
		   }catch(Exception ex){
			   
			   say("Erro a ler ficheiro relativo ás Notificações.");
			   
		   } 
	}

	/**
	* Le dados guardados em ficheiros, 
	**/
	//@SuppressWarnings("unchecked")
	/*public static void lerdados_tcp()
	{
		 try {

            FileInputStream fin = new FileInputStream("data");
            ObjectInputStream ois = new ObjectInputStream(fin);
            
            listaUtilizadores = (ArrayList<Utilizador>) ois.readObject();
            ois.close();
            
            FileInputStream finNotes = new FileInputStream("notas");
            ObjectInputStream oisNotes = new ObjectInputStream(finNotes);
            
            notasAgendadas = (ArrayList<NotificacaoRMI>) oisNotes.readObject();
            oisNotes.close();
            
            fin = new FileInputStream("Timersfile");
            ois = new ObjectInputStream(fin);
            listaTimers = (ArrayList<DelayedNoti>) ois.readObject();
            // Vai pelo vector e verifica que timers deve recuperar e quais deve eliminar
            for(int i = 0; i < listaTimers.size() ; i++){
            	if (listaTimers.get(i).ocorri == 0){
            		// Re-schedule esta notificacao
        		   	Timer t = new Timer();
               		t.schedule(listaTimers.get(i), listaTimers.get(i).pub);
            	      
        	    } else {
        	    	listaTimers.remove(i);
        	    	i--;
        	    }

        	}
            

            ois.close();

            
        } catch (Exception e) {
            //System.out.println("Erro na leitura do disco");
        }
        
	}*/
	
	/**
	* Escreve dados guardados em ficheiros, 
	**/
	//@SuppressWarnings("unchecked")
	/*public static void escreverdados_tcp()
	{
		 try {

            FileOutputStream fout = new FileOutputStream("data");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
           	oos.writeObject(listaUtilizadores);
            oos.close();
            
            FileOutputStream foutNote = new FileOutputStream("notas");
            ObjectOutputStream oosNote = new ObjectOutputStream(foutNote);
            oosNote.writeObject(notasAgendadas);
            oosNote.close();
            
            fout = new FileOutputStream("Timersfile");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(listaTimers);
            oos.close();
           
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro na escrita para o disco");
        }
	}
*/

	public String sayHello() throws RemoteException {
		System.out.println("Printing on server...");
		return "ACK";
	}

	public void remote_print(String s) throws RemoteException {
		System.out.println("Server:" + s);
	}
	
	/*public Message ping_pong(Message m) throws RemoteException {
		Message m1 = new Message("");
		m1.text = m.text + "....";
		return m1;
	}*/

	 /**
     * Procura e devolve um utilizador dado apenas o seu username
     * @param util username
     * @return null caso nao exista nenhum user com estes dados, ou o utilizador encontrado
     */
        public Utilizador procuraUtilizador(String util) throws RemoteException {

            for (Utilizador user : Utilizadores) {
                if (user.getusername().compareTo(util) == 0) {
                    return user;
                }
            }

            return null;
        }
	
	public int gerariduser()
	{
		int max=-1;
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getiduser()>max)
			{
				max=Utilizadores.get(i).getiduser();
			}
		}
		return max;
	}
        
        
	//Validação de dados de login
	public Utilizador login(String username,String password) throws RemoteException
	{
		System.out.println("Chegou Username:" + username);
		System.out.println("Chegou Pass:" + password);
		System.out.println("Utilizadores.size()="+ Utilizadores.size());

		if(Utilizadores.isEmpty())
		{
			return null;
		}
		
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				System.out.println("Encontra username");
				if(password.compareTo(Utilizadores.get(i).getpassword())==0)
				{
					//Retorna Cliente
					say("Utilizador:["+username+"] logado no sistema.");
					return Utilizadores.get(i);
				}
				else
				{
					return null;
				}
			}
		}
		return null;

	}

        public boolean verificaUtilizador(String util, String passEncript, boolean verificaApenasUser) throws RemoteException {
            
            boolean existe = false;
            
            for (Utilizador user : Utilizadores) {
                if ((user.getusername().compareTo(util) == 0) && (user.getpassword().compareTo(passEncript) == 0 || verificaApenasUser)) {
                    existe = true;
                    break;
                }
            }
            return existe;
        }
        
	//Registar um utilizador no sistema
	public Utilizador registar(String username,String password) throws RemoteException
	{
		Utilizador _user = null;
		System.out.println("Chegou Username:" + username);
		System.out.println("Chegou Pass:" + password);

		if(!Utilizadores.isEmpty())
		{
			//Verifica se o utilizador já existe
			for(int i=0;i<Utilizadores.size();i++)
			{
				if((Utilizadores.get(i).getusername().compareTo(username))==0)
				{
					say("Utilizador[" + username + "] já existe no sistema.");
					return null;
				}
				
			}
		}
                
                boolean root = false;
                if(username.compareTo("root") == 0)
                {
                    root = true;
                }
                
		//Se chegou aqui é porque não existe este utilizador
		//Adicionar utilizador ao sistema
		_user = new Utilizador(gerariduser()+1,username,password,false);
		Utilizadores.add(_user);
		say(" Utilizador[" + username + "] foi criado no sistema.");
                try {
                    savedados();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
		return _user;

	}
	
        public void AdicionaCLiente(ClienteRMIInterface  _cliente)
        {
            System.out.println("Adicionar um Cliente TCP");
            ligacoes.add(_cliente);
        } 
        
        public void RemoverLigacao(ClienteRMIInterface _cliente)
        {
            System.out.println("Remove um Cliente TCP");
            ligacoes.remove(_cliente);
        }

	// =======================================================

	public static void main(String args[]) {
		
		if (args.length!=1) {

                    System.out.println("Uso: java IdeiaImpl <this.ip_networkadress>");
                    System.exit(0);

                }
		String ip=args[0];
		System.out.println("IP=" + ip);
		//Carregar dados das classes
		
                System.getProperties().put("java.security.policy", "policy.all");
                System.setProperty("java.rmi.server.hostname", ip);
		System.setSecurityManager(new RMISecurityManager());

		 try {
                       // cliente = new IdeaBrokerServerTCP();
                        
		 	IdeiaImpl h = new IdeiaImpl();
			Naming.rebind("//" + ip + ":1099/Ideia", h);
			
		} catch (RemoteException e) {

			try{
				IdeiaImpl h = new IdeiaImpl();
				Naming.rebind("//localhost:6000/Ideia", h);
			}
			catch(RemoteException e2){
				System.out.println("Erro a iniciar servidor: " + e2);
				return;
			} catch (MalformedURLException e1) {
				System.out.println("MalformedURLException in IdeiaImpl.main: " + e1);
			}

		} catch (MalformedURLException e2) {
			System.out.println("MalformedURLException in IdeiaImpl.main: " + e2);
		}

	}


	public ArrayList<Topico> gettopicos() throws RemoteException {
		return Topicos;
	}
	
	public int geraridideia(int i)
	{
		int max=-1;
		if(Topicos.get(i).getIdeia()!=null)
		{
			for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
			{
				if(Topicos.get(i).getIdeia().get(j).getidideia()>max)
				{
				max=Topicos.get(i).getIdeia().get(j).getidideia();
				
				}
			}
		}
		return max;
	}
	
	public int geraridresposta(int idtopico,int idideia)
	{
		int max=-1;
		if(!Topicos.get(idtopico).getIdeia().get(idideia).getResposta().isEmpty())
		{
			for(int i=0;i<Topicos.get(idtopico).getIdeia().get(idideia).getResposta().size();i++)
			{
				if(Topicos.get(idtopico).getIdeia().get(idideia).getResposta().get(i).getideia().getidideia()>max)
				{
					max=Topicos.get(idtopico).getIdeia().get(idideia).getResposta().get(i).getideia().getidideia();
				}
			}
	
		}
		return max;
	}
	
        public boolean ApagarIdeia(int idtopico, int idideia,String username)
        {
            System.out.println("[ApagarIdeia] Topico == "+idtopico+" Ideia == "+idideia);
            for(int i=0;i<Topicos.size();i++)
            {
                    
                        System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Encontrar a ideia;
				for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
				{
					if(Topicos.get(i).getIdeia().get(j).getidideia()==idideia)
					{
						//Ideia Encontrada , apagala se as acções > 50%
						//Encontrar accções da ideia e verificar se existe alguma com username dado maior que 50%
						for(int k=0;k<Topicos.get(i).getIdeia().get(j).getAccoes().size();k++ )
						{
							if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getutilizador().compareTo(username)==0)
							{
								//Verificar se acçoes maior 50%
								if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getpercentagem()>50)
								{
									
                                                                    try {
                                                                            Topicos.get(i).getIdeia().remove(j);
                                                                            savedados();

                                                                            //Adicionar nas notificações
                                                                            for(int l=0;l<Utilizadores.size();l++)
                                                                            {
                                                                                    if(Utilizadores.get(l).getusername().compareTo(username)==0)
                                                                                    {
                                                                                            Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você apagou a ideia nº" + idideia + " do topico " + idtopico +"."));
                                                                                    }
                                                                            }
                                                                            return true;
                                                                            
                                                                        } catch (IOException ex) {
                                                                              Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                              
                                                                            return false;
                                                                       }
									
								}
								else
								{
									return false;
								}
							}
						}
						
					}
				}
				
			}
		}
		return false;
		
        }
        
	public String apagarideia(int idtopico,int idideia,String username)
	{
                System.out.println("[ApagarIdeia] Topico == "+idtopico+" Ideia == "+idideia);
		for(int i=0;i<Topicos.size();i++)
		{
			System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Encontrar a ideia;
				for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
				{
					if(Topicos.get(i).getIdeia().get(j).getidideia()==idideia)
					{
						//Ideia Encontrada , apagala se as acções > 50%
						//Encontrar accções da ideia e verificar se existe alguma com username dado maior que 50%
						for(int k=0;k<Topicos.get(i).getIdeia().get(j).getAccoes().size();k++ )
						{
							if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getutilizador().compareTo(username)==0)
							{
								//Verificar se acçoes maior 50%
								if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getpercentagem()>50)
								{
									
                                                                    try {
                                                                            Topicos.get(i).getIdeia().remove(j);
                                                                            savedados();

                                                                            //Adicionar nas notificações
                                                                            for(int l=0;l<Utilizadores.size();l++)
                                                                            {
                                                                                    if(Utilizadores.get(l).getusername().compareTo(username)==0)
                                                                                    {
                                                                                            Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você apagou a ideia nº" + idideia + " do topico " + idtopico +"."));
                                                                                    }
                                                                            }
                                                                            return "Ideia apagada";
                                                                            
                                                                        } catch (IOException ex) {
                                                                              Logger.getLogger(IdeiaImpl.class.getName()).log(Level.SEVERE, null, ex);
                                              
                                                                            return "Não tem acções suficientes para apagar esta ideia";
                                                                       }
									
								}
								else
								{
									return "Não tem acções suficientes para apagar esta ideia";
								}
							}
						}
						
					}
				}
				
			}
		}
		return "Erro Interno";
		
	}

	public int responderideia(String natureza,int idtopico,int idideia,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException
	{		
		for(int i=0;i<Topicos.size();i++)
		{
			System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Encontrar a ideia;
				for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
				{
					if(Topicos.get(i).getIdeia().get(j).getidideia()==idideia)
					{
						//Ideia Encontrada , adicionar resposta;
						Topicos.get(i).getIdeia().get(j).getResposta().add(new Resposta(natureza,new Ideia(geraridresposta(idtopico,idideia)+1,texto,null,autor,null,null)));
					}
				}
				return 1;
			}
		}
		return 0;
	}
	
	public boolean verificarSaldo(String autor, double investimento)
        {
            System.out.println("Autor == "+autor+" investimento == "+investimento);
            for(Utilizador user: Utilizadores)
            {
                System.out.println("[Ciclos] "+user.getusername()+" == "+autor);
                if(user.getusername().compareTo(autor) == 0)
                {
                    System.out.println("[Saldo] "+user.getsaldo()+" >= "+investimento);
                    if(user.getsaldo() >= investimento)
                    {
                        System.out.println("VerificarSaldo returna true");
                        user.retirarsaldo(investimento);
                        return true;
                    }
                }
            }
            System.out.println("VerificarSaldo returna false");
            return false;
        }

        
	public int criarideia(int idtopico,String texto,String autor,Anexo anexo, int investimento) throws java.rmi.RemoteException
	{	
                System.out.println("ID_topico == "+idtopico+" texto == "+texto+" autor == "+autor);
                //double preco_por_accoes = investimento / 100000;
		for(int i=0;i<Topicos.size();i++)
		{
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				//retirar investimento ao saldo do utilizador
                                if(!verificarSaldo(autor,investimento))
                                    return 0;
                                
				Accao Accao = new Accao(investimento,100,autor); // preco accoes 100
				Ideia Ideia=new Ideia(geraridideia(i)+1,texto,anexo,autor,null,Accao);
  
				System.out.println("I == "+i+" ideia == "+Ideia.getidideia()+"\n");
				
				Topicos.get(i).getIdeia().add(Ideia);
				for(int l=0;l<Utilizadores.size();l++)
				{
					if(Utilizadores.get(l).getusername().compareTo(autor)==0)
					{
						Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você criou uma ideia no topico " + idtopico +"."));
					}
				}
                                
                                try {
                                    System.out.println("Entra no savedados()");
                                    savedados();
                                    return 1;
                                } catch (IOException ex) {
                                    System.out.println("EROOOOOOOOOOOO");
                                    return 0;
                                }
				
			}
		}
		return 0;
	}
   
	public int criarideia(int idtopico,String texto,String autor,Anexo anexo, int investimento, String face) throws java.rmi.RemoteException
	{	
                System.out.println("ID_topico == "+idtopico+" texto == "+texto+" autor == "+autor);
                
                //double preco_por_accoes = investimento / 100000;
		for(int i=0;i<Topicos.size();i++)
		{
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				//retirar investimento ao saldo do utilizador
                                if(!verificarSaldo(autor,investimento))
                                    return 0;
                                
				Accao Accao = new Accao(investimento,100,autor); // preco accoes 100
				Ideia Ideia=new Ideia(geraridideia(i)+1,texto,anexo,autor,null,Accao);
                                System.out.println("Adicionar ideia ["+idtopico+"]["+Ideia.getidideia()+"] face == "+face);
                                Ideia.setFaceID(face);
                                
				System.out.println("I == "+i+" ideia == "+Ideia.getidideia()+" face =="+face+"\n");
				
				Topicos.get(i).getIdeia().add(Ideia);
				for(int l=0;l<Utilizadores.size();l++)
				{
					if(Utilizadores.get(l).getusername().compareTo(autor)==0)
					{
						Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você criou uma ideia no topico " + idtopico +"."));
					}
				}
                                
                                try {
                                    savedados();
                                    return 1;
                                } catch (IOException ex) {
                                    return 0;
                                }
			}
		}
		return 0;
	}

        public Ideia GetIdeia(int id_topico,int id_ideia)
        {
            if(!Topicos.isEmpty())
            {
                for(int i = 0; i < Topicos.size() ; i++)
                {
                    if((Topicos.get(i).getidtopico() == id_topico) && (!Topicos.get(i).getIdeia().isEmpty()))
                    {
                        for(Ideia ideia : Topicos.get(i).getIdeia())
                        {
                            if(ideia.getidideia() == id_ideia)
                                return ideia;
                        }
                    }
                }
                System.out.println("ID == "+id_topico+" == "+Topicos.get(id_topico).gettema());
                return null;
            }
            else
            {
                return null;
            }
        }
        
      /** -------------------------- **/
        public String historico(int idtopico,int idideia) throws RemoteException
        {
            String returnthis="";
            int passou=0;
              for(int i=0;i<Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().size();i++)
                {
                    passou=1;
                    returnthis=returnthis + "<tr><td>" +Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().get(i).getIDHistorico()+"</td><td>" + Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().get(i).getdescricao() + "</td></tr>";
                }
            if(passou==1)
            {
                return returnthis;
            }
            else
            {
                return "<tr><td colspan='2'>Nao existem transacoes efetuadas.</td></tr>";
            }
        }
        public int encontrar_user(String Username)
        {
            for (int i = 0; i < Utilizadores.size(); i++) {
                if (Utilizadores.get(i).getusername().compareTo(Username) == 0) 
                {
                        return i;
                        //Erro interno terminar
                    
                }
       }
       return -1;
    }
        public int takeover(int idtopico,int idideia) throws RemoteException
        {
            String vendedor;
            int idvendedor;
            System.out.println("ENTREI TAKEOVER RMI");
            for(int i=0;i<Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().size();i++)
            {
                //Passo 1 buscar idvendedor da acção I
                idvendedor=encontrar_user(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getutilizador());
                if(idvendedor==-1)
                {
                    return -1;
                }
                Utilizadores.get(idvendedor).adicionarsaldo(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(i).getpreco());
            }
            //Eliminar accoes da ideia 
           Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().clear();
           //Dar ao root
           Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().add(new Accao(-1,100,"root"));
           return 1;
           
        }
        
        public String profile(String Username) throws RemoteException
        {
            System.out.println("PROFILE RMI");
            String returnthis="";
            for(int i=0;i<Utilizadores.size();i++)
            {
                if(Utilizadores.get(i).getusername().compareTo(Username)==0)
                {
                    return "<tr><td>"+Utilizadores.get(i).getiduser()+"</td><td>"+Utilizadores.get(i).getusername()+"</td><td>"+ Utilizadores.get(i).getsaldo()+"</td><td><a href='portofolio.jsp'>[My Shares]</a></td></tr>";
                    
                }
            }
            return "<tr><td colspan='4'>Erro Interno!</td></tr>";
        }
        
   @Override
   public String portifolio(String Username,int html) throws RemoteException
   {
        System.out.println("CHEGUEI AQUI AO PORTIFOLIO RMI");
        String returnthis="";
        int encontrado=0;
        for(int i=0;i<Topicos.size();i++)
        {
            for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
            {
                for(int k=0;k<Topicos.get(i).getIdeia().get(j).getAccoes().size();k++)
                {
                    if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getutilizador().compareTo(Username)==0)
                    {
                        encontrado=1;
                        returnthis=returnthis+"<tr><td>"+Topicos.get(i).gettema()+"</td><td>"+j+"</td><td>"+ Topicos.get(i).getIdeia().get(j).getdescricao() + "</td><td>"+Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getpercentagem()+"%</td></tr>";


                    }
                }
               
            }
        }
        if(encontrado==1)
        return returnthis;
        else
        return "<tr><td align='center' colspan='4'>Sem Shares.</td></tr>";
     }
        
    @Override
    public String halloffame(int html)  throws RemoteException
    {
        System.out.println("Chamou Hall");
        int encontrado=0;
        String returnthis="";
        if(html==1)
        {
                for(int i=0;i<Topicos.size();i++)
            {
                for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
                {
                    if((Topicos.get(i).getIdeia().get(j).getAccoes().get(0).getpercentagem()==100) && (Topicos.get(i).getIdeia().get(j).getAccoes().get(0).getutilizador().compareTo("root")==0))
                    {
                        encontrado=1;
                        //Adicionar Indice á matriz
                        returnthis=returnthis + "<tr><td>" + i + "</td> <td>" + j + "</td><td>" + Topicos.get(i).getIdeia().get(j).getdescricao() + "</td><td><a href='vertransacoes.jsp?idtopico=" +i+"&ididideia="+j+"</a></td></tr>";
                    }
                }

            }

        }
        else
        {

            for(int i=0;i<Topicos.size();i++)
            {
                for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
                {
                    if((Topicos.get(i).getIdeia().get(j).getAccoes().get(0).getpercentagem()==100) && (Topicos.get(i).getIdeia().get(j).getAccoes().get(0).getutilizador().compareTo("root")==0))
                    {
                        //Adicionar Indice á matriz
                        returnthis=returnthis + " \tIDTopico:" + i + "\t\nIDIdeia:" + j + "\t\nDescricao:" + Topicos.get(i).getIdeia().get(j).getdescricao() + "\n----------------------------------------\n\n";

                    }
                }

            }
        }
        if(encontrado==1)
        {
            return returnthis;
        }
        else
        {
            if(html==1)
            {
                return "<tr><td colspan='4'>Sem Ideias no hall of fame </td></tr>";
            }
            else
            {
                return "\n\tSem ideias no hall of fame.\n";
            }
        }
    } 
    
    public int geraridhistorico(int idtopico,int idideia)
    {
          int max=-1;
		for(int i=0;i<Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().size();i++)
		{
			if(Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().get(i).getIDHistorico()>max)
			{
				max=Topicos.get(idtopico).getIdeia().get(idideia).getHistorico().get(i).getIDHistorico();
			}
		}
		return max;  
   }
                
	
        
      /** ------------------------ **/
 
}