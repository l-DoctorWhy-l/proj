package com.example.moviequotes.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviequotes.Entities.Quote;

@Database(entities = {Quote.class}, version = 1)
public abstract class BookDB extends RoomDatabase {
    // Хранимый синглтоном единственный экземпляр
    private static BookDB instance;

    // Ссылка на DAO
    public abstract QuoteDAO quoteDAO();


    // Метод создания и получения экземпляра синглтона
    public static synchronized BookDB getInstance(Context context) {
        if (instance == null) {
            // Инициализация базы данных
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BookDB.class, "quotes_database")
                    .build();
        }
        return instance;
    }

}
