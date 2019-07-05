/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Messaggi.CampiGrafo;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;


/**
 *
 * @author Iacopo
 */
public class ParamConfClient {
 
    public ConfigurazioneChatBoxXML confchat;
    public ConfigurazioneServerXML confserver;

    public ParamConfClient(String xml) throws IOException {
        
        XStream xsconf = new XStream();
        ParamConfClient pc= (ParamConfClient)xsconf.fromXML(xml);  
        this.confchat= pc.confchat;
        this.confserver=pc.confserver;
    }
    
    
    
    class ConfigurazioneChatBoxXML{
   
        public String fontCelleTabella ;
        
    }
    
   
    class ConfigurazioneServerXML {
        public String ip; 
        public int port; 

    
    }
}
