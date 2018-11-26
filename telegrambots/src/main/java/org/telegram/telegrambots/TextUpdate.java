package org.telegram.telegrambots;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class TextUpdate extends TelegramUpdate
{
    enum insertState  {InsertName , InsertYear , InsertRunTIme , Insertgenres , InsertRate };
    boolean addMovie = false;
    List<String> movieInfo = new ArrayList<String>();
    insertState addingState = insertState.InsertName;
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
        String answer = "";
        long chat_id = msg.getChatId();
        CheckNewUser(msg , chat_id);

        if (message_text.equals("/start")) {
            OnStartState(chat_id);
        }
        else if (message_text.equals("/keyboard")) {
            OnKeyBoardOpen(chat_id);
        } else if (message_text.equals("/hide")) {
            OnKeyBoardClose(chat_id);
        }
        else if(addMovie)
        {
            AddMovie(message_text , chat_id);
        }
        else if(message_text.equals("Add movie to IMDB"))
        {
          StartAdd(chat_id);
        }
        else if(message_text.equals("Show Top 20 IMDB movies"))
        {
            ShowTopMovies(20 , chat_id);

        }
        else if (message_text.startsWith("/search")) {
            SearchMMovie(message_text , chat_id);
        }
        else if(message_text.equals("Show my fav movies"))
        {
            ShowFavMovies(chat_id);
        }
      //  UpdateManager.GetInstance().login( chat_id , message_text, answer);
    }
    public void AddMovie(String message_text , long chat_id)
    {
        movieInfo.add(message_text);

        if (addingState == insertState.InsertYear)
        {
            SendMessage(chat_id , "Write movie's year:" );
            addingState = insertState.InsertRunTIme;
            return;
        }
        else if(addingState == insertState.InsertRunTIme)
        {
            SendMessage(chat_id , "Write movie's runTime : ");
            addingState =insertState.Insertgenres;
            return;
        }
        else if(addingState == insertState.Insertgenres)
        {
            SendMessage(chat_id , "Write movies genre :");
            addingState = insertState.InsertRate;
            return;
        }
        else if(addingState == insertState.InsertRate)
        {
            SendMessage(chat_id , "Add your prefered Rate : " );
            addingState = insertState.InsertName;

        }
        else if(addingState == insertState.InsertName)
        {
            if(DBConnection.GetInstance().InsertNewMovie(chat_id , movieInfo.get(0) , Integer.valueOf(movieInfo.get(1)) , Integer.valueOf(movieInfo.get(2)) , movieInfo.get(3),Double.valueOf(movieInfo.get(4)) , 1))
            {
                SendMessage(chat_id , "Insertion is Sucessfull , You will be notify after verifing movie content by admin.");
            }
            else
            {
                SendMessage(chat_id , "Insertion is not  Sucessfull , check the values you inserted");
            }
            movieInfo.clear();
            addMovie = false;
        }
    }
    public void OnKeyBoardClose(long chat_id)
    {
        SendMessage msg = new SendMessage()
                .setChatId(chat_id)
                .setText("Keyboard hidden");
        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        msg.setReplyMarkup(keyboardMarkup);
        UpdateManager.GetInstance().Execute(msg);
    }
    public void OnKeyBoardOpen(long chat_id)
    {
        String answer = "Here is your keyboard";
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(answer);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Show my fav movies");
        row.add("Show Top 20 IMDB movies");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Add movie to IMDB");
        row.add("Show my movie suggests status");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        UpdateManager.GetInstance().Execute(message);
    }
    public SendMessage SendMessage(long chatID , String answer)
    {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatID)
                .setText(answer);
        UpdateManager.GetInstance().Execute(message); // Sending our message object to user
        return  message;
    }
    public String GetAnswer(Movie m, long chat_id)
    {
        String answer = "";
        answer += "\uD83C\uDFAC " +m.populerTitle + "\n";
        double[] ratingResults = DBConnection.GetInstance().GetMovieRatingInfo(m.movieID);
        answer += " ⭐️IMDB rate : " + m.rating + "\n";
        answer += " vote numbers : " + ratingResults[1] + "\n";
        answer += " Year : " + m.startYear + "\n";
        answer += " Runtime : " +m.runtime + "\n";
        //answer += "genres : " + movieList.get(i). + "\n";
        answer += m.photo + "\n";
        SendMessage(chat_id ,answer);
        return answer;
    }
    void OnStartState(long chat_id)
    {
        String answer = "HOW-TO search films\n" +
                "-----------------------------\n" +
                "1️⃣ type '/search lost in translation'\n" +
                "2️⃣ type 'lost in translation'\n" +
                "\n" +
                "You can send messages with or without /search, they will show the same result.";
        UpdateManager.GetInstance().SendMessage(chat_id , answer);
    }
    void CheckNewUser(Message msg ,long userID)
    {
        if(DBConnection.GetInstance().IsNewUser(userID))
        {
            if(DBConnection.GetInstance().InsertUser(msg))
                System.out.println("Insert User : Successful");
            else
                System.out.println("Insert User : Error");
        }
    }
    void ShowTopMovies(int count , long chat_id)
    {
        String answer;
        List<Movie> movieList = DBConnection.GetInstance().GetTopMovies(count);
        if(movieList.size() >0) {
            for (int i = 0; i < movieList.size(); i++) {
                answer = GetAnswer(movieList.get(i), chat_id);
                SendMessage(chat_id , answer);
            }
        }
        else {
            answer = "UnSuccessful";
            SendMessage(chat_id ,answer);
        }
    }
    void StartAdd(long chat_id)
    {
        addMovie = true;
        if(addingState == insertState.InsertName) {
            SendMessage(chat_id, "Write movie's name:");
            addingState = insertState.InsertYear;
        }
    }
    void SearchMMovie(String message_text , long chat_id)
    {
        String answer;
        int c = "/search".length();
        while(message_text.length() > c && message_text.charAt(c) == ' ')
            c++;
        String movieName = message_text.substring(c);
        List<Movie> results = DBConnection.GetInstance().SearchMovies(movieName);
        if(results.size() > 0)
        {
            for (Movie m:results) {
                answer = GetAnswer(m,chat_id);
                UpdateManager.GetInstance().SendMessage(chat_id , answer);
                SendMessage trailerMSG = new SendMessage()
                        .setChatId(chat_id)
                        .setText(results.get(10) + "\n ");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Add Movie to your favs").setCallbackData("Add Movie to your favs ," + results.get(0)));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                trailerMSG.setReplyMarkup(markupInline);
                UpdateManager.GetInstance().Execute(trailerMSG); // Sending our message object to user
            }

        }
        else
        {
            answer = "movie not found!";
            UpdateManager.GetInstance().SendMessage(chat_id , answer);
        }

    }
    void ShowFavMovies( long chat_id)
    {
        String answer;
        List<Movie> movieList = DBConnection.GetInstance().GetFavMovies((int) chat_id);
        if(movieList.size() >0) {
            Movie m;
            for (int i = 0; i < movieList.size(); i++) {
                m = movieList.get(i);
                answer = GetAnswer(m , chat_id);
                UpdateManager.GetInstance().SendMessage(chat_id ,answer);
            }
        }
        else {
            answer = "there isn't any movie in your fav list.";
            UpdateManager.GetInstance().SendMessage(chat_id ,answer);
        }

    }

}
