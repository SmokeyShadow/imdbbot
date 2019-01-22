package nl.stil4m.imdb.domain;

import java.util.List;

@lombok.Setter(lombok.AccessLevel.NONE)
@lombok.Data
public class TvEpisodeDetails extends Details{
    public String episodeName;
    public long seasonNumber;
    public long episodeNumber;

    public TvEpisodeDetails(String episodeName, long seasonNumber, long episodeNumber) {
        this.episodeName = episodeName;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }
}
