package imdb;

import imdb.parsers.MovieDetailsPageParser;
import imdb.parsers.TvEpisodeDetailsPageParser;
import imdb.parsers.TvShowDetailsPageParser;
import imdb.util.ElementUtil;
import imdb.util.impl.DefaultElementUtil;

import java.util.Properties;

public class IMDBFactory {

    public IMDB createInstance(Properties properties) {
        ElementUtil elementUtil = new DefaultElementUtil();
        MovieDetailsPageParser movieDetailsPageParser = new MovieDetailsPageParser(elementUtil, properties);
        TvEpisodeDetailsPageParser tvEpisodeDetailsPageParser = new TvEpisodeDetailsPageParser(properties);
        TvShowDetailsPageParser tvShowDetailsPageParser = new TvShowDetailsPageParser(properties, elementUtil);
        return new IMDB(new DocumentBuilder(), movieDetailsPageParser, tvShowDetailsPageParser, tvEpisodeDetailsPageParser);
    }

}
