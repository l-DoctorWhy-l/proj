package com.example.moviequotes.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviequotes.Network.Network;
import com.example.moviequotes.Entities.Quote;
import com.example.moviequotes.Adapters.QuoteItemAdapter;
import com.example.moviequotes.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;

    QuoteItemAdapter quoteItemAdapter;
    ArrayList<Quote> quotesArrayList;
    ArrayList<String> likedQuotesIdList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);

        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.searchRecyclerView.setHasFixedSize(true);

        quotesArrayList = new ArrayList<>();
        likedQuotesIdList = new ArrayList<>();
        quoteItemAdapter = new QuoteItemAdapter(getContext(),quotesArrayList);
        binding.searchRecyclerView.setAdapter(quoteItemAdapter);
        final ValueEventListener likedQuotesListener = Network.createLikedQuotesEventListener(likedQuotesIdList);

        Network.likedQuotesRef.addListenerForSingleValueEvent(likedQuotesListener);


        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.searchEditText.getText().toString().length() != 0){
                    binding.searchIsEmpty.setVisibility(View.GONE);
                    binding.searchRecyclerView.setVisibility(View.VISIBLE);
                    onProcessSearching(binding.searchEditText.getText().toString());
                }
            }
        });


        return binding.getRoot();
    }

    private void onProcessSearching(String key) {

        final ValueEventListener quotesListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Quote quote = ds.getValue(Quote.class);
                    if (likedQuotesIdList.contains(ds.getKey())) {
                        assert quote != null;
                        quote.setFavourite(true);
                    }
                    quotesArrayList.add(quote);

                }
                quoteItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        quotesArrayList.clear();
        ArrayList<Query> queries= new ArrayList<>();
        String capitalizedKey = Character.toUpperCase(key.charAt(0)) + key.substring(1);
        queries.add(Network.quotesRef.orderByChild("desc").startAt(key.toLowerCase()).endAt(key.toLowerCase() + "\uf8ff"));
        queries.add(Network.quotesRef.orderByChild("desc").startAt(capitalizedKey).endAt(capitalizedKey + "\uf8ff"));
        queries.add(Network.quotesRef.orderByChild("film").startAt(key.toLowerCase()).endAt(key.toLowerCase() + "\uf8ff"));
        queries.add(Network.quotesRef.orderByChild("film").startAt(capitalizedKey).endAt(capitalizedKey + "\uf8ff"));

        for(Query q : queries){
            q.addListenerForSingleValueEvent(quotesListener);
        }


    }


}