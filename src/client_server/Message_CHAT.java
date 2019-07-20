/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_server;

import java.util.ArrayList;

/**
 *
 * @author Iacopo
 */
public class Message_CHAT extends Message{
    public String testo;
    public Message_CHAT(String Testo,Type t, String m, String d) {
        super(t, m, d);
        this.testo = Testo;
    }
    public String getTesto(){
        return this.testo;
    }       
    public ArrayList getCampi(){return null;}
    
}
