/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Iacopo
 */
public enum TipoEvento {
    TABLE_CLICK,MENU_CLICK,INVIA_CLICK,AGGSTAT_CLICK,EXIT_CLICK,DEL_CLICK;
    public String toString(){
        switch(this){
            case TABLE_CLICK: return "TABLE_CLICK";
            case MENU_CLICK: return "MENU_CLICK";
            case INVIA_CLICK: return "INVIA_CLICK";
            case AGGSTAT_CLICK: return "AGGSTAT_CLICK";
            case EXIT_CLICK : return "EXIT_CLICK";
            case DEL_CLICK: return "DEL_CLICK";
            default: return null;
        }
    }
}
//Descrive le tipologie di evento da memoriizare nel file di log remoto