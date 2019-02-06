package imdb.domain;


public class TvEpisodeDetails  {
    public String episodeName;
    public long seasonNumber;
    public long episodeNumber;
    public TvEpisodeDetails(String episodeName, long seasonNumber, long episodeNumber) {
        this.episodeName = episodeName;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

}
