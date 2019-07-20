/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import client_server.CampiGrafo;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart; 

/**
 *
 * @author Iacopo
 */
public class GrafoMessaggiRicevuti extends PieChart {
    private static ObservableList<PieChart.Data> utenti =
                FXCollections.observableArrayList(); 

    public GrafoMessaggiRicevuti() {
        super(utenti);
        setTitle("Attività Degli Utenti"); 
        setClockwise(true); //visione senso orario dei dati
        setLabelLineLength(50); //impostare spessore delle etichette
        setLabelsVisible(true); 
        setStartAngle(180);
    }
    
    public void aggiungiuntenti(ArrayList<CampiGrafo> al){
        utenti.clear();
        for(int i =0;i<al.size();i++){
            utenti.add(new PieChart.Data(
                        al.get(i).getNome(),
                        al.get(i).getNumeroMessaggi())
                       );
        
        }
    
    } 
    
    public ObservableList getUtenti(){return this.utenti;}
    
}
