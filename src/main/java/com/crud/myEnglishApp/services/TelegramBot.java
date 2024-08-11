package com.crud.myEnglishApp.services;

import com.crud.myEnglishApp.config.BotConfig;
import com.crud.myEnglishApp.models.*;
import com.crud.myEnglishApp.repositories.*;
import com.vdurmont.emoji.EmojiParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    RegistrationService registrationService;
    @Autowired
    AddWordService addWordService;

    @Autowired
    ChooseLanguageService chooseLanguageService;

    final BotConfig config;

    static final String ERROR_TEXT = "Error occurred: ";
    static final String ESPAÑOL = "Español";
    static final String ENGLISH = "English";



    //тут инициализируем бота с командами
    public TelegramBot(BotConfig config) { // BotConfig config - Объект конфигурации, содержащий параметры бота, такие как имя и токен.
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>(); //создание списка команд
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/add_a_word", "add a word in the dictionary"));
        try {

            // Регистрация команд в Telegram API: тут выше в параметрах метода создается объект команды SetMyCommands
            // с указанным списком команд, областью видимости команд (BotCommandScopeDefault) и параметрами языка (в данном случае null).
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            log.info("Message received: " + messageText + " from chatId: " + chatId);

            if (messageText.equals("/start")) {
                log.info("Processing /start command");
             registrationService.registerUserFromStartCommand(update);


                chooseLanguageService.chooselanguage(chatId); // Предлагаем пользователю выбрать язык после регистрации
            }
            if (messageText.equals("/add_a_word")){
                log.info("Processing /add a word command");
            }
        }
            if (update.hasCallbackQuery()) { //метод для обработки нажатия на кнопку
                String callbackData = update.getCallbackQuery().getData();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                log.info("Сюда доходим ");


                if (callbackData.equals(ESPAÑOL) || callbackData.equals(ENGLISH)) {
                    log.info("Callback data received: " + callbackData);
                    // Устанавливаем язык в базе данных
                    chooseLanguageService.setUserLanguage(chatId, callbackData);
                    log.info("Message received: " + " язык установлен " + " from chatId: " + chatId);

                    // Отправляем сообщение о том, что язык установлен
                    String text = callbackData.equals(ESPAÑOL) ? "You have selected English" : "Seleccionaste español";
                    executeMessage(chooseLanguageService.prepareAndSendMessage(chatId, text));


                }
            }
    }

    private void executeMessage(SendMessage message) { //отправляется сообщение в чат
        try {
          execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message: ", e);
        }
    }



    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}




