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
public class MessageDEL extends Message{

    public MessageDEL(String m,String ora) {
        super(Type.DEL,m);
        setTime(ora);
    }
    
    public String getTesto(){return null;}
    public ArrayList getCampi(){return null;}

}
//Messaggio che notifica la cancellazione di un messaggio da parte del suo autore