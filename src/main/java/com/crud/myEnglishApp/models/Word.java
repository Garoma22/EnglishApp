package com.crud.myEnglishApp.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "words", uniqueConstraints = {@UniqueConstraint(columnNames = {"language_id", "word"})})
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false, foreignKey = @ForeignKey(name = "fk_language"))
    private Language language;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private User user;
    @Column(nullable = false)
    private String word;
    @Column(nullable = false)
    private String translation;
    private String example;
}

