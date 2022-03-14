package com.valgen.atd.main;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.gravity.goose.Article;
import com.gravity.goose.Configuration;
import com.gravity.goose.CrawlCandidate;
import com.gravity.goose.Goose;
import static com.valgen.atd.htmlparser.HttpURLParser.getHttpURLContent;
import static com.valgen.atd.htmlparser.JsoupParser.SourceGetter;
import static com.valgen.atd.main.DBConnectivity.AdditionalQuery;
import static com.valgen.atd.main.DBConnectivity.CoreContentExtraction;
import static com.valgen.atd.main.DBConnectivity.recordinputstatus;
import com.valgen.atd.pojo.HtmlHeaderAttributes;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import sun.net.www.protocol.https.Handler;

public class Article_Download_Main {

    public static String Page_source = "";
    public static int ID;
    public static String link;
    public static String exmsg;
    public static long starttime;
    public static String urlid;
    public static String Aboutus_Matched;
    public static String LinkLookup_matched;
    public static String DomainURL;
    public static String comment;
    public static String US_CityState;
    public static String down_on;
    public static Detector detector;
    public static String US_Zipcode;
    public static String tagg_sub;
    public static String content;
    public static String language;
    public static int status;
    public static int oid;
    public static int is_map;
    public static int is_searched;
    public static int count_check;
    public static int output_totalcount;
    public static boolean OutputTableCreation;
    public static List<String> Exception_List;
    public static String toolstart;
    public static String toolend;
    static Calendar Gcalendar;
    static DateFormat dateformate;
    static String currdate;
    static Date date;
    static DateFormat dateFormat;
    static Date utilDate;
    static String machineIP;
    static String Executionstatus;
    static String ErrorDescription;
//    public static HtmlHeaderAttributes html_Attrib = null;

    public static void main(String[] args) throws Exception {
        try {
            PropertyConfigurator.configure("log4j.properties");
            starttime = System.currentTimeMillis();
            toolstart = dateformate.format(date);
            System.out.println(currdate);
//            machineIP = Article_Download_Main.MachineIP();
            DBConnectivity.LoadProperties();
//            Article_Download_Main.processtracker(DBConnectivity.processname, machineIP, currdate, toolstart, toolend, Executionstatus, DBConnectivity.InstanceNO, DBConnectivity.TrackerTable, DBConnectivity.Stage);
            File designations = null;
            Scanner scanner = null;
            designations = new File("ExceptionsList.txt");
            String line = "";
            scanner = new Scanner(designations);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                Exception_List.add(line);
            }
            scanner.close();
            try {
                DetectorFactory.loadProfile("profiles");
                detector = DetectorFactory.create();
            } catch (Exception var16) {
            }
//            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//            System.setProperty("https.protocols", "TLSv1.2");
            Article_Download_Main.Core_Content_Extraction();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void Core_Content_Extraction() throws SQLException {
        try {
            DBConnectivity.LoadProperties();
            DBConnectivity.DBConnection();
            Article_Download_Main.CreateOutputtable();
            ExecutorService executor = Executors.newFixedThreadPool(DBConnectivity.Thread_Pool);
            System.out.println(">>>>>>>>>> DB Connected <<<<<<<<<<");
            try {
                PreparedStatement st1 = DBConnectivity.con.prepareStatement("BEGIN TRY BEGIN TRANSACTION SET DEADLOCK_PRIORITY ? COMMIT TRANSACTION END TRY BEGIN CATCH IF XACT_STATE() <> 0 ROLLBACK TRANSACTION END CATCH");
                st1.setInt(1, DBConnectivity.deadlockpriority);
                st1.executeUpdate();
                st1.clearBatch();
                st1.close();
            } catch (SQLException s) {
                s.printStackTrace();
                System.err.println(s.getMessage());
            }
            Statement st = DBConnectivity.con.createStatement();
            System.out.println("select * from " + DBConnectivity.DBName + ".dbo." + DBConnectivity.Input_Table + " WITH (NOLOCK) where Status in ("+DBConnectivity.recordinputstatus+") and "+AdditionalQuery+" ID between " + DBConnectivity.Start + " and " + DBConnectivity.End + "  order by id");
            ResultSet rs = st.executeQuery("select * from " + DBConnectivity.DBName + ".dbo." + DBConnectivity.Input_Table + " WITH (NOLOCK) where Status in ("+DBConnectivity.recordinputstatus+") and "+AdditionalQuery+" ID between " + DBConnectivity.Start + " and " + DBConnectivity.End + "  order by id");
            try {
                while (rs.next()) {
                    String baseurl = "";
                    ID = rs.getInt("id");
                    link = rs.getString("InputURL").trim();
                    link = link.trim();
                    urlid=rs.getString("InputID");
//                    urlid =Integer.parseInt(test);
                    Aboutus_Matched = rs.getString("AboutUs_ListLookup");
                    LinkLookup_matched = rs.getString("LinkLookupKeyword_ListLookup");
                    DomainURL = rs.getString("DomainURL");
                    US_CityState = rs.getString("US_City_State_MatchedWord");
                    US_CityState = rs.getString("US_ZipCode_MatchedWord");
//                    down_on = rs.getString(8);
//                    is_map = rs.getInt(9);
//                    is_searched = rs.getInt(10);
//                    oid = rs.getInt(13);
//                    language = rs.getString(15);
                    baseurl = link.trim();
//                    if (!StringUtils.isBlank((String) link) && !StringUtils.isEmpty((String) link) && (StringUtils.containsIgnoreCase((String) link, (String) "http") || StringUtils.containsIgnoreCase((String) link, (String) "www"))) {
                    if (!StringUtils.isBlank((String) link) && !StringUtils.isEmpty((String) link)) {
                        String urlcheck = Article_Download_Main.URLVerifier(link);
                        if (!StringUtils.equalsIgnoreCase((String) urlcheck, (String) "Empty")) {
                            Execute_ContentDownload content_extractor = new Execute_ContentDownload(ID, link, urlid,DomainURL);
                            executor.execute(content_extractor);
                            continue;
                        }
                        Article_Download_Main.insert_query(ID, urlid, DomainURL, link, "", 0,2, "Failure","URL ends with .pdf, .doc etc",8,"",0,"","","");
                        continue;
                    }
                    Article_Download_Main.insert_query(ID, urlid, DomainURL, link, "", 0,3, "Failure","URL is empty",9,"",0,"","","");
//                    Article_Download_Main.insert_query(ID, urlid, DomainURL, link, Aboutus_Matched, LinkLookup_matched, US_CityState, US_Zipcode, "URL is empty","Failure",2);
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                }
                executor.shutdown();
                long endtime = System.currentTimeMillis();
                long totaltime = endtime - starttime;
                long mins = (totaltime /= 1000) / 60;
                System.out.println("Time taken for Article download:" + totaltime + " seconds");
                System.out.println("Time taken for Article download:" + mins + " mins");
                System.out.println(">>>>>>>>>> Process Completed <<<<<<<<<<");
                rs.close();
                st.close();
                Executionstatus = "success";
                Date date1 = new Date();
                toolend = dateformate.format(date1);
//                Article_Download_Main.processtrackerUpdate(toolend, Executionstatus, ErrorDescription, DBConnectivity.processname, currdate, DBConnectivity.TrackerTable, toolstart);
            } catch (Exception ex) {
                Date date2 = new Date();
                toolend = dateformate.format(date2);
                Executionstatus = "Failure";
                ex.printStackTrace();
                ErrorDescription = ex.getLocalizedMessage();
//                Article_Download_Main.processtrackerUpdate(toolend, Executionstatus, ErrorDescription, DBConnectivity.processname, currdate, DBConnectivity.TrackerTable, toolstart);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }

    public static String URLVerifier(String link) {
        block4:
        {
            link = link.trim();
            String regex = "";
            if (!StringUtils.isBlank((String) link) && !StringUtils.isEmpty((String) link)) {
                regex = DBConnectivity.URL_Exclusion_Regex.trim();
                try {
                    Pattern regsearch = Pattern.compile(regex, 234);
                    Matcher regex_search = regsearch.matcher(link);
                    if (regex_search.find()) {
                        String matchedext = regex_search.group(1);
                        System.out.println("Extension Matched for link " + link + " :" + matchedext);
                        link = "Empty";
                        break block4;
                    }
                    return link;
                } catch (PatternSyntaxException ex) {
                    System.err.println("Error in URL Exclusion Keyord matching regex:" + ex.getLocalizedMessage());
                }
            }
        }
        return link;
    }

    public static void CreateOutputtable() {
        PreparedStatement ps = null;
        String query = "";
        query = "if not exists (select * from  " + DBConnectivity.DBName + ".INFORMATION_SCHEMA.TABLES where TABLE_NAME ='" + DBConnectivity.New_OutputTable + "' )\n" + "    CREATE TABLE [dbo].[" + DBConnectivity.New_OutputTable + "](\n" +
                "[id] [int] IDENTITY(1,1) NOT NULL,\n" +
"	[InputID] [nvarchar](50) NULL,\n" +
"	[URL_ID] [nvarchar](50) NULL,\n" +
"	[LINK] [nvarchar](max) NULL,\n" +
"	[DOMAINURL] [nvarchar](max) NULL,\n" +
"	[DOWNLOADED_DATE] [nchar] (30),\n" +
"	[PAGE_SOURCE] [text] NULL,\n" +
"	[STATUS] [int] NULL,\n" +
"	[HASH] [int] NULL,\n" +
"	[LANGUAGE] [nchar](10) NULL,\n" +
"	[BOTSTATUS] [nvarchar](50) NULL,\n" +
"	[BOTSTATUSDescription] [nvarchar](max) NULL,\n" +
"	[ErrorCode] [int] NULL,\n" +
"	[ErrorDescription] [nvarchar](max) NULL,\n" +
"	[ResponseCode] [int] NULL,\n" +
"	[ParserType] [nchar](10) NULL,\n" +
"	[ContentType] [nvarchar](50) NULL\n" + ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
        try {
            DBConnectivity.DBConnection();
            ps = DBConnectivity.con.prepareStatement(query);
            ps.executeUpdate();
            System.out.println(DBConnectivity.New_OutputTable + " Output Table Created ");
            ps.close();
        } catch (Exception ex) {
            System.err.println("Error in Creating Output Table:" + ex.getMessage());
        }
    }

    public static void insert_query(int id, String Url_id, String DomainURL, String Link, String PageSource, int hashcode, int Status,String BotStatus,String BotStatusDescription, int ErrorCode, String ErrorDescription,
            int ResponseCode, String ParserType, String ContentType,String Languauge) {
        try {
        {
                Link = Link.trim();
                PreparedStatement pstmt = null;
                String qry = "INSERT INTO " + DBConnectivity.DBName + ".dbo." + DBConnectivity.New_OutputTable + "  ([InputID],[URL_ID],[LINK],[DOMAINURL],[DOWNLOADED_DATE],[PAGE_SOURCE],[STATUS],[HASH],[LANGUAGE],[BOTSTATUS],[BOTSTATUSDescription],[ErrorCode],[ErrorDescription],[ResponseCode],[ParserType],[ContentType]) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = DBConnectivity.con.prepareStatement(qry);
                pstmt.setInt(1, id);
                pstmt.setString(2, Url_id);
                pstmt.setString(3, Link);
                pstmt.setString(4, DomainURL);
                SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
            Date date = new Date();
//            System.out.println(formatter.format(date));
            pstmt.setString(5, formatter.format(date));
//                pstmt.setString(5, currdate);
                pstmt.setString(6, PageSource);
                pstmt.setInt(7, Status);
//                pstmt.setString(7, Downloaded_On);
                pstmt.setInt(8, hashcode);
                pstmt.setString(9, Languauge);
                pstmt.setString(10, BotStatus);
                pstmt.setString(11, BotStatusDescription);
                pstmt.setInt(12, ErrorCode);
                pstmt.setString(13, ErrorDescription);
                pstmt.setInt(14, ResponseCode);
                pstmt.setString(15, ParserType);
                pstmt.setString(16, ContentType);
                pstmt.executeUpdate();
                pstmt.clearBatch();
                pstmt.clearWarnings();
                pstmt.close();
                System.out.println("Record Inserted:" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toHtml(String string) throws IOException {
        BufferedReader st = new BufferedReader(new StringReader(string));
        StringBuffer buf = new StringBuffer("<html><body>");
        try {
            String str = st.readLine();
            while (str != null) {
                if (str.equalsIgnoreCase("<br/>")) {
                    str = "<br>";
                }
                buf.append(str);
                if (!str.equalsIgnoreCase("<br>")) {
                    buf.append("<br>");
                }
                str = st.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buf.append("</body></html>");
        string = buf.toString();
        st.close();
        return string;
    }

    public static String SentenceDetect(String Pagesource) throws IOException, Exception {
        String paragraph = Pagesource;
        String eachsentence = "";
        String converted_output = "";
        StringBuffer buf = new StringBuffer();
        try {
            FileInputStream is = new FileInputStream("en-sent.bin");
            SentenceModel model = new SentenceModel((InputStream) is);
            SentenceDetectorME sdetector = new SentenceDetectorME(model);
            String[] sentences = sdetector.sentDetect(paragraph);
            for (int i = 0; i < sentences.length; ++i) {
                eachsentence = sentences[i];
                converted_output = converted_output.concat(eachsentence).concat("\n");
            }
            is.close();
        } catch (Exception ex) {
            System.err.println("" + ex.getMessage());
        }
        return converted_output;
    }

    public static String crawlCorecontent(String pagelink, String pagesource) {
        String content = "";
        String CleanedContent = "";
        boolean source = false;
        System.out.println("Crawling in goose&boiler");
        if (pagesource == null || pagesource == "") {
            source = true;
            exmsg = "No page source";
            content = "";
            CleanedContent = "";
        }
        if (!source) {
            try {
                content = ArticleExtractor.INSTANCE.getText(pagesource);
                CleanedContent = Article_Download_Main.contentNormalization(content);
                CleanedContent = Article_Download_Main.CleanContent(CleanedContent);
                CleanedContent = Article_Download_Main.New_ReplaceAll_Method(CleanedContent);
            } catch (Exception ex) {
                exmsg = ex.getMessage();
                content = "";
                CleanedContent = "";
                ex.printStackTrace();
            }
        }
        if (CleanedContent.replaceAll("\\W+", "").trim().length() < 100) {
            exmsg = "Content too small";
            CleanedContent = "";
        }
        if (CleanedContent.isEmpty() || CleanedContent == null) {
            try {
                CrawlCandidate cr = null;
                Configuration configuration = new Configuration();
                configuration.setMinBytesForImages(4500);
                configuration.setLocalStoragePath("/tmp/goose");
                configuration.setEnableImageFetching(false);
                configuration.setImagemagickConvertPath("/opt/local/bin/convert");
                try {
                    cr = new CrawlCandidate(configuration, pagelink, pagesource);
                    Goose g = new Goose(configuration);
                    Article article = g.sendToActor(cr);
                    content = article.topNode().text();
                } catch (Exception ex) {
                    System.out.println("Error in Core Content " + ex.getMessage());
                }
                if (pagesource == null) {
                    exmsg = "No page source";
                } else if (pagesource.trim().length() == 0) {
                    exmsg = "No page source";
                } else {
                    CleanedContent = Article_Download_Main.contentNormalization(content);
                    CleanedContent = Article_Download_Main.CleanContent(CleanedContent);
                    CleanedContent = Article_Download_Main.New_ReplaceAll_Method(CleanedContent);
                }
            } catch (Exception e) {
                CleanedContent = "";
            }
        }
        return CleanedContent;
    }

    public static String Load_Url_Class(String stringurl, int InputID) throws Exception {
        DBConnectivity.LoadProperties();
        String Content = "";
        String line = "";
        StringBuffer strbuffer = new StringBuffer();
        BufferedReader bufferreader = null;
        try {
            System.out.println("Loading from URL Class");
            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] trust_mgr = Article_Download_Main.get_trust_mgr();
            ssl_ctx.init(null, trust_mgr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
            URL u = new URL(null, stringurl, new Handler());
            HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
            con.setConnectTimeout(DBConnectivity.Timeout);
            con.setReadTimeout(DBConnectivity.Timeout);
            con.connect();
            Thread.sleep(3000);
            bufferreader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = bufferreader.readLine()) != null) {
                strbuffer.append(line);
            }
            Content = strbuffer.toString();
            if ((Content = Article_Download_Main.ExceptionCheck(Content)).length() > 80) {
                System.out.println("Page Content Extracted by URL Class:" + InputID);
                Content = Article_Download_Main.CompressedContent(Content);
                Content = dataclean.dataclean(Content);
            } else {
                Content = "Error in Getting Source";
                System.out.println("Content Not Extracted by URL Class:" + InputID);
            }
            con.disconnect();
        } catch (Exception e) {
            String error_msg = e.getLocalizedMessage() + e.getMessage();
            if (StringUtils.containsIgnoreCase((String) e.toString(), (String) "Status=404") || StringUtils.containsIgnoreCase((String) e.toString(), (String) "Server returned HTTP response code: 503") || StringUtils.containsIgnoreCase((String) e.toString(), (String) "Status=503")) {
                Content = "404";
            }
            System.err.println("Error while getting Page source in URL class:" + error_msg);
        }
        return Content;
    }

    public static String CompressedContent(String getcontent) {
        String Compressed_PageSource = "";
        try {
            HtmlCompressor compressor = new HtmlCompressor();
            compressor.setEnabled(true);
            compressor.setRemoveComments(true);
            compressor.setRemoveMultiSpaces(true);
            compressor.setRemoveIntertagSpaces(true);
            compressor.setRemoveQuotes(true);
            compressor.setCompressCss(true);
            Compressed_PageSource = compressor.compress(getcontent);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Compressed_PageSource = "";
        }
        return Compressed_PageSource;
    }

    public static String JsoupClass(String link, int InputID) throws IOException, Exception {
        DBConnectivity.LoadProperties();
        System.out.println("Loading from JSOUP");
        String Page_Content = "";
        String redirecturl = "";
        String baseurl = link;
        try {
            Connection.Response response = Jsoup.connect((String) link).userAgent("Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.1)").timeout(DBConnectivity.Timeout).followRedirects(true).execute();
            Document doc = response.parse();
            URL url = response.url();
            redirecturl = url.toString();
            String urlcheck = Article_Download_Main.URLVerifier(redirecturl);
            if (!StringUtils.equalsIgnoreCase((String) urlcheck, (String) "Empty")) {
                if (!(StringUtils.isBlank((String) link) || StringUtils.isEmpty((String) link) || StringUtils.containsIgnoreCase((String) link, (String) "http") || StringUtils.containsIgnoreCase((String) link, (String) "www"))) {
                    URL myUrl = new URL(baseurl);
                    String linkhost = myUrl.getHost();
                    link = "http://" + linkhost + link;
                    urlcheck = "";
                    urlcheck = Article_Download_Main.URLVerifier(link);
                    if (!StringUtils.equalsIgnoreCase((String) urlcheck, (String) "Empty")) {
                        response = Jsoup.connect((String) link).userAgent("Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.1)").timeout(DBConnectivity.Timeout).followRedirects(true).execute();
                        doc = response.parse();
                    }
                }
                doc.outputSettings().charset("UTF-8");
                doc = doc.normalise();
                Page_Content = doc.toString();
                Page_Content = Article_Download_Main.ExceptionCheck(Page_Content);
                if (Page_Content.length() > 80) {
                    System.out.println("Page Content Extracted by JSOUP:" + InputID);
                } else {
                    Page_Content = "Error in Getting Source";
                    System.out.println("Content Not Extracted by JSoup:" + InputID);
                }
            } else {
                Page_Content = "404";
            }
        } catch (Exception ex) {
            String error_msg = ex.getLocalizedMessage() + ex.getMessage();
            if (StringUtils.containsIgnoreCase((String) ex.toString(), (String) "Status=404") || StringUtils.containsIgnoreCase((String) ex.toString(), (String) "Server returned HTTP response code: 503") || StringUtils.containsIgnoreCase((String) ex.toString(), (String) "Status=503")) {
                Page_Content = "404";
            }
            System.err.println("Error while getting Page source in jsoup:" + error_msg);
        }
        return Page_Content;
    }

    public static String Load_fromHttpcon(String linktoload, int InputID) throws InterruptedException, Exception {
        String Extracted_Content = "";
        int Code = 0;
        try {
            DBConnectivity.LoadProperties();
            String agent_string = "";
            agent_string = "Mozilla/5.0 (Windows; suburl; Windows NT 5.1; de-AT; rv:1.8a1) Gecko/20040520";
            URL suburl = new URL(linktoload);
            DisableSSLCertificateCheckUtil.disableChecks();
            HttpURLConnection huc = (HttpURLConnection) suburl.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            huc.setInstanceFollowRedirects(true);
            huc.setRequestMethod("GET");
            huc.setConnectTimeout(DBConnectivity.Timeout);
            huc.setReadTimeout(DBConnectivity.Timeout);
            huc.setRequestProperty("User-Agent", agent_string);
            huc.connect();
            try {
                Code = huc.getResponseCode();
                if (Code == 200) {
                    Extracted_Content = Article_Download_Main.ParseLinkToURLClass(huc, linktoload);
                } else {
                    Extracted_Content = "Error in Source Page: Response Code - " + Code;
                    System.out.println("Content Not Extracted by HTTPCON:" + InputID);
                }
            } catch (Exception e) {
                Extracted_Content = "";
                System.err.println("Error while getting page source in HTTPCON Class:" + e.getLocalizedMessage() + InputID);
            }
            if (Extracted_Content.length() > 80) {
                Extracted_Content = Article_Download_Main.CompressedContent(Extracted_Content);
                Extracted_Content = dataclean.dataclean(Extracted_Content);
                System.out.println("Content Extracted by HTTPCON Class:" + InputID);
            } else {
                Extracted_Content = "Error in Getting Source";
                System.out.println("Content Not Extracted by HTTPCON:" + InputID);
            }
        } catch (Exception ex) {
            Extracted_Content = "";
            System.err.println("Error while getting page source in HTTPCON Class:" + ex.getLocalizedMessage() + InputID);
        }
        return Extracted_Content;
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

    public static String Load_URL_HtmlUnit_Driver(String linktoload, int InputID) throws InterruptedException, Exception {
        String Extracted_Content = "";
        DBConnectivity.LoadProperties();
        try {
            String agent_string = "";
            agent_string = "Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.1)";
            try {
                HtmlUnitDriver driver = new HtmlUnitDriver(new BrowserVersion("Firefox", "5.0 (Windows)", agent_string, 66.0f));
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
                driver.get(linktoload);
                Extracted_Content = driver.getPageSource();
                Extracted_Content = Article_Download_Main.ExceptionCheck(Extracted_Content);
                driver.close();
            } catch (Exception e) {
                Extracted_Content = "";
                System.err.println("Error while getting page source in HTMLUnitdriver Class:" + e.getMessage() + e.getLocalizedMessage());
            }
            if (Extracted_Content.length() > 80) {
                Extracted_Content = Article_Download_Main.CompressedContent(Extracted_Content);
                Extracted_Content = dataclean.dataclean(Extracted_Content);
                System.out.println("Content Extracted by HTMLUnitdriver Class:" + InputID);
            } else {
                Extracted_Content = "Error in Getting Source";
                System.out.println("Content Not Extracted by HTMLUnitdriver:" + InputID);
            }
        } catch (Exception ex) {
            Extracted_Content = "";
            System.err.println("Error while getting page source in HTMLUnitdriver Class:" + ex.getLocalizedMessage());
        }
        return Extracted_Content;
    }

    public static String ExceptionCheck(String Source) throws Exception {
        for (int i = 0; i < Exception_List.size(); ++i) {
            String arrayvalue = Exception_List.get(i).toString();
            if (!Source.contains(arrayvalue) && !StringUtils.containsIgnoreCase((String) Source, (String) arrayvalue)) {
                continue;
            }
            Source = "Error";
            break;
        }
        return Source;
    }

    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

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

    public static void processtracker(String PName, String PMachine, String PDate, String STime, String ETime, String EStatus, String Instance2, String TTtable, String Stage) throws UnknownHostException, SQLException {
        try {
            DBConnectivity.LoadProperties();
            DBConnectivity.DBConnection();
            PreparedStatement insert_stmt = null;
            System.out.println("");
            System.out.println("Machine IP:" + PMachine);
            System.out.println("Date:" + PDate);
            System.out.println("Tool Instance:" + Instance2);
            System.out.println("Process Name:" + PName);
            System.out.println("start time:" + STime);
            System.out.println("End time:" + ETime);
            System.out.println("Stage:" + Stage);
            String sqlquery = "insert into " + TTtable + "(Process_Name,Module,Processing_Machine,Processing_Date,StartTime,EndTime,Execution_Status,Description,Instance,Processtype) values(?,?,?,?,?,?,?,?,?,?)";
            insert_stmt = DBConnectivity.con.prepareStatement(sqlquery);
            insert_stmt.setString(1, PName);
            insert_stmt.setString(2, Stage);
            insert_stmt.setString(3, PMachine);
            insert_stmt.setString(4, PDate);
            insert_stmt.setString(5, STime);
            insert_stmt.setString(6, ETime);
            insert_stmt.setString(7, "In Progress");
            insert_stmt.setString(8, DBConnectivity.shortdescription);
            insert_stmt.setString(9, DBConnectivity.InstanceNO);
            insert_stmt.setString(10, DBConnectivity.Processtype);
            insert_stmt.execute();
            insert_stmt.close();
        } catch (Exception ess) {
            ess.printStackTrace();
        }
    }

    public static void processtrackerUpdate(String toolend, String Execstatus, String ErrorDesc, String Procesname, String currentdate, String TTable, String StTime) throws SQLException {
        try {
            DBConnectivity.LoadProperties();
            CallableStatement sd = null;
            sd = DBConnectivity.con.prepareCall(" update " + TTable + " with(updlock) set EndTime= ? , Execution_Status=?, Error_Description= ?  where Process_Name = ? and Processing_Date = ? and StartTime = ?");
            sd.setString(1, toolend);
            sd.setString(2, Execstatus);
            sd.setString(3, ErrorDesc);
            sd.setString(4, Procesname);
            sd.setString(5, currentdate);
            sd.setString(6, StTime);
            sd.executeUpdate();
            System.out.println("Update count:" + sd.getUpdateCount());
            sd.clearBatch();
            sd.clearWarnings();
            sd.close();
        } catch (Exception ell) {
            ell.printStackTrace();
        }
    }

    public static String MachineIP() throws UnknownHostException, SocketException {
        String machineIPAddress = null;
        System.out.println("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        while (n.hasMoreElements()) {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            while (a.hasMoreElements()) {
                InetAddress addr = a.nextElement();
                if (!addr.getHostAddress().toString().contains("192")) {
                    continue;
                }
                System.out.println("  " + addr.getHostAddress().toString());
                machineIPAddress = addr.getHostAddress().toString();
            }
        }
        return machineIPAddress;
    }

    public static String New_ReplaceAll_Method(String content) {
        String content_to_replace = content;
        content_to_replace = content_to_replace.replaceAll("<div.*?>", "");
        content_to_replace = content_to_replace.replaceAll("</div>", "");
        content_to_replace = content_to_replace.replaceAll("<td.*?>", "");
        content_to_replace = content_to_replace.replaceAll("</td>", "");
        content_to_replace = content_to_replace.replaceAll("<table.*?>", "");
        content_to_replace = content_to_replace.replaceAll("</table>", "");
        content_to_replace = content_to_replace.replaceAll("<tr.*?>", "");
        content_to_replace = content_to_replace.replaceAll("</tr>", "");
        content_to_replace = content_to_replace.replaceAll("<link.*?>", "");
        content_to_replace = content_to_replace.replaceAll("<style.*?>", "");
        content_to_replace = content_to_replace.replaceAll("</style>", "");
        content_to_replace = content_to_replace.replaceAll("(?si)<script[^<>]*?>.*?</script>", "");
        content_to_replace = content_to_replace.replaceAll("(?si)<iframe[^<>]*?>.*?</iframe>", "");
        content_to_replace = content_to_replace.replaceAll("(?si)<iframe[^<>]*?>", "");
        content_to_replace = content_to_replace.replaceAll("(?si)<img[^<>]*?>|</img>", "");
        content_to_replace = content_to_replace.replaceAll("(?si)<noscript[^<>]*?>.*?</noscript>", "");
        content_to_replace = content_to_replace.replaceAll("<noscript/>", "");
        content_to_replace = content_to_replace.replaceAll("(?sim)<head>.*?</head>", "");
        content_to_replace = content_to_replace.replaceAll("(?sim)<!--.*?-->", "");
        return content_to_replace;
    }

    public static String contentNormalization(String content) {
        String normalizedContent = content;
        normalizedContent = normalizedContent.replaceAll("(?si)<script[^<>]*?>.*?</script>", "");
        normalizedContent = normalizedContent.replaceAll("(?si)<iframe[^<>]*?>.*?</iframe>", "");
        normalizedContent = normalizedContent.replaceAll("(?si)<iframe[^<>]*?>", "");
        normalizedContent = normalizedContent.replaceAll("&#65533;", "'");
        normalizedContent = normalizedContent.replaceAll("(?si)<img[^<>]*?>|</img>", "");
        normalizedContent = normalizedContent.replaceAll("(?si)<style[^<>]*?>.*?</style>", "");
        normalizedContent = normalizedContent.replaceAll("(?si)<noscript[^<>]*?>.*?</noscript>", "");
        normalizedContent = normalizedContent.replaceAll("<noscript/>", "");
        normalizedContent = normalizedContent.replaceAll("display\\s*:\\s*none\\s*", "");
        normalizedContent = normalizedContent.replaceAll("(?sim)<head>.*?</head>", "");
        normalizedContent = normalizedContent.replaceAll("(?sim)<!--[^<>]*>", "");
        normalizedContent = normalizedContent.replaceAll("(?sim)<!--.*?-->", "");
        normalizedContent = normalizedContent.replaceAll("(?sim)\\s\\s+", "\n");
        normalizedContent = normalizedContent.replaceAll("(?sim)&nbsp;", " ");
        return normalizedContent;
    }

    public static String CleanContent(String strClean) {
        strClean = strClean.replaceAll("(?sim)(?sim)<[^<>]+>", "");
        strClean = strClean.replaceAll("(?sim)&reg;", "");
        strClean = strClean.replaceAll("(?sim)&nbsp;", " ");
        strClean = strClean.replaceAll("(?sim)\\s+", " ");
        strClean = strClean.replaceAll("(?sim)&amp", "&");
        strClean = strClean.replaceAll("(?sim)&#9658;", "");
        strClean = strClean.replaceAll("(?sim)\u00c3\u00a2;", "");
        strClean = strClean.replaceAll("(?sim)&#9658;", "");
        strClean = strClean.replaceAll("(?sim)\u00e2\u201e\u00a2", "");
        strClean = strClean.replaceAll("(?sim)\u00e2\u201a\u00ac", "");
        strClean = strClean.replaceAll("(?sim)&#x201c", " ");
        strClean = strClean.replaceAll("(?sim)&#x201d", "");
        strClean = strClean.replaceAll("(?sim)&quot;", "");
        strClean = strClean.replaceAll("(?sim);", "");
        strClean = strClean.replaceAll("(?sim)\"", "");
        strClean = strClean.replaceAll("(?sim)\u00e2\u20ac\u00a6", "");
        strClean = strClean.replaceAll("(?sim)\\[\\]", "");
        strClean = strClean.replaceAll("(?sim)&nbsp;", " ");
        strClean = strClean.replaceAll("(?sim)\\s+", " ");
        strClean = strClean.replaceAll("(?sim)&#9658;", "");
        strClean = strClean.replaceAll("(?sim)&#x201d|\u00c3\u0192\u00c2\u00a2;|\u00c3\u0192\u00e2\u20ac\u0161|\u00c3\u0192\u00c2\u00a2|&#9658;|&quot;|\u00c3\u201a\u00e2\u201a\u00ac|\u00c3\u201a\u00e2\u201e\u00a2", "");
        strClean = strClean.replaceAll("(?sim)&#x201c", " ");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201a ", "\\S");
        strClean = strClean.replaceAll("(?sim)\"\"", "\"");
        strClean = strClean.replaceAll("(?sim)&#039;", "'");
        strClean = strClean.replaceAll("(?sim)&#233", "e");
        strClean = strClean.replaceAll("(?sim)&#235", "e");
        strClean = strClean.replaceAll("(?sim)&#8211;", "-");
        strClean = strClean.replaceAll("(?sim)&#8213;", "-");
        strClean = strClean.replaceAll("(?sim)&#8217;", "'");
        strClean = strClean.replaceAll("(?sim)&#8220;", "\"");
        strClean = strClean.replaceAll("(?sim)&#8221;", "\"");
        strClean = strClean.replaceAll("(?sim)&amp;", "&");
        strClean = strClean.replaceAll("(?sim)&bull;", "6");
        strClean = strClean.replaceAll("(?sim)&euro;", "\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac");
        strClean = strClean.replaceAll("(?sim)&euro;-", "\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac");
        strClean = strClean.replaceAll("(?sim)&ldquo;", "\"");
        strClean = strClean.replaceAll("(?sim)&mdash;", "'");
        strClean = strClean.replaceAll("(?sim)&nbsp;", "\\S");
        strClean = strClean.replaceAll("(?sim)&ndash;", "-");
        strClean = strClean.replaceAll("(?sim)&rdquo;", "\u00c3\u00a2\u00e2\u201a\u00ac ");
        strClean = strClean.replaceAll("(?sim)&rsquo;", "'");
        strClean = strClean.replaceAll("(?sim)&rsquo;", "");
        strClean = strClean.replaceAll("(?sim)&rsquo;-", "'");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201a\u00c2\u00bf", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201a\u00c2\u00ac", "\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac");
        strClean = strClean.replaceAll("(?sim)\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u20ac\u017e\u00c2\u00a2", "'");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a1", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192 ", "A");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192 ", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u201a\u00ac", "A");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u0161", "");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a4", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u017e", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00c6\u2019", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a3", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u00a6", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a5", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00a8", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00b4", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u201a\u00ac\u00c5\u00a1\u00c3\u201a\u00c2\u00a4", "\u00c3\u201a\u00c2\u00a3");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u201a\u00ac\u00c5\u00a1\u00c3\u201a\u00c2\u00ac", "\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u201a\u00c2\u00b1", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00b1", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00b1", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00ab", "e");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u201a\u00c2\u00a3", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u0161\u00c3\u201a\u00c2\u00a3", "\u00c3\u201a\u00c2\u00a3");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00a3", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00a4", "a");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u201a\u00c2\u00a7", "c");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00a7o", "c");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00a9", "e");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u201a\u00c2\u00ae", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u0161\u00c3\u201a\u00c2\u00ae", "\u00c3\u201a\u00c2\u00ae");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00b6", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u00a2\u00e2\u201a\u00ac\u00c2\u00b0", "e");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac", "\u00c3\u0192\u00e2\u201a\u00ac");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u201a ", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac?", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u201a\u00c2\u00a6", "...");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u2039\u00c5\u201c", "'");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u2039\u00c5\u201c", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u201a\u00ac\u00cb\u0153", "-");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u201a\u00ac\u00c5\u201c", "-");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u201a\u00ac ", "-");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u201a\u00c2\u00a2-", "");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u2026\u00e2\u20ac\u0153", "\"");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u20ac\u017e\u00c2\u00a2", "'");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00bc", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00b3", "o");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u201a\u00c2\u00aa", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a6", "ae");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac ", "ae");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c6\u2019\u00c3\u201a\u00c2\u00ba", "u");
        strClean = strClean.replaceAll("(?sim)a\u00c3\u2026 ", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00e2\u20ac\u00a1", "c");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e ", "c");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00c5\u2019", "C");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a7", "c");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b0", "eth");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u00b0", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a9", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a8", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00cb\u2020", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00aa", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00ab", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00e2\u20ac\u00ba", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00e2\u20ac\u00a2", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00e2\u201e\u00a2", "e");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00ad", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192 ", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00ac", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00ae", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00af", "i");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00e2\u20ac\u0161", "l");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00e2\u20ac\u017e", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00cb\u2020", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b1", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u02dc", "n");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b3", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b2", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b4", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00e2\u20ac\u009d", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b6", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b5", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00e2\u20ac\u02dc", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b8", "o");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00cb\u0153", "O");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00e2\u201e\u00a2", "r");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00c2\u00a1", "s");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026 ", "S");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00c5\u00b8", "s");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c5\u00b8", "ss");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00ba", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00b9", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00bb", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00bc", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c5\u201c", "u");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00bd", "y");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00bf", "y");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00c2\u00bc", "z");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00c2\u00be", "z");
        strClean = strClean.replaceAll("(?sim)\u00c3\u2026\u00c2\u00bd", "Z");
        strClean = strClean.replaceAll("(?sim)\u00c3\u201e\u00e2\u20ac\u00a6", "a");
        strClean = strClean.replaceAll("(?sim)\u00c3\u0192\u00c2\u00a2\u00c3\u00a2\u00e2\u20ac\u0161\u00c2\u00ac\u00c3\u00a2\u00e2\u201a\u00ac\u00c5\u201c", "-");
        strClean = strClean.replaceAll("(?sim)&#39", "'");
        strClean = strClean.replaceAll("\u00c2 ", " ");
        return strClean.trim();
    }

    static {

        link = "";
        exmsg = "";
        urlid = "";
        
        Aboutus_Matched = "";
        LinkLookup_matched = "";
//        created = "";
        comment = "";
        US_CityState = "";
        down_on = "";
        US_Zipcode = "";
        tagg_sub = "";
        content = "";
        language = "";
        status = 0;
        oid = 0;
        is_map = 0;
        is_searched = 0;
        count_check = 0;
        output_totalcount = 0;
        OutputTableCreation = false;
        Exception_List = new ArrayList<String>();
        toolstart = "";
        toolend = "";
        Gcalendar = new GregorianCalendar();
        dateformate = new SimpleDateFormat("HH:mm:ss");
        currdate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Gcalendar.getTime());
        date = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        machineIP = "";
        Executionstatus = "";
        ErrorDescription = "";
    }

    public static class Execute_ContentDownload
            implements Runnable {

        int InputID = 0;
        String LoadingURL = "";
        String Page_source = "";
        int hashcode = 0;
        String Core_Content = "";
        String urlid = "";
        String sub = "";
        String domainurl="";
        
        
        
        String pub_date = "";
        String created = "";
        String comment = "";
        String body = "";
        String down_on = "";
        String tagg_body = "";
        String tagg_sub = "";
        int oid = 0;
        int is_map = 0;
        int is_searched = 0;
        String language = "";

//        public Execute_ContentDownload(Integer InputID, String LoadingURL, Integer urlid, String Subject, String Published_Date, String Created_By, String Body, String Downloaded_On, Integer is_map, Integer Is_Searched, Integer oid, String Language) {
        public Execute_ContentDownload(Integer InputID, String LoadingURL, String urlid,String Domurl) {
            this.InputID = InputID;
            this.LoadingURL = LoadingURL;
            this.urlid = urlid;
            this.domainurl = Domurl;
//            this.pub_date = Published_Date;
//            this.created = Created_By;
//            this.body = Body;
//            this.down_on = Downloaded_On;
//            this.is_map = is_map;
//            this.is_searched = Is_Searched;
//            this.oid = oid;
//            this.language = Language;
        }

        @Override
        public void run() {
            try {

//                String ErrorStatus = "";
                int ErrorStatus = 0;
                String ErrorStatusDescription = "";
                System.out.println("ID : " + this.InputID);
                System.out.println("Loading URL: " + this.LoadingURL);
                this.Page_source = "";
                HtmlHeaderAttributes html_Attrib = new HtmlHeaderAttributes();
                try {
//                    this.LoadingURL = "http://feedproxy.google.com/~r/HappiBreakingNews/~3/DLSENWJPfiw/";
                    html_Attrib = SourceGetter(this.LoadingURL);
                    if (!html_Attrib.getPageSoure().isEmpty()) {
//                        this.Page_source = Article_Download_Main.JsoupClass(this.LoadingURL, this.InputID);
                        this.Page_source = html_Attrib.getPageSoure();
                    } else //                        if (this.Page_source.length() <= 50 && !StringUtils.equals((String) this.Page_source, (String) "404")) {
                    //                        if (this.Page_source.length() <= 50 && String.valueOf(html_Attrib.getResponseCode()).equalsIgnoreCase("404")) {
                    if (this.Page_source.length() <= 50 && html_Attrib.getResponseCode() != 200) {
                        html_Attrib = getHttpURLContent(this.LoadingURL);
                        this.Page_source = html_Attrib.getPageSoure();
                            if (this.Page_source.length() <= 50 && !StringUtils.equals((String) this.Page_source, (String) "404")) {
                                System.out.println("Loading from HTML Unit Driver");
                                this.Page_source = Article_Download_Main.Load_fromHttpcon(this.LoadingURL, this.InputID);
                            }
                    }

                } catch (Exception ex) {
//                try {
//                    this.Page_source = Article_Download_Main.JsoupClass(this.LoadingURL, this.InputID);
//                    if (this.Page_source.length() <= 50 && !StringUtils.equals((String) this.Page_source, (String) "404")) {
//                        this.Page_source = Article_Download_Main.Load_Url_Class(this.LoadingURL, this.InputID);
//                        if (this.Page_source.length() <= 50 && !StringUtils.equals((String) this.Page_source, (String) "404")) {
//                            System.out.println("Loading from HTML Unit Driver");
//                            this.Page_source = Article_Download_Main.Load_fromHttpcon(this.LoadingURL, this.InputID);
//                        }
//                    }
//                } catch (Exception ex) {
                    String errormsg = ex.getMessage();
                    System.err.println(errormsg + " " + Article_Download_Main.link);
                    System.err.println("Source unable to extract  --- " + Article_Download_Main.link);
//                                      Article_Download_Main.insert_query(ID, urlid, DomainURL, link, "", 0,3, "Failure","URL is empty","","","","","","");
                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, "",0,4,"Failure","RunTime Exception : " + errormsg,html_Attrib.getErrorCode(),html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),"");
                }

                if (html_Attrib.getResponseCode() == 200) {
                    
                    if (this.Page_source.length() > 100) {
                        this.language=Readability_Extraction.DetectLanguage(this.Page_source.replaceAll("(?sim)<[^<>]*>"," ").replaceAll("(?sim)\\s+"," "));
                        if(CoreContentExtraction.equalsIgnoreCase("true")){
                        this.Core_Content = Readability_Extraction.Readability(this.Page_source);
                        if (this.Core_Content.length() < 100) {
                            this.Page_source = Cleaner.dataclean(this.Page_source);
                            this.Core_Content = Article_Download_Main.crawlCorecontent(this.LoadingURL, this.Page_source);
                            this.Core_Content = Article_Download_Main.contentNormalization(this.Core_Content);
                            this.Core_Content = Article_Download_Main.CleanContent(this.Core_Content);
                            if (this.Core_Content.length() > 100) {
                                this.hashcode = this.Core_Content.hashCode();
                                this.Core_Content = this.Core_Content.replaceAll("<.*?>", "").replaceAll("(?sim)\\s+", " ");
                                this.Core_Content = Article_Download_Main.New_ReplaceAll_Method(this.Core_Content);
                                this.Core_Content = WordUtils.wrap((String) this.Core_Content, (int) 150).trim();
                                Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Core_Content, this.hashcode,1,"success","Core Content Extracted SuccessFully",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),this.language);
                            } else {
                                this.hashcode = this.Page_source.hashCode();
                                this.Page_source = this.Page_source.replaceAll("<.*?>", "").replaceAll("(?sim)\\s+", " ").trim();
                                this.Page_source = WordUtils.wrap((String) this.Page_source, (int) 150).trim();
                                this.Page_source = Article_Download_Main.contentNormalization(this.Page_source);
                                this.Page_source = Article_Download_Main.CleanContent(this.Page_source);
                                if (!(this.Page_source.contains("Error 503") || this.Page_source.contains("Service Temporarily Unavailable") || this.Page_source.contains("You are a spammer, hacker or an otherwise bad person") || this.Page_source.contains("hacker or an otherwise bad person") || this.Page_source.contains("Time taken: 0.2763 sec") || this.Page_source.contains("Please wait while we attempt to load") || this.Page_source.contains("We're having trouble verifing your authenticity") || this.Page_source.contains("Moved Permanently") || this.Page_source.contains("The document has moved here") || this.Page_source.contains("Page not found Sorry") || this.Page_source.contains("Page not found") || this.Page_source.contains("we can not find the page you're looking for") || this.Page_source.contains("Please return home or search for related pages. \u00c2  Go back to Homepage") || this.Page_source.contains("500 Internal server error") || this.Page_source.contains("Page Not Found Error 404") || this.Page_source.contains("We're sorry, the link you followed may be broken or expired.") || this.Page_source.contains("Object moved") || this.Page_source.contains("This page is a 403 forbidden page Please check the URL for proper spelling and capitalization.") || this.Page_source.contains("Please check the URL for proper spelling and capitalization.") || this.Page_source.contains("Page Not Found Click here to visit home page") || this.Page_source.contains("Exception occured while getting Page source - URL Problem") || this.Page_source.contains("The page cannot be displayed") || this.Page_source.contains("Server returned HTTP response code") || this.Page_source.contains("The proxy server is refusing connections") || this.Page_source.contains("You have received message because your internet address") || this.Page_source.contains("The connection has timed out") || this.Page_source.contains("document is no longer available") || this.Page_source.contains("The connection has timed out") || this.Page_source.contains("Server Error in '/AgentLocator' Application.") || this.Page_source.contains("404 - File or directory not found.") || this.Page_source.contains("The connection was reset") || this.Page_source.contains("The proxy server is refusing connections") || this.Page_source.contains("<body>403"))) {
//                                Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Page_source, 1, this.oid, this.hashcode, this.language, "", "");
                             Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Page_source, this.hashcode,1,"success","Page Source Extracted SuccessFully",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),this.language);
                            
//                                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Page_source, 1, this.oid, this.hashcode, this.language, "success", "1", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                                } else {
//                                Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 2, this.oid, 0, this.language, "Page not available/Internal Server Error", "");
                                 Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Page_source, this.hashcode,5,"Failure","Page not available/Internal Server Error",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),"");
                            
//                                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 2, this.oid, 0, this.language, "Page not available/Internal Server Error", "4", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                                }
                            }
                        
                        } else {
                            this.hashcode = this.Core_Content.hashCode();
//                        Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Core_Content, 1, this.oid, this.hashcode, this.language, "Core Content more than 100", "");
                            Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Core_Content, this.hashcode,1,"success","Readability Content Extracted SuccessFully",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),this.language);
  
//                            Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Core_Content, 1, this.oid, this.hashcode, this.language, "success", "1", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                        }
                    }else{
                            this.hashcode = this.Page_source.hashCode();
                            if (!(this.Page_source.contains("Error 503") || this.Page_source.contains("Service Temporarily Unavailable") || this.Page_source.contains("You are a spammer, hacker or an otherwise bad person") || this.Page_source.contains("hacker or an otherwise bad person") || this.Page_source.contains("Time taken: 0.2763 sec") || this.Page_source.contains("Please wait while we attempt to load") || this.Page_source.contains("We're having trouble verifing your authenticity") || this.Page_source.contains("Moved Permanently") || this.Page_source.contains("The document has moved here") || this.Page_source.contains("Page not found Sorry") || this.Page_source.contains("Page not found") || this.Page_source.contains("we can not find the page you're looking for") || this.Page_source.contains("Please return home or search for related pages. \u00c2  Go back to Homepage") || this.Page_source.contains("500 Internal server error") || this.Page_source.contains("Page Not Found Error 404") || this.Page_source.contains("We're sorry, the link you followed may be broken or expired.") || this.Page_source.contains("Object moved") || this.Page_source.contains("This page is a 403 forbidden page Please check the URL for proper spelling and capitalization.") || this.Page_source.contains("Please check the URL for proper spelling and capitalization.") || this.Page_source.contains("Page Not Found Click here to visit home page") || this.Page_source.contains("Exception occured while getting Page source - URL Problem") || this.Page_source.contains("The page cannot be displayed") || this.Page_source.contains("Server returned HTTP response code") || this.Page_source.contains("The proxy server is refusing connections") || this.Page_source.contains("You have received message because your internet address") || this.Page_source.contains("The connection has timed out") || this.Page_source.contains("document is no longer available") || this.Page_source.contains("The connection has timed out") || this.Page_source.contains("Server Error in '/AgentLocator' Application.") || this.Page_source.contains("404 - File or directory not found.") || this.Page_source.contains("The connection was reset") || this.Page_source.contains("The proxy server is refusing connections") || this.Page_source.contains("<body>403"))) {
//                                Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Page_source, 1, this.oid, this.hashcode, this.language, "", "");
                             Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Page_source, this.hashcode,1,"success","Page Source Extracted SuccessFully",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),this.language);
                            
//                                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, this.Page_source, 1, this.oid, this.hashcode, this.language, "success", "1", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                                } else {
//                                Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 2, this.oid, 0, this.language, "Page not available/Internal Server Error", "");
                                 Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, this.Page_source, this.hashcode,5,"Failure","Page not available/Internal Server Error",html_Attrib.getErrorCode(), html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),"");
                            
//                                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 2, this.oid, 0, this.language, "Page not available/Internal Server Error", "4", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                                }       
                        }
                    }else {
                        if (html_Attrib.getResponseCode() == 200) {
                            ErrorStatus = 3;
                            if (html_Attrib.getPageSoure().replaceAll("<[^>]*>", "").trim().isEmpty()) {
                                ErrorStatusDescription = "Page source without tags empty";
                            } else {
                                ErrorStatusDescription = "Page source Content less than 100";
                            }
                        }
                    }
                } else {
                    if (html_Attrib.getResponseCode() == 0) {
                        ErrorStatus = 6;
                        ErrorStatusDescription = "Response code zero";
                    } else if (html_Attrib.getResponseCode() > 200) {
                        ErrorStatus = 4;
                        ErrorStatusDescription = "response code > 300";
                    }

                
//                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 4, this.oid, 0, this.language, "Page source Content less than 100", "");
//                    Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 4, this.oid, 0, this.language, "Page source Content less than 100", "3", html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                             Article_Download_Main.insert_query(this.InputID, this.urlid, this.domainurl, this.LoadingURL, "", this.hashcode,6,"Failure",ErrorStatusDescription,ErrorStatus, html_Attrib.getErrorDescription(), html_Attrib.getResponseCode(), html_Attrib.getParserType(), html_Attrib.getContentType(),"");
                    
//                Article_Download_Main.insert_query(this.InputID, this.urlid, this.sub, this.LoadingURL, this.pub_date, this.created, this.body, this.down_on, this.is_map, this.is_searched, "", 4, this.oid, 0, this.language, ErrorStatusDescription, ErrorStatus, html_Attrib.getErrorDescription(), String.valueOf(html_Attrib.getResponseCode()), html_Attrib.getParserType(), html_Attrib.getContentType());
                }
            } catch (Exception ex) {
                String errormsg = ex.getMessage();
                System.err.println(errormsg + " " + Article_Download_Main.link);
            }
        }
    }

}
