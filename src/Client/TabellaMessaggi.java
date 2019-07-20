/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;



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
    
    
    
    public static synchronized void inserisci(String n,String d,String t){
        Platform.runLater(() -> {
            list_messages.add(new CampiTabella(n, d, t));
        });
    }

    public void pulisci(){list_messages.clear();}
   
    public void cancella(){
        list_messages.remove(getSelectionModel().getSelectedIndex());
    }
    
    public void cancella(String n,String o){
        CampiTabella ct;
        int i=0;
     
        for(;i<list_messages.size();i++){
            ct = list_messages.get(i);
            if(ct.getDataora().equals(o) && ct.getNome().equals(n)){
                list_messages.remove(i);
                break;
            }
        }
       
    }
    
    public String getNomeUt(){return getSelectionModel().getSelectedItem().getNome(); }
    public String getOra(){return getSelectionModel().getSelectedItem().getDataora();}
    public ObservableList<CampiTabella> getMessaggi(){return list_messages;}
 
}

