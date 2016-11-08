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
    }
}
