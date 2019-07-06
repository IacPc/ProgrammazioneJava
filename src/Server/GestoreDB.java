/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import java.sql.*;
import Messaggi.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
/**
 *
 * @author Iacopo
 */
public class GestoreDB {
    private static Connection connessioneADatabase; 
    private static PreparedStatement statementinseriscimessaggi; 
    private static PreparedStatement statementinserisciutenti; 
    private static PreparedStatement statementaggiornainterazione;
    
    public  GestoreDB(String n,String pw,int porta)throws SQLException {
    
            connessioneADatabase = DriverManager.getConnection("jdbc:mysql://localhost:"+3306+"/"+n,"root",pw);   
            statementinseriscimessaggi = connessioneADatabase.prepareStatement(
                         "INSERT INTO `prog_av`.`messaggi` (`NomeMittente`,"
                         + "`NomeDestinatario`, `DataInvio`, `Testo`)"
                         + " VALUES (?, ?, ?, ?);"
                    );
            statementinserisciutenti = connessioneADatabase.prepareStatement(
                       "INSERT INTO `prog_av`.`utenticonnessi` "
                       + "(`NomeUtente`, `MessaggiRicevuti`) VALUES (?, '0');"
                    );
            statementaggiornainterazione=connessioneADatabase.prepareStatement(
                           "select NomeDestinatario as nd,count(*) as nm"
                         + " FROM prog_av.messaggi where DataInvio >= (current_date - ?)"
                         + " group by NomeDestinatario"
                         + " order by nm desc;");

    }
    
    public boolean inserisciMessaggioDB(Message m){
    
        try {
            statementinseriscimessaggi.setString(1, m.getMittente());
            statementinseriscimessaggi.setString(2, m.getDest());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String t = m.getTime().format(formatter);
            statementinseriscimessaggi.setString(3, t);
            statementinseriscimessaggi.setString(4, m.getTesto());
            
            return(statementinseriscimessaggi.executeUpdate()>=1);
        } catch (Exception e) {
            return false;
        }
    
    }
    
    public boolean inserisciutente(String n){
    
        try {
            statementinserisciutenti.setString(1, n);
            return(statementinserisciutenti.executeUpdate()>=1);
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            System.out.println("utente già presente nel db");
            return false;
        }catch(SQLException se){
            se.printStackTrace();
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
    
    

