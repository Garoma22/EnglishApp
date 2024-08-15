package com.crud.myEnglishApp.services;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import com.crud.myEnglishApp.models.UserLanguage;
import com.crud.myEnglishApp.repositories.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@Data
public class RegistrationService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private UserWordRepository userWordRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private UserLanguageRepository userLanguageRepository;


    void registerUserFromStartCommand(Update update) {
        Message message = update.getMessage(); //извлекаем инфу об отправителе
        long chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        String lastName = message.getChat().getLastName();
        String userName = message.getChat().getUserName(); //это никнейм

      // Optional<User> existingUser = userRepository.findByUserName(userName);//вернулся garoma1989
     //  log.info(userName + " То что вернулось из запроса к базе по поиску юзера");

        List<User> existingUserList = userRepository.findAllByChatId(chatId);

        if (existingUserList.isEmpty()) {
            log.info("Запрос по chatId: {} не вернул ни одного пользователя.", chatId);
        } else if (existingUserList.size() == 1) {
            User user = existingUserList.get(0);
            log.info("Запрос по chatId: {} вернул одного пользователя: {}", chatId, user);
        } else {
            log.warn("Запрос по chatId: {} вернул более одного пользователя: {}", chatId, existingUserList.size());
            for (User user : existingUserList) {
                log.warn("Дублирующая запись: {}", user);
            }
        }

        if (existingUserList.isEmpty()) {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User registered: " + user);
            prepareAndSendMessage(chatId, "Registration complete! Now choose a language.");
        } else {
            log.info("User with username '{}' already exists.", userName);
            prepareAndSendMessage(chatId, "You are already registered. Please choose a language.");
        }

    }


    SendMessage prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }


}
