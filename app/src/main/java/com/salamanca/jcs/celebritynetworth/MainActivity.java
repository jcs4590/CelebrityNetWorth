package com.salamanca.jcs.celebritynetworth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
import com.jaunt.*;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;


public class MainActivity extends Activity {
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private EditText searchTextField;
    private Celebrity currentCelebrity;
    private UserAgent userAgent;
    private DrawerLayout drawerLayout;
    private ListView listViewDrawer;
    private String[] drawerMenuItems =  {"Search", "History", "Three"};
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCelebrity = new Celebrity();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        actionBar = getActionBar();
        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //set up action bar drawer toggle configurations
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                actionBar.setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle(getTitle().toString());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }



        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        //set up listView for the drawer
        listViewDrawer = (ListView)findViewById(R.id.left_drawer);
        listViewDrawer.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item,drawerMenuItems));
        listViewDrawer.setOnItemClickListener(new DrawerItemClickListener());


        fragmentManager = getFragmentManager();
        mainFragment = new MainFragment();
         userAgent = new UserAgent();


        //grab reference to views
        ImageView searchImage = (ImageView) findViewById(R.id.searchImage);
        ImageView randImage = (ImageView) findViewById(R.id.imageViewRandom);
        searchTextField = (EditText) findViewById(R.id.searchTextField);


       new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/random/");

        randImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/random/");

            }
        });
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchTextField.getText().toString().equals("")){
                 currentCelebrity.setName(searchTextField.getText().toString().toUpperCase());


                 new ProcessSearchAndScrape().execute("http://www.celebritynetworth.com/");

                }
                else {Toast.makeText(getApplicationContext(),"Celebrity Name Field Cannot Be Empty",Toast.LENGTH_LONG).show();}
            }
        });



        //start fragment transaction
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainFragment).addToBackStack("")
                .commit();

    }
public class ProcessSearchAndScrape extends AsyncTask<String,Void,Void>{




    @Override
    protected Void doInBackground(String... url) {

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
        }

    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        //action bar drawer toggle icon
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // The click listner for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),position + " ",Toast.LENGTH_LONG).show();
        }
    }
    private void selectItem(int position) {
        // update the main content by replacing fragments

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //syncs the drawer state with the action bar
        drawerToggle.syncState();
    }

}
