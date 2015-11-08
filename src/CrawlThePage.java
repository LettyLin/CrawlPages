import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by LinLi on 2015/11/4.
 */
public class CrawlThePage implements Runnable{

    private static String[] urlStr;
    private static String[] name;
    private static String[] number;
    private static String[] email;
    private static String[] area;
    private static String[] introduction;

    public static int count = 0;

    public CrawlThePage() throws IOException
    {
        Crawler();

        final int SIZE = urlStr.length;
        number = new String[SIZE];
        email = new String[SIZE];
        area = new String[SIZE];
        introduction = new String[SIZE];
    }
    public static void Crawler() throws IOException {
        List<String> urlList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        String mainURL = "http://www.wpi.edu/academics/cs/research-interests.html";
        Document doc = Jsoup.connect(mainURL).get();
        Elements divs = doc.getElementsByClass("half");

        for(Element div:divs) {
            Elements as = div.getElementsByTag("a");
            for(Element a:as)
            {
                String link = a.attr("href");
                String a_name =a.text();
                if(a!=null&&link != null&&link!=""&&a_name != null&& a_name!=""){
                    urlList.add(link);
                    nameList.add(a_name);
                }
            }
        }
        int size = urlList.size();
        urlStr = new String[size];

        int i=0;
        for(String temp:urlList) {
            urlStr[i++] = temp;
        }
        name = new String[size];

        int j=0;
        for(String temp:nameList)
        {
            name[j++] = temp;
        }
    }

    public static synchronized void CrawlInfo(String url[]) throws IOException {
        for(;count<url.length;count++) {
            String nowURL = url[count];
            Document doc = Jsoup.connect(nowURL).get();
            Elements info = doc.getElementById("contactinfo").getElementsByTag("p");

            Pattern pattern1 = Pattern.compile("\\+1-[0-9]{3}-[0-9]{3}-[0-9]{4}");
            Matcher matcher1 = pattern1.matcher(info.text());
            if (matcher1.find()) {
                number[count] = matcher1.group(0);
            }

            Pattern pattern2 = Pattern.compile("[a-z]+@[a-z]+\\.edu");
            Matcher matcher2 = pattern2.matcher(info.text());
            while (matcher2.find()) {
                email[count] = matcher2.group();
            }

            System.out.println(urlStr[count] + "\n" + name[count] + "\n" + number[count] + "\n" + email[count]);
        }

    }

    @Override
    public void run() {
        try {
            CrawlInfo(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        CrawlThePage crawlThePage = new CrawlThePage();
        Thread t1 = new Thread(crawlThePage);
        Thread t2 = new Thread(crawlThePage);
        Thread t3 = new Thread(crawlThePage);
        Thread t4 = new Thread(crawlThePage);
        Thread t5 = new Thread(crawlThePage);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

