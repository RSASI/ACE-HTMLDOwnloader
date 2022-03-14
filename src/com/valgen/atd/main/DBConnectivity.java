package com.valgen.atd.main;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectivity {

    public static Connection con = null;
    public static String IPAddress = "";
    public static String DBName = "";
    public static String UserName = "";
    public static String Password = "";
    static String Input_Table = "";
    public static String New_OutputTable = "";
    public static int Start;
    public static int End;
    public static int Timeout;
    public static int Thread_Pool;
    Properties props = null;
    static String Offline;
    public static int deadlockpriority;
    public static int lockout1;
    public static int lockout2;
    public static String MailUsername;
    public static String MailTo;
    public static String MailCC;
    public static String MailBCC;
    public static String Mail_Subject;
    public static String Mail_Desc;
    public static String CTPInsertion;
    public static String Highbeam_insertion;
    public static String URL_Exclusion_Regex;
    public static String InstanceNO;
    public static String processname;
    static String Stage;
    public static String TrackerTable;
    public static String shortdescription;
    public static String Processtype;
    public static String AdditionalQuery;

    public static String UserAgent = "";
    public static String MetaURLRegex = "";
    public static String CoreContentExtraction = "";

    public static int LoopCount = 0;
    public static int ConnectionTimeOut;
    public static String recordinputstatus;

    public static void LoadProperties() throws Exception {
        Properties props = new Properties();
        FileReader read = new FileReader(new File("ArticleDownload_ConfigNew.properties"));
        try {
            props.load(read);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        DBName = props.getProperty("DBName");
        IPAddress = props.getProperty("IPAddress");
        UserName = props.getProperty("UserName");
        Password = props.getProperty("Password");
        Input_Table = props.getProperty("Input_Table");
        New_OutputTable = props.getProperty("Output_Table");
        AdditionalQuery = props.getProperty("AdditionalQuery");
        Start = Integer.parseInt(props.getProperty("Start"));
        End = Integer.parseInt(props.getProperty("End"));
        Thread_Pool = Integer.parseInt(props.getProperty("Thread_Pool"));
        Timeout = Integer.parseInt(props.getProperty("Timeout"));
        deadlockpriority = Integer.parseInt(props.getProperty("Deadlockpriority"));
//        MailUsername = props.getProperty("mailUsername");
//        MailTo = props.getProperty("mailTo");
//        MailCC = props.getProperty("mailCC");
//        MailBCC = props.getProperty("mailBCC");
//        Mail_Subject = props.getProperty("Mail_Subject");
//        Mail_Desc = props.getProperty("Mail_Desc");
//        CTPInsertion = props.getProperty("CTP_Alerts_Insertion");
//        Highbeam_insertion = props.getProperty("HighBeam_Insertion");
        URL_Exclusion_Regex = props.getProperty("URL_Exclusion_Regex");
//        InstanceNO = props.getProperty("Instance");
//        processname = props.getProperty("processname");
//        Stage = props.getProperty("Stage");
//        TrackerTable = props.getProperty("TrackerTable");
//        shortdescription = props.getProperty("shortdescription");
//        Processtype = props.getProperty("Processtype");
        LoopCount = Integer.parseInt(props.getProperty("LoopCount"));
        UserAgent = props.getProperty("UserAgent");
        MetaURLRegex = props.getProperty("MetaURLRegex");
        CoreContentExtraction = props.getProperty("CoreContentExtraction");
        ConnectionTimeOut = Integer.parseInt(props.getProperty("ConnectionTimeOut"));
        recordinputstatus = props.getProperty("recordinputstatus");
    }

    public static void DBConnection() throws SQLException, ClassNotFoundException, Exception {
        DBConnectivity.LoadProperties();
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ConnectionUrl = "jdbc:sqlserver://" + IPAddress;
        con = DriverManager.getConnection("jdbc:sqlserver://" + IPAddress + ";" + "user=" + UserName + ";" + "Password=" + Password + ";" + "DatabaseName=" + DBName);
    }

    static {
        Offline = "";
        MailUsername = "";
        MailTo = "";
        MailCC = "";
        MailBCC = "";
        Mail_Subject = "";
        Mail_Desc = "";
        CTPInsertion = "";
        Highbeam_insertion = "";
        URL_Exclusion_Regex = "";
        InstanceNO = "";
        processname = "";
        Stage = "";
        TrackerTable = "";
        shortdescription = "";
        Processtype = "";
    }
}
