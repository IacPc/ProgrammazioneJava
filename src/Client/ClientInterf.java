/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import client_server.*;

import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;

import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.geometry.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.stage.*;




/**
 *
 * @author Iacopo
 */

public class ClientInterf  extends Application {

   private  GestoreServer gest_ser;
   private  static String nomeutente;
   private  XStream xs;
   
   private  Pane pannello_chat;
   private  Pane pannello_stat;

   private  Scene scena_connesso;
   private  Scene scena_grafo;
   
   private  BorderPane bp_connessione;
   private  VBox vb_statistiche;

   private  HBox hb_chatBox;
   private  VBox vb_int_chat;
   
   
   private  VBox vb_scenario_chat;
   // Menu
   private  MenuBar menu_stat;
   private  Menu mn_stat;
   private  MenuItem itemGrafo;
   private  MenuBar menu_chat ;
   private  Menu menuchatbox;
   private  MenuItem menusat;
   
    //Connessione al servizio
   private  Button btn_connetti;
   private  TextField txtconnetti;
   private  HBox hb_connessione;
   private  VBox vb_connessione;

   //contiene i messaggi inviati dagli utenti
   private  ScrollPane scrollmsg;
   private static TabellaMessaggi tab_msg;
   
   //contiene utenti collegati al servizio
   private static ListView<String> list_utenteon;
   private  ScrollPane scrollut;

   //campi casella di testo e bottoni btn_invia,btn_elimina
   private  HBox hbBtn_txt ;    
   private  TextField txt_invia;
   private static Button btn_invia;
   private static Button btn_elimina;

   //Grafo
   private Button btnAggiorna;
   private static GrafoMessaggiRicevuti grafo;
   private ParamConfClient parametri;
   
   private CacheBinaria cache;
   
   void cambia_scena(Stage s,Scene sc){ //1
        s.setScene(sc);
        s.show();
        System.out.println("cambio scena");          
       
   } 
   
   private void init_menu(Stage s){//2
        menu_stat = new MenuBar();
        mn_stat = new Menu("statistiche");
        itemGrafo = new MenuItem("Grafo");
        menu_chat = new MenuBar();
        menuchatbox = new Menu("ChatBox");
        menusat = new MenuItem("chat");
        menusat.setOnAction(evt->{
            if(gest_ser !=null){
                EventoLog ev = new EventoLog(TipoEvento.MENU_CLICK,gest_ser.get_localadd()
                                                                   ,nomeutente);
                try{
                    gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                }catch(IOException ie){
                        System.out.println("invio log fallito");
                }
            }
            cambia_scena(s,scena_connesso);
            
        });
        
        itemGrafo.setOnAction(evt->{
            try{
                EventoLog ev = new EventoLog(TipoEvento.MENU_CLICK,
                                        gest_ser.get_localadd(),nomeutente);
                gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
            }catch(IOException  ie){
                    System.out.println("invio log fallito");
            }catch(NullPointerException ne){
                    System.out.println("non connesso al server");
            }finally{
                cambia_scena(s,scena_grafo);
            }
        });
        
        menu_chat.getMenus().add(mn_stat);
        mn_stat.getItems().addAll(itemGrafo);
        
        menu_stat.getMenus().add(menuchatbox);
        menuchatbox.getItems().addAll(menusat);
               
   }
   
   public void init_stat(){//3
      vb_statistiche = new VBox();
      pannello_stat = new Pane();  
      btnAggiorna=new Button("Aggiorna");
      btnAggiorna.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                if(gest_ser!=null){
                    try {
                        
                        gest_ser.invia_msg(new MessageSTAT(nomeutente,null));
                        System.out.println("Inviata richiesta aggiornamento statistiche");
                        EventoLog ev = new EventoLog(TipoEvento.AGGSTAT_CLICK,
                                        gest_ser.get_localadd(),nomeutente);
                        gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
                }
           
            }
        });
        
      grafo= new GrafoMessaggiRicevuti();
     
      vb_statistiche.getChildren().addAll(menu_stat,grafo,btnAggiorna);
      pannello_stat.getChildren().addAll(vb_statistiche);
      scena_grafo= new Scene(pannello_stat);    

   
   }
   
   public void istanziaelementichat(){//4
        bp_connessione = new BorderPane();
        pannello_chat = new Pane();
        
        scrollmsg = new ScrollPane();
        scrollut  = new ScrollPane();
        
        hbBtn_txt= new HBox();
        hb_chatBox = new HBox();
        hb_connessione = new HBox();
        
        vb_int_chat = new VBox();
        vb_scenario_chat= new VBox();
        vb_connessione = new VBox();
        
        btn_connetti = new Button("connetti");
        btn_invia = new Button("  Invia  ");
        btn_elimina = new Button("Elimina");
        
        list_utenteon = new ListView<String>();
        txt_invia= new TextField();
        txtconnetti= new TextField();
        bp_connessione.setMargin(txtconnetti,new Insets(10,10,10,10));
        bp_connessione.setMargin(btn_connetti,new Insets(10,10,10,10));
        bp_connessione.setLeft(txtconnetti);
        bp_connessione.setCenter(btn_connetti);
        
       
        scrollmsg.setContent(tab_msg); 

        scrollut.setContent(list_utenteon);
        list_utenteon.setPrefWidth(150);
        list_utenteon.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE);
        txt_invia.setPrefWidth(500);
   
   }
   private void azioneBtnInvia(){//5
            String testo =txt_invia.getText();
            if(gest_ser != null){
                EventoLog ev = new EventoLog(TipoEvento.INVIA_CLICK,
                                            gest_ser.get_localadd(),nomeutente);
                    
                try {
                    ObservableList<String> l =list_utenteon.getSelectionModel().getSelectedItems();
                    if(l==null){
                        System.err.println("Slezionare un utente");
                        return;
                    }
                  
                    Message m=null;
                    for(int i =0;i<l.size();i++){
                        m=new Message_CHAT(testo,nomeutente,l.get(i));
                        gest_ser.invia_msg(m);
                    }
                    ins_in_tabella(m);
    
                    gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                    txt_invia.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                    txt_invia.setText("invio fallito");
                 }
                    
            }    
    }
   private boolean azioneBtnConnetti(){//6
        
       try{
           if(gest_ser==null){//connettersi al serv. di mess.
                nomeutente = txtconnetti.getText();
                if (nomeutente.length()<=1){
                    txtconnetti.setText("Nome utente non valido");
                    return false;
                }

                gest_ser = new GestoreServer(parametri.server_port,
                                InetAddress.getByName(parametri.server_ip),
                                nomeutente);
                
                gest_ser.start();    
                
                if(!gest_ser.isconnected()){
                    txtconnetti.clear();
                    txtconnetti.setText("nome utente non valido");
                }else{
                    btn_connetti.setText("Disconnetti");
                }
                
            }else{//DISconnettersi al serv. di mess.
               gest_ser.invia_msg(new MessageLOGIN_OUT(Type.LOG_OUT,nomeutente));
               list_utenteon.getItems().clear();
               
               gest_ser.chiudi();
               gest_ser=null;
               btn_connetti.setText("Connetti");

           }
        }catch( ConnectException e ){
            System.out.println("connessione al server non riuscita");
            return false;
        }
       
        catch( IOException |InterruptedException e ){
            e.printStackTrace();
            return false;
        }
      return true;
    }
        
   private void azioneBtnElimina(){//7
        
         try{
            if(gest_ser!=null){
                EventoLog ev = new EventoLog(TipoEvento.DEL_CLICK, 
                                         gest_ser.get_localadd(),
                                         nomeutente);
                Message m = new MessageLOG(xs.toXML(ev));
                gest_ser.invia_msg(m);

                 String s= tab_msg.getNomeUt();
                 String o= tab_msg.getOra();
                 if(s.equals(nomeutente)){
                        MessageDEL mess = new MessageDEL(s, o);
                        gest_ser.invia_msg(mess);
                 }
            }
            tab_msg.cancella();

        }catch(IOException ie){
            ie.printStackTrace();
        }

    }
    
   public void initareachat(){//8
        
        istanziaelementichat();
        
        btn_connetti.setOnAction(evt->{azioneBtnConnetti(); });

        
        btn_invia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                azioneBtnInvia();
            }
        });
        btn_elimina.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent evt){
                azioneBtnElimina();
            }
            
        });
      
        hbBtn_txt.getChildren().addAll(txt_invia,btn_invia,btn_elimina);
        vb_int_chat.getChildren().addAll(tab_msg,hbBtn_txt);
        hb_chatBox.getChildren().addAll(list_utenteon,vb_int_chat);
        vb_scenario_chat.getChildren().addAll(menu_chat,bp_connessione,hb_chatBox);
        pannello_chat.getChildren().add(vb_scenario_chat);
        
        scena_connesso = new Scene(pannello_chat);
    }
   public void init_tabmsg() {//8
        tab_msg = new TabellaMessaggi(parametri.fontCelleTabella);
        tab_msg.setOnMouseClicked( 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent mev){
                        if(gest_ser!=null){
                            EventoLog ev = new EventoLog(TipoEvento.TABLE_CLICK, 
                                                         gest_ser.get_localadd(),
                                                         nomeutente);
                            Message m = new MessageLOG(xs.toXML(ev));
                            try{
                                gest_ser.invia_msg(m);

                            }catch(IOException ie){
                               ie.printStackTrace();
                            }
                        }
                    }
                
                
                });
 
    }
    @Override
   public void start(Stage stage) throws Exception{
        xs= new XStream();
        xs.useAttributeFor(EventoLog.class,"tipo");
        caricaParametriConfigurazione();
        init_tabmsg();
        init_menu(stage);
        init_stat();
        initareachat();

        stage.setOnCloseRequest((WindowEvent) -> {
            if(gest_ser!=null){
                try {
                   EventoLog ev = new EventoLog(TipoEvento.EXIT_CLICK,
                                        gest_ser.get_localadd(),nomeutente);
                   gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                   gest_ser.invia_msg(new MessageLOGIN_OUT(Type.LOG_OUT,nomeutente));
                } catch (IOException e) {e.printStackTrace();
                }
               list_utenteon.getItems().clear();
               gest_ser.chiudi();
               gest_ser=null;
            }
            salvaInCache();

        });
        leggiDallaCache();
        stage.setTitle("MyChat");
        stage.setScene(scena_connesso);
        stage.show();
    
    }
   public static void main(String[] args) throws Exception {
        launch(args);
    }

   public static synchronized void set_lut(ArrayList<String> nm){//10
        if(nm==null)
           return; 
        for(int i =0;i< nm.size();i++) {
            String s=nm.get(i);
            if(!s.equals(nomeutente))
                list_utenteon.getItems().add(s);
        }
    }
    
   public static synchronized void aggiorna_utenti(String s){//11
        list_utenteon.getItems().add(s);
    }
   public static synchronized boolean elimina_utente(String u){//12
        list_utenteon.getItems().remove(u);
        return list_utenteon.getItems().isEmpty();
    }    
   public static void ins_in_tabella(Message m){//13
        tab_msg.inserisci(m.getMittente(), m.getTime(),m.getTesto());
    }
   public static void rimuovi_da_tabella(Message m){///14
        tab_msg.cancella(m.getMittente(),m.getTime());
    }
    
   public static void aggiornagrafo(ArrayList<CampiGrafo> al ){//15
        grafo.aggiungiuntenti(al);
   }
  
   public void caricaParametriConfigurazione(){//16
        validaXML();
        XStream xs =new XStream();
        try{  
            parametri =(ParamConfClient)xs.fromXML(new String(Files.readAllBytes(
                                           Paths.get("./ConfigurazioneClient.xml"))));
        }catch(IOException e){System.out.println(e.getMessage());}

    }

   private void validaXML(){//17
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
            Document d = (Document) db.parse(new File("./Configurazioneclient.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./ConfigurazioneclientXML.xsd")));  
            s.newValidator().validate(new DOMSource(d)); 
        } catch (Exception e) {System.out.println("Errore di validazione: " + e.getMessage());}
    }
    
   public void salvaInCache(){//18
      try(FileOutputStream scriviFile = new FileOutputStream("./CacheLocale.bin");
          ObjectOutputStream oggettoScritto = new ObjectOutputStream(scriviFile);
         ){
            
            oggettoScritto.writeObject(new CacheBinaria(
                                                        txt_invia.getText(), 
                                                        grafo.getUtenti(),
                                                        tab_msg.getMessaggi(),
                                                        txtconnetti.getText())
                                                        );
        } catch(IOException ioe){System.out.println(ioe.getMessage());}
    }
   public void leggiDallaCache(){//19
        try(FileInputStream leggiFile = new FileInputStream("./CacheLocale.bin");
            ObjectInputStream oggettoLetto = new ObjectInputStream(leggiFile);){
            cache = (CacheBinaria) oggettoLetto.readObject();
            String n,o,t;

            for(int i =0;i<cache.ultimi_mess.size();i++){
                n=cache.ultimi_mess.get(i).getNome();
                o=cache.ultimi_mess.get(i).getDataora();
                t=cache.ultimi_mess.get(i).getTesto();
                tab_msg.inserisci(n,o,t);
            }
            txt_invia.setText(cache.testoLasciatoincasella);
            txtconnetti.setText(cache.ultimouser);
            grafo.aggiungiuntenti(cache.ultime_interazioni);
        } catch(IOException ioe){System.out.println(ioe.getMessage());}
          catch(ClassNotFoundException cnfe){System.out.println(cnfe.getMessage());}
  
    }
   
}

//1 Funzione utilizzata per cambiare scena tra chat e grafo
//2 Funzione utilizzata per inizializzare i vari elementi del menu
//  superiore
//3 Funzione utilizzata per inizializzare il grafo e
//  l'interfaccia 
//4 Funzione per l'istanziazione dei vari elementi grafici, impiegata a parte 
//  per limitare la dimensione della funzione initareachat
//5 routine che definisce il comportamento del bottone invia
//6 routine che definisce il comportamento del bottone connetti
//7 routine che definisce il comportamento del bottone elimina
//8 funzione Backend principale per l'inizializzazione dell' interf. della chat
//9 funzione responsabile dell'inizializzazione della tabella messaggi
//10 funzioen utilizzata per costruire la lista degli utenti connessi a partire
//   dall'ArrayList contenuta nel messaggio di tipo OK
//11 funzione utilizzata per aggiornare la lista utenti ad ogni login richiamta
//   ogni volta che si riceve un messagio di tipo LOGIN
//12 funzione utilizzata per aggiornare la lista utenti ad ogni logout richiamta
//   ogni volta che si riceve un messagio di tipo LOGOUT
//13 funzione utilizzata per inserire un messaggio nella tabella
//14 funzione utilizzata per rimuovere un messaggio dalla tabella   
//15 funzione utilizza per aggiornare il grafo a partire dalla lista contenuta
//   nel messaggio di tipo STAT ricevuto dal server
//16 funzione utilizzata per estrarre i parametri di configurazione da file xml
//17 funzione utilizzata per validare i parametri di configurazione in xml
//18 funzione che crea\sovrascrive il file contenente la cache locale .bin
//19 funzione utilizzata per costruire la variabile cache leggendo la cache locale 
//   all'avvio.