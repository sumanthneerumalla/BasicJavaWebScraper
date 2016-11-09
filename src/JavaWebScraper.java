import sun.misc.IOUtils;
import sun.misc.Queue;

//import org.apache.commons.io.IOUtils;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;


public class JavaWebScraper {
    public static int getURLSize(String urlToRead) throws Exception {
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream pageStream = conn.getInputStream();

//        byte[] pageInBytes = IOUtils.toByteArray(pageStream);

        //alternative to IOUTILS

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = pageStream.read();
        while (reads != -1) {
            baos.write(reads);
            reads = pageStream.read();
        }
        byte[] pageInBytes = baos.toByteArray();

        return pageInBytes.length;


    }

    public static Vector<String> parsePage(String urlToRead) throws Exception{
        Vector<String> urlVector=new Vector<String>();
//        String urlRegExp = "(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})";
        String anchorRegExp = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
        String urlRegExp = "\"(.*)\"";
//        String urlRegExp = "(<a.*?>.*?</a>)";
        Pattern anchorPattern = Pattern.compile(anchorRegExp);
        Matcher anchorMatcher = anchorPattern.matcher(getHTML(urlToRead));

        Pattern urlPattern = Pattern.compile(urlRegExp);


        while(anchorMatcher.find()){

            String nextAnchor = anchorMatcher.group();
            urlVector.add(nextAnchor);
            System.out.println(nextAnchor);

            Matcher urlMatcher = urlPattern.matcher(nextAnchor);
            if (urlMatcher.find())
            {
                System.out.println(urlMatcher.group(1));
            }
        }
        System.out.println(urlVector.size());


        return urlVector;
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }


    public static void main(String[] args) throws Exception {
        String startingUrl = args[0];

        //queue of sites to visit. New anchor tags are added
        Queue<String> sitesToVisit = new Queue<String>();
        sitesToVisit.enqueue(startingUrl);

        // set of examined web pages
        HashSet<String> alreadyVisted = new HashSet<String>();

//        String webpage = Web.getWeb(startingUrl);
        System.out.println(startingUrl);
        System.out.println(getURLSize(startingUrl));
        System.out.println(getHTML(startingUrl));
        parsePage(startingUrl);
    }
}
