package com.juliawatzek.whatsob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ID_EXTRA = "com.juliawatzek.whatsob.Identifier";
    public static final String NOTE_TITLE_EXTRA = "com.juliawatzek.whatsob.Title";
    public static final String NOTE_MESSAGE_EXTRA = "com.juliawatzek.whatsob.Message";
    public static final String NOTE_CATEGORY_EXTRA = "com.juliawatzek.whatsob.Category";
    public static final String NOTE_OBSERVER_EXTRA = "com.juliawatzek.whatsob.Observer";
    public static final String NOTE_COMMENTS_EXTRA = "com.juliawatzek.whatsob.Comments";
    public static final String NOTE_WAS_FED_EXTRA = "com.juliawatzek.whatsob.WasFed";
    public static final String NOTE_HAD_FOOD_IN_ENCLOSURE_EXTRA = "com.juliawatzek.whatsob.HadFoodInEnclosure";
    public static final String NOTE_FRAGMENT_TO_LOAD_EXTRA = "com.juliawatzek.whatsob.FragmentToLoad";

    public enum FragmentToLaunch {VIEW, EDIT, CREATE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadPreferences();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteDetailActivity.class);
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.CREATE);
                startActivity(intent);

            }
        });
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
            Intent intent = new Intent(this, AppPreferences.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String notebookTitle = sharedPreferences.getString("title", "WhatsOb");
        setTitle(notebookTitle);

    }
}
