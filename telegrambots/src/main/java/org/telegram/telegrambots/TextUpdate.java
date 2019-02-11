package org.telegram.telegrambots;
import imdb.constants.IMDBConstants;
import imdb.domain.MovieDetails;
import imdb.domain.SearchResult;
import imdb.domain.TvShowDetails;
import imdb.parsers.MovieResultParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
public class TextUpdate extends TelegramUpdate
{
    List<String> movieInfo = new ArrayList<String>();
    private static TextUpdate instance;
    public static TextUpdate GetInstance()
    {
        if(instance == null)
            instance = new TextUpdate();
        return instance;
    }
    @Override
    public void OnNewUpdate(Message msg) {
        String message_text = msg.getText();
        long chat_id = msg.getChatId();
        String answer = "";
        if (message_text.equals("/start")) {
            answer = OnStartState(chat_id);
        }
        else if (message_text.equals("/keyboard")) {
            answer = OnKeyBoardOpen(chat_id);
        } else if (message_text.equals("/hide")) {
            answer =OnKeyBoardClose(chat_id);
        }
        else if(message_text.equals("Show Top Box Office"))
        {
            answer =ShowTopBoxOffice(chat_id);

        }
        else if (message_text.startsWith("/search")) {
            answer =SearchMMovie(message_text , chat_id , 0);
        }
        else
        {
            answer = SearchMMovie("/search" + message_text , chat_id , 0);
        }
        UpdateManager.GetInstance().login(chat_id , message_text , answer);
    }
    public String ShowTopBoxOffice(long chat_id)
    {
        Document doc = null;
        try {
             doc = Jsoup.connect(IMDBConstants.IMDB_BOXOFFICE).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select(IMDBConstants.IMDB_BF_ELEMNTS);
        return AddBFELElements(elements , chat_id);

    }
    public String AddBFELElements(Elements elements , long chat_id)
    {
        String answer = null;
        answer = "";
        Elements tds;
        Elements a;
        for(int i =0;i < elements.size();i++)
        {
            tds = elements.get(i).select("td");
            a = tds.get(1).select("a");
            answer += (i+1) + ". " + " <a href='"+ IMDBConstants.ROOT_URL + a.attr("href") +"'> " +  a.text()+ " </a>" + "  " +  tds.get(2).text() + "     "
            +tds.get(3).text()  + "   <b>" +  tds.get(4).text()  + "</b>\n\n";
        }
        UpdateManager.GetInstance().SendMessage(chat_id , answer , true);
        return answer;

    }
    public String OnKeyBoardClose(long chat_id)
    {
        SendMessage msg = new SendMessage()
                .setChatId(chat_id)
                .setText("Keyboard hidden");
        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        msg.setReplyMarkup(keyboardMarkup);
        UpdateManager.GetInstance().Execute(msg);
        return "Keyboard hidden";
    }
    public String OnKeyBoardOpen(long chat_id)
    {
        String answer = "Here is your keyboard";
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(answer);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
      //  row.add("Show my Collection");
        row.add("Show Top Box Office");
        keyboard.add(row);
        //row = new KeyboardRow();
      //  row.add("Suggest me a movie");
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        UpdateManager.GetInstance().Execute(message);
        return "Here is your keyboard";
    }
    public SendMessage SendMessage(long chatID , String answer)
    {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatID)
                .setText(answer);
        UpdateManager.GetInstance().Execute(message); // Sending our message object to user
        return  message;
    }


    public String GetAnswer(MovieDetails m , SearchResult result)
    {
        String answer = "";
        answer += "\uD83C\uDFA5 <b>" + result.name + "</b> (" + result.type + ")\n\n";
        if(m.categories != null  && m.categories.size() > 0) {
            answer += "\uD83D\uDD35 ";
            for (int i = 0; i < m.categories.size(); i++) {
                answer += "#" + m.categories.get(i) + (i != (m.categories.size() - 1) ? " , " : "\n");
            }
        }
        if(m.rating >= 0)
            answer += "⭐️IMDB rate : " + m.rating + "\n";
        if(m.votes >= 0)
            answer += " vote numbers : " + m.votes + "\n";
        answer += " Year : " + result.year + "\n";
        answer += " Runtime : " +m.duration + "\n\n";
        answer +=   m.description + "\n\n";
        if(m.awards != null)
            answer += "\uD83C\uDFC6 " + m.awards + "\n\n";
        answer += AddStringList(m.stars , "\uD83D\uDD74" , "Stars : " );
        answer +=AddStringList(m.directors , "\uD83C\uDFAC" , "Directors : ");
        answer +=AddStringList(m.writers , "✍️"  , "Writers : ");

            if(m.image.length() > 0 )
        answer +="<a href='"+m.image+"'> photo link </a>"  + "\n";

            return answer;
    }
    public String GetSeriesAnswer(MovieDetails details, TvShowDetails m, SearchResult result)
    {
        String answer = "";
        answer += "\uD83C\uDFA5 <b>" + result.name + "</b>  " + result.type + "\n\n";
        if(details.categories != null  && details.categories.size() > 0) {
            answer += "\uD83D\uDD35 ";
            for (int i = 0; i < details.categories.size(); i++) {
                answer += "#" + details.categories.get(i) + (i != (details.categories.size() - 1) ? " , " : "\n");
            }
        }
        if(details.rating >= 0)
            answer += "⭐️IMDB rate : " + details.rating + "\n";
        if(details.votes >= 0)
            answer += " vote numbers : " + details.votes + "\n";
        answer += " Year : " + result.year + "\n";
        answer += " Runtime : " +details.duration + "\n\n";
        answer +=   details.description + "\n\n";
        if(details.awards != null)
            answer += "\uD83C\uDFC6 " + details.awards + "\n\n";
        answer += AddStringList(details.stars , "\uD83D\uDD74"  , "Stars : ");
        answer +=AddStringList(m.creators , "\uD83C\uDFAC" , "Creators : ");
        answer +=AddStringList(m.seasons , " "  , "<b> Seasons : </b>");
        if(details.image.length() > 0 )
            answer +="<a href='"+details.image+"'> photo link </a>"  + "\n";

        return answer;

    }
    public String GetEpisideAnswer(MovieDetails details, SearchResult result)
    {
        String answer = "";
        answer += "\uD83C\uDFA5 <b>" + result.name + "</b>  \n\n";
        if(details.categories != null  && details.categories.size() > 0) {
            answer += "\uD83D\uDD35 ";
            for (int i = 0; i < details.categories.size(); i++) {
                answer += "#" + details.categories.get(i) + (i != (details.categories.size() - 1) ? " , " : "\n");
            }
        }
        if(details.rating >= 0)
            answer += "⭐️IMDB rate : " + details.rating + "\n";
        if(details.votes >= 0)
            answer += " vote numbers : " + details.votes + "\n";
        answer += " Runtime : " +details.duration + "\n\n";
        answer +=   details.description + "\n\n";
        if(details.awards != null)
            answer += "\uD83C\uDFC6 " + details.awards + "\n\n";
        answer += AddStringList(details.stars , "\uD83D\uDD74"  , "Stars : ");
        answer +=AddStringList(details.directors , "\uD83C\uDFAC" , "Directors : ");
        answer +=AddStringList(details.writers , "✍️"  , "Writers : ");
        if(details.image.length() > 0 )
            answer +="<a href='"+details.image+"'> photo link </a>"  + "\n";

        return answer;

    }
    String AddStringList(List<String> list  , String symbol, String cast)
    {
        String answer = "";
        if(list.size() > 0) {
            answer += symbol + cast;
            for (int i = 0; i < list.size(); i++) {
                answer +=list.get(i) + (i != (list.size() - 1) ? " , " : "\n\n");
            }
        }
        return  answer;
    }
    String OnStartState(long chat_id)
    {
        String answer = "HOW-TO search films\n" +
                "-----------------------------\n" +
                "1️⃣ type /search Harry Potter and the Goblet of Fire\n" +
                "2️⃣ type Harry Potter and the Goblet of Fire\n" +
                "\n" +
                "You can send messages with or without /search, they will show the same result.\n" +
                " For show/hide keyboard : \n" +
                "*️⃣ type '/keyboard \n" +
                "*️⃣ type '/hide \n";
        UpdateManager.GetInstance().SendMessage(chat_id , answer , true);
        return answer;
    }

    String GetImdbResultLink(String movie_name)
    {
        try {
            return IMDBConstants.ROOT_URL + "/find?ref_=nv_sr_fn&q=" + URLEncoder.encode(movie_name, "UTF-8") + "&s=tt";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    String SearchMMovie(String message_text , long chat_id , int number)
    {
        String answer = "";
        Document doc = null;
        int c = "/search".length();
        while(message_text.length() > c && message_text.charAt(c) == ' ')
            c++;
        if( message_text.substring(c) == " " || message_text.substring(c).trim().length() <= 0) {
            return OnStartState(chat_id);
        }
        String movieName = message_text.substring(c);
        try {
            doc = Jsoup.connect(GetImdbResultLink(movieName)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select(IMDBConstants.IMDB_Find);
        if(elements.size() <= 0)
        {
            answer = "movie not found!";
        }
        else {
                    SearchResult result = MovieResultParser.SearchResult(DBConnection.GetInstance().GetProperties() , elements , number);
                    if(result == null)
                        return "";
                    String imdbLink= MovieResultParser.GetMovieImdbLink(result.id);
                    SendMessage mainmessage = null;
                    MovieDetails details = null;
                    try {
                        details = DBConnection.GetInstance().GetImdb().getMovieDetails(result.id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(result.type.equals("Movie"))
                    {
                        answer = GetAnswer(details , result );
                        mainmessage = UpdateManager.GetInstance().SendMessage(chat_id , answer , false);
                        InlineKeyboardMarkup keyboard = CreateKeyBoard(number ,number  < elements.size() - 1 , imdbLink , result.id , movieName);
                        CheckTrailerAvailable(chat_id, mainmessage, details, keyboard);

                    }
                    else if(result.type.equals("TV Series")) // other movie types
                    {
                        TvShowDetails tvShowDetails = null;
                        try {
                            tvShowDetails = DBConnection.GetInstance().GetImdb().getTvShowDetails(result.id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        answer = GetSeriesAnswer(details  , tvShowDetails ,result );
                        mainmessage = UpdateManager.GetInstance().SendMessage(chat_id , answer , false);
                        InlineKeyboardMarkup keyboard = CreateKeyBoard(number ,number  < elements.size() - 1 , imdbLink ,result.id , movieName);
                        CheckTrailerAvailable(chat_id, mainmessage, details, keyboard);
                    }
                    else
                    {
                        answer = GetEpisideAnswer(details , result );
                        mainmessage = UpdateManager.GetInstance().SendMessage(chat_id , answer , false);
                        InlineKeyboardMarkup keyboard = CreateKeyBoard(number ,number  < elements.size() - 1 , imdbLink ,result.id , movieName);
                        mainmessage.setReplyMarkup(keyboard);
                        UpdateManager.GetInstance().Execute(mainmessage);

                    }


        }
        return answer;

    }

    private void CheckTrailerAvailable(long chat_id, SendMessage mainmessage, MovieDetails details, InlineKeyboardMarkup keyboard) {
        if(details.trailer != null) {
            SendMessage trailerMSG = CreateTrailerMessage(chat_id , details.trailer);
            trailerMSG.setReplyMarkup(keyboard);
            UpdateManager.GetInstance().Execute(mainmessage);
            UpdateManager.GetInstance().Execute(trailerMSG);
        }
        else
        {
            mainmessage.setReplyMarkup(keyboard);
            UpdateManager.GetInstance().Execute(mainmessage);
        }
    }

    InlineKeyboardMarkup CreateKeyBoard(int number ,boolean next , String imdbLink , String id , String movieName)
    {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        //rowInline.add(new InlineKeyboardButton().setText("Add to my Collection").setCallbackData("Add to my Collection ," +id));
        rowInline.add(new InlineKeyboardButton().setText(" \uD83C\uDF10 IMDB Link  \uD83C\uDF10 ").setUrl(imdbLink));
        if(next)
            rowInline.add((new InlineKeyboardButton().setText("next movie").setCallbackData("Next Movie" +movieName +','+ number)));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    SendMessage CreateTrailerMessage(long chat_id , String trailer )
    {
        SendMessage trailerMSG = new SendMessage();
        trailerMSG.setChatId(chat_id);
        trailerMSG.setText(trailer + "\n ");
        return trailerMSG;


    }


}
