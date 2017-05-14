/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * Klasa za rad s bazom podataka
 * 
 * @author Marko Domladovac
 */
public class DBHelper extends CoreHelper {

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    /**
     *
     * @param m
     * @return
     */
    public static boolean addIotUredaj(Matcher m) {
        boolean retVal = false;
        if (exists(getIotExistsQuery(m.group(1)))) {
            retVal = add(getInsertIotQuery(m));
        }
        return retVal;
    }

    /**
     *
     * @param m
     * @return
     */
    public static boolean addTemp(Matcher m) {
        boolean retVal = false;
        if (exists(getIotExistsQuery(m.group(1)))) {
            retVal = add(getInsertTempQuery(m));
        }
        return retVal;
    }

    /**
     *
     * @param m
     * @return
     */
    public static boolean addEvent(Matcher m) {
        boolean retVal = false;
        if (exists(getIotExistsQuery(m.group(1)))) {
            retVal = add(getInsertEventQuery(m));
        }
        return retVal;
    }

    private static boolean add(String query) {
        boolean isValid = false;
        try {
            openConnection();
            isValid = stmt.executeUpdate(query) == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    private static boolean exists(String query) {
        boolean exists = false;
        try {
            openConnection();
            rs = stmt.executeQuery(query);
            exists = rs.next() && rs.getBoolean(1);
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return exists;
    }

    private static void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(konfiguracija.getDriverDatabase());
        conn = DriverManager
                .getConnection(konfiguracija.getUrl(),
                        konfiguracija.getAdminUsername(),
                        konfiguracija.getAdminPassword());

        stmt = (Statement) conn.createStatement();
    }

    private static void closeDbResources() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String getInsertIotQuery(Matcher m) {
        String[] geodata = m.group(3).split(",");
        StringBuilder query = new StringBuilder("INSERT INTO uredaji ");
        query.append("(id, naziv, latitude, longitude) VALUES (");
        query.append(m.group(1));
        query.append(", '");
        query.append(m.group(2));
        query.append("', ");
        query.append(geodata[0]);
        query.append(", ");
        query.append(geodata[1]);
        query.append("");
        query.append(")");

        return query.toString();
    }

    private static String getInsertTempQuery(Matcher m) {
        StringBuilder query = new StringBuilder("INSERT INTO temperature ");
        query.append("(id, vrijeme_mjerenja, temp) VALUES (");
        query.append(m.group(1));
        query.append(", '");
        query.append(m.group(2));
        query.append("', ");
        query.append(m.group(9));
        query.append("");
        query.append(")");

        return query.toString();
    }

    private static String getInsertEventQuery(Matcher m) {
        StringBuilder query = new StringBuilder("INSERT INTO dogadaji ");
        query.append("(id, vrijeme_izvrsavanja, vrsta) VALUES (");
        query.append(m.group(1));
        query.append(", '");
        query.append(m.group(2));
        query.append("', ");
        query.append(m.group(9));
        query.append("");
        query.append(")");

        return query.toString();
    }

    private static String getIotExistsQuery(String id) {
        StringBuilder query = new StringBuilder("SELECT EXISTS(SELECT 1 FROM uredaji WHERE id = ");
        query.append(id);
        query.append(")");

        return query.toString();
    }
}
