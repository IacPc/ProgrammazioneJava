/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import Messaggi.Message;

import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import com.thoughtworks.xstream.XStream;
import javafx.scene.control.Cell;


/**
 *
 * @author Iacopo
 */
public class TabellaMessaggi extends TableView<CampiTabella> {
    
    private static ObservableList<CampiTabella> list_messages ; 

    public TabellaMessaggi(String stile) {
        
        setEditable(true);
        list_messages = FXCollections.observableArrayList();
        setItems(list_messages);

        TableColumn utente = new TableColumn("Utente");
        utente.setMinWidth(100);
        utente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        utente.setStyle(stile);
        

        TableColumn ora = new TableColumn("Data/Ora");
        ora.setMinWidth(100);
        ora.setCellValueFactory(new PropertyValueFactory<>("dataora"));
        ora.setStyle(stile);
        
        TableColumn testo = new TableColumn("Testo");
        testo.setMinWidth(500);
        testo.setCellValueFactory(new PropertyValueFactory<>("testo"));
        testo.setStyle(stile);
        
        getColumns().addAll(utente,ora,testo);
    }
    
    
    
    public static synchronized void inserisci(Message m){
        Platform.runLater(() -> {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
            CampiTabella ct =new CampiTabella(m.getMittente(),m.getTesto(),dtf.format(m.getTime()));
            list_messages.add(ct); 
        });
    }

    public void pulisci(){this.list_messages.clear();}

   
    
}