package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import com.crud.myEnglishApp.models.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    Optional<UserLanguage> findByUserAndLanguage(User user, Language language);

    Optional<UserLanguage> findByUser(User user);

    boolean existsByUserIdAndLanguageId(Long userId, Long languageId);// провертка того есть ли уже у пользователя то или иной изучаемый язык




}
