package com.example.moviequotes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviequotes.RoomDatabase.BookDB;
import com.example.moviequotes.Adapters.FavouritesQuoteItemAdapter;
import com.example.moviequotes.Network.Network;
import com.example.moviequotes.Entities.Quote;
import com.example.moviequotes.RoomDatabase.QuoteDAO;
import com.example.moviequotes.Adapters.QuoteItemAdapter;
import com.example.moviequotes.databinding.FragmentBookBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BookFragment extends Fragment {

    public FragmentBookBinding binding;
    QuoteItemAdapter quoteItemAdapter;
    ArrayList<Quote> quotesArrayList;
    ArrayList<Quote> downloadedQuotesArrayList = new ArrayList<>();

    BookDB bookDB;
    QuoteDAO quoteDAO;
    Disposable quotesListDisposable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBookBinding.inflate(inflater,container,false);


        bookDB = BookDB.getInstance(requireContext());
        quoteDAO = bookDB.quoteDAO();

        // Загружаем все цитаты из базы данных
        quotesListDisposable = quoteDAO
                .getAllQuotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onQuotesLoaded);


        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainRecyclerView.setHasFixedSize(true);

        quotesArrayList = new ArrayList<>();
        quoteItemAdapter = new FavouritesQuoteItemAdapter(getContext(),quotesArrayList);
        binding.mainRecyclerView.setAdapter(quoteItemAdapter);

        if(Network.isOnline(requireContext()) && Network.likedQuotesRef != null) {
            Network.likedQuotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    downloadedQuotesArrayList.clear();
                    quoteDAO.deleteAllQuotes().subscribeOn(Schedulers.io())
                            .subscribe();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String currentId = ds.child("id").getValue(String.class);
                        assert currentId != null;
                        Network.quotesRef.child(currentId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                Quote quote = dataSnapshot.getValue(Quote.class);
                                quote.setFavourite(true);
                                downloadedQuotesArrayList.add(quote);
                                quoteDAO
                                        .addQuote(quote)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




        return binding.getRoot();
    }

    public void onQuotesLoaded(List<Quote> quotes) {
        while (!quotesArrayList.isEmpty()){
            quoteItemAdapter.notifyItemRemoved(0);
            quotesArrayList.remove(0);
        }
        for(int i = 0; i < quotes.size(); i++){
            if(!quotesArrayList.contains(quotes.get(i))){
                quotesArrayList.add(0,quotes.get(i));
                quoteItemAdapter.notifyItemInserted(0);
            }
        }
        quotesListDisposable.dispose();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        quotesListDisposable.dispose();
    }
}