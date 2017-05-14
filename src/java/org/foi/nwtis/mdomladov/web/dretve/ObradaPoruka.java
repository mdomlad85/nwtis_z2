/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.dretve;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mdomladov.web.kontrole.DBHelper;
import org.foi.nwtis.mdomladov.web.kontrole.EmailHelper;
import org.foi.nwtis.mdomladov.web.kontrole.Statistika;

/**
 * Klasa ObradaPoruka provjerava na poslužitelju (adresa i IMAP port određena
 * konfiguracijom) u pravilnom intervalu (određen konfiguracijom u sek) ima li
 * poruka u poštanskom sandučiću korisnika (adresa i lozinka određeni
 * konfiguracijom, npr. servis@nwtis.nastava.foi.hr, 123456). Koristi se IMAP
 * protokol. Poruke koje u predmetu (subject) imaju točno traženi sadržaj
 * (određen konfiguracijom, npr. NWTiS poruka) obrađuju se tako da se ispituje
 * sadržaj poruke. Poruka treba biti u "text/plain" formatu kako bi se provjerio
 * njen sadržaj
 *
 * Obrađene poruke (određen konfiguracijom, npr. NWTiS poruka) prebacuju se u 
 * posebnu mapu (određena konfiguracijom, npr. NWTiS_Poruke). Ostale poruke 
 * prebacuju se u svoju mapu (određena konfiguracijom, npr. NWTiS_OstalePoruke). 
 * Ako neka mapa ne postoji, dretva ju treba sama kreirati. 
 * 
 * Dretva na kraju svakog ciklusa šalje email poruku u text/plain formatu na 
 * adresu (određena konfiguracijom, npr. admin@nwtis.nastava.foi.hr), uz predmet 
 * koji započinje statičkim dijelom (određen konfiguracijom, npr. Statistika 
 * poruka) iza kojeg dolazi redni broj poruke u formatu #.##0
 * 
 * @author Marko Domladovac
 */
public class ObradaPoruka extends EmailHelper {

    private long startTime;

    private Statistika statistika;

    /**
     * parametar trajanjeSpavanja definira koliko će trajati jedan ciklus u
     * milisekundama
     */
    private int interval;

    private ArrayList<IspravanPodatak> ispravniPodaci;

    /**
     *
     */
    public ObradaPoruka() {
        inicijalizirajVarijable();
    }

    ObradaPoruka(String filepath) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        super(filepath);
        inicijalizirajVarijable();
    }

    private void inicijalizirajVarijable() {
        interval = konfiguracija.getTimeSecThread() * 1000;
        ispravniPodaci = new ArrayList<>();
        username = konfiguracija.getUsernameThread();
        password = konfiguracija.getPasswordThread();
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {
            try {
                pripemiRad();
                messages = dohvatiPorukeZaMapu(INBOX_FOLDER, null, null, null);
                obradiNovePoruke();
                closeResources();
                obradiIspravnePodatke();
                zavrsiRad();
                sleep(getTrajanjeSpavanja());
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void pripemiRad() {
        startTime = System.currentTimeMillis();
        statistika = new Statistika();
        statistika.setStartTime(startTime);
    }

    private void obradiNovePoruke() {
        for (Message message : messages) {
            statistika.ukupnoPoruka++;
            
            try {
                String messageContent = message.getContent().toString();
                
                String ct = message.getContentType();
                
                if (message.getContentType().toLowerCase()
                        .contains(ContentType.TEXT_PLAIN)) {
                    if (jeIspravnaPoruka(message.getSubject(), messageContent)) {
                        statistika.ukupnoIspravnihPoruka++;
                        kopirajPoruku(konfiguracija.geFolderNWTiS(),
                                store, message, folder);
                    } else {
                        statistika.ukupnoNeispravnihPoruka++;
                        kopirajPoruku(konfiguracija.getFolderOther(),
                                store, message, folder);
                    }
                } else {
                    statistika.ukupnoNeispravnihPoruka++;
                    kopirajPoruku(konfiguracija.getFolderOther(), store, message, folder);
                }
            } catch (MessagingException | IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void obradiIspravnePodatke() {
        for (IspravanPodatak ispravanPodatak : ispravniPodaci) {
            switch (ispravanPodatak.vrstaNaredbe) {
                case ADD:
                    if (DBHelper.addIotUredaj(ispravanPodatak.naredba)) {
                        statistika.brojDodanihIota++;
                    } else {
                        statistika.brojPogresaka++;
                    }
                    break;
                case TEMP:
                    if (DBHelper.addTemp(ispravanPodatak.naredba)) {
                        statistika.brojMjerenihTemp++;
                    } else {
                        statistika.brojPogresaka++;
                    }
                    break;
                case EVENT:
                    if (DBHelper.addEvent(ispravanPodatak.naredba)) {
                        statistika.brojIzvrsenihEventa++;
                    } else {
                        statistika.brojPogresaka++;
                    }
                    break;
            }
        }
    }

    private void zavrsiRad() {
        statistika.setStopTime(System.currentTimeMillis());
        try {
            saljiPoruku(konfiguracija.getUsernameThread(), konfiguracija.getUsernameStatistics(),
                    String.format("%s %s", konfiguracija.getSubjectStatistics(), dohvatiSufixPredmeta()),
                    statistika.toString());
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String dohvatiSufixPredmeta() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(new Locale("hr"));
        unusualSymbols.setGroupingSeparator('.');
        String strange = "#,##0";
        DecimalFormat weirdFormatter = new DecimalFormat(strange, unusualSymbols);
        weirdFormatter.setGroupingSize(4);
        
        return weirdFormatter.format(Statistika.getBrojac());
    }

    private void kopirajPoruku(String nazivDirektorija, Store store, Message message, Folder folder)
            throws MessagingException {

        Folder direktorij = store.getFolder(nazivDirektorija);
        if (!direktorij.exists()) {
            direktorij.create(Folder.HOLDS_MESSAGES);
        }
        direktorij.open(Folder.READ_WRITE);
        Message[] zaKopiranje = {message};
        folder.copyMessages(zaKopiranje, direktorij);
        direktorij.close(false);
        message.setFlag(Flags.Flag.DELETED, true);
    }

    @Override
    public synchronized void start() {
        Logger.getLogger(ObradaPoruka.class.getName()).log(Level.INFO,
                "ObradaPoruka je započela s radom", this);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interrupt() {
        Logger.getLogger(ObradaPoruka.class.getName()).log(Level.INFO,
                "ObradaPoruka je zavrsila s radom", this);
        super.interrupt();
    }

    /**
     *
     * @return
     */
    protected long getTrajanjeSpavanja() {
        int i = 1;
        long spavanje = 0;
        do {
            spavanje = i++ * interval - (System.currentTimeMillis() - startTime) / 1000;
        } while (spavanje < 0);

        return spavanje;
    }

    /**
     *
     * @param subject
     * @param content
     * @return
     */
    public boolean jeIspravnaPoruka(String subject, String content) {

        boolean retVal = false;

        if (subject.equals(konfiguracija.getSubject())) {
            for (VrstaNaredbe vn : SINTAKSA.keySet()) {
                String sintaksa = SINTAKSA.get(vn);
                Pattern pattern = Pattern.compile(sintaksa);
                Matcher m = pattern.matcher(content);
                if (m.find()) {
                    retVal = true;
                    ispravniPodaci.add(new IspravanPodatak(vn, m));
                }
            }
        }

        return retVal;
    }

    private enum VrstaNaredbe {
        ADD,
        TEMP,
        EVENT
    }

    private class IspravanPodatak {

        protected VrstaNaredbe vrstaNaredbe;
        protected Matcher naredba;

        private IspravanPodatak(VrstaNaredbe vn, Matcher m) {
            this.vrstaNaredbe = vn;
            this.naredba = m;
        }
    }

    private static final HashMap<VrstaNaredbe, String> SINTAKSA;

    static {
        String datum = "((1[89][0-9]{2}|2[0-9]{3})\\.(0[1-9]|1[0-2])\\.(0[1-9]|[12][0-9]|3[01]) (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9]))";
        SINTAKSA = new HashMap<>();
        SINTAKSA.put(VrstaNaredbe.ADD, "^ADD IoT (\\d{1,6}) \\\"(.{1,30})\\\" ?GPS: (-?\\d{1,3}\\.\\d{6},-?\\d{1,3}\\.\\d{6});");
        SINTAKSA.put(VrstaNaredbe.TEMP, String.format("^TEMP IoT (\\d{1,6}) T: %s C: (-?\\d{1,2}\\.\\d);", datum));
        SINTAKSA.put(VrstaNaredbe.EVENT, String.format("^EVENT IoT (\\d{1,6}) T: %s F: (-?\\d{1,2});", datum));
    }
}
