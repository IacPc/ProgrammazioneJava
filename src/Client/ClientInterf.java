/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import Messaggi.*;
import Log.*;
import com.thoughtworks.xstream.XStream;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;

import javafx.application.*;
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
   private static XStream xs;
   
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
   private  MenuItem mi_top3;

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

   //campi casella di testo e bottone btn_invia
   private  HBox hbBtn_txt ;    
   private  TextField txt_invia;
   private static Button btn_invia;
   //Grafo
   private Button btnAggiorna;
   private static GrafoInterazioni grafo;
   private ParamConfClient parametri;
   
   void cambia_scena(Stage s,Scene sc){
        s.setScene(sc);
        s.show();
        System.out.println("cambio scena");          
       
   }
   
   private void init_menu(Stage s){
        menu_stat = new MenuBar();
        mn_stat = new Menu("statistiche");
        mi_top3 = new MenuItem("Top 3");
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
        
        mi_top3.setOnAction(evt->{
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

                System.out.println("cambio scena");
            }
        });
        
        menu_chat.getMenus().add(mn_stat);
        mn_stat.getItems().addAll(mi_top3);
        
        menu_stat.getMenus().add(menuchatbox);
        menuchatbox.getItems().addAll(menusat);
               
   }
   
   
   public void init_stat(){
      vb_statistiche = new VBox();
      pannello_stat = new Pane();  
      btnAggiorna=new Button("Aggiorna");
      btnAggiorna.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                if(gest_ser!=null){
                    try {
                        gest_ser.invia_msg(new MessageSTAT(nomeutente,null));
                        EventoLog ev = new EventoLog(TipoEvento.AGGSTAT_CLICK,
                                        gest_ser.get_localadd(),nomeutente);
                        gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
                }
           
            }
        });
        
      grafo= new GrafoInterazioni();
     
      vb_statistiche.getChildren().addAll(menu_stat,grafo,btnAggiorna);
      pannello_stat.getChildren().addAll(vb_statistiche);
      scena_grafo= new Scene(pannello_stat);    

   
   }
   
   public void istanziaelementichat(){
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
        btn_invia = new Button("Invia");

        list_utenteon = new ListView<String>();
        txt_invia= new TextField();
        txtconnetti= new TextField();
   
   }
   
   public void initareachat(){
        
        istanziaelementichat();
        
        bp_connessione.setMargin(txtconnetti,new Insets(10,10,10,10));
        bp_connessione.setMargin(btn_connetti,new Insets(10,10,10,10));
        bp_connessione.setLeft(txtconnetti);
        bp_connessione.setCenter(btn_connetti);
        
        btn_connetti.setOnAction(evt->{connetti(); });
       
        scrollmsg.setContent(tab_msg); 

        scrollut.setContent(list_utenteon);
        list_utenteon.setPrefWidth(150);
        txt_invia.setPrefWidth(500);
        
        btn_invia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                String testo =txt_invia.getText();
                
                String dest =list_utenteon.getSelectionModel().getSelectedItem();
               
                if(gest_ser != null){
                    EventoLog ev = new EventoLog(TipoEvento.INVIA_CLICK,
                                            gest_ser.get_localadd(),nomeutente);
                    
                    try {
                        Message m=new Message_CHAT(testo,Type.CHAT,nomeutente,dest);
                        gest_ser.invia_msg(m);
                        tab_msg.inserisci(m);
                        gest_ser.invia_msg(new MessageLOG(xs.toXML(ev)));
                    } catch (IOException e) {
                        txt_invia.setText("invio fallito");
                        
                    }
                    
                }
            }
        });
        
      
        hbBtn_txt.getChildren().addAll(txt_invia,btn_invia);
        vb_int_chat.getChildren().addAll(tab_msg,hbBtn_txt);
        hb_chatBox.getChildren().addAll(list_utenteon,vb_int_chat);
        vb_scenario_chat.getChildren().addAll(menu_chat,bp_connessione,hb_chatBox);
        pannello_chat.getChildren().add(vb_scenario_chat);
        
        scena_connesso = new Scene(pannello_chat);
    }

    public void init_tabmsg() {
        tab_msg = new TabellaMessaggi(parametri.confchat.fontCelleTabella);
        tab_msg.setOnMouseClicked( 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent mev){
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
               tab_msg.pulisci();
               gest_ser.chiudi();
               gest_ser=null;
            }
        });
        stage.setScene(scena_connesso);
        stage.show();
    
    }
   
    public static void main(String[] args) throws Exception {
        launch(args);
    }
   

    
    private boolean connetti(){
        
       try{
           if(gest_ser==null){//connettersi al serv. di mess.
                nomeutente = txtconnetti.getText();
                if (nomeutente.length()<=1){
                    txtconnetti.setText("Nome utente non valido");
                    return false;
                }

                gest_ser = new GestoreServer(parametri.confserver.port,
                                InetAddress.getByName(parametri.confserver.ip),
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
    
    public static synchronized void set_lut(ArrayList<String> nm){
        if(nm==null)
           return; 
        for(int i =0;i< nm.size();i++) {
            if(!nm.get(i).equals(nomeutente))
                list_utenteon.getItems().add(nm.get(i));
        }
    }
    
    public static synchronized void aggiorna_utenti(String s){
        list_utenteon.getItems().add(s);
    
    }
    
    public static synchronized boolean elimina_utente(String u){
        list_utenteon.getItems().remove(u);
        return list_utenteon.getItems().isEmpty();
   
    }    
   
    public static void ins_in_tabella(Message m){
        tab_msg.inserisci(m);
    }
    
    public static void aggiornagrafo(ArrayList<CampiGrafo> al ){
        grafo.aggiungiuntenti(al);
    }
    public void caricaParametriConfigurazione(){
        //validaXML();
       
        try{  
            parametri =new ParamConfClient(new String(Files.readAllBytes(
                                           Paths.get("./Configurazione.xml"))));
        }catch(IOException e){System.out.println(e.getMessage());}

    }

    private void validaXML(){
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
            Document d = (Document) db.parse(new File("./Configurazione.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./ConfigurazioneXML.xsd")));  
            s.newValidator().validate(new DOMSource(d)); 
        } catch (Exception e) {System.out.println("Errore di validazione: " + e.getMessage());}
    }
}
//pulire la tabella DAFARE

