/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import Messaggi.*;

import javafx.application.Platform;
import java.net.*;
import java.io.*;
/**
 *
 * @author Iacopo
 */
public class GestoreServer extends Thread{
    private  ObjectOutputStream invia;
    private  ObjectInputStream ricevi;
    private  Socket log_socket;
    private boolean in_attesa = true; //mi dice se sto aspettando la risposta del serv
    private boolean connesso_al_server = false;
    private  int ServerPort;
    private  InetAddress ip; // 
    private  String nomeut;
    public GestoreServer(int sv,InetAddress in,String n) throws IOException{
        ServerPort = sv;
        ip= in;
        nomeut=n;
        log_socket = new Socket(ip,ServerPort);
        invia= new  ObjectOutputStream(log_socket.getOutputStream());
        ricevi = new ObjectInputStream(log_socket.getInputStream());
    }
   
    public void run(){
     

        try {
            

            invia_login();
            System.out.println("login inviato al server");
            
            while(true){
                
                Message msg =(Message)ricevi.readObject();
                switch (msg.getTipo()){
                    case OK:
                        System.out.println("connessione accettata dal server");
                        synchronized(this){
                            connesso_al_server=true;
                            in_attesa=false;
                            notifyAll();
                        }
                        Platform.runLater(() -> {
                            ClientInterf.set_lut(msg.getCampi());

                        });
                        
                    break;
                    case ERR:
                        System.out.println("connessione rifiutata dal server");
                        synchronized(this){
                            connesso_al_server=false;
                            in_attesa=false;
                            notifyAll();
                        }
                    break;
                    case LOG_IN:
                        Platform.runLater(() -> {
                            ClientInterf.aggiorna_utenti(msg.getMittente());
                        });
                        
                    break;
                    case LOG_OUT:
                        Platform.runLater(() -> {
                            ClientInterf.elimina_utente(msg.getMittente());
                        });
                    break;
                    case CHAT:
                        System.out.println("ricevuto messaggio di tipo chat");

                        Platform.runLater(() -> {
                             ClientInterf.ins_in_tabella(msg);

                        });
                        
                    break;
                    case STAT:
                        System.out.println("ricevuto aggiornamento statistiche:"
                        + msg.getCampi().size());
                        
                        Platform.runLater(() -> {
                            ClientInterf.aggiornagrafo(msg.getCampi());
                        }); 
                
                }
 
            }
                 
            
        } catch (EOFException e) {
           System.out.println("Il server ha rifutato la connessione");
           synchronized(this){
            connesso_al_server = false;
            in_attesa=false;
            notifyAll();
           }
        }catch (IOException | ClassNotFoundException e) {
            System.out.println("chiusura della connessione");
            return;
        }              
    
    }
       
    public synchronized boolean invia_login() {
    
        try {
            invia.writeObject(new MessageLOGIN_OUT(Type.LOG_IN,nomeut));
       
            return connesso_al_server;
            
        } catch (IOException  ie) {
            return false;
        }
        

  }
    public synchronized void invia_msg(Message m)throws IOException{
        invia.writeObject(m);
    }
    public String get_Nomeut(){return nomeut;} 
    public String get_localadd(){return log_socket.getLocalAddress().getHostAddress();}
    public synchronized boolean isconnected()throws InterruptedException{
        while(in_attesa){
            wait();
        }
        return connesso_al_server;
    } 
    
    public synchronized boolean chiudi(){
        connesso_al_server = false;
        in_attesa=true;
        try {
            invia.close();
            ricevi.close();
            log_socket.close();
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

}
