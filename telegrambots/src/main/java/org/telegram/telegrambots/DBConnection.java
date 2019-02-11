package org.telegram.telegrambots;
import imdb.IMDB;
import imdb.IMDBFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection
{
    private static DBConnection instance;
    private IMDB imdb ;
   // private Connection con;
    private Properties properties;
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
    public void CreateDB()
    {
        try {

            try {
                InputStream inputStream =new ClassPathResource("nl/stil4m/imdb/parsing.properties").getInputStream();
                properties.load(inputStream);
            } catch (IOException e) {

            }

            imdb = new IMDBFactory().createInstance(properties);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public  Properties GetProperties()
    {
        return properties;
    }

    public IMDB GetImdb()
    {
        return imdb;
    }





}
