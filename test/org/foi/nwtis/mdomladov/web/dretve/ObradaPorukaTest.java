/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.dretve;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zeus
 */
public class ObradaPorukaTest {

    private static final String SUBJECT = "NWTiS poruka";

    /**
     *
     */
    public ObradaPorukaTest() {
    }

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of jeIspravnaPoruka method, of class ObradaPoruka.
     */
    @Test
    public void testJeIspravnaNeispravnaPoruka() {
        System.out.println("Je Ispravna Neispravna Poruka?");
        String addMsg = "ADD IoT 1 \"FOI Varaždin\" GPS: 46.307756,16.33668;";
        String tempMsg = "TEP IoT 1 T: 2017.04.11 17:20:19 C: 14.2;";
        String eventMsg = "EVENT IoT 1 T: 2017.04.1 16:45:36 F: 22;";

        ObradaPoruka instance;
        boolean addResult = false;
        boolean eventResult = false;
        boolean tempResult = false;
        try {
            instance = new ObradaPoruka("NWTiS.db.config_1.xml");
            addResult = instance.jeIspravnaPoruka(SUBJECT, addMsg);
            tempResult = instance.jeIspravnaPoruka(SUBJECT, tempMsg);
            eventResult = instance.jeIspravnaPoruka(SUBJECT, eventMsg);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ObradaPorukaTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertFalse(addResult && tempResult && eventResult);
    }

    /**
     * Test of jeIspravnaPoruka method, of class ObradaPoruka.
     */
    @Test
    public void testJeIspravnaIspravnaPoruka() {
        System.out.println("Je Ispravna Ispravna Poruka?");
        String addMsg = "ADD IoT 1 \"FOI Varaždin\" GPS: 46.307756,16.337668;";
        String tempMsg = "TEMP IoT 1 T: 2017.04.11 17:20:19 C: 14.2;";
        String eventMsg = "EVENT IoT 1 T: 2017.04.11 16:45:36 F: 22;";

        ObradaPoruka instance;
        boolean addResult = false;
        boolean eventResult = false;
        boolean tempResult = false;
        try {
            instance = new ObradaPoruka("NWTiS.db.config_1.xml");
            addResult = instance.jeIspravnaPoruka(SUBJECT, addMsg);
            tempResult = instance.jeIspravnaPoruka(SUBJECT, tempMsg);
            eventResult = instance.jeIspravnaPoruka(SUBJECT, eventMsg);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ObradaPorukaTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(addResult && tempResult && eventResult);
    }
}
