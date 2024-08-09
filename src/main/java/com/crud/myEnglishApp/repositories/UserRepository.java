package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//@Repository
//// в параметрах тип сущности с которой работает репозиторий, тип данных первичного ключа.
//public interface UserRepository extends JpaRepository<User, Long> {
//}


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByChatId(Long chatId);

    @Query("SELECT ul.language FROM UserLanguage ul WHERE ul.user.id = :userId")
    List<Language> findLanguagesByUserId(@Param("userId") Long userId);

}