/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.Serializable;


/**
 *
 * @author Iacopo
 */
public  class CampiTabella implements Serializable {
        private final String nome;
        private final String dataora;
        private final String testo;


        public CampiTabella(String nome, String daor, String testo) {
            this.nome =nome;
            this.testo =testo;
            this.dataora = daor;
        }
        public String getNome(){return nome;}
        public String getDataora(){return dataora;}
        public String getTesto(){return testo;}


    }