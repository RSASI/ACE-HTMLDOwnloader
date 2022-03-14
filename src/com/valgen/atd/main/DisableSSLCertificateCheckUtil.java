package com.valgen.atd.main;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class DisableSSLCertificateCheckUtil {
    public static void disableChecks() throws NoSuchAlgorithmException, KeyManagementException {
        try {
            new URL("https://0.0.0.0/").getContent();
        }
        catch (IOException e) {
            // empty catch block
        }
        SSLContext context = SSLContext.getInstance("SSLv3");
        TrustManager[] trustManagerArray = new TrustManager[]{new NullX509TrustManager()};
        context.init(null, trustManagerArray, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
    }

    private static class NullX509TrustManager
    implements X509TrustManager {
        private NullX509TrustManager() {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class NullHostnameVerifier
    implements HostnameVerifier {
        private NullHostnameVerifier() {
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}

