/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.kontrole;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 * Osnovne stvari koje koriste svi helperi idu
 * u ovu klase
 * 
 * @author Marko Domladovac
 */
public class CoreHelper extends Thread {

    /**
     *
     * konfiguracija
     */
    protected static APP_Konfiguracija konfiguracija;

    /**
     *
     * Podrazumijevani konstruktor
     * Inicijalizira konfiguraciju
     */
    public CoreHelper() {
        napuniKonfiguraciju();
    }

    /**
     *
     * Konstruktor kojemu se predaje putanja od konfiguracije
     * 
     * @param filePath
     * @throws NemaKonfiguracije
     * @throws NeispravnaKonfiguracija
     */
    public CoreHelper(String filePath) throws NemaKonfiguracije, NeispravnaKonfiguracija {
            konfiguracija  = new APP_Konfiguracija(filePath);
    }

    private void napuniKonfiguraciju() {
        if (konfiguracija == null) {
            ServletContext sc = (ServletContext) FacesContext
                    .getCurrentInstance().getExternalContext().getContext();

            konfiguracija = (APP_Konfiguracija) sc.getAttribute(SlusacAplikacije.APP_KONFIG);
        }
    }
}
