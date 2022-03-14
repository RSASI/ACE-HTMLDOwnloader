package com.valgen.atd.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageContent {
    private static int retry = 0;
    public static ArrayList<String> proxiesList = new ArrayList();
    public static ArrayList<String> latenciesList = new ArrayList();
    public static String[] userAgents = null;

    public static ArrayList<String> getPageContent(String url, boolean loadProxy) {
        String pageSource = "";
        String responseDescription = "";
        String proxyIp = "";
        String proxyPort = "";
        int responseCode = 0;
        if (!(url = url.trim()).startsWith("http")) {
            url = "http://" + url;
        }
        ArrayList<String> arrayPageContent = new ArrayList<String>();
        try {
            URL website = new URL(url);
            HttpURLConnection httpcon = (HttpURLConnection)website.openConnection();
            httpcon = (HttpURLConnection)website.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            httpcon.setRequestMethod("GET");
            httpcon.setConnectTimeout(50000);
            httpcon.setReadTimeout(50000);
            httpcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.1)");
            httpcon.connect();
            try {
                responseCode = httpcon.getResponseCode();
                responseDescription = httpcon.getResponseMessage();
            }
            catch (Exception e) {
                responseCode = 0;
                responseDescription = "";
            }
            try {
                StringBuffer strbuffer = new StringBuffer();
                String line = "";
                try {
                    strbuffer.setLength(0);
                }
                catch (Exception ee) {
                    // empty catch block
                }
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                while ((line = bufferreader.readLine()) != null) {
                    strbuffer.append(line);
                }
                pageSource = strbuffer.toString();
                pageSource = pageSource.replaceAll("(?sim)[^\\x00-\\x80]", " ");
            }
            catch (UnknownHostException e) {
                arrayPageContent.add("0");
                arrayPageContent.add("Unknown Host");
                arrayPageContent.add("");
                return arrayPageContent;
            }
            catch (Exception e) {
                if (responseCode == 0) {
                    responseDescription = e.getMessage();
                }
                pageSource = "";
            }
            arrayPageContent.add("" + responseCode);
            arrayPageContent.add(responseDescription);
            arrayPageContent.add(pageSource);
            return arrayPageContent;
        }
        catch (UnknownHostException e) {
            arrayPageContent.add("0");
            arrayPageContent.add("Unknown Host");
            arrayPageContent.add("");
            return arrayPageContent;
        }
        catch (Exception e) {
            if (responseCode == 0) {
                responseDescription = e.getMessage();
            }
            pageSource = "";
            arrayPageContent.add("0");
            arrayPageContent.add(responseDescription);
            arrayPageContent.add(pageSource);
            retry = 0;
            return arrayPageContent;
        }
    }

    public static Document ContentNormalization(Document document) {
        try {
            String[] tags = new String[]{"script", "style", "img", "noscript", "iframe", "link"};
            for (int i = 0; i < tags.length; ++i) {
                String tag = tags[i];
                Elements scripts = document.getElementsByTag(tag);
                for (int t = scripts.size() - 1; t >= 0; --t) {
                    Element element = scripts.get(t);
                    element.remove();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error occurs while removing tags from document \n" + e.getMessage());
        }
        return document;
    }

    public static String GetResponseMessage(int ResponseCode) {
        String ResponseDescription = "";
        try {
            if (ResponseCode == 100) {
                ResponseDescription = "Continue";
            } else if (ResponseCode == 101) {
                ResponseDescription = "Switching Protocols";
            } else if (ResponseCode == 200) {
                ResponseDescription = "OK";
            } else if (ResponseCode == 201) {
                ResponseDescription = "Created";
            } else if (ResponseCode == 202) {
                ResponseDescription = "Accepted";
            } else if (ResponseCode == 203) {
                ResponseDescription = "Non-Authoritative Information";
            } else if (ResponseCode == 204) {
                ResponseDescription = "No Content";
            } else if (ResponseCode == 205) {
                ResponseDescription = "Reset Content";
            } else if (ResponseCode == 206) {
                ResponseDescription = "Partial Content";
            } else if (ResponseCode == 300) {
                ResponseDescription = "Multiple Choices";
            } else if (ResponseCode == 301) {
                ResponseDescription = "Moved Permanently";
            } else if (ResponseCode == 302) {
                ResponseDescription = "Moved Temporarily";
            } else if (ResponseCode == 303) {
                ResponseDescription = "See Other";
            } else if (ResponseCode == 304) {
                ResponseDescription = "Not Modified";
            } else if (ResponseCode == 305) {
                ResponseDescription = "Use Proxy";
            } else if (ResponseCode == 306) {
                ResponseDescription = "(Unused)";
            } else if (ResponseCode == 307) {
                ResponseDescription = "Temporary Redirect";
            } else if (ResponseCode == 400) {
                ResponseDescription = "Bad Request";
            } else if (ResponseCode == 401) {
                ResponseDescription = "Unauthorized";
            } else if (ResponseCode == 402) {
                ResponseDescription = "Payment Required";
            } else if (ResponseCode == 403) {
                ResponseDescription = "Forbidden";
            } else if (ResponseCode == 404) {
                ResponseDescription = "Not Found";
            } else if (ResponseCode == 405) {
                ResponseDescription = "Method Not Allowed";
            } else if (ResponseCode == 406) {
                ResponseDescription = "Not Acceptable";
            } else if (ResponseCode == 407) {
                ResponseDescription = "Proxy Authentication Required";
            } else if (ResponseCode == 408) {
                ResponseDescription = "Request Timeout";
            } else if (ResponseCode == 409) {
                ResponseDescription = "Conflict";
            } else if (ResponseCode == 410) {
                ResponseDescription = "Gone";
            } else if (ResponseCode == 411) {
                ResponseDescription = "Length Required";
            } else if (ResponseCode == 412) {
                ResponseDescription = "Precondition Failed";
            } else if (ResponseCode == 413) {
                ResponseDescription = "Request Entity Too Large";
            } else if (ResponseCode == 414) {
                ResponseDescription = "Request-URI Too Long";
            } else if (ResponseCode == 415) {
                ResponseDescription = "Unsupported Media Type";
            } else if (ResponseCode == 416) {
                ResponseDescription = "Requested Range Not Satisfiable";
            } else if (ResponseCode == 417) {
                ResponseDescription = "Expectation Failed";
            } else if (ResponseCode == 500) {
                ResponseDescription = "Internal Server Error";
            } else if (ResponseCode == 501) {
                ResponseDescription = "Not Implemented";
            } else if (ResponseCode == 502) {
                ResponseDescription = "Bad Gateway";
            } else if (ResponseCode == 503) {
                ResponseDescription = "Service Unavailable";
            } else if (ResponseCode == 504) {
                ResponseDescription = "Gateway Timeout";
            } else if (ResponseCode == 505) {
                ResponseDescription = "HTTP Version Not Supported";
            }
        }
        catch (Exception e) {
            System.out.println("Error occurs while getting response description... \n" + e.getMessage());
        }
        return ResponseDescription;
    }

    public static void main(String[] args) {
        String Page_Content = "";
        ArrayList<String> a = PageContent.getPageContent("http://www.topix.com/city/erie-pa/2015/08/japanese-announce-unofficial-world-war-2-surrender-70-years-ago?fromrss=1", false);
        Page_Content = a.get(2);
        System.out.println(Page_Content);
    }
}

