package IdeaBroker;

import java.io.Serializable; 
import java.util.ArrayList;
import org.scribe.model.Token;

public class Utilizador implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iduser;
	private String username;
	private String password;
	private double dinheiro=1000000;
	private ArrayList<NotificacaoRMI> historico = new ArrayList<NotificacaoRMI>();
        private boolean root;
        private String acesso_token;
        //private OAuthService service; 
	
        //Construtor
	public Utilizador()
	{
		
	}
	
	public Utilizador(int iduser,String username, String password, boolean root) {

		this.iduser=iduser;
		this.username=username;
		this.password=password;
                this.root = root;
                this.acesso_token = null;   
                
	}


	
	//Getters
	//Retorna id do utilizador
	public ArrayList<NotificacaoRMI> getNotificacao()
	{
		return this.historico;
	}
	public int getiduser()
	{
		return this.iduser;

	}
	//Retorna o username
	public String getusername()
	{
		return this.username;

	}
	//Retorna a password
	public String getpassword()
	{
		return this.password;

	}
	public double getsaldo()
	{
		return this.dinheiro;
	}
	//SETTERS
	//Actualiza o iduser
	
	public void setsaldo(double saldo)
	{
		this.dinheiro=saldo;
	}
        
	public void retirarsaldo(double saldo)
	{
		this.dinheiro=this.dinheiro-saldo;
	
	}
	
	public void adicionarsaldo(double saldo)
	{
		this.dinheiro+=saldo;
	}
	public void setiduser(int iduser)
	{
		this.iduser=iduser;

	}
	//Actualiza o Username
	public void setusername(String username)
	{
		this.username=username;

	}
	//Actualiza a password
	public void setpassword(String password)
	{
		this.password=password;

	}
        
        public boolean getroot()
        {
            return this.root;
        }

        
        public void setAcesso(String acesso)
        {
            this.acesso_token = acesso;
        }
        
	public String getAcesso()
        {
            return  this.acesso_token;
        }

	//Retorna tudo separado por uma virgula
	public String getAll()
	{
		return this.iduser+"," + this.username+ "," +this.password;
	}

}