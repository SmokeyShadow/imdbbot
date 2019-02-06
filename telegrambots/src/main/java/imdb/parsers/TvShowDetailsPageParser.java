package imdb.parsers;

import imdb.domain.TvShowDetails;
import imdb.util.ElementUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class TvShowDetailsPageParser implements Parser<TvShowDetails> {

    private static final String CREATORS = "TvShowDetailsPageParser.creators";
    private static final String SEASONS = "TvShowDetailsPageParser.seasons";


    private final Properties properties;
    private final ElementUtil elementUtil;

    public TvShowDetailsPageParser(Properties properties, ElementUtil elementUtil) {
        this.properties = properties;
        this.elementUtil = elementUtil;
    }

    @Override
    public TvShowDetails parse(Element document) {

        List<String> seasons = getSeasons(document);
        List<String> creators = getCreators(document);
        return new TvShowDetails( seasons, creators );
    }


    private List<String> getCreators(Element document) {
        List<String> creators = new ArrayList<>();
        Elements elements = document.select(properties.get(CREATORS).toString()).get(1).select("a");
        for (Element e : elements) {
            creators.add(e.text());
        }
        return creators;
    }

    private List<String> getSeasons(Element document)
    {
        List<String> seasons = new ArrayList<>();
        Elements elements = document.select(properties.get(SEASONS).toString()).get(2).select("a");

        for (Element e : elements) {
            seasons.add(e.text());
        }
        return seasons;
    }

}
