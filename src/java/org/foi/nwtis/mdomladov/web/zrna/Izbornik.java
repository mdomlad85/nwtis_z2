/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.Serializable;
import javax.annotation.ManagedBean;

/**
 *
 * @author Zeus
 */
@ManagedBean
public class Izbornik implements Serializable {

    private String labela;
    
    private String vrijednost;

    /**
     *
     * @param labela
     * @param vrijednost
     */
    public Izbornik(String labela, String vrijednost) {
        this.labela = labela;
        this.vrijednost = vrijednost;
    }

    /**
     *
     */
    public Izbornik() {
    }

    /**
     *
     * @return
     */
    public String getLabela() {
        return labela;
    }

    /**
     *
     * @param labela
     */
    public void setLabela(String labela) {
        this.labela = labela;
    }

    /**
     *
     * @return
     */
    public String getVrijednost() {
        return vrijednost;
    }

    /**
     *
     * @param vrijednost
     */
    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    
}
