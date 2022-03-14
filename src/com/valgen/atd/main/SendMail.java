package com.valgen.atd.main;

//import ArticleDownload_latest.DBConnectivity;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
    static String date = "";

    public static void SendingMail(String Subject, String Description, int Count) throws AddressException, UnknownHostException, IOException {
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            System.out.println("Date: " + date);
            System.out.println("\n*** Sending Mail ***");
            DBConnectivity.LoadProperties();
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.host", "smtp.mailgun.org");
            properties.setProperty("mail.user", DBConnectivity.MailUsername);
            properties.put("mail.smtp.auth", "true");
            properties.setProperty("mail.password", "071a267a02fb6cc7ba86bf080731747c");
            properties.setProperty("mail.port", "2525");
            Session session = Session.getDefaultInstance((Properties)properties, (Authenticator)new Authenticator(){

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(DBConnectivity.MailUsername, "071a267a02fb6cc7ba86bf080731747c");
                }
            });
            try {
                MimeMessage message = new MimeMessage(session);
                Transport transport = session.getTransport();
                transport.connect();
                message.setFrom((Address)new InternetAddress(DBConnectivity.MailUsername));
                message.setRecipients(Message.RecipientType.TO, (Address[])InternetAddress.parse((String)DBConnectivity.MailTo));
                message.setRecipients(Message.RecipientType.CC, (Address[])InternetAddress.parse((String)DBConnectivity.MailCC));
                message.setRecipients(Message.RecipientType.BCC, (Address[])InternetAddress.parse((String)DBConnectivity.MailBCC));
                message.setSubject(Subject + ": " + date);
                message.setSentDate(new Date());
                String mailfinalresult = "";
                mailfinalresult = "<FONT COLOR=BLACK FACE=\"Verdana\" SIZE=2>Hi All,<br></FONT><br><FONT COLOR=BLACK FACE=\"Verdana\" SIZE=2>" + Description + "</FONT>" + "<br><FONT COLOR=BLACK FACE=\"Verdana\" SIZE=2> Total Count of " + DBConnectivity.New_OutputTable + ": " + Count + "</FONT>" + "<br><FONT COLOR=BLACK FACE=\"Verdana\" SIZE=2><br>Regards,<br>EMAC - ATD Tech Team.</FONT>";
                MimeMultipart multipart = new MimeMultipart("related");
                MimeBodyPart messagePart = new MimeBodyPart();
                messagePart.setText(mailfinalresult + "<br>", "UTF-8", "html");
                multipart.addBodyPart((BodyPart)messagePart);
                message.setContent((Multipart)multipart);
                try {
                    Transport.send((Message)message);
                    transport.close();
                    System.out.println("Mail Notification Sent");
                }
                catch (Exception e) {
                    System.out.println("Error while sending Mail!\n" + e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            catch (MessagingException e) {
                System.out.println("Error occurs while sending mail \n" + e.getMessage());
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

