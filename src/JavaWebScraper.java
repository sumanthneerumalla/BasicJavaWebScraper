import sun.misc.IOUtils;
import sun.misc.Queue;

//import org.apache.commons.io.IOUtils;


import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;
//

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

    public static Vector<String> parsePage(String urlToRead) throws Exception {
        Vector<String> urlVector = new Vector<String>();
//        String urlRegExp = "(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})";
        String anchorRegExp = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
        String urlRegExp = "\"(.*)\"";
//        String urlRegExp = "(<a.*?>.*?</a>)";
        Pattern anchorPattern = Pattern.compile(anchorRegExp);
        Matcher anchorMatcher = anchorPattern.matcher(getHTML(urlToRead));

        Pattern urlPattern = Pattern.compile(urlRegExp);


        while (anchorMatcher.find()) {

            String nextAnchor = anchorMatcher.group();
            Matcher urlMatcher = urlPattern.matcher(nextAnchor);
            if (urlMatcher.find()) {
                urlVector.add(urlMatcher.group(1));
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

    public static boolean isValidURL(String someURL){
        try{
        getHTML(someURL);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String startingUrl = args[0];

        //Set of sites to visit. New anchor tags are added
        HashSet<String> sitesToVisit = new HashSet<String>();
        sitesToVisit.add(startingUrl);


        // set of examined web pages
        HashSet<String> alreadyVisted = new HashSet<String>();

//        String webpage = Web.getWeb(startingUrl);
        System.out.println(startingUrl);
        System.out.println(getURLSize(startingUrl));
        System.out.println(getHTML(startingUrl));

        String nextUrl;
        int numVisited = 0;
        int totalBytes = 0;
        while (!sitesToVisit.isEmpty() &&numVisited<50 && totalBytes<1000000 ) {

            //get the next url by turning the set into an array and grabbing the first item from the list
            String[] sitesToVisitArray = sitesToVisit.toArray(new String[sitesToVisit.size()]);
            nextUrl = sitesToVisitArray[0];
            

            //remove the most recent url from the set
            sitesToVisit.remove(nextUrl);
            System.out.print("size of set is now:");
            System.out.println(sitesToVisit.size());

            //get a vector of urls at the most recent url and turn it into an array
            Vector<String> urlVector = parsePage(nextUrl);
            String[] arrayOfNextUrls = urlVector.toArray(new String[urlVector.size()]);

            int counter = 0;
            //add urls from the recent urls only while the queue of sites to visit is less than 15
            //or until the new vector
            while( sitesToVisit.size()<=15 && counter<arrayOfNextUrls.length){
                String urlCandidate ;
                urlCandidate = arrayOfNextUrls[counter];
                if(isValidURL(urlCandidate) && !alreadyVisted.contains(urlCandidate) && !sitesToVisit.contains(urlCandidate)){
                    sitesToVisit.add(urlCandidate);
                }
                counter++;
            }

       }

        System.out.println(sitesToVisit.size());

    }
}
