/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Iacopo
 */
public class ParamConfServer {
   
    public int porta_ascolto;
    public ParamDB pDB;
    public ParamGrafo pGR;

    public ParamConfServer(int porta_ascolto,ParamDB pd,ParamGrafo pg) {
        this.porta_ascolto = porta_ascolto;
        this.pDB= pd;
        this.pGR=pg; 
    }
    
    
    public class ParamDB{
        public String nomeDB;
        public String passDB;
        public int portadb;
        public String ipDB;

        public ParamDB(String nomeDB, String passDB,int porta,String ip) {
            this.nomeDB = nomeDB;
            this.passDB = passDB;
            this.portadb=porta;
            this.ipDB= ip;
        }
       
    }
    public class ParamGrafo{
        int giornigrafo;
        int quantiutenti;

        public ParamGrafo(int giornigrafo, int quantiutenti) {
            this.giornigrafo = giornigrafo;
            this.quantiutenti = quantiutenti;
        }
     
    }
    
}
