/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import client_server.*;
import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;

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
    private static int port ;
    private static ServerSocket listener;
    private static HashMap<String,GestoreUtente> listaGestUtente;
    public static ParamConfServer pcs;
    public static void main(String[] args) {
        try{
            XStream xs =new XStream();
            if(!validaXML("ConfigurazioneServer.xml","ConfigurazioneServerXML.xsd"))
                return;
            pcs = (ParamConfServer)(xs).fromXML(new String(Files.readAllBytes(
                                           Paths.get("./ConfigurazioneServer.xml"))));
            port=pcs.porta_ascolto;
            listener= new ServerSocket(port);
            System.out.println("Chat server avviato e in ascolto sulla porta:"+pcs.porta_ascolto);
            listaGestUtente = new HashMap();
            while (true) {
                Socket s= listener.accept();
                
                System.out.println("nuova richiesta di connessione ricevuta");
                
                GestoreUtente gu =new GestoreUtente(s,pcs);
                gu.start();
            }
           
        }catch(SQLException e){
            System.err.println("Connessione al db non riuscita");
        }
         catch(IOException |ClassNotFoundException ie){
             System.err.println("gestione nuova richiesta fallita");
         }
    }
    
    public static synchronized boolean inserisci_utente(String nm,GestoreUtente gu) throws IOException{
        if(listaGestUtente.containsKey(nm))
            return false;
       listaGestUtente.put(nm,gu);
       Server.broadcast(new MessageLOGIN_OUT(Type.LOG_IN, nm));
       
       return true;
    }
        
    public static synchronized boolean rimuovi_utente(String nm) {
        if(listaGestUtente.remove(nm)!=null) {
            try {
                broadcast(new MessageLOGIN_OUT(Type.LOG_OUT,nm));
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
       
    }
    
    public static synchronized ArrayList crea_ar_list(){
    
        ArrayList<String> ut = new ArrayList<>();       
        Iterator hmIterator = listaGestUtente.entrySet().iterator();
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
        
        Iterator hmIterator = listaGestUtente.entrySet().iterator();
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
        GestoreUtente g = listaGestUtente.get(m.getDest());
        if(g==null)
            return;
        g.invia_msg(m);
        
    }
  
    public static boolean validaXML(String doc,String xsd){
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
            Document d = (Document) db.parse(new File(doc));
            Schema s = sf.newSchema(new StreamSource(new File(xsd)));  
            s.newValidator().validate(new DOMSource(d)); 
            return true;
        } catch (Exception e) {System.out.println("Errore di validazione: " + e.getMessage());
                                return false;}
    }
}


//1)inizalizza il Server con i paraemtri letti dal file di conf in xml e istanzia
// le strutture dati necessari per il funizonamemento del server come listaGestUtente
// e il ServerSocket su cui ascolat le connessioni
//2)