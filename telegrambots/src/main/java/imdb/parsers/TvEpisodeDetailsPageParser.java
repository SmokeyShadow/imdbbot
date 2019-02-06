package imdb.parsers;

import imdb.domain.TvEpisodeDetails;
import org.jsoup.nodes.Element;

import java.util.Properties;

public class TvEpisodeDetailsPageParser implements Parser<TvEpisodeDetails> {

    private static final String EPISODE_NUMBER = "TvEpisodeDetailsPageParser.episodeNumber";
    private static final String SEASON_NUMBER = "TvEpisodeDetailsPageParser.seasonNumber";
    private static final String EPISODE_NAME = "TvEpisodeDetailsPageParser.episodeName";
    private final Properties properties;

    public TvEpisodeDetailsPageParser(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TvEpisodeDetails parse(Element document) {
        String episodeName = getEpisodeName(document);
        Long seasonNumber = getSeasonNumber(document);
        Long episodeNumber = getEpisodeNumber(document);
       return new TvEpisodeDetails(episodeName, seasonNumber, episodeNumber );
    }

    private Long getEpisodeNumber(Element document) {
        String episodeInfoText = document.select(properties.get(EPISODE_NUMBER).toString()).text();
        String episodeInfo = episodeInfoText.split(",")[1];
        return Long.parseLong(episodeInfo.replace("Episode", "").trim());
    }

    private Long getSeasonNumber(Element document) {
        String episodeInfoText = document.select(properties.get(SEASON_NUMBER).toString()).text();
        String seasonInfo = episodeInfoText.split(",")[0];
        return Long.parseLong(seasonInfo.replace("Season", "").trim());
    }

    private String getEpisodeName(Element document) {
        return document.select(properties.get(EPISODE_NAME).toString()).text();
    }



}
