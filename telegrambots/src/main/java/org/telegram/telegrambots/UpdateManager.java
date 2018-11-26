package org.telegram.telegrambots;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.lang.Math.toIntExact;

public class UpdateManager extends TelegramLongPollingBot
{
    private static UpdateManager instance;
    public static UpdateManager GetInstance()
    {
        if(instance == null)
            instance = new UpdateManager();
        return instance;
    }
    public static final String ADMIN_CHAT_ID = "@Atiehhhh_bot";
    boolean addMovie = false;
    enum insertState  {InsertName , InsertYear , InsertRunTIme , Insertgenres , InsertRate };
    insertState addingState = insertState.InsertName;
    List<String> movieInfo = new ArrayList<String>();
    @Override
    public void onUpdateReceived(Update update)
    {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            String answer = "";
            long chat_id = update.getMessage().getChatId();
            if(DBConnection.GetInstance().IsNewUser(chat_id))
            {
                if(DBConnection.GetInstance().InsertUser(update.getMessage()))
                    System.out.println("Insert User : Successful");
                else
                    System.out.println("Insert User : Error");
            }

            if (message_text.equals("/start")) {
                answer = "HOW-TO search films\n" +
                        "-----------------------------\n" +
                        "1️⃣ type '/search lost in translation'\n" +
                        "2️⃣ type 'lost in translation'\n" +
                        "\n" +
                        "You can send messages with or without /search, they will show the same result.";
                SendMessage(chat_id , answer);
            }
            else if (message_text.equals("/keyboard")) {
              OnKeyBoardOpen(chat_id);
            } else if (message_text.equals("/hide")) {
              OnKeyBoardClose(chat_id);
            }
            else if(addMovie)
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
                    addingState = insertState.Insertgenres;
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
                    System.out.println("size" + movieInfo.size());
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
            else if(message_text.equals("Add movie to IMDB"))
            {
                addMovie = true;
                if(addingState == insertState.InsertName) {
                    SendMessage(chat_id, "Write movie's name:");
                    addingState = insertState.InsertYear;
                }
            }
            else if(message_text.equals("Show Top 20 IMDB movies"))
            {
                List<Movie> movieList = DBConnection.GetInstance().GetTopMovies(20);
                if(movieList.size() >0) {
                    for (int i = 0; i < movieList.size(); i++) {
                        answer = "";
                        answer += "\uD83C\uDFAC " + movieList.get(i).populerTitle + "\n";
                        double[] ratingResults = DBConnection.GetInstance().GetMovieRatingInfo(movieList.get(i).movieID);
                        answer += " ⭐️IMDB rate : " + movieList.get(i).rating + "\n";
                        answer += " vote numbers : " + ratingResults[1] + "\n";
                        answer += " Year : " + movieList.get(i).startYear + "\n";
                        answer += " Runtime : " + movieList.get(i).runtime + "\n";
                        //answer += "genres : " + movieList.get(i). + "\n";

                        answer += movieList.get(9) + "\n";
                        SendMessage(chat_id ,answer);
                    }
                }
                else {
                    answer = "UnSuccessful";
                    SendMessage(chat_id ,answer);
                }

            }
            else if (message_text.startsWith("/search")) {
                int c = "/search".length();
                while(message_text.length() > c && message_text.charAt(c) == ' ')
                    c++;
                String movieName = message_text.substring(c);
                List<String> results = DBConnection.GetInstance().SearchMovies(movieName);
                if(results.size() > 0)
                {
                    answer += "\uD83C\uDFAC "+ results.get(2)+"\n";
                    double [] ratingResults = DBConnection.GetInstance().GetMovieRatingInfo(results.get(0));
                    answer += " ⭐️IMDB rate : " + ratingResults[0] +"\n";
                    answer += " vote numbers : " + ratingResults[1] + "\n";
                    answer += " Year : " + results.get(5) +"\n";
                    answer += " Runtime : " + results.get(7) + "\n";
                    answer += "genres : " + results.get(8) + "\n";
                    answer +=  results.get(9) + "\n";


                }
                else
                {
                    answer = "movie not found!";
                }
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText(answer);
                try {
                    execute(msg); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
                try {
                    execute(trailerMSG); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(message_text.equals("Show my fav movies"))
            {
                List<Movie> movieList = DBConnection.GetInstance().GetFavMovies((int) chat_id);
                if(movieList.size() >0) {
                    for (int i = 0; i < movieList.size(); i++) {
                        answer += "\uD83C\uDFAC" + movieList.get(i).populerTitle + "\n";
                        double[] ratingResults = DBConnection.GetInstance().GetMovieRatingInfo(movieList.get(i).movieID);
                        answer += " ⭐️IMDB rate : " + ratingResults[0] + "\n";
                        answer += " vote numbers : " + ratingResults[1] + "\n";
                        answer += " Year : " + movieList.get(i).startYear + "\n";
                        answer += " Runtime : " + movieList.get(i).runtime + "\n";
                        //answer += "genres : " + movieList.get(i). + "\n";
                        answer += movieList.get(i).photo + "\n";

                    }
                }
                else
                    answer = "there isn't any movie in your fav list.";
                SendMessage(chat_id ,answer);

            }
            login( chat_id , message_text, answer);
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Message contains photo
            // Set variables
            long chat_id = update.getMessage().getChatId();

            List<PhotoSize> photos = update.getMessage().getPhoto();
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            int f_height = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
            SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(f_id)
                    .setCaption(caption);
            try {
                execute(msg); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            if (call_data.startsWith("Add Movie to your favs")) {
                String movie_id = call_data.substring(call_data.lastIndexOf(",") + 1 , call_data.length());
                System.out.println("MOVIE IS" + movie_id);
                String a = "This movie added to your favorites";
                boolean result = DBConnection.GetInstance().InsertFavMovie(movie_id, (int)chat_id);
                System.out.println("Call back query succesfull"+ call_data + "reuslt" + result );
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(a);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void login( long user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from  (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }

    @Override
    public String getBotUsername() {
        // TODO
        return ADMIN_CHAT_ID;
    }
    public void OnKeyBoardClose(long chat_id)
    {
        SendMessage msg = new SendMessage()
                .setChatId(chat_id)
                .setText("Keyboard hidden");
        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        msg.setReplyMarkup(keyboardMarkup);
        try {
            execute(msg); // Call method to send the photo
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void OnKeyBoardOpen(long chat_id)
    {
        String answer = "Here is your keyboard";
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(answer);
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Show my fav movies");
        row.add("Show Top 20 IMDB movies");
   //     row.add("Show Top 20 Horror movies");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
      //  row.add("Show My reviews");
        row.add("Add movie to IMDB");
        row.add("Show my movie suggests status");
        // Add the second row to the keyboard
        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotToken() {
        // TODO
        return "572227597:AAFzgrCPaUafHVfjRi5dXNyCftaGcctZQiU";
    }
    public SendMessage SendMessage(long chatID , String answer)
    {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatID)
                .setText(answer);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return  message;
    }
    public void Execute(SendMessage msg)
    {
        try {
            execute(msg); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(new UpdateManager());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}