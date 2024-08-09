package com.crud.myEnglishApp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class UserState {


    private Map<Long, AddWordState> userStates = new ConcurrentHashMap<>();

    public AddWordState getState(Long chatId) {
        return userStates.get(chatId);
    }

    public void setState(Long chatId, AddWordState state) {
        userStates.put(chatId, state);
    }

    public void removeState(Long chatId) {
        userStates.remove(chatId);
    }
}
