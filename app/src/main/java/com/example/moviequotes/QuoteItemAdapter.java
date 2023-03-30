package com.example.moviequotes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviequotes.databinding.QuoteItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;

public class QuoteItemAdapter extends RecyclerView.Adapter<QuoteItemAdapter.MyViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    FirebaseDatabase db;
    DatabaseReference likedQuotesRef;
    String currentUserId;
    ArrayList<Quote> list;

    public QuoteItemAdapter(Context context, ArrayList<Quote> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public QuoteItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        currentUserId = currentUser.getUid();
        db = FirebaseDatabase.getInstance();
        likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteItemAdapter.MyViewHolder holder, int position) {
        Quote quote = list.get(position);
        holder.desc.setText(quote.getDesc());
        holder.film.setText(quote.getFilm());
        if(quote.isFavourite()){
            holder.like.setImageResource(R.drawable.baseline_favorite_24);
        } else
            holder.like.setImageResource(R.drawable.baseline_favorite_border_24);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quote.isFavourite()){
                    likedQuotesRef.push().setValue(new Quote(quote.getId())).addOnSuccessListener(unused -> {
                        quote.setFavourite(true);
                        holder.like.setImageResource(R.drawable.baseline_favorite_24);
                        Toast.makeText(view.getContext(), "Цитата успешно добавлена в ваши любимые)", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(runnable -> {
                        Toast.makeText(view.getContext(), "Упс, что-то пошло не так..", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    final ValueEventListener findEqualId = new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                System.out.println(ds.child("id").getValue(String.class).equals(quote.getId()));
                                if (ds.child("id").getValue(String.class).equals(quote.getId())) {
                                   likedQuotesRef.child(ds.getKey()).removeValue().addOnSuccessListener(unused ->{
                                       Toast.makeText(view.getContext(), "Цитата успешно удалена из любимых", Toast.LENGTH_SHORT).show();
                                       quote.setFavourite(false);
                                       holder.like.setImageResource(R.drawable.baseline_favorite_border_24);
                                   });
                                   break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    likedQuotesRef.addListenerForSingleValueEvent(findEqualId);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView desc, film;
        ImageButton like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.item_desc);
            film = itemView.findViewById(R.id.item_film);
            like = itemView.findViewById(R.id.like);
        }
    }


}
