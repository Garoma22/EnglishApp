package com.crud.myEnglishApp.repositories;

import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {


}
