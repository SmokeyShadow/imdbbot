package org.telegram.telegrambots;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class TelegramUpdate {
    public abstract void OnNewUpdate(Message msg);
}
