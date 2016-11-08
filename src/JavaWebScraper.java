import sun.misc.Queue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;


public class JavaWebScraper {
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
        System.out.println(getHTML(startingUrl));
    }
}
