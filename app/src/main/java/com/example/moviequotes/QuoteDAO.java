package com.example.moviequotes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface QuoteDAO {

    @Insert
    Completable addQuote(Quote quote);

    // Метод чтения необходимой цитаты из таблицы по id
    @Query("SELECT * FROM quotes_table WHERE id = :id")
    Observable<Quote> getQuoteByKey(String id);

    // Метод чтения всех цитат из таблицы
    @Query("SELECT * FROM quotes_table")
    Observable<List<Quote>> getAllQuotes();

    // Метод удаления всех цитат из таблицы
    @Query("DELETE FROM quotes_table")
    Completable deleteAllQuotes();

    // Метод изменения значения isCompleted  в имеющейся цитате по id
    @Query("UPDATE quotes_table SET favourite = :favourite WHERE id = :id")
    Completable setFavourite(boolean favourite, String id);

    // Метод удаления цитаты по id
    @Query("DELETE FROM quotes_table WHERE id = :id")
    Completable deleteQuote(String id);
}
