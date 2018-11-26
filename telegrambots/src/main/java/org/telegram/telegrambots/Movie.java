package org.telegram.telegrambots;

public class Movie
{
    public Movie() {

    }
    public Movie(String movieid ,String movieType , String movieName , double startYear , double runtime , String photo , String trailer)
    {
        this.movieType = movieType;
        movieID = movieid;
        populerTitle = movieName;
        this.startYear = startYear;
        this.runtime = runtime;
        this.photo = photo;
        this.trailer = trailer;
    }

    String movieID;
    String movieType;
    String populerTitle;
    String originalTItle;
    double isAdult = 0;
    double startYear;
    double endYear;
    double runtime;
    String photo;
    String trailer;
    double rating = 3;


}
