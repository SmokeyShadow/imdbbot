package nl.stil4m.imdb.domain;

import java.util.Set;

@lombok.Setter(lombok.AccessLevel.NONE)
@lombok.Data
public class TvShowDetails extends Details{

   public Set<String> creators;
   public Set<String> seasons;
}
