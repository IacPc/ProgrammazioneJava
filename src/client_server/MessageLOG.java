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
public class MessageLOG extends Message {
    private String xml;

    public MessageLOG(String xml) {
        super(Type.LOG);
        this.xml = xml;
    }
    public String getTesto(){return xml;}
    public ArrayList getCampi(){return null;}
}
