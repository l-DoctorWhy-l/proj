package com.example.moviequotes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotes_table")
public class Quote {
    private String film;
    private String desc;
    private boolean favourite;
    private String id;

    @PrimaryKey(autoGenerate = true)
    int key;

    public Quote(String id) {
        this.id = id;
        favourite = true;
    }

    public Quote(String film, String desc, boolean favourite, String id) {
        this.film = film;
        this.desc = desc;
        this.favourite = favourite;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Quote() {
    }

    @Override
    public String toString() {
        return "Quote{" +
                "film='" + film + '\'' +
                ", desc='" + desc + '\'' +
                ", favourite=" + favourite +
                '}';
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
