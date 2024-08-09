package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
