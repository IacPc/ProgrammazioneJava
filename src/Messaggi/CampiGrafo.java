/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messaggi;

import java.io.Serializable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Iacopo
 */
public class CampiGrafo implements Serializable{
    String nome;
    int numeroMessaggi;

    public CampiGrafo(String n, int numero) {
        this.nome = n;
        this.numeroMessaggi =numero;
    }
    
    public String getNome(){return nome;}
    public int getNumeroMessaggi(){return numeroMessaggi;}
    
}
