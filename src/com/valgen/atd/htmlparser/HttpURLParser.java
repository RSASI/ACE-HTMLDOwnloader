/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valgen.atd.htmlparser;

import static com.valgen.atd.main.DBConnectivity.ConnectionTimeOut;
import static com.valgen.atd.main.DBConnectivity.LoopCount;
import static com.valgen.atd.main.DBConnectivity.UserAgent;
import com.valgen.atd.pojo.HtmlHeaderAttributes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;
import sun.nio.cs.StandardCharsets;

/**
 *
 * @author Developer
 */
public class HttpURLParser {

    static Logger log = Logger.getLogger(HttpURLParser.class);

    public static HtmlHeaderAttributes getHttpURLContent(String url) {
        HtmlHeaderAttributes htmlHeaderAttributes = new HtmlHeaderAttributes();
        int timeout = ConnectionTimeOut * 1000;
        int responseCode = 0;
        try {
//            if (!url.toLowerCase().startsWith("https") && !url.toLowerCase().startsWith("http")) {
//
//                if (url.toLowerCase().startsWith("www")) {
//                    url = "http://" + url;
//                } else {
//                    url = "http://www." + url;
//                }
//
//            }

            int rediret_attemp = 0, res = 0;
            boolean is_reattempt = false;
            String error_desc = "";
            HttpURLConnection con = null;
            URL u = null;
//            url = String.format(url, new Object[]{URLEncoder.encode("", "UTF-8")});
            StringBuffer sb = null;
            String tempURL = "";
            boolean flagMeta = false;
            while (true) {
                sb = new StringBuffer();
                do {
                    con = null;
                      is_reattempt = false;
                    u = new URL(url.replaceAll(" ", "%20"));
//                    htmlHeaderAttributes.setModifiedURL(url);
                    con = (HttpURLConnection) u.openConnection();
                    con.setFollowRedirects(true);
                    con.setConnectTimeout(timeout);
                    con.setReadTimeout(timeout);

//                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                    con.setRequestProperty("User-Agent", UserAgent);
                    con.connect();

                    responseCode = con.getResponseCode();
                    if (responseCode > 300 && responseCode < 400) {
                        if (con.getHeaderField("Location") != null) {
                            String redirecturl = con.getHeaderField("Location");
                            if (!url.equalsIgnoreCase(redirecturl)) {
                                url = redirecturl;

                                is_reattempt = true;
                            }
                        }
                    }
                    rediret_attemp++;
                } while (rediret_attemp < 3 && is_reattempt);
                //  InputStreamReader in = new InputStreamReader((InputStream) con.getContent(), StandardCharsets.UTF_8);
                InputStreamReader in = new InputStreamReader((InputStream) con.getInputStream(), "UTF-8");
                String line = "";

                BufferedReader buff = new BufferedReader(in);
                while ((line = buff.readLine()) != null) {
//                line = ;
                    sb.append(line);
                }

                tempURL = JsoupParser.getRedirectURLFromMeta(sb.toString(), url);
                if ("".equals(tempURL)) {
                    break;
                } else {
                    url = tempURL;
                    if (flagMeta) {
                        break;
                    }
                    flagMeta = true;
                    continue;
                }

            }

            res = con.getResponseCode();
            String page = sb.toString();
            String temp_page = contentNormalization(page);
            if (temp_page.trim().isEmpty() || temp_page.trim().length() <= 10) {
                page = "";
                res = 0;
                error_desc = "Unable to load domain using HTTP";
            }

            htmlHeaderAttributes.setResponseCode(res);
            htmlHeaderAttributes.setResponseMessage(con.getResponseMessage());
            htmlHeaderAttributes.setPageSoure(sb.toString());
//            htmlHeaderAttributes.setContentLength(sb.toString().length() + "");
            String content_type = con.getContentType();
            if (content_type == null || content_type.isEmpty()) {
                content_type = con.getHeaderField("Content-Type");
            }
            htmlHeaderAttributes.setContentType(content_type);
            htmlHeaderAttributes.setLandingUrl(con.getURL().toString());
            htmlHeaderAttributes.setParserType("HTTP");
            htmlHeaderAttributes.setErrorDescription(error_desc);
//            if (is_reattempt) {
//                htmlHeaderAttributes.setRedirectedFrom("location");
//            } else {
//                htmlHeaderAttributes.setRedirectedFrom("Default");
//            }
//            if (sb.length() != 0) {
////                System.out.println(sb.toString());
//               // return sb.toString();
//            }
        } catch (SSLException sse) {
//            htmlHeaderAttributes.setResponseCode(0);
            htmlHeaderAttributes.setResponseCode(responseCode);
            htmlHeaderAttributes.setPageSoure("");
            htmlHeaderAttributes.setErrorCode(21);
            htmlHeaderAttributes.setErrorDescription(sse.getMessage());
            htmlHeaderAttributes.setParserType("HTTP");
            htmlHeaderAttributes.setLandingUrl(url);
            htmlHeaderAttributes = SSl_fun_url(url);

        } catch (Exception e) {
            log.error("getHttpURLContent()", e);
//            htmlHeaderAttributes.setResponseCode(0);
            System.out.println("Responsecode : " + responseCode + "url : " + url);
            htmlHeaderAttributes.setResponseCode(responseCode);
            htmlHeaderAttributes.setPageSoure("");
            htmlHeaderAttributes.setErrorCode(22);
            htmlHeaderAttributes.setParserType("HTTP");
            htmlHeaderAttributes.setLandingUrl(url);
            htmlHeaderAttributes.setErrorDescription(e.getMessage());
        }
        return htmlHeaderAttributes;
    }

    public static HtmlHeaderAttributes SSl_fun_url(String pdfurl) {
        //StringBuffer sb = new StringBuffer();
        HtmlHeaderAttributes htmlHeaderAttributes = new HtmlHeaderAttributes();
        int res = 0;
        String error_desc = "";
        StringBuffer sb = null;
        try {
            doTrustToCertificates();
            int rediret_attemp = 0;
            boolean is_reattempt = false;
            HttpsURLConnection con = null;
            String tempURL = "";
            boolean flagMeta = false;
            while (true) {
                sb = new StringBuffer();
                do {
                    con = null;
                    URL u = new URL(pdfurl.replaceAll(" ", "%20"));
                    con = (HttpsURLConnection) u.openConnection();
                    con.setFollowRedirects(true);
//                    htmlHeaderAttributes.setModifiedURL(pdfurl);
                    int responseCode = con.getResponseCode();
                    if (responseCode > 300 && responseCode < 400) {
                        if (con.getHeaderField("Location") != null) {
                            String redirecturl = con.getHeaderField("Location");
                            if (!pdfurl.equalsIgnoreCase(redirecturl)) {
                                pdfurl = redirecturl;
                                is_reattempt = true;
                            }
                        }
                    }
                    rediret_attemp++;
                } while (rediret_attemp < LoopCount && is_reattempt);
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                tempURL = JsoupParser.getRedirectURLFromMeta(sb.toString(), pdfurl);
                if ("".equals(tempURL)) {
                    break;
                } else {
                    pdfurl = tempURL;
                    if (flagMeta) {
                        break;
                    }
                    flagMeta = true;
                    continue;
                }

            }

            res = con.getResponseCode();
            String page = sb.toString();
            String temp_page = contentNormalization(page);
            if (temp_page.trim().isEmpty() || temp_page.trim().length() <= 10) {
                page = "";
                res = 0;
                error_desc = "Unable to load domain using HTTP TLS";
            }

            htmlHeaderAttributes.setResponseCode(res);
            htmlHeaderAttributes.setResponseMessage(con.getResponseMessage());
            htmlHeaderAttributes.setPageSoure(page);
//            htmlHeaderAttributes.setContentLength(page.toString().getBytes().length + "");
            String content_type = con.getContentType();
            if (content_type == null || content_type.isEmpty()) {
                content_type = con.getHeaderField("Content-Type");
            }
            htmlHeaderAttributes.setContentType(content_type);
            htmlHeaderAttributes.setLandingUrl(con.getURL().toString());
            htmlHeaderAttributes.setParserType("HTTP TLS");
            htmlHeaderAttributes.setErrorDescription(error_desc);
//            if (flagMeta) {
//                htmlHeaderAttributes.setRedirectedFrom("MetaTag");
//            } else {
//                if (is_reattempt) {
//                    htmlHeaderAttributes.setRedirectedFrom("location");
//                } else {
//                    htmlHeaderAttributes.setRedirectedFrom("Default");
//                }
//            }

//            if (sb.length() != 0) {
////                System.out.println(sb.toString());
//                return sb.toString();
//            }
        } catch (Exception e) {

//            htmlHeaderAttributes.setResponseCode(0);
            htmlHeaderAttributes.setResponseCode(res);
            htmlHeaderAttributes.setPageSoure("");
            htmlHeaderAttributes.setErrorCode(23);
            htmlHeaderAttributes.setParserType("HTTP TLS");
            htmlHeaderAttributes.setErrorDescription(e.getMessage());
            htmlHeaderAttributes.setLandingUrl(pdfurl);
            //htmlHeaderAttributes.setResponseCode(0);
            //htmlHeaderAttributes.setPageSoure("");
            log.error("SSl_fun_url()", e);
        }
        return htmlHeaderAttributes;
    }

//    public static HtmlHeaderAttributes LoadURL(String url) {
//        HtmlHeaderAttributes htmlHeaderAttributes = new HtmlHeaderAttributes();
//        try {
//
//            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//            TrustManager[] trust_mgr = get_trust_mgr();
//            ssl_ctx.init(null, trust_mgr, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//            URL u = new URL(url);
//            HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
//            con.setConnectTimeout(60000);
//            con.setReadTimeout(60000);
//            con.setHostnameVerifier(new HostnameVerifier() {
//                public boolean verify(String host, SSLSession sess) {
//                    if (host.equals("localhost")) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            });
//            int res = con.getResponseCode();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            StringBuffer sb = new StringBuffer();
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            htmlHeaderAttributes.setResponseCode(res);
//            htmlHeaderAttributes.setResponseMessage(con.getResponseMessage());
//            htmlHeaderAttributes.setPageSoure(sb.toString());
//            htmlHeaderAttributes.setContentType(con.getContentType());
//            htmlHeaderAttributes.setLandingUrl(con.getURL().toString());
//            htmlHeaderAttributes.setParserType("HTTP TLS");
//
//        } catch (Exception e) {
//            htmlHeaderAttributes.setResponseCode(0);
//            htmlHeaderAttributes.setPageSoure("");
//            htmlHeaderAttributes.setErrorCode(23);
//            htmlHeaderAttributes.setErrorDescription(e.getMessage());
//            //htmlHeaderAttributes.setResponseCode(0);
//            //htmlHeaderAttributes.setPageSoure("");
//            System.out.println("Exception : " + e.getMessage());
//        }
//        return htmlHeaderAttributes;
//    }
    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String t) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String t) {
                }
            }
        };
        return certs;
    }

    public static String contentNormalization(String content) {
        String normalizedContent = content;
        try {
            normalizedContent = normalizedContent.replaceAll("(?si)<script[^<>]*?>.*?</script>", " ");
            normalizedContent = normalizedContent.replaceAll("(?si)<iframe[^<>]*?>.*?</iframe>", " ");
            normalizedContent = normalizedContent.replaceAll("(?si)<iframe[^<>]*?>", "");
            normalizedContent = normalizedContent.replaceAll("&#65533;", "'");
            normalizedContent = normalizedContent.replaceAll("(?si)<img[^<>]*?>|</img>", " ");
            normalizedContent = normalizedContent.replaceAll("(?si)<style[^<>]*?>.*?</style>", " ");
            normalizedContent = normalizedContent.replaceAll("(?si)<noscript[^<>]*?>.*?</noscript>", " ");
            normalizedContent = normalizedContent.replaceAll("<noscript/>", "");
            normalizedContent = normalizedContent.replaceAll("(?sim)<head>.*?</head>", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)<title\\s*[^<>]*>[^<>]*</title>", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)<!--[^<>]*>", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)<!--.*?-->", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)&nbsp;", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)<.*?>", " ");
            normalizedContent = normalizedContent.replaceAll("(?sim)\\s\\s+", " ");
            normalizedContent = normalizedContent.trim();
        } catch (Exception e) {
            log.error("Error contentNormalization():", e);
        }
        return normalizedContent;
    }

    public static void doTrustToCertificates() throws Exception {
        // Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        TrustManager[] trustAllCerts = {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }
        }};
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
//                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
//                            + session.getPeerHost() + "'.");
                    log.info("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
                            + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
    public static void main(String[] args) {
        getHttpURLContent("http://economictimes.indiatimes.com/news/india-unlimited/rssfeeds/45228216.cms");
    }
}
