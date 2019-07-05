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
public class Message_CHAT  extends Message{
    String testo;

    public Message_CHAT(String Testo, Type mt, String m, String d) {
        super(mt, m, d);
        this.testo = Testo;
    }
    public String getTesto(){
        return this.testo;
    }       
    public ArrayList getCampi(){return null;}

    
}
