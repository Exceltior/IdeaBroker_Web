package IdeaBroker;

import java.io.Serializable; 
import java.util.ArrayList;

public class Historico implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int idhistorico;
	public String descricao;//Descrição da ideia em si
	//Construtor 
	public Historico(int idhistorico,String descricao) 
        {
		this.idhistorico=idhistorico;
                this.descricao=descricao;
            
	}
	
	//Getters
	//Retorna id da ideia
	
	public int getIDHistorico()
	{
		return idhistorico;
	}
	
	public String getdescricao()
	{
		return this.descricao;
	}
	
 
    

}