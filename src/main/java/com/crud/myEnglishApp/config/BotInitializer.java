package com.crud.myEnglishApp.config;


import com.crud.myEnglishApp.services.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {
    @Autowired
    TelegramBot bot; // экземпляр бота


    /*
    @EventListener({ContextRefreshedEvent.class}): Эта аннотация указывает, что метод init должен
    быть вызван при возникновении события ContextRefreshedEvent, которое происходит после полной
    нициализации контекста Spring.
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot); // Регистрирует вашего бота в API Telegram.
        }
        catch (TelegramApiException e){
            log.error("Error occurred: " + e.getMessage());
        }
    }
}


