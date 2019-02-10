package org.telegram.telegrambots;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String ADMIN_CHAT_ID = "@imdb_atibot";
    @Override
    public String getBotUsername() {
        // TODO
        return ADMIN_CHAT_ID;
    }

    @Override
    public String getBotToken() {
        // TODO
        return "543558923:AAEPOirgoJYKsWNq_H5eCSRmUPAiy-MNMH0";
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
    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText()) {
                TextUpdate.GetInstance().OnNewUpdate(update.getMessage());
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {

                PhotoUpdate.GetInstance().OnNewUpdate(update.getMessage());
        }
        else if (update.hasCallbackQuery()) {
            OnCallBack(update.getCallbackQuery());
        }
        else if(update.hasInlineQuery())
        {
            long chat_id = update.getInlineQuery().getFrom().getId();
            InlineKeyboardMarkup keyboard =new InlineKeyboardMarkup();
            List<InlineKeyboardButton> btns = new ArrayList<>();
            List<List<InlineKeyboardButton>> b = new ArrayList<>(5);
            InlineKeyboardButton row = new InlineKeyboardButton();
            row.setText("atiyeh");
            row.setText("Norouzi");
            btns.add(row);
            b.add(btns);
            keyboard.setKeyboard(b);
            SendMessage m = new SendMessage() // Create a message object object
                    .setChatId(chat_id)
                    .setText("HELLO")
                    .setReplyMarkup(keyboard);
            try {
                execute(m); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
    void OnCallBack(CallbackQuery q)
    {
        String call_data = q.getData();
        long message_id = q.getMessage().getMessageId();
        long chat_id = q.getMessage().getChatId();
        if (call_data.startsWith("Add to my Collection")) {
            String movie_id = call_data.substring(call_data.lastIndexOf(",") + 1 , call_data.length());
            String a = "This movie added to your favorites";
            boolean result = DBConnection.GetInstance().InsertFavMovie(movie_id, (int)chat_id);
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
        else if(call_data.startsWith("Next Movie"))
        {
            int index = call_data.lastIndexOf(',');
            int movienum = Integer.parseInt(call_data.substring(index + 1 , call_data.length()));
            String movieName = call_data.substring(10 , index);
            TextUpdate.GetInstance().SearchMMovie("/search " + movieName , chat_id , movienum + 1);
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
    public SendPhoto SendPhoto(long chat_id , String link)
    {
        SendPhoto message = new SendPhoto() // Create a message object object
                .setChatId(chat_id)
                .setPhoto(link)
                .setCaption("atiyeh");

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return  message;
    }
    public SendMessage SendMessage(long chatID , String  answer, boolean execute)
    {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatID)
                .setParseMode("HTML")
                .setText(answer);
        if (!execute)
            return  message;
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
    public void Execute(SendPhoto msg)
    {
        try {
            execute(msg); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}