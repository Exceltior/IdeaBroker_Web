/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/
package IdeaBroker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

public interface WebClienteInterface extends Remote{

        public void addNote(String user,String notificacao) throws RemoteException;
        public void updateprice(ArrayList<Accao> _Accao, int id_topico, int id_ideia) throws RemoteException;
        
        //public void mostraOnline(Vector<String> users) throws RemoteException;

}
