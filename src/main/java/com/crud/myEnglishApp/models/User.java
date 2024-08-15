package com.crud.myEnglishApp.models;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@Data
//@Entity(name= "users")
//public class User {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // уникальный идентификатор пользователя
//
//    private Long chatId; //идентификатор чата
//
//    private String firstName;
//    private String lastName;
//
//    @Column(name = "user_nickname", unique = true) // уникальность на уровне JPA
//    private String userName;
//
//    // Связь с таблицей user_languages
//    //Указывает, что текущая сторона связи не является владельцем связи. В данном случае, связь между
//    // User и UserLanguage/UserWord управляется полем user в классах UserLanguage и UserWord.
//    // Это поле user является владельцем связи и отвечает за обновление внешнего ключа.
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<UserLanguage> userLanguages;
//
//    // Связь с таблицей user_words
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<UserWord> userWords;
//
//    private Timestamp registeredAt; //дата регистрации
//}



@Data
@Entity(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор пользователя

    private Long chatId; // идентификатор чата

    private String firstName;
    private String lastName;

    @Column(name = "user_nickname", unique = true)
    private String userName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserLanguage> userLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserWord> userWords = new ArrayList<>();

    private Timestamp registeredAt;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}




