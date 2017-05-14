/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Zeus
 */
@Named(value = "navigacija")
@ManagedBean
@SessionScoped
public class Navigacija implements Serializable {

    private static boolean pocetnaOmogucena;

    private static boolean slanjeOmoguceno;

    private static boolean pregledOmogucen;

    /**
     * Creates a new instance of Navigacija
     */
    public Navigacija() {
    }

    /**
     *
     * @throws IOException
     */
    public void slanjePorukeLink() throws IOException {
        pocetnaOmogucena = true;
        slanjeOmoguceno = false;
        pregledOmogucen = true;

        FacesContext.getCurrentInstance()
                .getApplication()
                .getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "slanjePoruke.xhtml");
    }

    /**
     *
     * @throws IOException
     */
    public void pregledPorukaLink() throws IOException {
        pocetnaOmogucena = true;
        slanjeOmoguceno = true;
        pregledOmogucen = false;
        
        FacesContext.getCurrentInstance()
                .getApplication()
                .getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "pregledPoruka.xhtml");
    }

    /**
     *
     * @throws IOException
     */
    public void pocetnaLink() throws IOException {
        pocetnaOmogucena = false;
        slanjeOmoguceno = true;
        pregledOmogucen = true;
        
        FacesContext.getCurrentInstance()
                .getApplication()
                .getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
    }

    public boolean isPocetnaOmogucena() {
        return pocetnaOmogucena;
    }

    public boolean isSlanjeOmoguceno() {
        return slanjeOmoguceno;
    }

    public boolean isPregledOmogucen() {
        return pregledOmogucen;
    }

    public static void setPocetnaOmogucena(boolean pocetnaOmogucena) {
        Navigacija.pocetnaOmogucena = pocetnaOmogucena;
    }

    public static void setSlanjeOmoguceno(boolean slanjeOmoguceno) {
        Navigacija.slanjeOmoguceno = slanjeOmoguceno;
    }

    public static void setPregledOmogucen(boolean pregledOmogucen) {
        Navigacija.pregledOmogucen = pregledOmogucen;
    }

}
