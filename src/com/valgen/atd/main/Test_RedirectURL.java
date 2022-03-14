package com.valgen.atd.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test_RedirectURL
        extends Article_Download_Main {

    public static Document document = null;

    public static void main(String[] args) throws Exception {
        ArrayList<Object> a = Test_RedirectURL.urlClassLoader("http://www.bridgestone.com.au/media/articles/p1600.aspx");
    }

    public static ArrayList<Object> urlClassLoader(String link) {
        String baseurl = "";
        String docStr = "";
        boolean Status = false;
        String Content = "";
        String Desc = "";
        int Code = 0;
        String redirecturl = "";
        try {
            baseurl = link;
            URL suburl = new URL(link);
            DisableSSLCertificateCheckUtil.disableChecks();
            HttpURLConnection huc = (HttpURLConnection) suburl.openConnection();
            HttpURLConnection.setFollowRedirects(false);
            huc.setInstanceFollowRedirects(false);
            huc.setRequestMethod("GET");
            huc.setConnectTimeout(50000);
            huc.setReadTimeout(50000);
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
            huc.connect();
            try {
                Code = huc.getResponseCode();
                Desc = huc.getResponseMessage();
                boolean redirect = false;
                if (Code != 200) {
                    redirect = true;
                }
                if (redirect) {
                    String newUrl;
                    baseurl = newUrl = huc.getHeaderField("Location");
                    redirecturl = newUrl;
                    System.out.println("Re-directed URL:" + redirecturl);
                    huc = (HttpURLConnection) new URL(newUrl).openConnection();
                    HttpURLConnection.setFollowRedirects(true);
                    huc.setRequestMethod("GET");
                    huc.setConnectTimeout(50000);
                    huc.setReadTimeout(50000);
                    huc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520");
                    huc.connect();
                }
            } catch (Exception e) {
                exmsg = e.getMessage();
            }
            try {
                docStr = Test_RedirectURL.ParseLinkToURLClass(huc, link);
                try {
                    document = Jsoup.parse((String) docStr, (String) baseurl);
                } catch (Exception e) {
                }
            } catch (Exception e) {
                // empty catch block
            }
            if (Content == null || Content == "") {
                exmsg = "No PageSource";
            }
            huc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurs while loading url1 using URL Class...");
            System.out.println("ERROR : " + e.getLocalizedMessage());
        }
        ArrayList<Object> URLOutput = new ArrayList<Object>();
        URLOutput.add(docStr);
        URLOutput.add(Code);
        URLOutput.add((Object) document);
        URLOutput.add(redirecturl);
        return URLOutput;
    }

    public static String ParseLinkToURLClass(HttpURLConnection huc, String link) {
        StringBuilder strbuffer = new StringBuilder();
        try {
            String line = "";
            strbuffer.setLength(0);
            BufferedReader bufferreader = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            while ((line = bufferreader.readLine()) != null) {
                strbuffer.append(line);
            }
            bufferreader.close();
        } catch (Exception e) {
            // empty catch block
        }
        return strbuffer.toString();
    }
}
