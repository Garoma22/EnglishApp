package com.crud.myEnglishApp.services;



import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import com.crud.myEnglishApp.models.UserLanguage;
import com.crud.myEnglishApp.repositories.LanguageRepository;
import com.crud.myEnglishApp.repositories.UserLanguageRepository;
import com.crud.myEnglishApp.repositories.UserRepository;
import com.crud.myEnglishApp.repositories.WordRepository;
import com.crud.myEnglishApp.utils.UserState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute;


//Класс отвечающий за добавление слова

@Slf4j
@Service
public class AddWordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    UserLanguageRepository userLanguageRepository;

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private UserState userState;


    public void chooseDictionary(Long chatId) {

        List<Language> languages = getLanguagesForUser(chatId);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Choose the dictionary you want to add a word to:");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Language language : languages) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(language.getName());
            button.setCallbackData("DICT_" + language.getCode());
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }


        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);



    }


    //Метод выдирает список языков из пользователя
    public List<Language> getLanguagesForUser(Long chatId) {
        User user = userRepository.findByChatId(chatId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        return userRepository.findLanguagesByUserId(chatId);
    }
}
