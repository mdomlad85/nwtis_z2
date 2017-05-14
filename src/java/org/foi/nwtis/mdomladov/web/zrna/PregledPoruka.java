/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import org.foi.nwtis.mdomladov.web.kontrole.EmailHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import org.foi.nwtis.mdomladov.web.kontrole.Poruka;

/**
 *
 * @author Zeus
 */
@Named(value = "pregledPoruka")
@RequestScoped
public class PregledPoruka extends EmailHelper {

    private String adresaPosluzitelja;

    private String korisnickoIme;

    private String lozinka;

    private ArrayList<Izbornik> preuzeteMape;

    private static String izabranaMapa;

    private HashMap<String, Integer> ukupnoPoMapi;

    private ArrayList<Poruka> preuzetePoruke;

    private int ukupanBrojPoruka;

    private int velicinaStranice;

    private static int porukaOd;

    private static int porukaDo;

    private String izrazZaPretragu;

    private static boolean sljedecaStranicaOmogucena = true;

    private static boolean prethodnaStranicaOmogucena = true;

    /**
     * Creates a new instance of PregledPoruka
     */
    public PregledPoruka() {
        username = konfiguracija.getUsernameView();
        password = konfiguracija.getPasswordView();
        velicinaStranice = konfiguracija.getNumMessages();
        preuzmiMape();
        if (izabranaMapa == null && preuzeteMape.size() > 0) {
            izabranaMapa = preuzeteMape.get(0).getVrijednost();
        }
        preuzmiPoruke();
    }

    private void preuzmiMape() {
        preuzeteMape = new ArrayList<>();
        ukupnoPoMapi = new HashMap<>();

        try {

            Folder[] mape = dohvatiMape();
            prethodnaStranicaOmogucena = false;
            sljedecaStranicaOmogucena = mape.length > 0;
            for (Folder mapa : mape) {
                ukupnoPoMapi.put(mapa.getName(), mapa.getMessageCount());
                ukupanBrojPoruka += mapa.getMessageCount();
                preuzeteMape.add(new Izbornik(mapa.getName() + " - "
                        + mapa.getMessageCount(), mapa.getName()));
            }
            closeResources();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preuzmiPoruke() {
        preuzetePoruke = new ArrayList<>();

        if (porukaOd <= 0) {
            porukaOd = 1;
            porukaDo = velicinaStranice;
        }
        
        int brojPoruka = ukupnoPoMapi.get(izabranaMapa);
        sljedecaStranicaOmogucena = porukaDo < brojPoruka;

        Message[] poruke = dohvatiPorukeZaMapu(izabranaMapa, porukaOd, porukaDo, izrazZaPretragu);

        for (Message message : poruke) {
            preuzetePoruke.add(Poruka
                    .createFromMessageObj(message));
        }

        closeResources();
    }

    /**
     *
     * @return
     */
    public String promjenaMape() {

        porukaOd = -1;
        izrazZaPretragu = null;
        prethodnaStranicaOmogucena = false;
        int brojPoruka = ukupnoPoMapi.get(izabranaMapa);
        sljedecaStranicaOmogucena = porukaDo < brojPoruka;
        preuzmiPoruke();

        return "PromjenaMape";
    }

    /**
     *
     * @return
     */
    public String traziPoruke() {

        preuzmiPoruke();

        return "TraziPoruke";
    }

    /**
     *
     * @return
     */
    public String prethodnePoruke() {

        if (porukaOd != 1) {
            porukaOd -= velicinaStranice;
            porukaDo -= velicinaStranice;
            sljedecaStranicaOmogucena = true;
            prethodnaStranicaOmogucena = porukaOd != 1;
            preuzmiPoruke();
        }

        return "PrethodnePoruke";
    }

    /**
     *
     * @return
     */
    public String sljedecePoruke() {
        int brojPoruka = ukupnoPoMapi.get(izabranaMapa);
        if (porukaDo < brojPoruka) {
            porukaOd += velicinaStranice;
            porukaDo += velicinaStranice;
            prethodnaStranicaOmogucena = true;
            sljedecaStranicaOmogucena = porukaDo < brojPoruka;
            preuzmiPoruke();
        }

        return "SljedecePoruke";
    }

    /**
     *
     * @return
     */
    public String getAdresaPosluzitelja() {
        return adresaPosluzitelja;
    }

    /**
     *
     * @param adresaPosluzitelja
     */
    public void setAdresaPosluzitelja(String adresaPosluzitelja) {
        this.adresaPosluzitelja = adresaPosluzitelja;
    }

    /**
     *
     * @return
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     *
     * @param korisnickoIme
     */
    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    /**
     *
     * @return
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     *
     * @param lozinka
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     *
     * @return
     */
    public ArrayList<Izbornik> getPreuzeteMape() {
        return preuzeteMape;
    }

    /**
     *
     * @param preuzeteMape
     */
    public void setPreuzeteMape(ArrayList<Izbornik> preuzeteMape) {
        this.preuzeteMape = preuzeteMape;
    }

    /**
     *
     * @return
     */
    public String getIzabranaMapa() {
        return izabranaMapa;
    }

    /**
     *
     * @param izabranaMapa
     */
    public void setIzabranaMapa(String izabranaMapa) {
        this.izabranaMapa = izabranaMapa;
    }

    /**
     *
     * @return
     */
    public ArrayList<Poruka> getPreuzetePoruke() {
        return preuzetePoruke;
    }

    /**
     *
     * @param preuzetePoruke
     */
    public void setPreuzetePoruke(ArrayList<Poruka> preuzetePoruke) {
        this.preuzetePoruke = preuzetePoruke;
    }

    /**
     *
     * @return
     */
    public int getUkupanBrojPoruka() {
        return ukupanBrojPoruka;
    }

    /**
     *
     * @param ukupanBrojPorukaMapa
     */
    public void setUkupanBrojPoruka(int ukupanBrojPorukaMapa) {
        this.ukupanBrojPoruka = ukupanBrojPorukaMapa;
    }

    /**
     *
     * @return
     */
    public int getVelicinaStranice() {
        return velicinaStranice;
    }

    /**
     *
     * @param velicinaStranice
     */
    public void setVelicinaStranice(int velicinaStranice) {
        this.velicinaStranice = velicinaStranice;
    }

    /**
     *
     * @return
     */
    public int getPorukaOd() {
        return porukaOd;
    }

    /**
     *
     * @param porukaOd
     */
    public void setPorukaOd(int porukaOd) {
        this.porukaOd = porukaOd;
    }

    /**
     *
     * @return
     */
    public int getPorukaDo() {
        return porukaDo;
    }

    /**
     *
     * @param porukaDo
     */
    public void setPorukaDo(int porukaDo) {
        this.porukaDo = porukaDo;
    }

    /**
     *
     * @return
     */
    public String getIzrazZaPretragu() {
        return izrazZaPretragu;
    }

    /**
     *
     * @param izrazZaPretragu
     */
    public void setIzrazZaPretragu(String izrazZaPretragu) {
        this.izrazZaPretragu = izrazZaPretragu;
    }

    /**
     *
     * @return
     */
    public boolean isSljedecaStranicaOmogucena() {
        return sljedecaStranicaOmogucena;
    }

    /**
     *
     * @param sljedecaStranicaOmogucena
     */
    public void setSljedecaStranicaOmogucena(boolean sljedecaStranicaOmogucena) {
        PregledPoruka.sljedecaStranicaOmogucena = sljedecaStranicaOmogucena;
    }

    /**
     *
     * @return
     */
    public boolean isPrethodnaStranicaOmogucena() {
        return prethodnaStranicaOmogucena;
    }

    /**
     *
     * @param prethodnaStranicaOmogucena
     */
    public void setPrethodnaStranicaOmogucena(boolean prethodnaStranicaOmogucena) {
        PregledPoruka.prethodnaStranicaOmogucena = prethodnaStranicaOmogucena;
    }

}
