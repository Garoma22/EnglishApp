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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        String lastName = message.getChat().getLastName();
        String userName = message.getChat().getUserName();

        Optional<User> existingUser = userRepository.findByUserName(userName);

        if (existingUser.isEmpty()) {
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



    //метод для проверки, есть ли у пользователя уже выбранный язык
    public boolean isLanguageSet(Long chatId) {
        Optional<User> user = userRepository.findByChatId(chatId);
        Optional<UserLanguage> userLanguage = userLanguageRepository.findByUser(user.get());

        if (user.isPresent() && userLanguage.isPresent()) {

            log.info("isLanguageSet отработал, язык установлен " + userLanguage);
            return true;
        }
        log.info("isLanguageSet отработал, язык не найден");
        return false;
    }



    @Transactional
    public void setUserLanguage(Long chatId, String languageName) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        Optional<Language> languageOptional = languageRepository.findByName(languageName);

        log.info("setUserLanguage отработал");
        log.info("User: " + (userOptional.isPresent() ? userOptional.get().toString() : "not found"));
        log.info("Language: " + (languageOptional.isPresent() ? languageOptional.get().toString() : "not found"));

        if (userOptional.isPresent() && languageOptional.isPresent()) {
            User user = userOptional.get();
            Language language = languageOptional.get();

            // Проверяем, существует ли уже связь между пользователем и языком
            if (!userLanguageRepository.existsByUserIdAndLanguageId(user.getId(), language.getId())) {
                UserLanguage userLanguage = new UserLanguage();
                userLanguage.setUser(user);
                userLanguage.setLanguage(language);

                try {
                    log.info("Saving UserLanguage: " + userLanguage);
                    userLanguageRepository.save(userLanguage);
                    log.info("UserLanguage saved");
                } catch (DataIntegrityViolationException e) {
                    log.warn("Error while saving UserLanguage: {}", e.getMessage());
                }
            } else {
                log.info("User already has this language.");
            }
        } else {
            log.warn("User or Language not found");
        }
    }



    public void chooselanguage(Long chatId) {
        // Проверьте, есть ли уже установленный язык
        if (isLanguageSet(chatId)) {
            prepareAndSendMessage(chatId, "Language is already set. Use /change_language to update.");
            return;
        }

        SendMessage message = new SendMessage(); // стандартный класс из пакета package org.telegram.telegrambots.meta.api.methods.send;

        message.setChatId(String.valueOf(chatId)); // устанавливаем идентификатор чата
        message.setText("Choose the language:"); // текст сообщения

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(); // объект клавиатура

        List<List<InlineKeyboardButton>> rows = new ArrayList<>(); // список строк клавиатуры

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton englishButton = new InlineKeyboardButton();
        englishButton.setText("English");
        englishButton.setCallbackData("English"); // здесь используется код языка
        row.add(englishButton);

        InlineKeyboardButton spanishButton = new InlineKeyboardButton();
        spanishButton.setText("Español");
        spanishButton.setCallbackData("Español"); // здесь используется код языка
        row.add(spanishButton);

        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message); //отправляем сообщение
    }

    private void executeMessage(SendMessage message) { //отправляется сообщение в чат
        try {
          execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message: ", e);
        }
    }


}
