package IdeaBroker;

import java.rmi.*;
import java.util.ArrayList;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public interface IdeiaMethods extends Remote {
	
	public void loaddados() throws java.rmi.RemoteException;

	public String sayHello() throws java.rmi.RemoteException;

	public void remote_print(String s) throws java.rmi.RemoteException;

	public Utilizador login(String user,String password) throws java.rmi.RemoteException;

	public Utilizador registar(String username,String password) throws java.rmi.RemoteException;
	
	public ArrayList<Topico> gettopicos() throws java.rmi.RemoteException;
	
	public int adicionartopico(String username, String tema) throws java.rmi.RemoteException;
		
	public Utilizador procuraUtilizador(String util) throws java.rmi.RemoteException;

	public void Notificacao_vista(int indice)  throws java.rmi.RemoteException;

	public void adicionarNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException;

	public void removerNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException;
	
	public String verficarNotificacao(String _user) throws java.rmi.RemoteException;

	public int criarideia(int idtopico,String texto,String autor,Anexo anexo, int investimento) throws java.rmi.RemoteException;
	
        public int criarideia(int idtopico,String texto,String autor,Anexo anexo, int investimento, String face)  throws java.rmi.RemoteException;
       
        //public int criarideia(int idtopico,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException;
	
	public int responderideia(String natureza,int idtopico,int idideia,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException;

	public String apagarideia(int id_topico,int id_ideia,String username) throws java.rmi.RemoteException;
	
	//public int efectuarcompra(String comprador,String vendedor,int idtopico,int idideia,int idaccao,int percentagem,int precorevenda) throws java.rmi.RemoteException;
	public int efectuarcompra(String comprador, int idtopico, int idideia, int percentagem, int precorevenda, int investimento) throws java.rmi.RemoteException;
	
        public String dadosutilizador(String username) throws java.rmi.RemoteException;
	
	public String historicoutilizador(String username) throws java.rmi.RemoteException;
	
        
        /** META 2
        * @param _WebCliente
        * @throws java.rmi.RemoteException **/
        public void Webcliente(WebClienteInterface _WebCliente) throws RemoteException;
        
        public void adicionaOnlineWeb(String username) throws RemoteException;
        
        public boolean verificaUtilizador(String util, String passEncript, boolean verificaApenasUser) throws java.rmi.RemoteException;
        
        public void retiraOnlineWeb(String username) throws RemoteException;

        public boolean verifica_ideia(int id_topico, int id_ideia) throws RemoteException;
        
        public boolean verifica_topico(int id_topico) throws RemoteException;
        
        public boolean verifica_tema_topico(String tema) throws RemoteException;
        
        public int geraridtopico(ArrayList<Topico> Topicos) throws RemoteException;
        
        public Topico GetTopico(int id_topico) throws RemoteException;
        
        public Ideia GetIdeia(int id_topico,int id_ideia) throws RemoteException;
        
        public boolean PermissaoApagar(Ideia ideia, String username) throws RemoteException;
        
        public boolean ApagarIdeia(int idtopico, int idideia,String username) throws RemoteException;
        
        public ArrayList<Accao> getAccoes(int id_topico, int id_ideia) throws RemoteException;
        
        public Accao getAccao(int id_topico, int id_ideia, String username) throws RemoteException;
        
        public int eliminaraccoesinvalidas(int idtopico, int idideia) throws RemoteException;
        
        public boolean EditarAccao(int id_topico, int id_ideia, String username, double novo_preco) throws RemoteException;
       
        public ArrayList<Ideia> GetPesquisa( String autor_ideia, int valor_ideia) throws RemoteException;
        
        public void enviarNotificacao(String username, String notificacao) throws RemoteException;
        
        public void AdicionaCLiente(ClienteRMIInterface  _cliente) throws RemoteException;
        
        public void RemoverLigacao(ClienteRMIInterface _cliente) throws RemoteException;
        
        public Utilizador getAutorIdeia(Ideia ideia) throws RemoteException;
        
        public void setOAuth(String username,Token code) throws RemoteException;
        
        public String getOAuth(String username) throws RemoteException;
        
        public void adicionaToken(String username, String acesso) throws RemoteException;
        
        /** ---------------- **/
        
         public String historico(int idtopico,int idideia) throws java.rmi.RemoteException;
        
        public int takeover(int idtopico,int idideia) throws java.rmi.RemoteException;
        
	public String profile(String Username) throws java.rmi.RemoteException;
        
        public String portifolio(String Username,int html) throws java.rmi.RemoteException;
                
        public String halloffame(int html) throws RemoteException;
        
        //ublic String historicoutilizador(String username) throws java.rmi.RemoteException;
        
        
        public void setService(OAuthService _service) throws RemoteException;

        public OAuthService getService() throws RemoteException;
        
        /** ----------------- **/
        
	
}