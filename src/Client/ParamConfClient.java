/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;


import java.io.IOException;


/**
 *
 * @author Iacopo
 */
public class ParamConfClient {
 
    public String fontCelleTabella;
    public String server_ip; 
    public int server_port; 

    public ParamConfClient(String ip,String f, int port) throws IOException {
       fontCelleTabella=f;
       server_ip=ip;
       server_port=port;
      
    }
    //Descrive i parametri di configurazione del client
}
