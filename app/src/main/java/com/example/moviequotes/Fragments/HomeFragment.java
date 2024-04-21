package com.example.moviequotes.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviequotes.Entities.User;
import com.example.moviequotes.Network.Network;
import com.example.moviequotes.Entities.Quote;
import com.example.moviequotes.Adapters.QuoteItemAdapter;

import com.example.moviequotes.R;
import com.example.moviequotes.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;


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
        Network.init();
        getUserDataFromDB();

        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainRecyclerView.setHasFixedSize(true);

        quotesArrayList = new ArrayList<>();
        likedQuotesIdList = new ArrayList<>();
        quoteItemAdapter = new QuoteItemAdapter(getContext(),quotesArrayList);
        binding.mainRecyclerView.setAdapter(quoteItemAdapter);
        binding.addQuoteBtn.setOnClickListener(v -> showAddQuoteDialog(requireContext()));

        final ValueEventListener likedQuotesListener = Network.createLikedQuotesEventListener(likedQuotesIdList);

        likedQuotesRef.addValueEventListener(likedQuotesListener);


        final ValueEventListener quotesListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quotesArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Quote quote = ds.getValue(Quote.class);
                    if (likedQuotesIdList.contains(ds.getKey())) {
                        assert quote != null;
                        quote.setFavourite(true);
                    }
                    quotesArrayList.add(quote);
                }
                Collections.shuffle(quotesArrayList);
                System.out.println(likedQuotesIdList);

                quoteItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        quotesRef.addValueEventListener(quotesListener);



        return binding.getRoot();
    }

    void showAddQuoteDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_quote_window);
        EditText item_desc_et, item_film_et;
        Button sendBtn;
        item_film_et = dialog.findViewById(R.id.item_film_et);
        item_desc_et = dialog.findViewById(R.id.item_desc_et);
        sendBtn = dialog.findViewById(R.id.send_quote);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sendBtn.setOnClickListener(v -> {
            String quoteDesc = item_desc_et.getText().toString();
            String quoteFilm = item_film_et.getText().toString();
            if(!quoteDesc.isEmpty() && !quoteFilm.isEmpty()){
                String qUUID = UUID.randomUUID().toString();
                Quote newQuote = new Quote(
                        quoteFilm,
                        quoteDesc,
                        qUUID
                );
                Network.quotesRef.child(qUUID).setValue(newQuote).addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Новая цитата успешно добавлена!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
            }
        });


        dialog.show();
    }

    private void getUserDataFromDB(){
        ValueEventListener profileDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                if(user.isAdmin)
                    binding.addQuoteBtn.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Network.user.addValueEventListener(profileDataListener);
    }


}