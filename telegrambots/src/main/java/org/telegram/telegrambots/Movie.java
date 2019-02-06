package org.telegram.telegrambots;

import java.util.List;

public class Movie
{
    public Movie() {

    }
    public Movie(String movieid ,String movieType , String movieName , int startYear , String runtime , String photo , String trailer , float rating , String imdbLink)
    {
        this.movieType = movieType;
        movieID = movieid;
        populerTitle = movieName;
        this.startYear = startYear;
        this.runtime = runtime;
        this.photo = photo;
        this.trailer = trailer;
        this.rating = rating;
        this.imdbLink =  imdbLink;
    }

    public Movie(String movieID, String movieType, String populerTitle, int startYear, String runtime, String photo, String trailer, float rating, String imdbLink, int numberOfVotes, List<String> directors, List<String> writers, List<String> stars, String description , List<String> category , String awards) {
        this.movieID = movieID;
        this.movieType = movieType;
        this.populerTitle = populerTitle;
        this.startYear = startYear;
        this.runtime = runtime;
        this.photo = photo;
        this.trailer = trailer;
        this.rating = rating;
        this.imdbLink = imdbLink;
        this.numberOfVotes = numberOfVotes;
        this.directors = directors;
        this.writers = writers;
        this.stars = stars;
        this.description = description;
        this.genres = category;
        this.awards = awards;
    }

    String movieID;
    String movieType;
    String populerTitle;
    String originalTItle;
    double isAdult = 0;
    int startYear;
    String runtime;
    String photo;
    String trailer;
    float rating ;
    String imdbLink;
    int numberOfVotes;
    List<String> directors;
    List<String> writers;
    List<String> stars;
    String description;
    List<String> genres;
    String awards;



}
