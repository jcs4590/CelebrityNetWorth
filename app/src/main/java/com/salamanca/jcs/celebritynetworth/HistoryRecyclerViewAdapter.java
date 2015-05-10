package com.salamanca.jcs.celebritynetworth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Celebrity> celebrityArrayListHistory;
    protected LayoutInflater inflater;



    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView celebImageView;
        private TextView nameTextView;


        public ViewHolder(View v){
        super(v);
        celebImageView = (ImageView)v.findViewById(R.id.celebImageView);
        nameTextView = (TextView)v.findViewById(R.id.nameTextView);
            nameTextView.setOnClickListener(this);
            celebImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Celebrity celebrity = celebrityArrayListHistory.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(),MainActivity.class);
            String celebJSon = new Gson().toJson(celebrity);
            intent.putExtra("celeb",celebJSon);
            intent.putExtra("fromActivity","HistoryActivity");
            view.getContext().startActivity(intent);
        }

    }




    public HistoryRecyclerViewAdapter(Context context, ArrayList<Celebrity> historyList){
        inflater = LayoutInflater.from(context);
        celebrityArrayListHistory = historyList;


    }

    @Override
    public int getItemCount(){

    return celebrityArrayListHistory.size();
}


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){

        Picasso.with(viewHolder.celebImageView.getContext()).load(celebrityArrayListHistory.get(position).getImageUrl()).into(viewHolder.celebImageView);
        viewHolder.nameTextView.setText(celebrityArrayListHistory.get(position).getName());




    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = inflater.inflate(R.layout.history_layout, viewGroup, false);
        return new ViewHolder(view);
    }
}

