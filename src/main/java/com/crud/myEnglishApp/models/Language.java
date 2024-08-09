package com.crud.myEnglishApp.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name= "languages")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "name"}) //составной ключ гарантирующий уникальность каждоый пары код-название
})
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String name;

}
