package imdb.parsers;

import imdb.constants.IMDBConstants;
import imdb.domain.SearchResult;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        return IMDBConstants.ROOT_URL + "/title/" + movie_id;
    }
    public static List<SearchResult> SearchResults(Properties properties , Elements document) {
        List<SearchResult> results = new ArrayList<>();
        SearchResult r ;
        for (int i =0; i < document.size() ; i++) {
            r = new SearchResult();
            r.id = getMovieId(document.get(i).getElementsByAttribute("href").first());
            r.name = document.get(i).text().substring(0 , document.get(i).text().indexOf("(") -1);
            r.year = Integer.parseInt(document.get(i).text().substring(r.name.length() + 2 ,r.name.length() + 6));
            r.type = getMovieType(properties , document.get(i));

            results.add(r);
        }
        return results;
    }

    public static SearchResult SearchResult(Properties properties, Elements document, int number) {
        SearchResult result = new SearchResult();
        if(document.size() <= number)
            return null;
        result.id = getMovieId(document.get(number).getElementsByAttribute("href").first());
        result.type = getMovieType(properties , document.get(number));
        if(result.type != "TV Episode")
            result.name = document.get(number).text().substring(0 , document.get(number).text().indexOf("(") -1);
        else {
            result.name = document.get(number).text();
            return result;
        }
        for(int i = result.name.length() + 1; i < document.get(number).text().length();i++)
        {
            if(document.get(number).text().charAt(i) == '(')
            {
                if(document.get(number).text().charAt(i + 1) == '1' || document.get(number).text().charAt(i + 1) == '2')
                    result.year = Integer.parseInt(document.get(number).text().substring(i+1 ,i + 5));
            }
        }

         return result;
    }

}
