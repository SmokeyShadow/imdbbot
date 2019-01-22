package nl.stil4m.imdb.parsers;
import nl.stil4m.imdb.constants.IMDBConstants;
import nl.stil4m.imdb.domain.SearchResult;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class MovieResultParser {

    public static String getMovieId( Element document) {
        String href = document.getElementsByTag("a").get(0).attr("href");
        href = href.substring(0, href.indexOf("?"));
        if (href.endsWith("/")) {
            href = href.substring(0, href.length() - 1);
        }
        href = href.substring(href.lastIndexOf("/") + 1);
        return href;
    }
    public static String getMovieType(Properties properties , Element document) {

        if(document.text().contains("Episode"))
        {
            return "TV Episode";
        }
        else if(document.text().contains("Series"))
        {
            return "TV Series";
        }
        else
        {
            return "Movie";
        }
    }


    public static String GetMovieImdbLink( String movie_id) {
        return IMDBConstants.ROOT_URL + "/title" + movie_id;
    }
    public static List<SearchResult> SearchResults(Properties properties , Elements document) {
        List<SearchResult> results = new ArrayList<>();
        SearchResult r ;
        for (int i =0; i < document.size() ; i++) {
            r = new SearchResult();
            r.id = getMovieId(document.get(i).getElementsByAttribute("href").first());;
            r.name = document.get(i).text().substring(0 , document.get(i).text().indexOf("(") -1);
            r.year = Integer.parseInt(document.get(i).text().substring(r.name.length() + 2 ,r.name.length() + 6));
            r.type = getMovieType(properties , document.get(i));
            results.add(r);
        }
        return results;
    }

}
