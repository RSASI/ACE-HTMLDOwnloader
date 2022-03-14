package com.valgen.atd.main;

//import ArticleDownload_latest.PrepareDocument;
import static com.valgen.atd.main.Article_Download_Main.detector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Readability_Extraction {

    public static String Readability(String Content) {
        String CoreContent = "";
        try {
            PrepareDocument prepare = new PrepareDocument();
            Element article = prepare.createDOMTree(Content);
            CoreContent = article.toString();
            Element Title = prepare.getArticleTitle(Document.createShell((String) Content));
            CoreContent = CoreContent.replaceAll("(?sim)(<td\\s*class=\"tabTitleLeftWhite\">.*?</a>\\s*</td>\\s*</tr>\\s*</table>)", "");
            CoreContent = CoreContent.replaceAll("(?sim)(<[^<>]*script[^<>]*>.*?<[^<>]*>)", "");
            CoreContent = CoreContent.replaceAll("(?sim)(<pre\\s*(id|class)[^<>]*\"(cake|stack-trace)+[^<>]*>.*?</pre>)", "");
            if (CoreContent.length() < 100) {
                System.out.println("Readability Content null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CoreContent;
    }

    public static String Title(String Content) {
        String title = "";
        try {
            PrepareDocument prepare = new PrepareDocument();
            Element Title = prepare.getArticleTitle(Document.createShell((String) Content));
            title = Title.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    public static void main(String[] args) {
        try {
            String pageSource = Jsoup.connect((String) "http://zeenews.india.com/business/news/companies/indian-oil-stake-sale-full-subscribed-govt-bags-rs-9-300-crore_134435.html").timeout(30000).followRedirects(true).execute().parse().html();
            String test = Readability_Extraction.Readability(pageSource);
            System.out.println("Core Content " + test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public  static String DetectLanguage(String sourceContent) {
   String Language="";
       try {
                    detector.append(sourceContent);
                    Language = detector.detect();
                } catch (Exception var28) {
//                    url_exception = var28.getMessage();
                    var28.printStackTrace();
                    Language = "Not Found";
                }
       return Language;
   
   }
}
