package com.crud.myEnglishApp.services;


import com.crud.myEnglishApp.models.Language;
import com.crud.myEnglishApp.repositories.LanguageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public void run(String... args) throws Exception{
        if (languageRepository.count() == 0) {
            Language english = new Language();
            english.setCode("en");
            english.setName("English");
            languageRepository.save(english);

            Language spanish = new Language();
            spanish.setCode("es");
            spanish.setName("Español");
            languageRepository.save(spanish);
        }
    }
}
