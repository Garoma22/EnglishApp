package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserWordRepository extends JpaRepository<UserWord, Long> {

    // Примерный метод для поиска слов по пользователю и языку
//    Optional<UserWord> findByUserAndWord_UserIdAndWord_LanguageId(Long userId, Long languageId, String word);

    @Query("SELECT ul.language FROM UserLanguage ul WHERE ul.user.id = :userId")
    List<Language> findLanguagesByUserId(@Param("userId") Long userId);


}
