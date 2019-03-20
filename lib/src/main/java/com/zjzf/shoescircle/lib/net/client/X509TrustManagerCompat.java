/**
 * Represents an ordered list of {@link X509TrustManager}s with additive trust. If any one of the composed managers
 * trusts a certificate chain, then it is trusted by the composite manager.
 * <p>
 * This is necessary because of the fine-print on {@link SSLContext#init}: Only the first instance of a particular key
 * and/or trust manager implementation type in the array is used. (For example, only the first
 * javax.net.ssl.X509KeyManager in the array will be used.)
 *
 * @author codyaray
 * @see <a href="http://stackoverflow.com/questions/1793979/registering-multiple-keystores-in-jvm">
 * http://stackoverflow.com/questions/1793979/registering-multiple-keystores-in-jvm
 * </a>
 * @since 4/22/2013
 */
package com.zjzf.shoescircle.lib.net.client;


import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 陈志远 on 2017/5/5.
 * <p>
 * gist:https://gist.github.com/HughJeffner/6eac419b18c6001aeadb
 * <p>
 * 兼容系统keystore和android keystore的trust manager
 */

@SuppressWarnings("unused")
public class X509TrustManagerCompat implements X509TrustManager {

    private final List<X509TrustManager> trustManagers;

    public X509TrustManagerCompat(List<X509TrustManager> trustManagers) {
        this.trustManagers = new ArrayList<>();
        if (trustManagers != null) {
            this.trustManagers.addAll(trustManagers);
        }
    }

    public X509TrustManagerCompat(KeyStore keystore) {
        this.trustManagers = new ArrayList<>();
        this.trustManagers.add(getDefaultTrustManager());
        this.trustManagers.add(getTrustManager(keystore));

    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkClientTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkServerTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> certificates = new ArrayList<>();
        for (X509TrustManager trustManager : trustManagers) {
            for (X509Certificate cert : trustManager.getAcceptedIssuers()) {
                certificates.add(cert);
            }
        }
        return (X509Certificate[]) certificates.toArray();
    }

    public static TrustManager[] getTrustManagers(KeyStore keyStore) {

        return new TrustManager[]{new X509TrustManagerCompat(keyStore)};

    }

    public static X509TrustManager getDefaultTrustManager() {

        return getTrustManager(null);

    }

    public static X509TrustManager getTrustManager(KeyStore keystore) {

        return getTrustManager(TrustManagerFactory.getDefaultAlgorithm(), keystore);

    }

    public static X509TrustManager getTrustManager(String algorithm, KeyStore keystore) {

        TrustManagerFactory factory;

        try {
            factory = TrustManagerFactory.getInstance(algorithm);
            factory.init(keystore);
            TrustManager[] trustManagers = factory.getTrustManagers();
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        return null;

    }

}