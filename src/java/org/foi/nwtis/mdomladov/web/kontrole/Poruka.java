/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.kontrole;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author dkermek
 */
public class Poruka {

    /**
     *
     * @param message
     * @return
     */
    public static Poruka createFromMessageObj(Message message) {

        Poruka poruka = null;
        try {
            poruka = new Poruka();

            poruka.id = String.valueOf(message.getMessageNumber());
            poruka.vrijemeSlanja = message.getSentDate();
            poruka.vrijemePrijema = message.getReceivedDate();
            
            poruka.salje = ((InternetAddress)(message.getFrom()[0]))
                    .getAddress();
            
            poruka.predmet = message.getSubject();
            poruka.sadrzaj = message.getContent().toString();
            poruka.vrsta = message.getContentType();
            
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Poruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return poruka;
    }

    private String id;
    private Date vrijemeSlanja;
    private Date vrijemePrijema;
    private String salje;
    private String predmet;
    private String sadrzaj;
    private String vrsta;

    /**
     *
     * @param id
     * @param vrijemeSlanja
     * @param vrijemePrijema
     * @param salje
     * @param predmet
     * @param sadrzaj
     * @param vrsta
     */
    public Poruka(String id, Date vrijemeSlanja, Date vrijemePrijema, String salje, String predmet, String sadrzaj, String vrsta) {
        this.id = id;
        this.vrijemeSlanja = vrijemeSlanja;
        this.vrijemePrijema = vrijemePrijema;
        this.salje = salje;
        this.predmet = predmet;
        this.sadrzaj = sadrzaj;
        this.vrsta = vrsta;
    }

    /**
     *
     */
    public Poruka() {
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public Date getVrijemeSlanja() {
        return vrijemeSlanja;
    }

    /**
     *
     * @return
     */
    public Date getVrijemePrijema() {
        return vrijemePrijema;
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
     * @return
     */
    public String getSalje() {
        return salje;
    }

    /**
     *
     * @return
     */
    public String getVrsta() {
        return vrsta;
    }

    /**
     *
     * @return
     */
    public String getSadrzaj() {
        return sadrzaj;
    }

}
