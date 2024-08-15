package com.crud.myEnglishApp.services;


import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import com.crud.myEnglishApp.models.UserLanguage;
import com.crud.myEnglishApp.models.Word;
import com.crud.myEnglishApp.repositories.LanguageRepository;
import com.crud.myEnglishApp.repositories.UserLanguageRepository;
import com.crud.myEnglishApp.repositories.UserRepository;
import com.crud.myEnglishApp.repositories.WordRepository;
import com.crud.myEnglishApp.utils.UserState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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


    private Map<Long, Word> tempWordStorage = new HashMap<>();

    public boolean isInWordAdditionProcess(Long chatId) {
        return tempWordStorage.containsKey(chatId);
    }


    //тут идет установка выбранного с кнопки языка. полученный язык сверяется с базой и текущему слову присваивается язык
    public void setSelectedLanguage(Long chatId, String languageName) {
        Word currentWord = tempWordStorage.getOrDefault(chatId, new Word());
//        Language language = languageRepository.findByName(languageName).orElseThrow(() -> new RuntimeException("Language not found"));
        Language language = languageRepository.findByCode(languageName).orElseThrow(() -> new RuntimeException("Language not found"));
        currentWord.setLanguage(language);
        tempWordStorage.put(chatId, currentWord);
    }

    //этот метод отвечает за обработку воода юзером слова, его перевода и примера фразы
    public SendMessage processUserInput(Update update) {
        Long chatId = update.getMessage().getChatId(); // Получаем chatId
        String text = update.getMessage().getText();

        // Получаем или создаем объект Word для текущего чата
        Word currentWord = tempWordStorage.getOrDefault(chatId, new Word());

        // Проверяем, выбран ли язык
        if (currentWord.getLanguage() == null) {
            // Запрашиваем у пользователя выбор языка, если он еще не выбран
            return chooseDictionary(update); // отправляем пользователю кнопки для выбора языка
        }

        // Проверяем, введено ли слово
        if (currentWord.getWord() == null) {
            // Пользователь вводит слово
            currentWord.setWord(text);
            tempWordStorage.put(chatId, currentWord); // Обновляем временное хранилище
            return createMessage(chatId, "Please enter the translation:"); // Запрашиваем перевод
        }

        // Проверяем, введен ли перевод
        else if (currentWord.getTranslation() == null) {
            // Пользователь вводит перевод
            currentWord.setTranslation(text); // Сохраняем перевод в объекте Word
            tempWordStorage.put(chatId, currentWord); // Обновляем временное хранилище
            return createMessage(chatId, "Please enter an example sentence:"); // Запрашиваем пример предложения
        }

        // Проверяем, введен ли пример предложения
        else if (currentWord.getExample() == null) {
            // Пользователь вводит пример предложения
            currentWord.setExample(text); // Сохраняем пример предложения

            // Сохранение слова в базу данных
            User user = userRepository.findByChatId(chatId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            currentWord.setUser(user); // Связываем слово с пользователем

            log.info("Saving word: " + currentWord);
            wordRepository.save(currentWord); // Сохраняем слово в базе данных

            // Очистка временного хранилища
            tempWordStorage.remove(chatId);

            return createMessage(chatId, "The word has been added to your dictionary!");
        }

        // Если что-то пошло не так, отправляем сообщение об ошибке
        return createMessage(chatId, "Something went wrong. Please start over.");
    }


    private SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }


//в этом методе кидаем пользователю две кнопки с выбором языка
    public SendMessage chooseDictionary(Update update) {
        Long chatId = update.getMessage().getChatId(); // Получаем chatId из Update

        // Получаем список языков для пользователя
        List<Language> languages = getLanguagesForUser(update);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); // Устанавливаем chatId в сообщение
        message.setText("Choose the dictionary you want to add a word to:");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Создаем кнопки для каждого языка
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

        return message;
    }

    // Метод получает список языков для пользователя
    public List<Language> getLanguagesForUser(Update update) {
        Long chatId = update.getMessage().getChatId(); // Получаем chatId из Update
        User user = userRepository.findByChatId(chatId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        return userRepository.findLanguagesByUserId(user.getId()); // Предполагается, что метод принимает userId
    }
}
