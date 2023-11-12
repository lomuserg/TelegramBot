package ru.podol.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.podol.controller.TelegramBot;
import ru.podol.service.UpdateProducer;
import ru.podol.utils.MessageUtils;

import static ru.podol.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer){
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
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
        var sendMMessage = messageUtils.generateSendMessageWithText(update, "I understand only TEXT");
        setView(sendMMessage);
    }
    private void setMessageTypeView(Update update){
        var sendMMessage = messageUtils.generateSendMessageWithText(update, "Hello");
        setView(sendMMessage);
    }
    private void setFileIsReceived(Update update) {
        var sendMMessage = messageUtils.generateSendMessageWithText(update, "(^.^)");
        setView(sendMMessage);
    }
    private void setView(SendMessage sendMMessage) {
        telegramBot.sendAnswerMessage(sendMMessage);
    }
    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE,update);
        setFileIsReceived(update);
    }
    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE,update);
        setFileIsReceived(update);
    }
    private void processTextMessage(Update update) {
      updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
        setMessageTypeView(update); //all massage Answers Hello + logging
    }
}
