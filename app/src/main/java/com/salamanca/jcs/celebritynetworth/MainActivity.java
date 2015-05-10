package com.salamanca.jcs.celebritynetworth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jaunt.*;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class MainActivity extends BaseWithDrawer {
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private UserAgent userAgent;
    private Celebrity currentCelebrity;



    protected DrawerLayout drawerLayout;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        currentCelebrity = new Celebrity();
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);

        fragmentManager = getFragmentManager();
        mainFragment = new MainFragment();

        //start fragment transaction
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainFragment).addToBackStack("")
                .commit();


    }
    public class ProcessSearchAndScrape extends AsyncTask<String,Void,Void>{




    @Override
    protected Void doInBackground(String... url) {
        //userAgent for JAunt Scraper
        userAgent = new UserAgent();

        try {
           //scrape website and add values to a Celebrity object
            if (!url[0].contains("random")) {

                userAgent.visit(url[0]);
                userAgent.doc.apply(currentCelebrity.getName());
                userAgent.doc.submit("Search");
                currentCelebrity.setUrl(userAgent.doc.outerHTML().contains("<div class=\"search_result_lead_bio\">") ?
                        userAgent.doc.findFirst("<div class=\"search_result_lead_bio\">")
                                .getFirst("<a href").getAt("href") : null);


            } else {
                userAgent.visit(url[0]);
                currentCelebrity.setUrl(userAgent.doc.findFirst("<link rel=\"canonical\"").getAt("href"));
                String name = currentCelebrity.getUrl().split("/")[currentCelebrity.getUrl().split("/").length - 1];

                name = name.split("-")[0] + " " + name.split("-")[1];
                currentCelebrity.setName(name.toUpperCase());

            }

            if (currentCelebrity.getUrl() != null) {
                userAgent.visit(currentCelebrity.getUrl());
                Element allContent = userAgent.doc.findFirst("<div id=\"cnw_new_profile");
                currentCelebrity.setImageUrl(allContent.getFirst("<img").getAt("src"));
                int count = allContent.findFirst("<div id=\"cnw_profile_bio\">").getChildElements().size();

                currentCelebrity.setNetWorthString(allContent.outerHTML().contains("meta_row networth") ?
                        allContent.findFirst("<<div class=\"meta_row networth\">").findFirst("<div class=\"value\">").getText() :
                        "Unknown");
                currentCelebrity.setDateOfBirth(allContent.outerHTML().contains("meta_row birth_date") ?
                        allContent.findFirst("<<div class=\"meta_row birth_date\">").findFirst("<div class=\"value\">").getText() :
                        "Unknown");
                currentCelebrity.setPlaceOfBirth(allContent.outerHTML().contains("meta_row birth_place") ?
                        allContent.findFirst("<<div class=\"meta_row birth_place\">").findFirst("<div class=\"value\">").getText() :
                        "Unknown");


                currentCelebrity.setProfession(allContent.outerHTML().contains("meta_row profession") ?
                        allContent.findFirst("<<div class=\"meta_row profession\">").findFirst("<div class=\"value\">").getText() :
                        "Unknown");


                if (allContent.outerHTML().contains("meta_row category")) {
                    List<Element> elementList = allContent.findFirst("<<div class=\"meta_row category\">").findEach("<a").toList();
                    String categoryString = "";
                    for (int i = 0; i < elementList.size(); i++) {

                        categoryString += elementList.get(i).getAt("title");
                    }

                    currentCelebrity.setCategory(categoryString);
                } else {
                    currentCelebrity.setCategory("Unknown");

                }
                List<Element> bioElements = allContent.findFirst("<div id=\"cnw_profile_content\"").getChildElements();
                List<Element> elementsToRemove = allContent.findFirst("<div id=\"cnw_profile_content\"").findEach("<div").getChildElements();

                bioElements.removeAll(elementsToRemove);

                String bio = "";
                for (int i = 0; i < bioElements.size(); i++) {

                    bio += bioElements.get(i).getText();
                }
                currentCelebrity.setInfo(bio);


                currentCelebrity.setAnnualSalaryString(allContent.outerHTML().contains("meta_row salary") ?
                        allContent.findFirst("<<div class=\"meta_row salary\">").findFirst("<div class=\"value\">").getText() :
                        "Unknown");


                }
            else{
                    return  null;


            }
            }catch(JauntException e){
                e.printStackTrace();
            }

            return null;

        }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //didn't find celebrity show toast
        if(currentCelebrity.getUrl() == null){
            Toast.makeText(getApplicationContext(),"Sorry we did not find information on this celebrity",Toast.LENGTH_SHORT)
                    .show();
        }
        else {

            mainFragment.populateFragment(new Gson().toJson(currentCelebrity));
            Celebrity temp = new Celebrity(currentCelebrity);
            addToHistory(temp);

        }

    }
}







    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

            handleIntent(intent);


    }

    private void handleIntent(Intent intent) {


        //handle search from action bar
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(!query.equals("")){
                currentCelebrity.setName(query.toUpperCase());
                new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/");

            }
            else {Toast.makeText(getApplicationContext(),"Celebrity Name Field Cannot Be Empty",Toast.LENGTH_LONG).show();}


        }

        //check to see if intent was started by History activity
        else if( (intent.getExtras() != null) && intent.getExtras().getString("fromActivity") != null){
            String fromActivity = intent.getExtras().getString("fromActivity");
            if(fromActivity.equals("HistoryActivity") ){
                mainFragment.populateFragment(intent.getExtras().getString("celeb"));


            }
        }else {
            currentCelebrity = new Celebrity();
            new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/random/");

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.random) {
            MainActivity mainActivity = new MainActivity();
            new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/random/");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

}
