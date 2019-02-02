package imdb;
import imdb.commands.TitleDetailsCommand;
import imdb.domain.MovieDetails;
import imdb.domain.TvEpisodeDetails;
import imdb.domain.TvShowDetails;
import imdb.parsers.MovieDetailsPageParser;
import imdb.parsers.TvEpisodeDetailsPageParser;
import imdb.parsers.TvShowDetailsPageParser;
import org.jsoup.nodes.Document;


public class IMDB {

    private final DocumentBuilder documentBuilder;
    private final MovieDetailsPageParser movieDetailsPageParser;
    private final TvShowDetailsPageParser tvShowDetailsPageParser;
    private final TvEpisodeDetailsPageParser tvEpisodeDetailsPageParser;

    public IMDB(DocumentBuilder documentBuilder, MovieDetailsPageParser movieDetailsPageParser, TvShowDetailsPageParser tvShowDetailsPageParser, TvEpisodeDetailsPageParser tvEpisodeDetailsPageParser) {
        this.documentBuilder = documentBuilder;
        this.movieDetailsPageParser = movieDetailsPageParser;
        this.tvShowDetailsPageParser = tvShowDetailsPageParser;
        this.tvEpisodeDetailsPageParser = tvEpisodeDetailsPageParser;
    }

    public MovieDetails getMovieDetails(String movieId) throws Exception {

        try {
            Document doc = documentBuilder.buildDocument(new TitleDetailsCommand(movieId));
            return movieDetailsPageParser.parse(doc);
        } catch (Exception e) {
            throw new Exception("Could not find movie details for id: '" + movieId + "'", e);
        }
    }

    public TvEpisodeDetails getTvEpisodeDetails(String episodeId) throws Exception {
        try {
            Document doc = documentBuilder.buildDocument(new TitleDetailsCommand(episodeId));
            return tvEpisodeDetailsPageParser.parse(doc);
        } catch (Exception e) {
            throw new Exception("Could not find episode details for id: '" + episodeId + "'", e);
        }
    }

    public TvShowDetails getTvShowDetails(String showId) throws Exception {
        try {
            Document doc = documentBuilder.buildDocument(new TitleDetailsCommand(showId));
            return tvShowDetailsPageParser.parse(doc);
        } catch (Exception e) {
            throw new Exception("Could not find show details for id: '" + showId + "'", e);
        }
    }
}