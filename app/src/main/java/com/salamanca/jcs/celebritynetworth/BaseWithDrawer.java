package com.salamanca.jcs.celebritynetworth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*
This base class helps with implementing the drawer navigation in all of the activites
along with holding reference to celebrity history and saving history to a file


 */

public class BaseWithDrawer extends Activity {
    private DrawerLayout drawerLayout;
    private ListView listViewDrawer;
    private String[] drawerMenuItems =  {"Search", "History"};
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private static ArrayList<Celebrity> historyList;
    private File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_with_drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        //read from file which is json formatted
        if(this.historyList == null) {
            this.historyList = readFromFile("history");
        };


        //set up Action BAr icon that allows drawer to be opened and close through toggle
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //set up action bar drawer toggle configurations
        //handle state change to update title on actionbar
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                actionBar.setTitle(getTitle().toString());
                invalidateOptionsMenu();

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle("Navigation!");
                invalidateOptionsMenu();

            }



        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        //set up listView for the drawer
        listViewDrawer = (ListView)findViewById(R.id.left_drawer);
        listViewDrawer.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item,drawerMenuItems));
        listViewDrawer.setOnItemClickListener(new DrawerItemClickListener());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_with_drawer, menu);
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
            Intent intent;
            if(position == 1){
                intent = new Intent(getApplicationContext(),History.class);

                startActivity(intent);

            }
            if(position == 0){
                intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }

        }
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






    public void saveToFile(ArrayList<Celebrity> content, String fileName){

        try {
            file = new File(getApplicationContext().getFilesDir(), fileName);
            FileOutputStream outPutStream = new FileOutputStream(file);

            String jsonString = new Gson().toJson(content);
            outPutStream.write(jsonString.getBytes());
            outPutStream.close();


        }catch (Exception e){e.printStackTrace();}

    }

    public ArrayList<Celebrity> readFromFile(String fileName){
        ArrayList<Celebrity> tempList;
        try {

            file = new File(getApplicationContext().getFilesDir()+"/"+ fileName);
            if(file.exists()) {

                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String lineFromFile = "";
                StringBuilder builder = new StringBuilder();
                while ((lineFromFile = bufferedReader.readLine()) != null) {
                    builder.append(lineFromFile);
                }

                //use Gson to deserialize string to arraylist of Celebrity objects
                tempList = new Gson().fromJson(builder.toString(), new TypeToken<List<Celebrity>>() {
                }.getType());

                return tempList;
            }
        }catch (Exception e){e.printStackTrace();}

        return new ArrayList();
    }


    public static ArrayList<Celebrity> getHistoryList() {
        return historyList;
    }

    public static void setHistoryList(ArrayList<Celebrity> historyList) {
        BaseWithDrawer.historyList = historyList;
    }
    public void addToHistory(Celebrity tempCeleb){
        if(! (this.historyList.contains(tempCeleb)) ) {

            this.historyList.add(tempCeleb);

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //read from file celeb history
        this.historyList = readFromFile("history");

    }

    @Override
    protected void onPause() {
        super.onPause();
        //save to file celeb history
        saveToFile(this.getHistoryList(), "history");

    }
}
