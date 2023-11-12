package ru.podol.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.podol.controller.TelegramBot;
import ru.podol.utils.MessageUtils;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils){
        this.messageUtils = messageUtils;
    }

    public void  RegisterBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if(update == null){
            log.error("Received update is null");
            return;
        }

        if(update.getMessage() != null){
            distributeMessagesByType(update);
        }else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.getText() != null){
            processTextMessage(update);
        } else if (message.getDocument() != null){
            processDocMessage(update);
        } else if (message.getPhoto() != null){
            processPhotoMessage(update);
        } else  {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
    var sendMMessage = messageUtils.generateSendMessageWithText(update, "Я понимаю только текст, дружище");
    setView(sendMMessage);
    }

    private void setView(SendMessage sendMMessage) {
        telegramBot.sendAnswerMessage(sendMMessage);
    }

    private void processPhotoMessage(Update update) {
        
    }

    private void processDocMessage(Update update) {
        
    }

    private void processTextMessage(Update update) {
        
    }
}
