/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 *
 * Klasa za upravljanje resursima pri promjeni jezika
 * i18n standard
 * 
 * @author Marko Domladovac
 */
public class i18n extends ResourceBundle {

    /**
     *
     * Puni naziv klase
     */
    protected static final String BUNDLE_NAME = "org.foi.nwtis.mdomladov.i18n";

    /**
     *
     * Ekstenzija
     */
    protected static final String BUNDLE_EXTENSION = "properties";

    /**
     *
     * UTF8 kontrola
     */
    protected static final Control UTF8_CONTROL = new UTF8Control();

    /**
     *
     * konstruktor u kojem postavljamo i18n kontrolu
     */
    public i18n() {
        setParent(ResourceBundle.getBundle(BUNDLE_NAME, 
            FacesContext.getCurrentInstance().getViewRoot().getLocale(), UTF8_CONTROL));
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }

    /**
     *
     * Definiranje UTF8 kontrole
     */
    protected static class UTF8Control extends Control {
        @Override
        public ResourceBundle newBundle
            (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException
        {
            // The below code is copied from default Control#newBundle() implementation.
            // Only the PropertyResourceBundle line is changed to read the file as UTF-8.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }

}