package com.example.moviequotes.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moviequotes.Network.Network;
import com.example.moviequotes.Entities.Quote;
import com.example.moviequotes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

public class FavouritesQuoteItemAdapter extends QuoteItemAdapter {

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
                            quoteDAO.deleteQuote(quote.getId()).subscribeOn(Schedulers.io()).subscribe();
                            holder.like.setImageResource(R.drawable.baseline_favorite_border_24);
                            notifyItemRemoved(list.indexOf(quote));
                            list.remove(quote);
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
