package org.telegram.telegrambots;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import java.util.Comparator;
import java.util.List;

public class PhotoUpdate extends TelegramUpdate {
    private static PhotoUpdate instance;
    public static PhotoUpdate GetInstance()
    {
        if(instance == null)
            instance = new PhotoUpdate();
        return instance;
    }
    @Override
    public void OnNewUpdate(Message msg) {
        long chat_id = msg.getChatId();

        List<PhotoSize> photos = msg.getPhoto();
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
        SendPhoto message = new SendPhoto()
                .setChatId(chat_id)
                .setPhoto(f_id)
                .setCaption(caption);
        UpdateManager.GetInstance().Execute(message); // Call method to send the message

    }
}
