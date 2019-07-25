/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;


/**
 *
 * @author Iacopo
 */
public class EventoLog implements Serializable {
    private TipoEvento tipo;
    private String ip;
    private String utente;
    private String dataora;
    private final String nome = "MyChat";
    public EventoLog(TipoEvento tipo, String ip, String utente) {
        this.tipo = tipo;
        this.ip = ip;
        this.utente = utente;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        this.dataora =dtf.format(LocalDateTime.now()) ;
    }
   
 
}
