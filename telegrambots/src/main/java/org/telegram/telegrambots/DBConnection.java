package org.telegram.telegrambots;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection
{
    private static DBConnection instance;
    Connection con;
    private DBConnection()
    {
        CreateDB();
    }
    public static DBConnection GetInstance()
    {
        if(instance == null)
            instance = new DBConnection();
        return instance;
    }
    public static Connection GetConnection()
    {
        return instance.con;
    }
    public void CreateDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/imdb_db", "root", "mazmaz22");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public boolean InsertFavMovie(String movie_id , int user_id)
    {
        String q = "INSERT INTO user_fav_movies (user_id  , movie_id)  VALUES ( ? , ?)";
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            preparedStatement = con.prepareStatement(q);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, movie_id);
            preparedStatement.execute();
        } catch (SQLException e1) {
            System.out.println("Insert Fav Movies" + e1);
            result = false;
        }
        return result;
    }
    public List<Movie> GetTopMovies(int count)
    {
        List<Movie> results = new ArrayList<>();
        String q = "SELECT * FROM movie order by rating desc LIMIT " + count;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(q);
            ResultSet r = preparedStatement.executeQuery(q);
            Movie m ;
            while (r.next()) {
                m = new Movie(r.getString(1) , r.getString(2) , r.getString(3) , r.getDouble(6) , r.getDouble(8)
                        ,r.getString(10) , r.getString(11));
                m.rating = r.getDouble(12);
                results.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Failed with errors");
            e.printStackTrace();
        }
        return results;
    }
    public boolean InsertNewMovie(long user_id , String movieTitle , int startYear , int runttime , String genres , double rate , int voteNumbers)
    {
        String q = "INSERT INTO suggested_movie (user_id  , populer_title , start_year , runtime_minutes , genres , rate , vote_nums)  VALUES ( ? , ?, ?, ? , ? , ? ,?)";
        PreparedStatement preparedStatement = null;
        boolean result = true;
        try {
            preparedStatement = con.prepareStatement(q);
            preparedStatement.setInt(1, (int) user_id);
            preparedStatement.setString(2, movieTitle);
            preparedStatement.setInt(3, startYear);
            preparedStatement.setInt(4, runttime);
            preparedStatement.setString(5, genres);
            preparedStatement.setDouble(6, rate );
            preparedStatement.setInt(7 , voteNumbers);
            preparedStatement.execute();
        } catch (SQLException e1) {
            result = false;
        }
        return result;
    }
    public double[] GetMovieRatingInfo(String movie_id)
    {
        String q = "SELECT average_rating , vote_nums FROM rating WHERE id =" + "'" + movie_id + "'";
        PreparedStatement preparedStatement = null;
        double [] results = new double[10];
        try {
            preparedStatement = con.prepareStatement(q);
            ResultSet r = preparedStatement.executeQuery(q);
            ResultSetMetaData rsmd = r.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (r.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.println("info " + r.getDouble(i));
                    results[i-1] = r.getDouble(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;

    }
    public boolean IsNewUser(long user_id)
    {
        String q = "SELECT user_id FROM  user WHERE user_id =" + user_id;
        PreparedStatement preparedStatement = null;
        boolean newUser = true;
        try {
            preparedStatement = con.prepareStatement(q);
            ResultSet r = preparedStatement.executeQuery(q);
            ResultSetMetaData rsmd = r.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (r.next()) {
                newUser = false;
                for (int i = 1; i <= columnsNumber; i++) {

                    System.out.print(r.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;

    }
    public List<Movie> SearchMovies(String movieName)
    {
        List<Movie> results = new ArrayList<>();
        String q = "SELECT * FROM  movie WHERE populer_title =" + "'"+movieName+"'";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(q);
            ResultSet r = preparedStatement.executeQuery(q);
            ResultSetMetaData rsmd = r.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            Movie m;
            while (r.next()) {
                m = new Movie(r.getString(1) , r.getString(2) , r.getString(3) , r.getDouble(6) , r.getDouble(8)
                        ,r.getString(10) , r.getString(11));

                results.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    public List<Movie> GetFavMovies(int user_id)
    {
        List<Movie> results = new ArrayList<>();
        String q = "SELECT * FROM movie where movie.movie_id in (SELECT movie_id FROM user_fav_movies where user_fav_movies.user_id ="+ user_id + ")";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(q);
            ResultSet r = preparedStatement.executeQuery(q);
            Movie m ;
            while (r.next()) {
                m = new Movie(r.getString(1) , r.getString(2) , r.getString(3) , r.getDouble(6) , r.getDouble(8)
                ,r.getString(10) , r.getString(11));

                results.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean InsertUser(Message msg)
    {
        long chat_id = msg.getChatId();
        String first_name = msg.getChat().getFirstName();
        String last_name = msg.getChat().getLastName();
        String user_name = msg.getChat().getUserName();
        boolean result = false;
        try {
            String q = "INSERT INTO user (user_id , user_name , family_name , user_username , user_sex , user_location)  VALUES ( ? , ?, ?, ? , ? ,?)";
            PreparedStatement preparedStatement = con.prepareStatement(q);
            preparedStatement.setInt(1, (int) chat_id);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, user_name);
            preparedStatement.setInt(5, 1);
            preparedStatement.setString(6, "Tehran");
            result = preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(e);
        }
        return  result;
    }
    public boolean DeleteUser(int user_id)
    {
        return true;
    }
}
