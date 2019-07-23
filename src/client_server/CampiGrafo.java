/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_server;

import java.io.Serializable;


/**
 *
 * @author Iacopo
 */
public class CampiGrafo implements Serializable{
    String nome;
    long numeroMessaggi;

    public CampiGrafo(String n, long numero) {
        this.nome = n;
        this.numeroMessaggi =numero;
    }
    
    public String getNome(){return nome;}
    public long getNumeroMessaggi(){return numeroMessaggi;}
    
}
//classe utilizzata per il trasferimento dei dati del grafo da server a client