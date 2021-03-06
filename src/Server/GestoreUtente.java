/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import client_server.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.nio.file.Files;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Iacopo
 */
/*
Gestore utente si occupa di gestire i messaggi 
inviati da un determinato utente,ce n'è uno
per ogni utente connesso
*/
public class GestoreUtente extends Thread {
    
    private Socket sock;
    private ObjectOutputStream invia;
    private  ObjectInputStream ricevi;
    private String utente;
    private int prog;
    private static GestoreDB gDB;

    public GestoreUtente(Socket sock,ParamConfServer ps) throws SQLException, IOException,ClassNotFoundException{
        this.sock = sock;
        ricevi = new ObjectInputStream(sock.getInputStream());
        invia = new ObjectOutputStream(sock.getOutputStream());
        gDB =new GestoreDB(ps.pDB.nomeDB,ps.pDB.passDB,ps.pDB.portadb,ps.pDB.ipDB);
    }
    
    public void run(){//1
        try{
            while(true){
                Message msg = (Message)ricevi.readObject();
                switch(msg.getTipo()){

                    case LOG_IN:
                            
                        utente = msg.getMittente();
                        if(Server.inserisci_utente(utente, this)){
                            ArrayList al =Server.crea_ar_list();
                            Message m = new Message_OK(al);
                            invia.writeObject(m);
                           
                            System.out.println("utente:"+ utente +" loggato");
                             
                        }
                        else{
                            System.out.println("collisione con lo username");
                            invia.writeObject(new MessageERR());
                        }
                        break;   
                        case LOG_OUT:
                            Server.rimuovi_utente(utente);
                            invia.close();
                            ricevi.close();
                            sock.close();
                            return;
                        case CHAT:
                            System.out.println(msg.getMittente()+" scrive a "+msg.getDest() +": "+msg.getTesto());

                            Server.invia_chat(msg);
                            prog++;
                            if(!inserisciMessaggioDB(msg,prog))
                                System.err.println("Messagio non inserito nel db");
;
                        break;
                        
                        case LOG:
                            String s =new String( msg.getTesto()+'\n');
                            System.out.print(s);
                            try {
                                if(Server.validaXML("Log.xml","ValidazionelogXML.xsd")){
                                    Files.write(
                                                Paths.get("./EventiLog.xml"),
                                                s.getBytes(),
                                                StandardOpenOption.APPEND
                                                );
                                }
                            }catch(NoSuchFileException nse){
                                Files.write(Paths.get("./EventiLog.xml"),
                                    s.getBytes(),StandardOpenOption.CREATE_NEW);
                            }
                            catch(IOException e){
                                invia.close();
                                ricevi.close();
                                sock.close();
                            }
                        break;
                        
                        case STAT:
                              System.out.println("ricevuta richiesta aggiornamento statistiche");
                              ArrayList<CampiGrafo> al = getStatistiche();
                              invia_msg(new MessageSTAT(null,al));
                                
                        break;
                        case DEL:
                            System.out.println("ricevuta richiesta eliminazione messaggio");
                            Server.broadcast(msg);
                    }
        
            }     
                        
        }
        catch(java.net.SocketException se){
            Server.rimuovi_utente(utente);
        }
        catch (Exception e) {e.printStackTrace();}
           
      
    }
  
    public void invia_msg(Message mc) throws IOException{//
        invia.writeObject(mc);
    }
   
    //funzioni di utilità per il db
    public static synchronized boolean inserisciMessaggioDB(Message m,int i){//4
       return gDB.inserisciMessaggioDB(m,i);
    } 
    
    public static synchronized ArrayList getStatistiche(){//5
        try {
            return  gDB.aggiornaInterazioneUtente(Server.pcs.pGR.giornigrafo,
                                                  Server.pcs.pGR.quantiutenti);
        } catch (Exception e) {e.printStackTrace();return null;
        }
    }
 
}
//1 Resta in ascolto dei messaggi ricevuti dall'utente a cui è associato l'oggetto
//  e gestisce le varie casistiche
//2 serializza un msg nello stream di uscita connesso al client assocciato
//3 richiama la funzione server.logout 
//4 e 5 richiamano le funzioni di utilità per il database
