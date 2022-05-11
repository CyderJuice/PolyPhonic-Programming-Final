

import java.util.ArrayList;
import java.net.URLConnection;
import java.net.URL;
import java.util.Scanner;
import java.lang.*;
import java.io.IOException;

public class YoutubeSearch
{
   String url;

   String content;
   
   
   URLConnection connection;
   
   
   public YoutubeSearch(String keyword) {
       // url of the top result. made using (https://developers.google.com/youtube/v3/docs/search/list?apix=true)
       url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + keyword.replace(" ", "+") + "&key=AIzaSyBCCk7omNL4fys4577SekOIya4jnS3R0Bw";
       try {
           // opens a connection to the html page and copies it as a string ooga booga
           connection = new URL(url).openConnection();
           Scanner scanner = new Scanner(connection.getInputStream());
           scanner.useDelimiter("\\Z");
           content = scanner.next();
           scanner.close();
       } catch (IOException e) {
           System.out.println("Your search contains illegal characters!");
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public String getVideoID() {
       // if the search returns results
       if (content.contains("\"videoId\": ")) {
           String newContent = content.split("\"videoId\": ")[1];
           // takes the first 11 characters after "videoID":
           char[] charList = new char[11];
           String videoID = "";
           for (int i = 1; i < charList.length+1; i++) {
               charList[i-1] = newContent.toCharArray()[i];
           }
           for (char c: charList) {
               // assembles the videoID
               videoID = videoID + c;
           }
           return videoID;
       } else { // if the search doesn't return results
           System.out.println("No results");
           return null;
       }
   }
   public String[] getTitle() {
       // the whole title
       String all = "";
       // the title split by -
       // for if it's in "artist - title"
       String title = "";
       String artist = "";
       
       int i = 1;
       char[] contentCharList = content.split("\"title\": ")[1].toCharArray();
       ArrayList<Character> charList = new ArrayList<Character>();
       // adds characters until it reaches the end of the title (marked by ")
       while (Character.compare(contentCharList[i], '"') != 0) {
           charList.add(contentCharList[i]);
           i++;
       }
       for (char c: charList) {
           all = all + c;
       }
       // if the full title has a -
       if (all.indexOf("-") != -1) {
           // splits the full title by -
           artist = all.split("-")[1];
           artist = artist.substring(1);
           title = all.split("-")[0];
       } else {
           title = all;
       }
       // returns all three Strings in a list
       String[] list = new String[] {title, artist, all};
       return list;
   }
}
