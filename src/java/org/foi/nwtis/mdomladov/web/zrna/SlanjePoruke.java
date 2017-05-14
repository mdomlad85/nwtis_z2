/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import org.foi.nwtis.mdomladov.web.kontrole.EmailHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.MessagingException;

/**
 *
 * @author Marko Domladovac
 */
@Named(value = "slanjePoruke")
@ManagedBean
@RequestScoped
public class SlanjePoruke extends EmailHelper {

    /**
     * adresa primatelja poruke
     */
    private String primatelj;

    /**
     * adresa posiljatelja poruke
     */
    private String posiljatelj;

    /**
     * predmet poruke
     */
    private String predmet;

    /**
     * tekst same poruke
     */
    private String tekst;

    private String poruka;
    
    private boolean slanjeOmoguceno;

    /**
     *
     * @throws MessagingException
     */
    public void saljiPoruku() throws MessagingException {
        try {         
            slanjeOmoguceno = false;
            saljiPoruku(posiljatelj, primatelj, predmet, tekst);
            resetirajFormu();
            poruka = "Poruka je poslana!";
        } catch (MessagingException ex) {
            Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            poruka = "Došlo je do pogreške!";
        }
    }

    /**
     * konstruktor zrna SlanjePoruke
     */
    public SlanjePoruke() {
        poruka = "";
        slanjeOmoguceno = true;
    }

    /**
     *
     * @return
     */
    public String getPrimatelj() {
        return primatelj;
    }

    /**
     *
     * @param primatelj
     */
    public void setPrimatelj(String primatelj) {
        this.primatelj = primatelj;
    }

    /**
     *
     * @return
     */
    public String getPosiljatelj() {
        return posiljatelj;
    }

    /**
     *
     * @param posiljatelj
     */
    public void setPosiljatelj(String posiljatelj) {
        this.posiljatelj = posiljatelj;
    }

    /**
     *
     * @return
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     *
     * @param predmet
     */
    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    /**
     *
     * @return
     */
    public String getTekst() {
        return tekst;
    }

    /**
     *
     * @param tekst
     */
    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public boolean isSlanjeOmoguceno() {
        return slanjeOmoguceno;
    }

    /**
     *
     * @param slanjeOmoguceno
     */
    public void setSlanjeOmoguceno(boolean slanjeOmoguceno) {
        this.slanjeOmoguceno = slanjeOmoguceno;
    }

    private void resetirajFormu() {
        tekst = "";
        primatelj = "";
        posiljatelj = "";
        predmet = "";
        slanjeOmoguceno = true;
    }

    
}
