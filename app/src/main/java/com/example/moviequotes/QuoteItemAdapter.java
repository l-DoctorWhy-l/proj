package com.example.moviequotes;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviequotes.databinding.QuoteItemWindowBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuoteItemAdapter extends RecyclerView.Adapter<QuoteItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Quote> list;

    public QuoteItemAdapter(Context context, ArrayList<Quote> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public QuoteItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false);
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
        Log.d("GGWP", quote.toString() + " " + position);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quote.isFavourite()){
                    addToFavourites(view, quote.getId()).addOnSuccessListener(o -> {
                        quote.setFavourite(true);
                        holder.like.setImageResource(R.drawable.baseline_favorite_24);
                    });

                } else {
                    deleteLike(view, quote,holder);
                }
            }
        });

        holder.moreInfo.setOnClickListener(b -> {
            showQuoteCard(context, quote);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView desc, film;
        ImageButton like;
        LinearLayout moreInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.item_desc);
            film = itemView.findViewById(R.id.item_film);
            like = itemView.findViewById(R.id.like);
            moreInfo = itemView.findViewById(R.id.itemInfo);

        }
    }

    Task addToFavourites(View view, String id){
         return Network.likedQuotesRef.push().setValue(new Quote(id)).addOnSuccessListener(unused -> {
            Toast.makeText(view.getContext(), "Цитата успешно добавлена в ваши любимые)", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(runnable -> {
            Toast.makeText(view.getContext(), "Упс, что-то пошло не так..", Toast.LENGTH_SHORT).show();
        });

    }


    void deleteLike(View view, Quote quote,@NonNull QuoteItemAdapter.MyViewHolder holder){
        final ValueEventListener findEqualId = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("id").getValue(String.class).equals(quote.getId())) {

                        Network.likedQuotesRef.child(ds.getKey()).removeValue().addOnSuccessListener(unused -> {
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
        Network.likedQuotesRef.addListenerForSingleValueEvent(findEqualId);
    }

    void showQuoteCard(Context context, Quote quote){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.quote_item_window);
        TextView item_desc, item_film;
        item_film = dialog.findViewById(R.id.item_film_more);
        item_desc = dialog.findViewById(R.id.item_desc_more);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        System.out.println(quote.getDesc());
        item_desc.setText(quote.getDesc());
        item_film.setText(quote.getFilm());
        dialog.show();
    }


}
