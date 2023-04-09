package com.example.moviequotes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviequotes.databinding.FragmentBookBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookFragment extends Fragment {

    FragmentBookBinding binding;
    FirebaseAuth mAuth;

    FirebaseDatabase db;
    DatabaseReference quotesRef;
    DatabaseReference likedQuotesRef;
    String currentUserId;

    QuoteItemAdapter quoteItemAdapter;
    ArrayList<Quote> quotesArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBookBinding.inflate(inflater,container,false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        currentUserId = currentUser.getUid();
        db = FirebaseDatabase.getInstance();
        quotesRef = db.getReference().child("quotes");
        likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");


        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainRecyclerView.setHasFixedSize(true);

        quotesArrayList = new ArrayList<>();
        quoteItemAdapter = new FavouritesQuoteItemAdapter(getContext(),quotesArrayList);
        binding.mainRecyclerView.setAdapter(quoteItemAdapter);

        // Инициализация списка
        likedQuotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quotesArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String currentId = ds.child("id").getValue(String.class);
                    assert currentId != null;
                    quotesRef.child(currentId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Quote quote = dataSnapshot.getValue(Quote.class);
                            quote.setFavourite(true);
                            quotesArrayList.add(quote);
                            quoteItemAdapter.notifyItemInserted(quotesArrayList.indexOf(quote));
                            System.out.println("Цитата добавлена в список");
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




        return binding.getRoot();
    }
}