/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Messaggi.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.Map;

/**
 *
 * @author Iacopo
 */
/*
Rimane in ascolto delle connessioni da parte dei client
e avvia un nuovo GestoreUtente ogni volta che riceve un log-in
*/
public class Server {

    /**
     * @param args the command line arguments
     */
    private static int port = 8080;
    private static ServerSocket listener;
    private static HashMap<String,GestoreUtente> Lgestut = new HashMap();
    private static GestoreDB gDB;
    public static void main(String[] args) {
        System.out.println("Chat server avviato e in ascolto sulla porta:8080");
        try{
            gDB  = new GestoreDB();
            listener= new ServerSocket(port);
            while (true) {
                Socket s= listener.accept();
                
                System.out.println("nuova richiesta di connessione ricevuta");
                
                GestoreUtente gu =new GestoreUtente(s);
                gu.start();
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public static synchronized boolean inserisci_utente(String nm,GestoreUtente gu){
        if(Lgestut.containsKey(nm))
            return false;
       Lgestut.put(nm,gu);
       return true;
    }
        
    public static synchronized boolean rimuovi_utente(String nm)throws IOException{
            broadcast(new MessageLOGIN_OUT(Type.LOG_OUT,nm));

            return(Lgestut.remove(nm)!=null);
    }
    
    public static synchronized ArrayList crea_ar_list(){
    
        ArrayList<String> ut = new ArrayList<>();       
        Iterator hmIterator = Lgestut.entrySet().iterator();
        int i =0;
        while (hmIterator.hasNext()) { 
            
           HashMap.Entry element=(HashMap.Entry)hmIterator.next();  
           ut.add((String)element.getKey());
           i++;
           
        } 
        
        return ut;
    }
    
    public static synchronized void broadcast(Message m)throws IOException{
        GestoreUtente g;
        
        Iterator hmIterator = Lgestut.entrySet().iterator();
        int i =0;
        try {
      
            while (hmIterator.hasNext()) { 

               HashMap.Entry element=(HashMap.Entry)hmIterator.next(); 

               if(m.getMittente()!= element.getKey()){
                    g=(GestoreUtente)element.getValue();
                    g.invia_msg(m);

               }
            } 
        } catch (IOException e) {e.printStackTrace();}  
    }
    
    public static synchronized void invia_chat(Message m) throws IOException{
        GestoreUtente g = Lgestut.get(m.getDest());
        if(g==null)
            return;
        g.invia_msg(m);
        
    }
  
    //funzioni di utilità per il db
    
    public static synchronized boolean inserisciMessaggioDB(Message m){
       return gDB.inserisciMessaggioDB(m);
    }
    
    public static synchronized boolean inserisciutenteDB(String n){
        return gDB.inserisciutente(n);    
    }
    
    public static synchronized ArrayList getStatistiche(){
    
        ArrayList<CampiGrafo> al = null;
        try {
            return  gDB.aggiornaInterazioneUtente(100,3);
        } catch (Exception e) {e.printStackTrace();
        }
        
        
        return al;
    }
}
