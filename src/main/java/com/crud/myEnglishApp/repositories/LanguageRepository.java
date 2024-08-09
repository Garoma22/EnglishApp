package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language,Long> {
    Optional<Language> findByName(String name);

    Optional<Language> findByCode(String code);
}
