package imdb.domain;

import java.util.List;


public class MovieDetails extends Details {

    public  String image;


    public MovieDetails(String movieName, Integer year, String description, float rating, List<String> directors, List<String> writers, List<String> stars, List<String> categories, String image, String trailer, int votes, String duration, String awards) {
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
        this.awards = awards;
    }
}