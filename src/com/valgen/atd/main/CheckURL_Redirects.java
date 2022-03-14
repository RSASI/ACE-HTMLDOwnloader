package com.valgen.atd.main;

//import ArticleDownload_latest.DBConnectivity;
//import ArticleDownload_latest.DisableSSLCertificateCheckUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class CheckURL_Redirects {
    public static String URLRedirects(String link) throws Exception {
        String redirecturl;
        block10 : {
            String baseurl = "";
            int Code = 0;
            redirecturl = "";
            try {
                baseurl = link;
                URL suburl = new URL(link);
                DisableSSLCertificateCheckUtil.disableChecks();
                HttpURLConnection huc = (HttpURLConnection)suburl.openConnection();
                HttpURLConnection.setFollowRedirects(false);
                huc.setInstanceFollowRedirects(false);
                huc.setRequestMethod("GET");
                huc.setConnectTimeout(DBConnectivity.Timeout);
                huc.setReadTimeout(DBConnectivity.Timeout);
                huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
                huc.connect();
                try {
                    String newUrl;
                    Code = huc.getResponseCode();
                    boolean redirect = false;
                    if (Code == 200 && (Code < 300 || Code >= 400)) {
                        return baseurl;
                    }
                    redirect = true;
                    if (!redirect) break block10;
                    redirecturl = newUrl = huc.getHeaderField("Location");
                    if (newUrl == null) {
                        redirecturl = link;
                        break block10;
                    }
                    redirecturl = newUrl;
                    huc = (HttpURLConnection)new URL(newUrl).openConnection();
                    HttpURLConnection.setFollowRedirects(true);
                    huc.setInstanceFollowRedirects(true);
                    huc.setRequestMethod("GET");
                    huc.setConnectTimeout(DBConnectivity.Timeout);
                    huc.setReadTimeout(DBConnectivity.Timeout);
                    huc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
                    huc.connect();
                    Code = huc.getResponseCode();
                    newUrl = huc.getHeaderField("Location");
                    if (newUrl == null || Code == 200 && (Code < 300 || Code >= 400)) {
                        return redirecturl;
                    }
                    redirecturl = newUrl;
                    CheckURL_Redirects.jsoupcheck(redirecturl);
                    System.out.println("Re-directed URL:" + redirecturl);
                }
                catch (IOException e) {
                    String exmsg = e.getMessage();
                    if (exmsg.contains("Server redirected too many")) {
                        if (redirecturl != null) {
                            CheckURL_Redirects.jsoupcheck(redirecturl);
                        }
                        break block10;
                    }
                    System.err.println("ERROR in checking redirected url: " + exmsg);
                }
            }
            catch (Exception e) {
                if (StringUtils.containsIgnoreCase((CharSequence)e.toString(), (CharSequence)"javax.net.ssl.SSLHandshakeException") || StringUtils.containsIgnoreCase((CharSequence)e.toString(), (CharSequence)"ssl")) {
                    redirecturl = link;
                }
                e.printStackTrace();
                redirecturl = link;
                System.err.println("Error occurs while loading url..." + link);
                System.err.println("ERROR : " + e.getLocalizedMessage());
            }
        }
        return redirecturl;
    }

    public static String URLRedirects_HTTPS(String link) throws Exception {
        String redirecturl;
        block10 : {
            String baseurl = "";
            int Code = 0;
            redirecturl = "";
            try {
                baseurl = link;
                URL suburl = new URL(link);
                DisableSSLCertificateCheckUtil.disableChecks();
                SSLContext ssl_ctx = SSLContext.getInstance("TLS");
                TrustManager[] trust_mgr = CheckURL_Redirects.get_trust_mgr();
                ssl_ctx.init(null, trust_mgr, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
                HttpsURLConnection huc = (HttpsURLConnection)suburl.openConnection();
                HttpsURLConnection.setFollowRedirects(false);
                huc.setInstanceFollowRedirects(false);
                huc.setRequestMethod("GET");
                huc.setConnectTimeout(DBConnectivity.Timeout);
                huc.setReadTimeout(DBConnectivity.Timeout);
                huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
                huc.connect();
                try {
                    String newUrl;
                    Code = huc.getResponseCode();
                    boolean redirect = false;
                    if (Code == 200 && (Code < 300 || Code >= 400)) {
                        return baseurl;
                    }
                    redirect = true;
                    if (!redirect) break block10;
                    redirecturl = newUrl = huc.getHeaderField("Location");
                    if (newUrl == null) {
                        redirecturl = link;
                        break block10;
                    }
                    redirecturl = newUrl;
                    huc = (HttpsURLConnection)new URL(newUrl).openConnection();
                    HttpsURLConnection.setFollowRedirects(true);
                    huc.setInstanceFollowRedirects(true);
                    huc.setRequestMethod("GET");
                    huc.setConnectTimeout(DBConnectivity.Timeout);
                    huc.setReadTimeout(DBConnectivity.Timeout);
                    huc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
                    huc.connect();
                    Code = huc.getResponseCode();
                    newUrl = huc.getHeaderField("Location");
                    if (newUrl == null || Code == 200 && (Code < 300 || Code >= 400)) {
                        return redirecturl;
                    }
                    redirecturl = newUrl;
                    CheckURL_Redirects.jsoupcheck(redirecturl);
                    System.out.println("Re-directed URL:" + redirecturl);
                }
                catch (IOException e) {
                    String exmsg = e.getMessage();
                    if (exmsg.contains("Server redirected too many")) {
                        if (redirecturl != null) {
                            CheckURL_Redirects.jsoupcheck(redirecturl);
                        }
                        break block10;
                    }
                    System.err.println("ERROR in checking redirected url: " + exmsg);
                }
            }
            catch (Exception e) {
                if (StringUtils.containsIgnoreCase((CharSequence)e.toString(), (CharSequence)"javax.net.ssl.SSLHandshakeException")) {
                    redirecturl = link;
                }
                e.printStackTrace();
                redirecturl = link;
                System.err.println("Error occurs while loading url..." + link);
                System.err.println("ERROR : " + e.getLocalizedMessage());
            }
        }
        return redirecturl;
    }

    public static void main(String[] args) throws Exception {
        String finalurl = CheckURL_Redirects.URLRedirects("https://media.info/newspapers/news/former-sun-editor-dominic-mohan-appointed-chief-executive-of-pr-firm");
        System.out.println("Re-directed URL:" + finalurl);
    }

    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager(){

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        }};
        return certs;
    }

    public static String jsoupcheck(String link) throws Exception {
        String redirecturl = "";
        int code = 0;
        if (link != null) {
            Connection.Response response = Jsoup.connect((String)link).userAgent("Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.1)").timeout(DBConnectivity.Timeout).followRedirects(true).execute();
            URL url = response.url();
            redirecturl = url.toString();
            code = response.statusCode();
        }
        System.out.println("Original Link:" + link);
        System.out.println("Redirected Link:" + redirecturl);
        return redirecturl;
    }

}

