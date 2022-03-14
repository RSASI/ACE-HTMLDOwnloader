package com.valgen.atd.htmlparser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Developer
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.valgen.atd.main.DBConnectivity.ConnectionTimeOut;
import static com.valgen.atd.main.DBConnectivity.LoopCount;
import static com.valgen.atd.main.DBConnectivity.MetaURLRegex;
import static com.valgen.atd.main.DBConnectivity.UserAgent;
import com.valgen.atd.pojo.HtmlHeaderAttributes;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLException;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Attributes;

/**
 *
 * @author researchers
 */
public class JsoupParser {

//    public static void main(String[] args) {
//        String url = "https://www.mobiusservices.com/";
//        String page = "", res = "";
//
////        page = SourceGetter(url);
//        System.out.println("Page Source : " + page);
//
//    }
    static Logger log = Logger.getLogger(JsoupParser.class);

    public static HtmlHeaderAttributes SourceGetter(String url) throws SSLException {

        HtmlHeaderAttributes htmlHeaderAttributes = new HtmlHeaderAttributes();
        String PageSource = "";
        url = url.trim();
        int tout = ConnectionTimeOut * 1000;
//        int tout = 30000;
        if (url.contains("http")) {

        } else {
            url = "http://" + url;
        }

        org.jsoup.Connection.Response response = null;
        Document doc = null;

        int rescode = 0;
//        response = null;
        try {
            htmlHeaderAttributes.setDomainUrl(url);
//            url = String.format(url, URLEncoder.encode("", "UTF-8"));
            try {
                String tempURL = "";
                boolean flagMeta = false;
                while (true) {
                    response = null;
                    rescode=0;
//                    htmlHeaderAttributes.setModifiedURL(url);
                    response = Jsoup.connect(url.replaceAll(" ", "%20")).userAgent(UserAgent)
                            .followRedirects(true).timeout(tout).execute();
                    rescode = response.statusCode();

                    tempURL = getRedirectURLFromMeta(response.body(), url);
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

                htmlHeaderAttributes.setResponseCode(rescode);
                htmlHeaderAttributes.setResponseMessage(response.statusMessage());
                htmlHeaderAttributes.setContentType(response.contentType());
                htmlHeaderAttributes.setParserType("Jsoup 1");
                htmlHeaderAttributes.setLandingUrl(response.url().toString());
//                htmlHeaderAttributes.setModifiedURL(url);
//                if (flagMeta) {
//                    htmlHeaderAttributes.setRedirectedFrom("MetaTag");
//                } else {
//                    htmlHeaderAttributes.setRedirectedFrom("Default");
//                }
//                if (rescode == 301 || rescode == 302) {
//
//                }
            } catch (UnknownHostException ue) {

                try {
                    if (!url.toLowerCase().contains("www.")) {
                        htmlHeaderAttributes.setDomainUrl(url);
                        String tempURL = "";
                        boolean flagMeta = false;
                        while (true) {

                            if (!url.toLowerCase().contains("www.")) {
                                url = url.replaceFirst("(http.*?://)", "$1www.");
                            }
                            response = null;
                            rescode=0;
//                            htmlHeaderAttributes.setModifiedURL(url);
                            response = Jsoup.connect(url.replaceAll(" ", "%20")).userAgent(UserAgent)
                                    .followRedirects(false).timeout(tout).execute();
                            rescode = response.statusCode();

                            tempURL = getRedirectURLFromMeta(response.body(), url);
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

                        htmlHeaderAttributes.setResponseCode(rescode);

                        htmlHeaderAttributes.setResponseMessage(response.statusMessage());
                        htmlHeaderAttributes.setContentType(response.contentType());

                        htmlHeaderAttributes.setLandingUrl(response.url().toString());
                        htmlHeaderAttributes.setParserType("Jsoup 2");
//                        htmlHeaderAttributes.setModifiedURL(url.replaceFirst("(http.*?://)", "$1www."));
//                        htmlHeaderAttributes.setRedirectedFrom("Default");
                    } else {

//                        htmlHeaderAttributes.setResponseCode(0);
                        htmlHeaderAttributes.setResponseCode(rescode);
                        htmlHeaderAttributes.setPageSoure("");
                        htmlHeaderAttributes.setErrorCode(31);
                        htmlHeaderAttributes.setParserType("Jsoup 2");
                        htmlHeaderAttributes.setErrorDescription(ue.getMessage());
                        htmlHeaderAttributes.setLandingUrl(url);

                    }

                } catch (Exception ex) {
//                    htmlHeaderAttributes.setResponseCode(0);
                    htmlHeaderAttributes.setResponseCode(rescode);
                    htmlHeaderAttributes.setPageSoure("");
                    htmlHeaderAttributes.setErrorCode(31);
                    htmlHeaderAttributes.setParserType("Jsoup 2");
                    htmlHeaderAttributes.setErrorDescription(ex.getMessage());
//                    htmlHeaderAttributes.setLandingUrl(htmlHeaderAttributes.getModifiedURL());
//                    System.out.println("" + ex.getMessage());
                }
            } catch (SSLException ue) {
//                ue.printStackTrace();
//                throw ue;

                htmlHeaderAttributes.setErrorDescription("SSl Exception from JSOUP");

                if (!url.toLowerCase().startsWith("https") && !url.toLowerCase().startsWith("http")) {

                    if (url.toLowerCase().startsWith("www")) {
                        url = "https://" + url;
                    } else {
                        url = "https://www." + url;
                    }
                }
                htmlHeaderAttributes = HttpURLParser.SSl_fun_url(url);

//                try {
//
//                    if (!url.toLowerCase().contains("https")) {
//                        url = url.replaceFirst("http", "https").replaceAll(" ", "%20");
//                    }
//
//                    doc = Jsoup.parse(sslcertification.LoadURL(url));
//                } catch (Exception ex) {
//                    System.out.println("" + ex.getMessage());
//                }
            } catch (IOException ie) {
                try {
                    if (!url.toLowerCase().contains("www.")) {
                        htmlHeaderAttributes.setDomainUrl(url);
                        String tempURL = "";
                        boolean flagMeta = false;
                        while (true) {
                            if (!url.toLowerCase().contains("www.")) {
                                url = url.replaceFirst("(http.*?://)", "$1www.");
                            }
                            response = null;
                            rescode=0;
//                            htmlHeaderAttributes.setModifiedURL(url);
                            response = Jsoup.connect(url.replaceAll(" ", "%20")).userAgent(UserAgent).followRedirects(false).timeout(tout).execute();
                            rescode = response.statusCode();

                            tempURL = getRedirectURLFromMeta(response.body(), url);
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

                        htmlHeaderAttributes.setResponseCode(rescode);
                        htmlHeaderAttributes.setResponseMessage(response.statusMessage());
                        htmlHeaderAttributes.setLandingUrl(response.url().toString());
                        htmlHeaderAttributes.setContentType(response.contentType());
                        htmlHeaderAttributes.setParserType("Jsoup 3");
//                        htmlHeaderAttributes.setModifiedURL(url.replaceFirst("(http.*?://)", "$1www."));
//                        htmlHeaderAttributes.setRedirectedFrom("Default");
                    } else {

                        htmlHeaderAttributes.setResponseCode(rescode);
                        htmlHeaderAttributes.setPageSoure("");
                        htmlHeaderAttributes.setErrorCode(31);
                        htmlHeaderAttributes.setParserType("Jsoup 3");
                        htmlHeaderAttributes.setErrorDescription(ie.getMessage());
                        htmlHeaderAttributes.setLandingUrl(url);

                    }
                } catch (Exception e) {
//                    HTMLDOWNLOAD_DS.l.error(HTMLDOWNLOAD_DS.appendLog(""), e);
                    log.error("", e);
                    // DepthManager.crawlresponse = 4;
//                    System.out.println("" + e.getMessage());
                    htmlHeaderAttributes.setResponseCode(rescode);
                    htmlHeaderAttributes.setPageSoure("");
                    htmlHeaderAttributes.setErrorCode(32);
                    htmlHeaderAttributes.setParserType("Jsoup 3");
                    htmlHeaderAttributes.setErrorDescription(e.getMessage());
//                    htmlHeaderAttributes.setLandingUrl(htmlHeaderAttributes.getModifiedURL());
//                    System.out.println("Exception Name:" + e);
                    //ActionsDecider.exmsg = e.getMessage();
                }
            } catch (Exception e) {
                //   DepthManager.crawlresponse = 4;
//                System.out.println("" + e.getMessage());
                log.error("", e);
                htmlHeaderAttributes.setErrorCode(33);
                htmlHeaderAttributes.setParserType("Jsoup 1");
                htmlHeaderAttributes.setErrorDescription(e.getMessage());
//                htmlHeaderAttributes.setLandingUrl(htmlHeaderAttributes.getModifiedURL());
                //ActionsDecider.exmsg = e.getMessage();
            }
            String redirect_url = "";
            try {
                if (rescode == HttpURLConnection.HTTP_MOVED_TEMP || rescode == HttpURLConnection.HTTP_MOVED_PERM
                        || rescode == HttpURLConnection.HTTP_SEE_OTHER) {
                    //int count = 0, co = 0;
                    int redirect_attemptcount = 0;
                    do {
                        redirect_url = "";
                        if (response.header("location") != null) {
                            redirect_url = response.header("location");
                        }

                        URL baseUrl = new URL(url);
                        URL absurl = new URL(baseUrl, redirect_url);
                        redirect_url = absurl.toString();

                        String tempURL = "";
                        boolean flagMeta = false;
                        while (true) {

                            response = null;
                            rescode=0;
//                            htmlHeaderAttributes.setModifiedURL(redirect_url);
                            response = Jsoup.connect(redirect_url).userAgent(UserAgent).timeout(tout).followRedirects(false).execute();
                            rescode = response.statusCode();

                            tempURL = getRedirectURLFromMeta(response.body(), url);
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

                        htmlHeaderAttributes.setResponseCode(rescode);
                        htmlHeaderAttributes.setResponseMessage(response.statusMessage());
                        htmlHeaderAttributes.setLandingUrl(response.url().toString());
                        htmlHeaderAttributes.setContentType(response.contentType());
                        htmlHeaderAttributes.setParserType("Jsoup 4");
//                        htmlHeaderAttributes.setModifiedURL(redirect_url);
//                        htmlHeaderAttributes.setRedirectedFrom("location");
                        if (redirect_attemptcount >= LoopCount) {
                            break;
                        }
                        redirect_attemptcount++;
                    } while (rescode == HttpURLConnection.HTTP_MOVED_TEMP || rescode == HttpURLConnection.HTTP_MOVED_PERM || rescode == HttpURLConnection.HTTP_SEE_OTHER);
                }
            } catch (Exception e) {
                log.error("e:", e);
//                htmlHeaderAttributes.setResponseCode(0);
                htmlHeaderAttributes.setResponseCode(rescode);
                htmlHeaderAttributes.setPageSoure("");
                htmlHeaderAttributes.setErrorCode(34);
                htmlHeaderAttributes.setParserType("Jsoup 4");
                htmlHeaderAttributes.setErrorDescription(e.getMessage());
//                htmlHeaderAttributes.setModifiedURL(redirect_url);
//                htmlHeaderAttributes.setLandingUrl(htmlHeaderAttributes.getModifiedURL());
            }
            if (PageSource.equals("")) {
                if (response != null) {
                    PageSource = response.body();
                    htmlHeaderAttributes.setPageSoure(PageSource);
//                    htmlHeaderAttributes.setContentLength(PageSource.getBytes().length + "");
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("e:", e);
//            htmlHeaderAttributes.setResponseCode(0);
            htmlHeaderAttributes.setResponseCode(rescode);
            htmlHeaderAttributes.setPageSoure("");
            htmlHeaderAttributes.setErrorCode(34);
            htmlHeaderAttributes.setParserType("Jsoup");
            htmlHeaderAttributes.setErrorDescription(e.getMessage());
//            htmlHeaderAttributes.setLandingUrl(htmlHeaderAttributes.getModifiedURL());

            //    DepthManager.crawlresponse = 4;
            //    ActionsDecider.exmsg = e.getMessage();
        }
//            ue.printStackTrace();
        return htmlHeaderAttributes;
    }

    public static String getRedirectURLFromMeta(String page, String url) {
        String redirectedURL = "";
        page = page.replaceAll("(?sim)<noscript>(.*?)</noscript>", "");
        Document doc = Jsoup.parse(page);
        String text = doc.body().text();
        text = text.replaceAll("(?sim)<title>(.*?)</title>", "");
//        if ("".equals(text)) {
//            Elements metaText = doc.getElementsByTag("meta");
//            for (Element e : metaText) {
        Elements temp = doc.getElementsByAttributeValue("http-equiv", "Refresh");
        String line = "";
        if (temp.size() > 0) {
            line = temp.get(0).toString();

            if (line.toLowerCase().contains("refresh")) {
                redirectedURL = getTextByRegex(line, MetaURLRegex, 1);
            }
            if (!"".equals(redirectedURL)) {
                try {
                    URL baseUrl = new URL(url);
                    URL absurl = new URL(baseUrl, redirectedURL);
                    redirectedURL = absurl.toString();

                } catch (Exception ex) {
                    log.error("", ex);
                }

//                    break;
            }

        }
//                redirectedURL = e.attr("http-equiv", "Refresh").val("URL").text();
//                Attributes nf = e.attr("http-equiv", "Refresh").attributes();
//                int sizeofattr=nf.size();
//                if ("".equals(redirectedURL)) {
//                    redirectedURL = e.attr("http-equiv", "refresh").attr("URL");
//                }

//            }
//        }
        return redirectedURL;
    }

    public static String getTextByRegex(String text, String reg, int group) {
        try {
            Pattern regex = Pattern.compile(reg,
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(text);
            if (regexMatcher.find()) {
                return regexMatcher.group(group);
                // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
        return "";
    }

    public static String getElement(Document doc) {
        String result = "", lin = "";
        Set<String> temp = new HashSet<String>();
        Elements links = doc.getElementsByTag("a");

        for (Element link : links) {
            lin = link.absUrl("href");
            if (!temp.contains(lin)) {

                temp.add(lin);

//                result += ","+lin;
                System.out.println(lin);
            }

        }

        return result;
    }

    public static void main(String[] s) {
        try {
            URL absurl = new URL(new URL("http://www.apple.com"), "https://www.apple.com/testing");
            System.out.println("absurl:" + absurl);
        } catch (Exception e) {
        }
    }
}
