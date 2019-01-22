package nl.stil4m.imdb.it;

import nl.stil4m.imdb.IMDB;
import nl.stil4m.imdb.IMDBFactory;
import nl.stil4m.imdb.constants.IMDBConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import static com.sun.org.apache.bcel.internal.util.SecuritySupport.getResourceAsStream;

public class Main
{


    public static  void main(String [] args) throws Exception {
        IMDB imdb ;
        Properties properties = new Properties();
        Document doc = null;
        try {
            doc = Jsoup.connect(IMDBConstants.ROOT_URL + "/find?ref_=nv_sr_fn&q=" + URLEncoder.encode("inglourious basterds", "UTF-8") + "&s=tt").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements newsHeadlines = doc.select("#main .findList tr");
        for (Element headline : newsHeadlines) {
          //  System.out.print(headline.text() +"\n" );
        }
        try {
            InputStream inputStream =getResourceAsStream("nl/stil4m/imdb/parsing.properties");
            properties.load(inputStream);
        } catch (IOException e) {

        }

        imdb = new IMDBFactory().createInstance(properties);

        System.out.println(imdb.getMovieDetails("tt2119543"));

    }
}
