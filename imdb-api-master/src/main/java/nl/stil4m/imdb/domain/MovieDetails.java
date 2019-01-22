package nl.stil4m.imdb.domain;
import java.util.List;

@lombok.Setter(lombok.AccessLevel.NONE)
@lombok.Data
public class MovieDetails extends Details{

    public  String image;
    public String trailer;


    public MovieDetails(String movieName, Integer year, String description, Double rating, List<String> directors, List<String> writers, List<String> stars, List<String> categories, String image, String trailer, int votes, String duration) {
        this.movieName = movieName;
        this.year = year;
        this.description = description;
        this.rating = rating;
        this.directors = directors;
        this.writers = writers;
        this.stars = stars;
        this.categories = categories;
        this.image = image;
        this.trailer = trailer;
        this.votes = votes;
        this.duration = duration;
    }
}