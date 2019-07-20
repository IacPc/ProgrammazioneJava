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
public class MessageSTAT extends Message {

    ArrayList<CampiGrafo> campi;
    public MessageSTAT(String n,ArrayList al) {
        super(Type.STAT,n);
        campi =al;
    }
    public ArrayList getCampi(){return this.campi;}
    public String getTesto(){return null;}

    
}
