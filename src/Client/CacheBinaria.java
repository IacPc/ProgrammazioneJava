/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Messaggi.CampiGrafo;
import java.io.Serializable;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 *
 * @author Iacopo
 */
public class CacheBinaria implements Serializable{
    
    String testoLasciatoincasella;
    ArrayList<CampiTabella> ultimi_mess;
    String ultimouser;
    ArrayList<CampiGrafo> ultime_interazioni;


    public CacheBinaria(String testoL,ObservableList<PieChart.Data> ui, ObservableList<CampiTabella> um,String ul) {
        this.testoLasciatoincasella = testoL;
        this.ultimouser = ul;
        ultimi_mess= new ArrayList<>();
        for(int i =0;i<um.size();i++)
            ultimi_mess.add((CampiTabella) um.get(i));
        ultime_interazioni = new ArrayList<>();
        for(int i =0;i<ui.size();i++){
           long val= Math.round( ui.get(i).getPieValue());
           ultime_interazioni.add(new CampiGrafo(ui.get(i).getName(),val));
        }

    }
    
    
}
