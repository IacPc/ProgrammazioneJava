/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import client_server.*;

import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Iacopo
 */
public class GestoreDB {
    private static Connection connessioneADatabase; 
    private static PreparedStatement statementinseriscimessaggi; 
    private static PreparedStatement statementaggiornainterazione;
   
    public  GestoreDB(String nomeconn,String pw,int porta,String ip)throws SQLException {
            connessioneADatabase = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porta+"/"+nomeconn,"root",pw);
            statementinseriscimessaggi = connessioneADatabase.prepareStatement(
                         "INSERT INTO `prog_av`.`messaggi` (`NomeMittente`,"
                         + "`NomeDestinatario`, `DataInvio`, `Testo`,`Prog`)"
                         + " VALUES (?, ?, ?, ?,?);"
                    );
            
        statementaggiornainterazione=connessioneADatabase.prepareStatement(
                           " select NomeDestinatario as nd,count(*) as nm"
                         + " FROM prog_av.messaggi where DataInvio >= (current_date - ?)"
                         + " group by NomeDestinatario"
                         + " order by nm desc;");
    }
    
    public boolean inserisciMessaggioDB(Message m,int i){
    
        try {
            statementinseriscimessaggi.setString(1, m.getMittente());
            statementinseriscimessaggi.setString(2, m.getDest());
            String t = m.getTime();
            statementinseriscimessaggi.setString(3, t);
            statementinseriscimessaggi.setString(4, m.getTesto());
            statementinseriscimessaggi.setInt(5, i);
            return(statementinseriscimessaggi.executeUpdate()>=1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    
    }
    
    public ArrayList aggiornaInterazioneUtente(int giorni,int quanti ){
        ArrayList<CampiGrafo> al = new ArrayList<>();
        try {
            statementaggiornainterazione.setInt(1, giorni);
            ResultSet rs = statementaggiornainterazione.executeQuery();
            int i =0;
            while (rs.next() & i<quanti){
                al.add(new CampiGrafo(rs.getString("nd"), rs.getInt("nm")));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return al;
    }
  
 }
    
//statementinseriscimessaggi è responsabile dell'inseriemnto nel db dei messaggi inviati
//statementaggiornainterazioni ricava le informazioni per costruire il grafo
//Le funzioni inserisciMessaggioDB(Message m,int i) e aggiornaInterazioneUtente(int giorni,int quanti )
//settano i parametri delle query e interrogano il db