package com.example.moviequotes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moviequotes.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    FirebaseAuth mAuth;

    FirebaseDatabase db;
    DatabaseReference quotesRef;
    DatabaseReference likedQuotesRef;
    String currentUserId;

    QuoteItemAdapter quoteItemAdapter;
    ArrayList<Quote> quotesArrayList;
    ArrayList<String> likedQuotesIdList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        currentUserId = currentUser.getUid();
        db = FirebaseDatabase.getInstance();
        quotesRef = db.getReference().child("quotes");
        likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");
    }




    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainRecyclerView.setHasFixedSize(true);

        quotesArrayList = new ArrayList<>();
        likedQuotesIdList = new ArrayList<>();
        quoteItemAdapter = new QuoteItemAdapter(getContext(),quotesArrayList);
        binding.mainRecyclerView.setAdapter(quoteItemAdapter);

        final ValueEventListener likedQuotesListener = Network.createLikedQuotesEventListener(likedQuotesIdList);



        final ValueEventListener quotesListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quotesArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Quote quote = ds.getValue(Quote.class);
                    System.out.println(likedQuotesIdList + "    " + ds.getKey());
                    if (likedQuotesIdList.contains(ds.getKey())) {
                        assert quote != null;
                        quote.setFavourite(true);
                    }
                    quotesArrayList.add(quote);
                }
                Collections.shuffle(quotesArrayList);

                quoteItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        quotesRef.addValueEventListener(quotesListener);

        likedQuotesRef.addListenerForSingleValueEvent(likedQuotesListener);


        return binding.getRoot();
    }





}