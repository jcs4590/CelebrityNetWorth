package com.salamanca.jcs.celebritynetworth;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


public class History extends BaseWithDrawer   {
    protected DrawerLayout drawerLayout;
    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_history, null, false);
        //set up recycler view adpater and layout manager
        historyRecyclerView = (RecyclerView)contentView.findViewById(R.id.historyRecyclerView);
        recyclerViewAdapter = new HistoryRecyclerViewAdapter(this,getHistoryList());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        historyRecyclerView.setAdapter(recyclerViewAdapter);
        historyRecyclerView.setLayoutManager(layoutManager);



        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.addView(contentView, 0);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

