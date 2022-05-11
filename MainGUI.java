

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainGUI
{
    String videoID;
    File searchHistory;
    String title;
    
    JFrame mainFrame;
    JTextField search;
    JButton save;
    // opens the saved songs
    JButton history;
    JPanel bottomPanel;
    
    JPanel centerPanel;
    JLabel nowPlaying;
    JLabel titleLabel;
    JLabel artist;
    
    public MainGUI() {
        // creates the SearchHistory.txt file
        createFile();
        mainFrame = new JFrame("PolyPhonic");
        mainFrame.setLayout(new BorderLayout());
        
        search = new JTextField("Search");
        save = new JButton("Save");
        history = new JButton("Library");
        bottomPanel = new JPanel();
        bottomPanel.add(save);
        bottomPanel.add(history);
        
        // centerPanel has all the song information
        nowPlaying = new JLabel("Now Playing:");
        titleLabel = new JLabel("Title");
        artist = new JLabel("Artist");
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3,1));
        centerPanel.add(nowPlaying);
        centerPanel.add(titleLabel);
        centerPanel.add(artist);
        
        search.addActionListener(new TextFieldListener());
        save.addActionListener(new SaveButtonListener());
        history.addActionListener(new HistoryButtonListener());

        mainFrame.add(bottomPanel, BorderLayout.SOUTH);
        mainFrame.add(search, BorderLayout.NORTH);
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300,300);
        mainFrame.setVisible(true);
    }
    

    public void openYoutube(String videoID) {
        try {
            // open this link on your desktop
            Desktop.getDesktop().browse(new URL("https://www.youtube.com/watch?v=" + videoID).toURI());
        } catch (MalformedURLException e) {
            // in case the video ID is invalid or YouTube changes its url format for some reason
            System.out.println("Invalid video ID");
        } catch (IOException e) {
            // I don't know how this would happen
            e.printStackTrace();
        } catch(URISyntaxException e) {
            // or this
            e.printStackTrace();
        }
    }
    
    public void save() {
        // saves the information to SearchHistory.txt
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            FileWriter writer = new FileWriter(searchHistory, true);
            // saves "date/time; title; link"
            writer.append(time.format(formatter) + "; " + title + " https://www.youtube.com/watch?v=" + videoID + "\n");
            writer.close();
        } catch (IOException e) {
            // This could happen if the user deletes the file in the middle of a session
            System.out.println("Search History file not found. Try restarting the program.");
        }
        // the file won't update if it's already open when something is saved
        // I tried to make it so it would close and reopen but to my knowledge
        // that's not possible
        try {
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Cannot open file becauses that feature is not supported.");
            } else {
                Desktop desktop = Desktop.getDesktop();
                if (searchHistory.exists()) {
                    // opens the file when something is saved
                    desktop.open(searchHistory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createFile() {
        // called on startup
        try {
            // sets the searchHistory variable to a File called SearchHistory.txt
            searchHistory = new File("SearchHistory.txt");
            // creates a new file if "SearchHistory.txt" doesn't already exist
            searchHistory.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("PolyPhonic does not have access to the required destination");
        }
        
    }
    
    class SaveButtonListener implements ActionListener {
       @Override
       public void actionPerformed(ActionEvent event) {
          save();
       }
    }
    class TextFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            // creates a new YoutubeSearch object with the text from the search bar
            YoutubeSearch s = new YoutubeSearch(search.getText());
            // sets the video ID and opens it in a browser
            videoID = s.getVideoID();
            openYoutube(videoID);
            // sets the artist and title if they are in the "artist - title" format
            // it doesn't actually matter which order they are in because either way they are going on seperate lines
            titleLabel.setText(s.getTitle()[0]);
            artist.setText(s.getTitle()[1]);
            // this is the full title of the video
            title = s.getTitle()[2];
            centerPanel.revalidate();
        }
    }
    class HistoryButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            // opens SearchHistory.txt
            try {
                if (!Desktop.isDesktopSupported()) {
                    System.out.println("Cannot open file becauses that feature is not supported.");
                } else {
                    Desktop desktop = Desktop.getDesktop();
                    if (searchHistory.exists()) {
                        desktop.open(searchHistory);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
