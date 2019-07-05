/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Iacopo
 */
public  class CampiTabella {
        private final SimpleStringProperty nome;
        private final SimpleStringProperty dataora;
        private final SimpleStringProperty testo;


        public CampiTabella(String nome, String testo, String daor) {
            this.nome =new SimpleStringProperty(nome);
            this.testo =new SimpleStringProperty(testo);
            this.dataora = new SimpleStringProperty(daor);
        }
        public String getNome(){return nome.get();}
        public String getDataora(){return dataora.get();}
        public String getTesto(){return testo.get();}


    }
