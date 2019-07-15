/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messaggi;

import java.util.ArrayList;

/**
 *
 * @author Iacopo
 */
public class Message_CHAT extends Message{
    private String testo;
    public Message_CHAT(String Testo, String m, String d) {
        super(Type.CHAT, m, d);
        this.testo = Testo;
    }
    public String getTesto(){
        return this.testo;
    }       
    public ArrayList getCampi(){return null;}
    
}
