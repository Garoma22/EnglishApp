package com.crud.myEnglishApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
// exclude: Этот параметр позволяет явно указать, какие автоконфигурации должны быть исключены из процесса настройки Spring Boot.

@SpringBootApplication
public class EnglishApp {

    public static void main(String[] args) {
        SpringApplication.run(EnglishApp.class, args);
    }
}
