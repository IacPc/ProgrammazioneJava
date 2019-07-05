/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messaggi;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 *
 * @author Iacopo
 */
public abstract class Message implements Serializable {
    private Type type;
    private String mittente;
    private String destinatario;
    private LocalDateTime date_time;
    public Message(Type mt,String m,String d){
       this.type= mt;
       this.mittente=m;
       this.destinatario=d;
       date_time = LocalDateTime.now();
    }

    
    public Message(Type mt,String m){
       this(mt,m,null);
    }
 
    public Message(Type mt){
       this(mt,null,null);
    }
    
   public Type getTipo() {return type;}
   public String getMittente(){return this.mittente;}
   public String getDest(){return this.destinatario;}
   public LocalDateTime getTime(){return date_time;}
   public abstract ArrayList getCampi();
   public abstract String getTesto();
}

/**
 *
 * @author Iacopo
 */

