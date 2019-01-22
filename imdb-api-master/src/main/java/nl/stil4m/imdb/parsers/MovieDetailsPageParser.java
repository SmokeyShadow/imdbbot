package nl.stil4m.imdb.parsers;
import nl.stil4m.imdb.constants.IMDBConstants;
import nl.stil4m.imdb.domain.MovieDetails;
import nl.stil4m.imdb.util.ElementUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MovieDetailsPageParser implements Parser<MovieDetails> {

    private static final String IMAGE = "MovieDetailsPageParser.image";
    private static final String CATEGORIES = "MovieDetailsPageParser.categories";
    private static final String DESCRIPTION = "MovieDetailsPageParser.description";
    private static final String RATING = "MovieDetailsPageParser.rating";
    private static final String WRITERS = "MovieDetailsPageParser.writers";
    private static final String DIRECTORS = "MovieDetailsPageParser.directors";
    private static final String YEAR = "MovieDetailsPageParser.year";
    private static final String NAME_TITLE_HEADER = "MovieDetailsPageParser.nameTitleHeader";
    private static final String STARS = "MovieDetailsPageParser.stars";
    private static final String DURATION = "MovieDetailsPageParser.duration";
    private static final String TRAILER = "MovieDetailsPageParser.trailer";
    private static final String VOTES = "MovieDetailsPageParser.votes";
    private final ElementUtil elementUtil;
    private final Properties properties;


    public MovieDetailsPageParser(ElementUtil elementUtil, Properties properties) {
        this.elementUtil = elementUtil;
        this.properties = properties;
    }

    @Override
    public MovieDetails parse(Element document) {
        String movieName = parseMovieName(document);
        Integer year = parseMovieYear(document);
        String description = parseDescription(document);
        Double rating = parseRating(document);
        List<String> directors = parseDirectors(document);
        List<String> writers = parseWriters(document);
        List<String> stars = parseStars(document);
        List<String> categories = parseCategories(document);
        String image = parseImage(document);
        String duration = parseRuntime(document);
        String trailer = parseTrailer(document);
        int votes = parseVotes(document);
        return new MovieDetails(movieName, year, description, rating, directors, writers, stars, categories, image , trailer , votes , duration );
    }
    private String parseTrailer(Element document) {
        return IMDBConstants.ROOT_URL + document.select(properties.get(TRAILER).toString()).attr("href");
    }
    private String parseRuntime(Element document) {

       return document.select(properties.get(DURATION).toString()).text();

    }

    private int parseVotes(Element document) {
        String voteText = document.select(properties.get(VOTES).toString()).text();
        if(voteText.length() <= 0)
            return -1;
        String strNew = voteText.replace(",", "");
        return Integer.parseInt(strNew.toString());
    }

    private String parseImage(Element document) {
        return document.select(properties.get(IMAGE).toString()).attr("src");
    }

    private List<String> parseCategories(Element document) {
        List<String> categories = new ArrayList<>();
        Elements elements = document.select(properties.get(CATEGORIES).toString());
        for (Element e : elements) {
                if(e.attr("href").contains("release"))
                break;
            categories.add(e.text());
        }
        return categories;
    }

    private String parseDescription(Element document) {
        return document.select(properties.get(DESCRIPTION).toString()).text();
    }

    private double parseRating(Element document) {
        Elements elements = document.select(properties.get(RATING).toString());
        if (elements.text().trim().length() > 0) {
            return Double.parseDouble(elements.text());
        } else {
            return -1;
        }
    }

    private List<String> parseWriters(Element document) {
        List<String> writers = new ArrayList<>();
        Elements elements = document.select(properties.get(WRITERS).toString());
        for (Element e : elements) {
            writers.add(e.text());
        }
        return writers;
    }

    private List<String> parseDirectors(Element document) {
        List<String> directors = new ArrayList<>();
        Elements elements = document.select(properties.get(DIRECTORS).toString());
        for (Element e : elements) {
            if(e.text().contains("full cast"))
                break;
            directors.add(e.text());
        }
        return directors;
    }

    private List<String> parseStars(Element document) {
        List<String> stars = new ArrayList<>();
        Elements elements = document.select(properties.get(STARS).toString());
        for (Element e : elements) {
            if(e.text().contains("full cast"))
                break;
            stars.add(e.text());
        }
        return stars;
    }

    private String parseMovieName(Element document) {
        Elements headers = document.select(properties.get(NAME_TITLE_HEADER).toString());
        for (Element headline : headers) {
            return headline.text();
        }
        return "No Name";
    }

    private Integer parseMovieYear(Element document) {
        String yearString = document.select(properties.get(YEAR).toString()).text();

        if(yearString.length() > 0) {
            yearString = yearString.substring(1, 5);
            return Integer.parseInt(yearString);
        }
        return -1;
    }

}
