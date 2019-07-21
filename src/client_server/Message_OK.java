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
public class Message_OK extends Message{
    ArrayList<String> utenti;
            
    
    public Message_OK(ArrayList<String> al) {
        super(Type.OK);
        utenti = al;
    }
    public String getTesto(){return null;}
    public ArrayList getCampi(){return utenti;}
    
}
