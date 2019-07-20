/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_server;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 *
 * @author Iacopo
 */
public abstract class Message implements Serializable {
    private Type type;
    private String mittente;
    private String destinatario;
    private String date_time;
    public Message(Type mt,String m,String d){
       this.type= mt;
       this.mittente=m;
       this.destinatario=d;
       DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       date_time =fmt.format(LocalDateTime.now());
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
   public String getTime(){return date_time;}
   public void setTime(String d){this.date_time=d;}
   public abstract ArrayList getCampi();
   public abstract String getTesto();
}

/**
 *
 * @author Iacopo
 */

