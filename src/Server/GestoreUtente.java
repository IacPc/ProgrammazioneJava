/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import Messaggi.*;
import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.nio.file.Files;


import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;


/**
 *
 * @author Iacopo
 */
/*
Gestore utenti si occupa di gestire i messaggi 
inviati da un determinato utente
*/
public class GestoreUtente extends Thread {
    
    private Socket sock;
    private ObjectOutputStream invia;
    private  ObjectInputStream ricevi;
    private String utente;
    private int prog;
    private static GestoreDB gDB;

    public GestoreUtente(Socket sock,String conn,String pwd,int port) throws SQLException, IOException,ClassNotFoundException{
        this.sock = sock;
        ricevi = new ObjectInputStream(sock.getInputStream());
        invia = new ObjectOutputStream(sock.getOutputStream());
        gDB =new GestoreDB(conn,pwd,port);
    }
    
     
    public void run(){
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
                            Server.broadcast(msg);
                            
                            System.out.println("utente:"+ utente +" loggato");
                             
                        }
                        else{
                            System.out.println("collisione con lo username");
                            invia.writeObject(new MessageERR());
                        }
                        break;   

                        case LOG_OUT:
                            System.out.println(msg.getMittente()+"si è disconnesso");
                            Server.broadcast(new MessageLOGIN_OUT(Type.LOG_OUT,utente));
                            
                            invia.close();
                            ricevi.close();
                            sock.close();
                            
                        break;
                        case CHAT:
                            System.out.println(msg.getMittente()+" scrive a "+msg.getDest() +": "+msg.getTesto());

                            Server.invia_chat(msg);
                            if(!inserisciMessaggioDB(msg,this.prog++))
                                System.err.println("Messagio non inserito nel db");
;
                        break;
                        case CHAT_GROUP:
                            System.out.println(msg.getMittente()+" scrive a "+msg.getDest() +": "+msg.getTesto());

                            Server.invia_chat(msg);
                            if(!inserisciMessaggioDB(msg,this.prog++))
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
                    }
        
            }     
                        
        }
        catch(java.net.SocketException se){
           rimuoviut(utente);
           System.out.println("utente "+this.utente +" disconnesso");
        }
        catch (Exception e) {e.printStackTrace();}
           
      
    }
  
    public void invia_msg(Message mc) throws IOException{
        invia.writeObject(mc);
    }
    
    public void rimuoviut(String u) {
        try {
            Server.rimuovi_utente(u);
            invia.close();
            ricevi.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //funzioni di utilità per il db

    
   public static synchronized boolean inserisciMessaggioDB(Message m,int i){
       return gDB.inserisciMessaggioDB(m,i);
    } 
    
    public static synchronized ArrayList getStatistiche(){
    
        try {
            return  gDB.aggiornaInterazioneUtente(Server.pcs.pGR.giornigrafo,
                                                  Server.pcs.pGR.quantiutenti);
        } catch (Exception e) {e.printStackTrace();return null;
        }
       
    }
    
    
}
