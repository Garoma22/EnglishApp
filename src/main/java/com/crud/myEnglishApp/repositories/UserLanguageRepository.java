package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import com.crud.myEnglishApp.models.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {



    boolean existsByUserIdAndLanguageId(Long userId, Long languageId);// провертка того есть ли уже у пользователя то или иной изучаемый язык


    List<UserLanguage> findByUser(User user);
}
