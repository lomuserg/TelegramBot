package ru.podol.controller;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
//import java.util.logging.Logger;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String BotName;
    @Value("${bot.token}")
    private String BotToken;  //this token need private
    private ru.podol.controller.UpdateController UpdateController;

    public TelegramBot(UpdateController UpdateController){
       this.UpdateController = UpdateController;
    }
    @PostConstruct
    public void init(){
       UpdateController.RegisterBot(this);
    }
    @Override
    public String getBotUsername() {
        return BotName;
    }

    @Override
    public String getBotToken() {
        return BotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateController.processUpdate(update);
         }
         private static final Logger log = Logger.getLogger(TelegramBot.class);
    public void sendAnswerMessage(SendMessage message){
        if(message != null){
            try{
                execute(message);
            }catch (TelegramApiException e){
                log.error(e);
            }
        }
    }
}
