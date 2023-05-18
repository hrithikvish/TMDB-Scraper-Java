import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class TMDBScrapper {

    Scanner sc = new Scanner(System.in);
    String url = "https://www.themoviedb.org/movie/38-eternal-sunshine-of-the-spotless-mind";
    String posterSrc, moviePosterLink, movieTitleString, movieTagLine, movieOverview;
    List<String> genreList = new ArrayList<String>();

    void Scrape() {
//        System.out.print("Enter the URL: ");
//        String url = sc.nextLine();

        try {
            Document doc = Jsoup.connect(url).get();

            Elements movieTitle = doc.select("div.title > *:first-child > a");
            Elements releaseYear = doc.select("div.title > *:first-child > span.release_date");
            Elements certification = doc.select("div.title > div.facts > span.certification");
            Elements moviePoster = doc.select("div.image_content > img.poster");
            Elements tagLine = doc.select("div.header_info > *.tagline");
            Elements overView = doc.select("div.header_info > div.overview > p");
            Elements genre = doc.select("div.title > div.facts > span.genres > a");

            for(Element data: movieTitle) {
                movieTitleString = data.text();
                System.out.println("Name: " + movieTitleString);
            }
            for(Element data: releaseYear) {
                System.out.println("Release: " + data.text());
            }
            for(Element data: certification) {
                System.out.println("Certification: " + data.text());
            }
            for(Element data: moviePoster) {
                posterSrc = data.attr("src");
                System.out.println("Movie Poster: " + posterSrc);
            }

            moviePosterLink = "http://image.tmdb.org/t/p/w500/" + extractPosterPath(posterSrc);
            System.out.println("Movie Poster (Link): " + moviePosterLink);

            for(Element data: tagLine) {
                movieTagLine = data.text();
                System.out.println("Tag Line: " + movieTagLine);
            }
            for(Element data: overView) {
                movieOverview = data.text();
                System.out.println("Overview: " + movieOverview);
            }
            System.out.print("Genres: ");
            for(Element data: genre) {
                genreList.add(data.text());
            }
            System.out.println(genreList);

//            loadPoster(moviePosterLink);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String extractPosterPath(String src) {
        if (src != null) {
            int lastIndex = src.lastIndexOf('/');
            if (lastIndex >= 0 && lastIndex < src.length() - 1) {
                return src.substring(lastIndex + 1);
            }
        }
        return "";
    }

    void loadPoster(String moviePosterLink) throws IOException {
        URL posterUrl = new URL(moviePosterLink);
        BufferedImage image = ImageIO.read(posterUrl);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label, BorderLayout.CENTER);
        frame.setTitle(movieTitleString);
        frame.setSize(imageWidth, imageHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}