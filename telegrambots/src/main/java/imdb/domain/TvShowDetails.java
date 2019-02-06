package imdb.domain;

import java.util.List;

public class TvShowDetails {

   public List<String> creators;
   public List<String> seasons;

   public TvShowDetails( List<String> seasons, List<String> creators) {

      this.seasons = seasons;
      this.creators = creators;
   }
}
