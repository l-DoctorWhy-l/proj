package com.example.moviequotes;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouritesQuoteItemAdapter extends QuoteItemAdapter{

    public FavouritesQuoteItemAdapter(Context context, ArrayList<Quote> list) {
        super(context, list);
    }

    @Override
    void deleteLike(View view, Quote quote, @NonNull MyViewHolder holder) {
        final ValueEventListener findEqualId = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("id").getValue(String.class).equals(quote.getId())) {

                        Network.likedQuotesRef.child(ds.getKey()).removeValue().addOnSuccessListener(unused -> {
                            Toast.makeText(view.getContext(), "Цитата успешно удалена из любимых", Toast.LENGTH_SHORT).show();
                            quote.setFavourite(false);
                            holder.like.setImageResource(R.drawable.baseline_favorite_border_24);
                            notifyItemRemoved(list.indexOf(quote));
                            list.remove(quote);
                            System.out.println(list);
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        Network.likedQuotesRef.addListenerForSingleValueEvent(findEqualId);}
}
